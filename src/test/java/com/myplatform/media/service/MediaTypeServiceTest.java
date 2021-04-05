package com.myplatform.media.service;

import com.myplatform.media.dto.MediaTypeDto;
import com.myplatform.media.entity.MediaType;
import com.myplatform.media.exception.MediaTypeNotFoundException;
import com.myplatform.media.mappers.MediaTypeMapperImpl;
import com.myplatform.media.repository.MediaTypeRepository;
import com.myplatform.media.service.impl.MediaTypeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class MediaTypeServiceTest {
  @MockBean MediaTypeRepository mediaTypeRepository;
  @Spy
  MediaTypeMapperImpl mediaTypeMapper;
  @InjectMocks MediaTypeServiceImpl mediaTypeService;

  @Test
  public void getMediaTypeByType_Should_Return_Media() throws MediaTypeNotFoundException {
    final String type = "series";
    final MediaType mediaType =
        new MediaType().builder().type(type).iterationType("season").releaseType("episode").build();

    Mockito.when(mediaTypeRepository.findByType(type)).thenReturn(Optional.of(mediaType));
    Mockito.when(mediaTypeMapper.mediaToMediaDto(mediaType)).thenCallRealMethod();
    final MediaTypeDto mediaTypeDto = mediaTypeService.getMediaTypeByType(type);
    assertNotNull(mediaTypeDto);
    assertEquals(mediaTypeDto.getType(), type);
    assertEquals(mediaTypeDto.getIterationType(), mediaType.getIterationType());
    assertEquals(mediaTypeDto.getReleaseType(), mediaType.getReleaseType());
    verify(mediaTypeRepository, times(1)).findByType(type);
  }

  @Test
  public void getMediaTypeByType_Should_ShouldThrow_MediaNotFoundException() {
    final String type = "notFound";
    when(mediaTypeRepository.findByType(type)).thenReturn(Optional.ofNullable(null));
    assertThrows( MediaTypeNotFoundException.class, () -> {mediaTypeService.getMediaTypeByType(type);});
  }
}
