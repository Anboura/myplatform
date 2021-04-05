package com.myplatform.media.service;

import com.myplatform.media.dto.MediaTypeDto;
import com.myplatform.media.entity.MediaType;
import com.myplatform.media.exception.MediaTypeNotFoundException;

import java.util.List;
import java.util.Optional;

public interface MediaTypeService {
    List<MediaTypeDto> getMediaTypes();
    MediaTypeDto getMediaTypeByType(String type) throws MediaTypeNotFoundException;
    Optional<MediaType> findMediaTypeByType(String type);
}
