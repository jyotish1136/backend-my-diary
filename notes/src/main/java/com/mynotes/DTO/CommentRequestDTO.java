package com.mynotes.DTO;

import lombok.Data;

@Data
public class CommentRequestDTO {
    private Long userId;
    private Long postId;
    private String comment;
}
