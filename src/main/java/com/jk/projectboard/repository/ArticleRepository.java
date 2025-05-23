package com.jk.projectboard.repository;

import com.jk.projectboard.domain.Article;
import com.jk.projectboard.domain.QArticle;
import com.jk.projectboard.repository.querydsl.ArticleRepositoryCustom;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        ArticleRepositoryCustom,
        // Entity 안에 필드의 기본 검색 기능 추가
        QuerydslPredicateExecutor<Article>,
        QuerydslBinderCustomizer<QArticle> {
    // QuerydslBinderCustomizer extends 시 customize 를 Override 해줘야한다. -> 검색 세부사항 재구성
    Page<Article> findByTitleContaining(String title, Pageable pageable);
    Page<Article> findByContentContaining(String content, Pageable pageable);
    Page<Article> findByUserAccount_UserIdContaining(String userId, Pageable pageable);
    Page<Article> findByUserAccount_NicknameContaining(String nickname, Pageable pageable);

    void deleteByIdAndUserAccount_UserId(Long articleId, String userId);
    @Override
    default void customize(QuerydslBindings bindings, QArticle root){
        // 선택적 검색 설정 -> 기본값 false 에서 true 로 설정
        bindings.excludeUnlistedProperties(true);
        // title, content, hashtag, createdAt, createdBy 를 검색 할 수 있도록 추가
        bindings.including(root.title, root.content, root.hashtags, root.createdAt, root.createdBy);

//        bindings.bind(root.title).first((StringExpression::likeIgnoreCase));        // like ''
        bindings.bind(root.title).first((StringExpression::containsIgnoreCase));    // like '%{v}%'  -> contain 기능이 편리
        bindings.bind(root.content).first((StringExpression::containsIgnoreCase));
        bindings.bind(root.hashtags.any().hashtagName).first((StringExpression::containsIgnoreCase));
        bindings.bind(root.createdAt).first((DateTimeExpression::eq));
        bindings.bind(root.createdBy).first((StringExpression::containsIgnoreCase));

    }
}