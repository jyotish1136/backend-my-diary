package com.mynotes.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @JsonIgnore
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Lob
    @Column(name="comments",nullable = false)
    private String comment;

    @Column(nullable = false)
    private LocalDateTime date;

    @Transient
    @JsonProperty("username")
    public String getUserId() {
        return user != null ? user.getUsername() : null;
    }

    @Transient
    @JsonProperty("avatar")
    public String getAvatar() {
        return user != null ? user.getAvatar() : null;
    }
}
