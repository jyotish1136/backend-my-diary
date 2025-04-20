package com.mynotes.DTO;

import lombok.Data;

@Data
public class CommentDeleteDTO {
    private Long postId;
    private Long commentId;
}
