import com.mindshare.api.infra.post.RedissonPostInteractionCounter
import com.mindshare.domain.post.InteractionType
import com.mindshare.domain.post.PostInteraction
import com.mindshare.domain.post.PostInteractionRepository
import org.junit.jupiter.api.*
import org.mockito.Mockito.*
import kotlinx.coroutines.*
import org.redisson.api.RedissonClient
import org.redisson.Redisson
import org.redisson.config.Config
import org.junit.jupiter.api.Assertions.*
import java.util.concurrent.Executors
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.Network
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RedissonPostInteractionCounterTest {

    private lateinit var redisContainer: GenericContainer<*>
    private lateinit var redissonClient: RedissonClient
    private lateinit var postInteractionRepository: PostInteractionRepository
    private lateinit var counter: RedissonPostInteractionCounter

    @BeforeAll
    fun setup() {
        val network = Network.newNetwork()

        redisContainer = GenericContainer(DockerImageName.parse("redis:7.0.11")).apply {
            withExposedPorts(6379)
            withNetwork(network)
            withNetworkAliases("redis-ㅅㄷㄴㅅ")
            waitingFor(Wait.forListeningPort())
        }

        // Redisson 클라이언트 설정 ㅇ.ㅇㅇ 해결좀 ㅇㅇ..
        val redisAddress = "redis://${redisContainer.host}:${redisContainer.getMappedPort(6379)}"
        val config = Config()
        config.useSingleServer().address = redisAddress
        redissonClient = Redisson.create(config)

        // Mockito를 사용하여 리포지토리 모킹
        postInteractionRepository = mock(PostInteractionRepository::class.java)

        // 테스트 대상 객체 생성
        counter = RedissonPostInteractionCounter(redissonClient, postInteractionRepository)
    }

    @AfterAll
    fun tearDown() {
        redissonClient.shutdown()
        redisContainer.stop()
    }

    @Test
    @Tag("docker-exclude")
    fun `동시성 증가 연산 테스트`() = runBlocking {
        val postId = 1L
        val type = InteractionType.VIEW_COUNT

        // 데이터베이스에서 조회 시 null을 반환하도록 설정
        `when`(postInteractionRepository.findByPostIdAndType(postId, type)).thenReturn(null)

        val numThreads = 100
        val numIncrementsPerThread = 100

        val dispatcher = Executors.newFixedThreadPool(numThreads).asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher)

        val jobs = mutableListOf<Job>()

        for (i in 1..numThreads) {
            val job = scope.launch {
                repeat(numIncrementsPerThread) {
                    counter.incrementCount(postId, type)
                }
            }
            jobs.add(job)
        }

        // 모든 코루틴이 완료될 때까지 대기
        jobs.forEach { it.join() }

        // 최종 카운트 검증
        val expectedCount = numThreads * numIncrementsPerThread.toLong()
        val actualCount = counter.getCount(postId, type)

        redissonClient.getKeys().delete("interaction_count:$postId:$type")
        assertEquals(expectedCount, actualCount)
    }

    @Test
    @Tag("docker-exclude")
    fun `기존에 PostInteraction 값이 존재할 경우 동시성 증가 연산 테스트`() = runBlocking {
        val postId = 1L
        val type = InteractionType.VIEW_COUNT
        val basePostInteractionCount = 300L

        val postInteraction = PostInteraction(postId, basePostInteractionCount, type)
        `when`(postInteractionRepository.findByPostIdAndType(postId, type)).thenReturn(postInteraction)

        val numThreads = 100
        val numIncrementsPerThread = 100

        val dispatcher = Executors.newFixedThreadPool(numThreads).asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher)

        val jobs = mutableListOf<Job>()

        for (i in 1..numThreads) {
            val job = scope.launch {
                repeat(numIncrementsPerThread) {
                    counter.incrementCount(postId, type)
                }
            }
            jobs.add(job)
        }

        // 모든 코루틴이 완료될 때까지 대기
        jobs.forEach { it.join() }

        // 최종 카운트 검증
        val expectedCount = numThreads * numIncrementsPerThread.toLong() + basePostInteractionCount
        val actualCount = counter.getCount(postId, type)

        redissonClient.getKeys().delete("interaction_count:$postId:$type")
        assertEquals(expectedCount, actualCount)
    }
}
