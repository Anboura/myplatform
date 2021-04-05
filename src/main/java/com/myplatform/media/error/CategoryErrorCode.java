package com.myplatform.media.error;

public class CategoryErrorCode extends AbstractErrorCode{

    public static CategoryErrorCode CATEGORY_NOT_FOUND_CODE = new CategoryErrorCode("CATEGORY_NOT_FOUND", "The category is not found");
    public static CategoryErrorCode CATEGORY_ALREADY_EXIST_CODE = new CategoryErrorCode("CATEGORY_ALREADY_EXIST", "The category already exist");
    public static CategoryErrorCode INCOMPLETE_CATEGORY_CODE = new CategoryErrorCode("INCOMPLETE_CATEGORY", "The category informtions are incomplete");
    public static CategoryErrorCode INVALID_CATEGORY_CODE = new CategoryErrorCode("INVALID_CATEGORY", "The category is not valid");

    private CategoryErrorCode(final String code, final String message) {
        this.code = code;
        this.message = message;
    }
}
