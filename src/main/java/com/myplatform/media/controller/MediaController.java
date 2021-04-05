package com.myplatform.media.controller;

import com.myplatform.media.dto.MediaDto;
import com.myplatform.media.error.MediaErrorCode;
import com.myplatform.media.exception.*;
import com.myplatform.media.service.MediaService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/medias")
public class MediaController {
  @Autowired private MediaService mediaService;

  @ApiOperation(value = "Get the list of all medias")
  @GetMapping()
  public ResponseEntity<List<MediaDto>> getMediaList() {
    final List<MediaDto> mediaList = mediaService.getMediaList();
    return ResponseEntity.ok(mediaList);
  }

  @ApiOperation(value = "Get the list of all medias of a user using uid")
  @GetMapping("/users/{uid}")
  public ResponseEntity<List<MediaDto>> getUserMediaListByUserId(
      @ApiParam(name = "uid", value = "the user id", required = true) @PathVariable
          final String uid,
      @ApiParam(name = "type", value = "the type of the media") @RequestParam(required = false)
          final String type,
      @ApiParam(name = "isFavourite", value = "If media is one of user favourites")
          @RequestParam(required = false)
          final Boolean isFavourite,
      @ApiParam(name = "categories", value = "Categories of media") @RequestParam(required = false)
          List<String> categories,
      @ApiParam(name = "title", value = "The media Title") @RequestParam(required = false)
          final String title) {
    Map<String, Object> params = new HashMap<>();
    if (StringUtils.isNotBlank(type)) {
      params.put("type", type);
    }
    if (isFavourite != null) {
      params.put("isFavourite", isFavourite);
    }
    if (StringUtils.isNotBlank(title)) {
      params.put("title", title);
    }
    if (!CollectionUtils.isEmpty(categories)) {
      params.put("categories", categories);
    }
    final List<MediaDto> mediaList = mediaService.getUserMediaList(uid, params);
    return ResponseEntity.ok(mediaList);
  }

  @GetMapping("/{id}")
  public ResponseEntity<MediaDto> getMediaById(@PathVariable final String id) {
    try {
      final MediaDto mediaDto = mediaService.getMediaById(id);
      return ResponseEntity.ok(mediaDto);
    } catch (MediaNotFoundException e) {
      e.printStackTrace();
      final Map<String, String> frontParams = new HashMap<>();
      frontParams.put("id", id);
      throw new ApplicationException(
          MediaErrorCode.MEDIA_NOT_FOUND_CODE, HttpStatus.BAD_REQUEST, frontParams,"");
    }
  }

  @ApiOperation("Insert new media for the user")
  @PostMapping("/users/{uid}")
  public ResponseEntity<String> insertMedia(
      @ApiParam(name = "uid", value = "The user id concerned by the operation", required = true)
          @PathVariable
          final String uid,
      @ApiParam(name = "Media", value = "The media to add", required = true) @RequestBody
          final MediaDto mediaDto,
      final UriComponentsBuilder uriComponentsBuilder) {
    checkMediaForUpdateAndInsert(uid, mediaDto);
    try {
      final String insertedId = mediaService.insertUserMedia(uid, mediaDto);
      final UriComponents uriComponents =
          uriComponentsBuilder.path("/medias/{id}").buildAndExpand(insertedId);
      return ResponseEntity.created(uriComponents.toUri()).build();
    } catch (MediaAlreadyExistException e) {
      e.printStackTrace();
      final Map<String, String> frontParams = new HashMap<>();
      frontParams.put("id", mediaDto.getId());
      throw new ApplicationException(
          MediaErrorCode.MEDIA_ALREADY_EXIST_CODE, HttpStatus.BAD_REQUEST, frontParams,"");
    } catch (CompletenessException e) {
      e.printStackTrace();
      throw new ApplicationException(
          MediaErrorCode.INCOMPLETE_MEDIA_CODE, HttpStatus.BAD_REQUEST, e.getCompletenessErrors(),"");
    }
  }

  @ApiOperation("Update the media for the user")
  @PutMapping("/users/{uid}")
  public ResponseEntity<MediaDto> updateMedia(
      @PathVariable final String uid, @RequestBody final MediaDto mediaDto) {
    checkMediaForUpdateAndInsert(uid, mediaDto);
    try {
      return ResponseEntity.ok(mediaService.updateUserMedia(uid, mediaDto));
    } catch (MediaNotFoundException e) {
      e.printStackTrace();
      final Map<String, String> frontParams = new HashMap<>();
      frontParams.put("id", mediaDto.getId());
      throw new ApplicationException(
              MediaErrorCode.MEDIA_NOT_FOUND_CODE, HttpStatus.NOT_FOUND, frontParams, "");
    } catch (CompletenessException e) {
      e.printStackTrace();
      throw new ApplicationException(
              MediaErrorCode.INCOMPLETE_MEDIA_CODE, HttpStatus.BAD_REQUEST, e.getCompletenessErrors(),"");
    }
  }

  private void checkMediaForUpdateAndInsert(final String uid, final MediaDto mediaDto) {
    if (mediaDto == null) {
      throw new ApplicationException(MediaErrorCode.NO_VALID_MEDIA_CODE, HttpStatus.BAD_REQUEST,"");
    }
    if (!uid.equals(mediaDto.getUid())) {
      final MediaErrorCode mediaErrorCode = MediaErrorCode.NO_VALID_MEDIA_CODE;
      mediaErrorCode.setMessage("The user is not correct");
      throw new ApplicationException(MediaErrorCode.NO_VALID_MEDIA_CODE, HttpStatus.BAD_REQUEST,"");
    }
  }
}
