package com.myplatform.media.service;

import com.myplatform.media.dto.CategoryDto;
import com.myplatform.media.dto.MediaDto;
import com.myplatform.media.dto.MediaTypeDto;
import com.myplatform.media.entity.Media;
import com.myplatform.media.exception.CategoryNotFoundException;
import com.myplatform.media.exception.CompletenessException;
import com.myplatform.media.exception.MediaAlreadyExistException;
import com.myplatform.media.exception.MediaTypeNotFoundException;
import com.myplatform.media.mappers.MediaMapperImpl;
import com.myplatform.media.repository.CategoryRepository;
import com.myplatform.media.repository.MediaRepository;
import com.myplatform.media.repository.MediaTypeRepository;
import com.myplatform.media.service.impl.MediaServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class MediaServiceTest {
  @Spy
  MediaMapperImpl mediaMapper;
  @MockBean MediaRepository mediaRepository;
  @MockBean MediaTypeService mediaTypeService;
  @MockBean CategoryRepository categoryRepository;
  @MockBean MediaTypeRepository mediaTypeRepository;
  @MockBean CategoryService categoryService;
  @InjectMocks
  MediaServiceImpl mediaService;
  @SpyBean  MongoTemplate mongoTemplate;
  @Mock
  MongoTemplate mongo;

  @Test
  public void test() {}

  @Test
  public void getMediaList_Should_Return_Media_List() {
    final Media media1 = new Media().builder().uid("1").id("1").title("title1").build();
    final Media media2 = new Media().builder().uid("1").id("2").title("title2").build();
    List<Media> mediaList = Arrays.asList(media1, media2);
    Mockito.when(mediaRepository.findAll()).thenReturn(mediaList);
    Mockito.when(mediaMapper.mediaToMediaDto(media1)).thenCallRealMethod();
    Mockito.when(mediaMapper.mediaToMediaDto(media2)).thenCallRealMethod();
    List<MediaDto> results = mediaService.getMediaList();
    Assertions.assertNotNull(results);
    Assertions.assertEquals(2, results.size());
  }

  @Test
  public void getUserMediaList_Should_Return_Media_List() {
    final Map<String, Object> params = new HashMap<>();
    final String uid = "1";
    final Media media1 = new Media().builder().uid(uid).id("1").title("title1").build();
    final Media media2 = new Media().builder().uid(uid).id("2").title("title2").build();
    final Query query = new Query(Criteria.where("uid").is(uid));
    List<Media> mediaList = Arrays.asList(media1, media2);
    Mockito.when(mongoTemplate.find(query, Media.class)).thenReturn(mediaList);
    Mockito.when(mediaMapper.mediaToMediaDto(media1)).thenCallRealMethod();
    Mockito.when(mediaMapper.mediaToMediaDto(media2)).thenCallRealMethod();
    List<MediaDto> results = mediaService.getUserMediaList("1", params);
    Assertions.assertNotNull(results);
    Assertions.assertEquals(2, results.size());
  }

  @Test
  public void insertUserMedia_Should_Return_InsertedMedia_Id()
      throws CategoryNotFoundException, MediaTypeNotFoundException, MediaAlreadyExistException,
          CompletenessException {
    final String uid = "1";
    final String mediaSavedId = "123";
    final String categoryLabel = "action";
    final String type = "manga";
    final MediaTypeDto mediaTypeDtoFound = new MediaTypeDto().builder().type(type).build();
    final CategoryDto categoryFound =
        new CategoryDto().builder().id("1").label(categoryLabel).build();
    final MediaDto mediaDtoToInsert =
        new MediaDto()
            .builder()
            .uid(uid)
            .id("1")
            .title("title1")
            .type(type)
            .categories(Arrays.asList(categoryLabel))
            .build();
    final Media mediaSaved = mediaMapper.mediaDtoToMedia(mediaDtoToInsert);
    mediaSaved.setId(mediaSavedId);
    Query query = new Query();
    Mockito.when(mongoTemplate.findOne(query, Media.class)).thenReturn(null);
    Mockito.when(categoryService.getCategoryByLabel(categoryLabel)).thenReturn(categoryFound);
    Mockito.when(mediaTypeService.getMediaTypeByType(type)).thenReturn(mediaTypeDtoFound);
    Mockito.when(mediaMapper.mediaDtoToMedia(mediaDtoToInsert)).thenCallRealMethod();
    Mockito.when(mediaRepository.insert(any(Media.class))).thenReturn(mediaSaved);
    final String resultId = mediaService.insertUserMedia(uid, mediaDtoToInsert);
    Assertions.assertEquals(resultId, mediaSavedId);
    verify(categoryService, times(1)).getCategoryByLabel(categoryLabel);
    verify(mediaTypeService, times(1)).getMediaTypeByType(type);
    verify(mediaRepository, times(1)).insert(any(Media.class));
  }

  @Test
  public void insertUserMedia_Should_Throw_MediaAlreadyExistException() {
    final String uid = "1";
    final MediaDto mediaDtoToInsert =
        new MediaDto()
            .builder()
            .uid(uid)
            .id("1")
            .title("title1")
            .type("manga")
            .categories(Arrays.asList("action"))
            .build();
    final Media mediaFound = mediaMapper.mediaDtoToMedia(mediaDtoToInsert);
    ArgumentCaptor<Query> argument = ArgumentCaptor.forClass(Query.class);
//    Query queryLimit=Mockito.mock(Query.class);
    Mockito.when(mongo.getCollectionName(any())).thenReturn("media");
    Mockito.when(mongo.findOne(Mockito.any(Query.class), Mockito.eq(Media.class))).thenReturn(mediaFound);
//    verify(mongo).find(argument.capture(), eq(Media.class));
    Assertions.assertThrows(
        MediaAlreadyExistException.class,
        () -> mediaService.insertUserMedia(uid, mediaDtoToInsert));
  }
}
