package com.jk.projectboard.dto.request;

import com.jk.projectboard.dto.ArticleDto;
import com.jk.projectboard.dto.UserAccountDto;

public record ArticleRequest(
        String title,
        String content,
        String hashtag
) {
    public ArticleRequest of(String title, String content, String hashtag) {
        return new ArticleRequest(title, content, hashtag);
    }

    public ArticleDto toDto(UserAccountDto userAccountDto) {
        return ArticleDto.of(
                userAccountDto
                , title
                , content
                , hashtag
        );
    }
}