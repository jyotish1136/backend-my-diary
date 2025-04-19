package com.mynotes.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "likes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "post_id"})
})
@Data
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = true)
    @JsonIgnore
    private Post post;

    @Column(nullable = false)
    private LocalDateTime likedAt = LocalDateTime.now();

    @Transient
    @JsonProperty("userId")
    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    @Transient
    @JsonProperty("postId")
    public Long getPostId() {
        return post != null ? post.getId() : null;
    }
}
