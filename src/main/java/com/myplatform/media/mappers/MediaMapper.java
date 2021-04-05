package com.myplatform.media.mappers;

import com.myplatform.media.dto.MediaDto;
import com.myplatform.media.entity.Media;
import org.mapstruct.Mapper;

@Mapper
public interface MediaMapper {
  MediaDto mediaToMediaDto(Media media);

  Media mediaDtoToMedia(MediaDto mediaDto);
}
