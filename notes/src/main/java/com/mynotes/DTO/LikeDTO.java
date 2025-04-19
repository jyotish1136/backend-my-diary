package com.mynotes.DTO;

import com.mynotes.entities.Post;
import com.mynotes.entities.User;
import lombok.Data;

@Data
public class LikeDTO {
        private Long userId;
        private Long postId;
}
