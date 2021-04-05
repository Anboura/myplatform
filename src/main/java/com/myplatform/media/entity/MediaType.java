package com.myplatform.media.entity;

import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="media-type")
public class MediaType {
    @Indexed
    private String type;
    @Field("release-type")
    private String releaseType;
    @Field("iteration-type")
    private String iterationType;
}
