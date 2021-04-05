package com.myplatform.media.mappers;

import com.myplatform.media.dto.MediaTypeDto;
import com.myplatform.media.entity.MediaType;
import org.mapstruct.Mapper;

@Mapper
public interface MediaTypeMapper {

  MediaTypeDto mediaToMediaDto(MediaType mediaTypemediaType);

  MediaType mediaDtoToMedia(MediaTypeDto mediaTypeDto);
}
