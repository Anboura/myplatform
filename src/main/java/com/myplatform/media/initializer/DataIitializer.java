package com.myplatform.media.initializer;

import com.myplatform.media.entity.Category;
import com.myplatform.media.entity.Media;
import com.myplatform.media.entity.MediaType;
import com.myplatform.media.repository.CategoryRepository;
import com.myplatform.media.repository.MediaRepository;
import com.myplatform.media.repository.MediaTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

@Component
public class DataIitializer implements CommandLineRunner {
  @Autowired private MediaRepository mediaRepository;
  @Autowired private MediaTypeRepository mediaTypeRepository;
  @Autowired private CategoryRepository categoryRepository;

  @Override
  public void run(String... args) throws Exception {
    createMediasInDb();
    createMediaTypesInDb();
    createCategoriesInDb();
  }

  private void createMediasInDb() {
    final List<Media> mediaResults = mediaRepository.findAll();
    if (CollectionUtils.isEmpty(mediaResults)) {
      Media blake =
          new Media()
              .builder()
              .uid("2")
              .isFavourite(false)
              .release(35)
              .iterationNumber(4)
              .iterationTitle("The dark empress")
              .title("blake")
              .type("book")
              .sources(Arrays.asList("www.fnac.com"))
              .categories(Arrays.asList("detective", "action", "supernatural"))
              .build();
      Media memorize =
          new Media()
              .builder()
              .uid("1")
              .isFavourite(false)
              .release(51)
              .title("memorize")
              .iterationNumber(1)
              .type("manga")
              .sources(Arrays.asList("https://mangatx.com"))
              .categories(Arrays.asList("fantasy", "rpg", "action"))
              .build();

      Media tbate =
          new Media()
              .builder()
              .uid("1")
              .isFavourite(true)
              .release(82)
              .iterationNumber(1)
              .title("The Beginning After the End")
              .type("manga")
              .sources(Arrays.asList("https://mangatx.com"))
              .categories(Arrays.asList("fantasy", "action", "isekai", "reborn"))
              .build();

      Media bb =
          new Media()
              .builder()
              .uid("1")
              .isFavourite(true)
              .release(75)
              .title("Breaking Bad")
              .iterationNumber(2)
              .type("series")
              .sources(Arrays.asList("www.netfilix.com"))
              .categories(Arrays.asList("detective", "action"))
              .build();
      mediaRepository.saveAll(Arrays.asList(blake, memorize, tbate, bb));
    }
  }

  private void createMediaTypesInDb() {
    List<MediaType> mediaTypeList = mediaTypeRepository.findAll();
    if (CollectionUtils.isEmpty(mediaTypeList)) {
      mediaTypeRepository.saveAll(
          Arrays.asList(
              new MediaType()
                  .builder()
                  .type("manga")
                  .releaseType("chapter")
                  .iterationType("tom")
                  .build(),
              new MediaType()
                  .builder()
                  .type("book")
                  .releaseType("chapter")
                  .iterationType("volume")
                  .build(),
              new MediaType()
                  .builder()
                  .type("anime")
                  .releaseType("episode")
                  .iterationType("season")
                  .build(),
              new MediaType()
                  .builder()
                  .type("series")
                  .releaseType("episode")
                  .iterationType("season")
                  .build()));
    }
  }

  private void createCategoriesInDb() {
    List<Category> categoryList = categoryRepository.findAll();
    if (CollectionUtils.isEmpty(categoryList)) {
      categoryRepository.saveAll(
          Arrays.asList(
              new Category().builder().label("action").build(),
              new Category().builder().label("supernatural").build(),
              new Category().builder().label("adventure").build(),
              new Category().builder().label("thriller").build(),
              new Category().builder().label("sci-fi").build(),
              new Category().builder().label("post apocalyptic").build(),
              new Category().builder().label("drama").build(),
              new Category().builder().label("romance").build(),
              new Category().builder().label("comic").build(),
              new Category().builder().label("historical").build(),
              new Category().builder().label("fiction").build(),
              new Category().builder().label("detective").build(),
              new Category().builder().label("historical fiction").build(),
              new Category().builder().label("slice of life").build(),
              new Category().builder().label("horror").build(),
              new Category().builder().label("fantasy").build(),
              new Category().builder().label("isekay").build(),
              new Category().builder().label("rpg").build(),
              new Category().builder().label("reborn").build()));
    }
  }
}
