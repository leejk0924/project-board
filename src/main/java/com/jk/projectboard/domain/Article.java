package com.jk.projectboard.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString(callSuper = true)
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy"),
})
@Entity
public class Article extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "userId")
    private UserAccount userAccount; // 유저 정보 (ID)
    @Setter
    @Column(nullable = false)
    private String title;
    @Setter
    @Column(nullable = false, length = 10000)
    private String content;
    @Setter
    private String hashtag;
    // toString 순환참조 막기 위해 아래 어노테이션 사용
    @ToString.Exclude
    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();

    // 하이버네이트 구현자를 가질 때 기본생성자를 가지고 있어야함
    protected Article() {
    }

    private Article(UserAccount userAccount, String title, String content, String hashtag) {
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }
    public static Article of(UserAccount userAccount, String title, String content, String hashtag) {
        return new Article(userAccount, title, content, hashtag);
    }
    // equals , hashCode 구현 (동일성, 동등성 검사를 위해)
    // DB에서 id가 동일하면 동등성, 동일성이 적합하다 (장점 : 성능을 높일 수 있음, 고로 롬복 사용 x)


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article that)) return false;
        // 영속화 되지 않았다면 다른 값으로 취급함
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
