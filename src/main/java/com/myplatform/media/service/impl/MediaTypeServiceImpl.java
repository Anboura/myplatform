package com.myplatform.media.service.impl;

import com.myplatform.media.dto.MediaDto;
import com.myplatform.media.dto.MediaTypeDto;
import com.myplatform.media.entity.MediaType;
import com.myplatform.media.exception.MediaTypeNotFoundException;
import com.myplatform.media.mappers.MediaTypeMapper;
import com.myplatform.media.mappers.MediaTypeMapperImpl;
import com.myplatform.media.repository.MediaTypeRepository;
import com.myplatform.media.service.MediaTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MediaTypeServiceImpl implements MediaTypeService {
  @Autowired private MediaTypeRepository mediaTypeRepository;
  @Autowired private MediaTypeMapper mediaTypeMapper;

  @Override
  public List<MediaTypeDto> getMediaTypes() {
    log.info("Get Media Types");
    return mediaTypeRepository.findAll().stream()
        .map(mediaType -> mediaTypeMapper.mediaToMediaDto(mediaType))
        .collect(Collectors.toList());
  }

  @Override
  public MediaTypeDto getMediaTypeByType(final String type) throws MediaTypeNotFoundException {
    final Optional<MediaType> mediaTypeOptional = findMediaTypeByType(type);
    if (!mediaTypeOptional.isPresent()) {
      final String errorMessage = "Media Type " + type + " not found";
      log.error(errorMessage);
      throw new MediaTypeNotFoundException(errorMessage);
    }
    return mediaTypeMapper.mediaToMediaDto(mediaTypeOptional.get());
  }

  @Override
  public Optional<MediaType> findMediaTypeByType(String type) {
    log.info("Searching for Media Type " + type);
    return mediaTypeRepository.findByType(type);
  }
}
