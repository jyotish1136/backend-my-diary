package com.mynotes.DTO;
import com.mynotes.entities.Comment;
import com.mynotes.entities.Like;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private ArrayList<String> hashtags;
    private LocalDateTime date;
    private int likeCount;
    private Long userid;
    private String username;
    private String avatar;
    private List<Like> likedUsers;
    private List<Comment> comments;
}
