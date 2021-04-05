package com.myplatform.media.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MediaDto {
  private String id;
  @NotBlank
  private String uid;
  @NotBlank
  private String title;
  @NotBlank
  private String type;
  private String iterationTitle;
  private int iterationNumber;
  @NotEmpty
  private List<String> categories = new ArrayList<>();
  private List<String> sources = new ArrayList<>();
  private boolean isFavourite;
  private int release;
  private int rating;

}
