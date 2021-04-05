package com.myplatform.media.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Document
public class Category {
    @Id
    private String id;
    private String label;
}
