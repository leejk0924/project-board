package com.jk.projectboard.repository;

import com.jk.projectboard.domain.ArticleComment;
import com.jk.projectboard.domain.QArticleComment;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ArticleCommentRepository extends
        JpaRepository<ArticleComment, Long>,
        QuerydslPredicateExecutor<ArticleComment>,
        QuerydslBinderCustomizer<QArticleComment> {

    List<ArticleComment> findByArticle_Id(Long articleId);

    void deleteByIdAndUserAccount_UserId(Long articleCommentId, String userId);
    @Override
    default void customize(QuerydslBindings bindings, QArticleComment root){
        // 선택적 검색 설정 -> 기본값 false 에서 true 로 설정
        bindings.excludeUnlistedProperties(true);
        // title, content, hashtag, createdAt, createdBy 를 검색 할 수 있도록 추가
        bindings.including(root.content, root.createdAt, root.createdBy);
        bindings.bind(root.content).first((StringExpression::containsIgnoreCase));
        bindings.bind(root.createdAt).first((DateTimeExpression::eq));
        bindings.bind(root.createdBy).first((StringExpression::containsIgnoreCase));
    }
}