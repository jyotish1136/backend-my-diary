package com.mynotes.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
        private Long id;
        private Long postId;
        private String comment;
        private String username;
        private String avatar;
        private Long userId;
        private LocalDateTime date;
}

