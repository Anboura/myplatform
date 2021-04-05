package com.myplatform.media.controller;

import com.myplatform.media.dto.CategoryDto;
import com.myplatform.media.error.CategoryErrorCode;
import com.myplatform.media.exception.ApplicationException;
import com.myplatform.media.exception.CategoryAlreadyExistException;
import com.myplatform.media.exception.CategoryNotFoundException;
import com.myplatform.media.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/categories")
public class CategoryController {
  @Autowired private CategoryService categoryService;

  @ApiOperation("Get all the possible categories")
  @GetMapping
  public ResponseEntity<List<CategoryDto>> getCategories() {
    return ResponseEntity.ok(categoryService.getCategories());
  }

  @ApiOperation("Get a category By Id")
  @GetMapping("/{id}")
  public ResponseEntity<CategoryDto> getCategoryById(
      @ApiParam(name = "id", value = "category id", required = true) @PathVariable
          final String id) {
    try {
      final CategoryDto categoryDto = categoryService.getCategoryById(id);
      return ResponseEntity.ok(categoryDto);
    } catch (CategoryNotFoundException e) {
      e.printStackTrace();
      log.error(e.getMessage());
      throw generateExceptionForCategoryNotFound(id, e);
    }
  }

  @ApiOperation("Insert a new Category")
  @PostMapping
  public ResponseEntity<String> insertCategory(
           @RequestBody final CategoryDto categoryDto, final UriComponentsBuilder uriComponentsBuilder) {
    if (StringUtils.isBlank(categoryDto.getLabel())) {
      throw new ApplicationException(
          CategoryErrorCode.INCOMPLETE_CATEGORY_CODE, HttpStatus.BAD_REQUEST,"");
    }
    try {
      final String insertedCategoryId = categoryService.insertCategory(categoryDto);
      final UriComponents uriComponents =
              uriComponentsBuilder.path("/categories/{id}").buildAndExpand(insertedCategoryId);
      return ResponseEntity.created(uriComponents.toUri()).build();
    } catch (CategoryAlreadyExistException e) {
      e.printStackTrace();
      final CategoryErrorCode categoryErrorCode = CategoryErrorCode.CATEGORY_ALREADY_EXIST_CODE;
      categoryErrorCode.setMessage(e.getMessage());
      final Map<String, String> frontParams = new HashMap<>();
      frontParams.put("category", categoryDto.getLabel());
      throw new ApplicationException(categoryErrorCode, HttpStatus.BAD_REQUEST, frontParams, "");
    }

  }

  @PutMapping("/{id}")
  public ResponseEntity<CategoryDto> updateCategory(
      @ApiParam(name = "id", value = "The id of the category to update", required = true)
          @PathVariable
          final String id,
      @RequestBody final CategoryDto categoryDto) {
    if (categoryDto == null || id.equals(categoryDto.getId())) {
      log.error("Invalid category check the id or the label");
      throw new ApplicationException(
          CategoryErrorCode.INVALID_CATEGORY_CODE, HttpStatus.BAD_REQUEST, "");
    }
    if (StringUtils.isBlank(categoryDto.getLabel())) {
      log.error("The label must not be blank");
      throw new ApplicationException(
          CategoryErrorCode.INCOMPLETE_CATEGORY_CODE, HttpStatus.BAD_REQUEST, "");
    }
    try {
      return ResponseEntity.ok(categoryService.updateCategory(categoryDto));
    } catch (CategoryNotFoundException e) {
      e.printStackTrace();
      throw generateExceptionForCategoryNotFound(id, e);
    }
  }

  private ApplicationException generateExceptionForCategoryNotFound(
      final String id, CategoryNotFoundException e) {
    final Map<String, String> frontParams = new HashMap<>();
    frontParams.put("category", id);
    final CategoryErrorCode categoryErrorCode = CategoryErrorCode.CATEGORY_NOT_FOUND_CODE;
    categoryErrorCode.setMessage(e.getMessage());
    return new ApplicationException(categoryErrorCode, HttpStatus.NOT_FOUND, frontParams, "");
  }
}
