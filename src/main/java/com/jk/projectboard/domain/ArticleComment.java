package com.jk.projectboard.domain;

import java.time.LocalDateTime;

public class ArticleComment {
    private Long id;
    private String title;

    private LocalDateTime createAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;
}
