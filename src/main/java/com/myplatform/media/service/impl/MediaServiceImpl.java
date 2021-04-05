package com.myplatform.media.service.impl;

import com.myplatform.media.dto.MediaDto;
import com.myplatform.media.entity.Media;
import com.myplatform.media.exception.*;
import com.myplatform.media.mappers.MediaMapper;
import com.myplatform.media.repository.MediaRepository;
import com.myplatform.media.service.CategoryService;
import com.myplatform.media.service.MediaService;
import com.myplatform.media.service.MediaTypeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MediaServiceImpl implements MediaService {
  @Autowired private MediaMapper mediaMapper;
  @Autowired private MediaRepository mediaRepository;
  @Autowired private MongoTemplate mongoTemplate;
  @Autowired private MediaTypeService mediaTypeService;
  @Autowired private CategoryService categoryService;

  @Override
  public List<MediaDto> getMediaList() {
    log.info("Getting Media List");
    return mediaRepository.findAll().stream()
        .map(mediaMapper::mediaToMediaDto)
        .collect(Collectors.toList());
  }

  @Override
  public List<MediaDto> getUserMediaList(final String uid, final Map<String, Object> params) {
    log.info("Getting Media List for user " + uid);
    final Query query = userMediaQueryWithParams(params);
    query.addCriteria(Criteria.where("uid").is(uid));
    List<Media> mediaList = mongoTemplate.find(query, Media.class);
    return mediaList.stream().map(mediaMapper::mediaToMediaDto).collect(Collectors.toList());
  }

  @Override
  public Optional<Media> findMediaById(final String id) {
    return  mediaRepository.findById(id);
  }

  @Override
  public MediaDto getMediaById(String id) throws MediaNotFoundException {
    final MediaDto mediaDto = getMediaDtoById(id);
    log.info("Getting media by id " + id);
    if (mediaDto == null) {
      log.error("Media with id " + id + " not found");
      throw new MediaNotFoundException("the media with id " + id + " is not found");
    }
    return mediaDto;
  }

  @Override
  public MediaDto getMediaDtoById(final String id) throws MediaNotFoundException {
    final Optional<Media> mediaOptional= findMediaById(id);
    MediaDto mediaDto = null;
    if (!mediaOptional.isPresent()) {
      log.error("The media with id " + id + " is not found");
      throw new MediaNotFoundException("The media with id " + mediaDto.getId() + " is not found.");
    }
    return mediaMapper.mediaToMediaDto(mediaOptional.get());
  }

  @Override
  public String insertUserMedia(final String uid, final MediaDto mediaDto)
      throws MediaAlreadyExistException, CompletenessException {
    log.info("Inserting Media for user " +uid );
    if (StringUtils.isNotBlank(mediaDto.getId()) && isMediaAlreadyExist(uid, mediaDto)) {
      final String error = "The media already exist";
      log.error(error);
      throw new MediaAlreadyExistException(error);
    }
    // check if there are any completeness errors and throw a completeness exception
    manageCompletenessErrors(checkCompleteness(mediaDto));
    mediaDto.setId(null);
    mediaDto.setType(mediaDto.getType().toLowerCase());
    final Media result = mediaRepository.insert(mediaMapper.mediaDtoToMedia(mediaDto));
    return result.getId();
  }

  @Override
  public MediaDto updateUserMedia(final String uid, final MediaDto mediaDto) throws MediaNotFoundException, CompletenessException {
    log.info("updating the media with id " + mediaDto.getId() + " for the user " +uid);
    final MediaDto mediaToUpdate = getMediaById(mediaDto.getId());
    log.debug("media found id " + mediaToUpdate.getId()  + " for the user " + uid);
    // check if there are any completeness errors and throw a completeness exception
    manageCompletenessErrors(checkCompleteness(mediaDto));
    mediaToUpdate.setTitle(mediaDto.getTitle());
    mediaToUpdate.setCategories(mediaDto.getCategories());
    mediaToUpdate.setType(mediaDto.getType());
    mediaToUpdate.setRelease(mediaDto.getRelease());
    mediaToUpdate.setRating(mediaDto.getRating());
    mediaToUpdate.setSources(mediaDto.getSources());
    mediaToUpdate.setIterationNumber(mediaDto.getIterationNumber());
    mediaToUpdate.setIterationNumber(mediaDto.getIterationNumber());
    mediaToUpdate.setFavourite(mediaDto.isFavourite());
    // update the media
    final Media mediaSaved = mediaRepository.save(mediaMapper.mediaDtoToMedia(mediaToUpdate));
    return mediaMapper.mediaToMediaDto(mediaSaved);
  }

  // throw the CompletenessException
  private void manageCompletenessErrors(final Map<String, String> completenessErrors) throws CompletenessException {
    if (!completenessErrors.isEmpty()) {
      String error = "Media is not complete";
      error +=
              completenessErrors.entrySet().stream()
                      .map(e -> e.getKey() + " : " + e.getValue())
                      .collect(Collectors.joining(" , "));
      log.error(error);
      throw new CompletenessException(error, completenessErrors);
    }
  }

  @Override
  public Optional<Media> findMediaByTitle(final String title) {
    final Query query = new Query(Criteria.where("title").regex("." + title + ".", "i"));
    final Media media = mongoTemplate.findOne(query, Media.class);
    return Optional.ofNullable(media);
  }

  // Check completeness of a MediaDto Object for update or insert
  private Map<String, String> checkCompleteness(MediaDto mediaDto) {
    final Map<String, String> completenessErrors = new HashMap<>();
    log.info("checking completeness of the media");
    if (CollectionUtils.isEmpty(mediaDto.getCategories())) {
      completenessErrors.put("categories", "must be at least one category");
    } else {
      for (String category : mediaDto.getCategories()) {
        try{
          categoryService.getCategoryByLabel(category);
        } catch (CategoryNotFoundException e) {
          log.error(e.getMessage());
          completenessErrors.put("categories", "The category " + category + "is not found");
        }
      }
    }
    if (StringUtils.isBlank(mediaDto.getTitle())) {
      completenessErrors.put("title", "Title is mandatory");
    }
    if (StringUtils.isBlank(mediaDto.getType())) {
      completenessErrors.put("type", "Type is mandatory");
    } else {
      try {
        mediaTypeService.getMediaTypeByType(mediaDto.getType());
      } catch (MediaTypeNotFoundException e) {
        completenessErrors.put("type", e.getMessage());
      }
    }
    return completenessErrors;
  }

  private boolean isMediaAlreadyExist(final String uid, final MediaDto mediaDto) {
    boolean isMediaExist = false;
    Criteria andCriteria = new Criteria();
    Criteria orCriteria = new Criteria();
    orCriteria.orOperator(
        Criteria.where("id").is(mediaDto.getId()),
        Criteria.where("title").regex("^" + mediaDto.getTitle(), "i"));
    andCriteria.andOperator(Criteria.where("uid").is(uid), orCriteria);
    final Query query = new Query(andCriteria);
    final Media media = mongoTemplate.findOne(query, Media.class);
    if (media != null) {
      isMediaExist = true;
    }
    return isMediaExist;
  }

  private Query userMediaQueryWithParams(Map<String, Object> params) {
    final Query query = new Query();
    final StringBuilder strBuilder = new StringBuilder();
    if (params.containsKey("type")) {
      strBuilder.append("type, ");
      query.addCriteria(Criteria.where("type").is((String) params.get("type")));
    }
    if (params.containsKey("isFavourite")) {
      strBuilder.append("isFavourite, ");
      query.addCriteria(
          Criteria.where("favourite").is(((Boolean) params.get("isFavourite")).booleanValue()));
    }
    if (params.containsKey("categories")) {
      strBuilder.append("categories, ");
      query.addCriteria(Criteria.where("categories").in((List<String>) params.get("categories")));
    }
    if (params.containsKey("title")) {
      strBuilder.append("title, ");
      query.addCriteria(
          Criteria.where("title").regex(".*" + ((String) params.get("title")).trim() + ".*", "i"));
    }
    if (strBuilder.length() > 0) {
      strBuilder.setLength(strBuilder.length() - 2);
      log.info("User Media Query with additional Filters " + strBuilder.toString());
    } else {
      log.info("No additional Filters for User Media Query");
    }
    return query;
  }
}
