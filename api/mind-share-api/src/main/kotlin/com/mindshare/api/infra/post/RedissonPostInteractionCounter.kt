package com.mindshare.api.infra.post

import com.mindshare.api.application.post.PostInteractionCounter
import com.mindshare.domain.post.InteractionType
import com.mindshare.domain.post.PostInteractionRepository
import kotlinx.coroutines.*
import org.redisson.api.RAtomicLong
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import java.util.concurrent.TimeUnit

@Component
class RedissonPostInteractionCounter(
    private val redisson: RedissonClient,
    private val postInteractionRepository: PostInteractionRepository,
) : PostInteractionCounter {

    override fun incrementCount(postId: Long, type: InteractionType): Long {
        val atomicLong = redisson.getAtomicLong("interaction_count:${createRedisKey(postId, type)}")

        if (!atomicLong.isExists) {
            initializeAtomicLong(postId, type ,atomicLong)
        }

        val newCount = atomicLong.incrementAndGet()
        val expirationTime = Instant.now().plus(Duration.ofMinutes(TTL_DURATION))
        atomicLong.expire(expirationTime)
        trackModifiedPostInteraction(postId, type)
        return newCount
    }

    override fun decrementCount(postId: Long, type: InteractionType): Long {
        val atomicLong = redisson.getAtomicLong("interaction_count:${createRedisKey(postId, type)}")

        if (!atomicLong.isExists) {
            initializeAtomicLong(postId, type, atomicLong)
        }

        val newCount = atomicLong.decrementAndGet()

        val expirationTime = Instant.now().plus(Duration.ofMinutes(TTL_DURATION))
        atomicLong.expire(expirationTime)
        trackModifiedPostInteraction(postId, type)
        return newCount
    }

    override fun getCount(postId: Long, type: InteractionType): Long {
        val atomicLong = redisson.getAtomicLong("interaction_count:${createRedisKey(postId, type)}")

        if(atomicLong.isExists.not()) {
            initializeAtomicLong(postId, type, atomicLong)
        }

        val expirationTime = Instant.now().plus(Duration.ofMinutes(TTL_DURATION))
        atomicLong.expire(expirationTime)
        return atomicLong.get()
    }

    private fun initializeAtomicLong(postId: Long, type: InteractionType, atomicLong: RAtomicLong) {
        val lock = redisson.getLock("interaction_count_lock:${createRedisKey(postId, type)}")
        val lockAcquired = lock.tryLock(5, 10, TimeUnit.SECONDS)
        if (lockAcquired) {
            try {
                if (!atomicLong.isExists) {
                    val countFromDB = fetchCountFromDatabase(postId, type)
                    atomicLong.set(countFromDB)
                    trackModifiedPostInteraction(postId, type)
                }
            } finally {
                lock.unlock()
            }
        } else {
            throw RuntimeException("Could not acquire lock to initialize atomic long for postId: $postId")
        }
    }

    private fun fetchCountFromDatabase(postId: Long, type: InteractionType): Long {
        return postInteractionRepository.findByPostIdAndType(postId, type)?.count ?: 0L
    }

    private fun trackModifiedPostInteraction(postId: Long, type: InteractionType) {
        redisson.getSet<Long>("modified_post_ids:$type").add(postId)
    }

    private fun createRedisKey(postId: Long, type: InteractionType): String {
        return "$postId:$type"
    }


    @OptIn(DelicateCoroutinesApi::class)
    @Scheduled(fixedRate = 10 * 60 * 1000) // Run every 10 minutes
    fun scheduledPersistCounts() = GlobalScope.launch {
        persistCounts()
    }

    private suspend fun persistCounts() = coroutineScope {

        InteractionType.entries.forEach { type ->
            launch {
                val lockKey = "persist_counts_lock:$type"
                val lock: RLock = redisson.getLock(lockKey)
                val lockAcquired = lock.tryLock(5, 10, TimeUnit.MINUTES)
                if (lockAcquired) {
                    try {
                        persistCountsForType(type)
                    } finally {
                        lock.unlock()
                    }
                } else {
                    println("Failed to acquire lock for type: $type")
                }
            }
        }
    }

    private suspend fun persistCountsForType(type: InteractionType) = withContext(Dispatchers.IO) {
        val modifiedPostIdsSet = redisson.getSet<Long>("modified_post_ids:$type")
        val postIds = modifiedPostIdsSet.readAll()
        if (postIds.isEmpty()) return@withContext

        postIds.forEach { postId ->
            val currentCount = getCount(postId, type)
            postInteractionRepository.findByPostIdAndType(postId, type)?.run {
                this.updateCount(currentCount)
                postInteractionRepository.save(this)
            }
        }

        modifiedPostIdsSet.delete()
    }
    companion object {
        private const val TTL_DURATION = 15L
    }
}