package com.jk.projectboard.dto.request;

import com.jk.projectboard.dto.ArticleDto;
import com.jk.projectboard.dto.HashtagDto;
import com.jk.projectboard.dto.UserAccountDto;

import java.util.Set;

public record ArticleRequest(
        String title,
        String content
) {
    public static ArticleRequest of(String title, String content) {
        return new ArticleRequest(title, content);
    }

    public ArticleDto toDto(UserAccountDto userAccountDto) {
        return toDto(userAccountDto, null);
    }
    public ArticleDto toDto(UserAccountDto userAccountDto, Set<HashtagDto> hashtagDtos) {
        return ArticleDto.of(
                userAccountDto
                , title
                , content
                , hashtagDtos
        );
    }
}
