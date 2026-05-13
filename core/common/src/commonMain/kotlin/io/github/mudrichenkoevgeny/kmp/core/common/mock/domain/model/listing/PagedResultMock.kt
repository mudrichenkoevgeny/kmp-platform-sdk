package io.github.mudrichenkoevgeny.kmp.core.common.mock.domain.model.listing

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult

@InternalApi
fun <T> pagedResultMock(
    items: List<T> = emptyList(),
    totalCount: Long = items.size.toLong(),
    pageNumber: Int = 1,
    pageSize: Int = 20,
    totalPages: Long = 1
) = PagedResult(
    items = items,
    totalCount = totalCount,
    pageNumber = pageNumber,
    pageSize = pageSize,
    totalPages = totalPages
)