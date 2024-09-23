package com.jk.projectboard.repository;

import com.jk.projectboard.domain.Article;
import com.jk.projectboard.domain.UserAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

// junit5에서는 테스트에서도 생성자 주입 가능 -> @DataJpaTest 에 @ExtendWith(SpringExtension.class) 가 있음 -> Autowired 로직 있음
@DisplayName("JPA 연결 테스트")
// spring:config.activate.on-profile 이 testdb 인 것을 설정으로 사용
@ActiveProfiles("testdb")
// 2. Test DB 를 따로 불러오지 않고 application.yaml 에 설정되어있는 DB를 불러온다.
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaRepositoryTest.TestJpaConfig.class)
// @DataJpaTest ->  @Transactional 어노테이션 포함
// 1. 아래 어노테이션이 Embedded database 불러옴
@DataJpaTest
class JpaRepositoryTest {
    private final ArticleRepository articleRepository;
    private ArticleCommentRepository articleCommentRepository;
    private final UserAccountRepository userAccountRepository;

    public JpaRepositoryTest(@Autowired ArticleRepository articleRepository,
                             @Autowired ArticleCommentRepository articleCommentRepository,
                             @Autowired UserAccountRepository userAccountRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
        this.userAccountRepository = userAccountRepository;
    }
    @DisplayName("select 테스트")
    @Test
    void givenTestData_whenSelecting_thenWorksFine() {
        //Given
        //When
        List<Article> articles = articleRepository.findAll();

        //Then
        assertThat(articles)
                .isNotNull()
                .hasSize(123);
    }
    @DisplayName("insert 테스트")
    @Test
    void givenTestData_whenInserting_thenWorksFine() {
        //Given
        long previousCount = articleRepository.count();
        UserAccount userAccount = userAccountRepository.save(UserAccount.of("newUno", "pw", null, null, null));
        Article article = Article.of(userAccount, "new article", "new content", "#spring");

        //When
        articleRepository.save(article);

        //Then
        assertThat(articleRepository.count())
                .isEqualTo(previousCount + 1);
    }
    @DisplayName("update 테스트")
    @Test
    void givenTestData_whenUpdating_thenWorksFine() {
        //Given
        Article article = articleRepository.findById(1L).orElseThrow();
        String updatedHashtag = "#springboot";
        article.setHashtag(updatedHashtag);

        //When
        Article savedArticle = articleRepository.saveAndFlush(article);

        //Then
        assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag", updatedHashtag);
    }
    @DisplayName("delete 테스트")
    @Test
    void givenTestData_whenDeleting_thenWorksFine() {
        //Given
        Article article = articleRepository.findById(1L).orElseThrow();
        long previousArticleCount = articleRepository.count();
        long previousArticleCommentCount = articleCommentRepository.count();
        int deleteCommentsSize = article.getArticleComments().size();

        //When
        articleRepository.delete(article);

        //Then
        assertThat(articleRepository.count()).isEqualTo(previousArticleCount - 1);
        assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount - deleteCommentsSize);
    }

    @EnableJpaAuditing
    // test 시 에만 빈등록 하기 위해 TestConfiguration 설정
    @TestConfiguration
    public static class TestJpaConfig{
        @Bean
        public AuditorAware<String> auditorAware() {
            return () -> Optional.of("uno");
        }
    }
}