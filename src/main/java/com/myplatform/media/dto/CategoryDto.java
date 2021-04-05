package com.myplatform.media.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryDto {
    private String id;
    @NotBlank
    private String label;
}
