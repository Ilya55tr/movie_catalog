package com.IlyaTr.movie_catalog.dto.filter;

import java.math.BigDecimal;

public record MovieFilter(String title, Integer releaseYear,
                          BigDecimal rating, Integer genre) {
}
