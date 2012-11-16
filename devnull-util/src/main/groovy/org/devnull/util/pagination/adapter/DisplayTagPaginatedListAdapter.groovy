package org.devnull.util.pagination.adapter

import groovy.util.logging.Slf4j
import org.devnull.util.pagination.Pagination
import org.devnull.util.pagination.Sort
import org.displaytag.pagination.PaginatedList
import org.displaytag.properties.SortOrderEnum

@Slf4j
class DisplayTagPaginatedListAdapter<T> implements PaginatedList, Pagination<T> {

    @Delegate
    Pagination<T> pagination

    DisplayTagPaginatedListAdapter(Pagination<T> pagination) {
        this.pagination = pagination
    }

    List getList() {
        return pagination.results
    }

    int getPageNumber() {
        return pagination.page
    }

    int getObjectsPerPage() {
        return pagination.max
    }

    int getFullListSize() {
        if (pagination.total > Integer.MAX_VALUE) {
            log.warn("Total results size ({}) is too large for DisplayTag PaginatedList. Using max value ({}) instead.", total, Integer.MAX_VALUE)
            return Integer.MAX_VALUE
        }
        return pagination.total.toInteger()
    }

    String getSortCriterion() {
        return firstSort?.field
    }

    SortOrderEnum getSortDirection() {
        return firstSort?.direction == Sort.DESC ? SortOrderEnum.DESCENDING : SortOrderEnum.ASCENDING
    }

    String getSearchId() {
        return null
    }

    protected getFirstSort() {
        if (!pagination.sorts) return null
        if (pagination.sorts.size() > 1) {
            log.warn("Display tag does not support multiple sort columns: {}. Using the first value.",
                    pagination.sorts.collect { [(it.field): it.direction] })
        }
        return pagination.sorts.first()
    }
}
