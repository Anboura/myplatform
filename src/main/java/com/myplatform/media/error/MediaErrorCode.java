package com.myplatform.media.error;

public class MediaErrorCode extends AbstractErrorCode {
  public static MediaErrorCode MEDIA_NOT_FOUND_CODE =
      new MediaErrorCode("MEDIA_NOT_FOUND", "The media is not found");

  public static MediaErrorCode MEDIA_ALREADY_EXIST_CODE =
      new MediaErrorCode("MEDIA_ALREADY_EXIST", "The media already exist");

  public static MediaErrorCode NO_VALID_MEDIA_CODE =
          new MediaErrorCode("NO_VALID_MEDIA", "The media is not valid");
  public static MediaErrorCode INCOMPLETE_MEDIA_CODE =
          new MediaErrorCode("INCOMPLETE_MEDIA", "The media informations are incomplete");
  public static MediaErrorCode BAD_MEDIA_TYPE_CODE =
          new MediaErrorCode("BAD_MEDIA_TYPE", "Bad media type");



  private MediaErrorCode(final String code, final String message) {
    this.code = code;
    this.message = message;
  }
}
