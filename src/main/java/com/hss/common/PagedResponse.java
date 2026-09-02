package com.hss.common;

import java.util.List;

/**
 * Generic paginated response wrapper.
 *
 * @param <T> the element type
 */
public class PagedResponse<T> {

    public List<T> content;
    public int page;
    public int size;
    public long totalElements;
    public int totalPages;

    public PagedResponse(List<T> content, int page, int size, long totalElements) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 0;
    }
}
