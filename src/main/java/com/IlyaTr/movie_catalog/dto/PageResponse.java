package com.IlyaTr.movie_catalog.dto;



import com.IlyaTr.movie_catalog.dto.movie.MovieReadDto;
import lombok.Value;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

@Value
public class PageResponse<T>{
    List<T> content;
    Metadata metadata;

    public static <T> PageResponse<T> of(Page<T> page) {
        var metadata = new Metadata(page.getNumber(), page.getSize(), page.getTotalPages(),
                page.hasPrevious(), page.hasNext());
        return new PageResponse<>(page.getContent(), metadata);
    }

    @Value
    public static class Metadata{
        int page;
        int size;
        long totalElements;
        boolean hasPrevious;
        boolean hasNext;
    }
}
