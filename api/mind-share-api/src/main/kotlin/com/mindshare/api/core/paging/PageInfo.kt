package com.mindshare.api.core.paging

import com.mindshare.api.core.util.toPageToken

data class PageInfo<T>(
    val pageToken: String?,
    val data: List<T>,
    val hasNext: Boolean
) {
    companion object {
        fun <T> List<T>.pagination(
            expectedSize: Int,
            firstPageToken: (T) -> Any,
            secondPageToken: (T) -> Any
        ): PageInfo<T> {
            if (size <= expectedSize) {
                return PageInfo(null, this, hasNext = false)
            }

            val lastValue = this[expectedSize - 1]
            val tokenPair = firstPageToken(lastValue) to secondPageToken(lastValue)
            val pageToken = tokenPair.toPageToken()

            return PageInfo(pageToken, this.subList(0, expectedSize), hasNext = true)
        }
    }
}