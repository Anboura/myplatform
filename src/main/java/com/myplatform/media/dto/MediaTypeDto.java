package com.myplatform.media.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MediaTypeDto {
    private String type;
    private String releaseType;
    private String iterationType;
}
