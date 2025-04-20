package com.mynotes.DTO;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private ArrayList<String> hashtags;
    private LocalDateTime date;
    private int likeCount;
    private int commentCount;
    private Long userId;
    private String username;
    private String avatar;
}
