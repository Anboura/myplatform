package com.myplatform.media.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Media {
  @Id
  private String id;
  @Indexed
  private String uid;
  private String title;
  private String type;
  @Field(name="iteration-title")
  private String iterationTitle;
  @Field(name="iteration-Number")
  private int iterationNumber;
  private List<String> categories = new ArrayList<>();
  private List<String> sources = new ArrayList<>();
  @Field(name="favourite")
  private boolean isFavourite;
  private int release;
  private int rating;
}
