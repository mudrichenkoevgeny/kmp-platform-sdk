package io.github.mudrichenkoevgeny.kmp.core.common.mapper.pagedresult

import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult

fun <T> PagedResult.Companion.empty(): PagedResult<T> = PagedResult(
    items = emptyList(),
    totalCount = 0,
    pageNumber = 1,
    pageSize = 20,
    totalPages = 0
)