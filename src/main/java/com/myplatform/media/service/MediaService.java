package com.myplatform.media.service;

import com.myplatform.media.dto.MediaDto;
import com.myplatform.media.entity.Media;
import com.myplatform.media.exception.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MediaService {

  List<MediaDto> getMediaList();

  List<MediaDto> getUserMediaList(String uid, Map<String, Object> params);

  MediaDto getMediaById(String id) throws MediaNotFoundException;

  Optional<Media> findMediaById(String id) ;

  MediaDto getMediaDtoById(String id) throws MediaNotFoundException;

  String insertUserMedia(String uid, MediaDto mediaDto)
          throws MediaAlreadyExistException, CompletenessException;

  MediaDto updateUserMedia(String uid , MediaDto mediaDto) throws MediaNotFoundException, CompletenessException;

  Optional<Media> findMediaByTitle(String title);
}
