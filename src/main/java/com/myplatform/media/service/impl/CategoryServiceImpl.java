package com.myplatform.media.service.impl;

import com.myplatform.media.dto.CategoryDto;
import com.myplatform.media.entity.Category;
import com.myplatform.media.exception.CategoryAlreadyExistException;
import com.myplatform.media.exception.CategoryNotFoundException;
import com.myplatform.media.mappers.CategoryMapper;
import com.myplatform.media.repository.CategoryRepository;
import com.myplatform.media.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
  @Autowired private CategoryRepository categoryRepository;
  @Autowired private CategoryMapper categoryMapper;

  @Override
  public Optional<Category> findCategoryByLabel(final String label) {
    return categoryRepository.findByLabel(label);
  }

  @Override
  public CategoryDto getCategoryByLabel(final String label) throws CategoryNotFoundException {
    Optional<Category> categoryOptional = findCategoryByLabel(label);
    if (!categoryOptional.isPresent()) {
      final String errorMessage = "The category with label " + label + " is not found.";
      log.error(errorMessage);
      CategoryNotFoundException e = new CategoryNotFoundException(errorMessage);
      e.setCategoryLabel(label);
      throw e;
    }
    return categoryMapper.categoryToCategoryDto(categoryOptional.get());
  }

  @Override
  public Optional<Category> findCategoryById(final String id) {
    return categoryRepository.findById(id);
  }

  @Override
  public CategoryDto getCategoryById(String id) throws CategoryNotFoundException {
    log.info("Searching for category with id " + id);
    Optional<Category> categoryOptional = findCategoryById(id);
    if (!categoryOptional.isPresent()) {
      final String errorMessage = "The category with id " + id + " is not found.";
      log.error(errorMessage);
      throw new CategoryNotFoundException(errorMessage);
    }
    return categoryMapper.categoryToCategoryDto(categoryOptional.get());
  }

  @Override
  public List<CategoryDto> getCategories() {
    log.info("Getting all the categories");
    List<Category> categoryList = categoryRepository.findAll();
    return categoryList.stream()
        .map(category -> categoryMapper.categoryToCategoryDto(category))
        .collect(Collectors.toList());
  }

  @Override
  public String insertCategory(final CategoryDto categoryDto) throws CategoryAlreadyExistException {
    log.info("Inserting new category " + categoryDto.toString());
    if (findCategoryByLabel(categoryDto.getLabel().trim().toLowerCase()).isPresent()) {
      final String errorMessage = "The category " + categoryDto.getLabel() + " already exist";
      log.error("Error while inserting the category " + errorMessage);
      throw new CategoryAlreadyExistException(errorMessage);
    }
    categoryDto.setId(null);
    categoryDto.setLabel(categoryDto.getLabel().trim().toLowerCase());
    final Category savedCategory =
        categoryRepository.insert(categoryMapper.categoryDtoToCategory(categoryDto));
    return savedCategory.getId();
  }

  @Override
  public CategoryDto updateCategory(CategoryDto categoryDto) throws CategoryNotFoundException {
    final CategoryDto categoryDtoToUpdate = getCategoryById(categoryDto.getId());
    categoryDtoToUpdate.setLabel(categoryDto.getLabel().trim().toLowerCase());
    final Category categorySaved =
        categoryRepository.save(categoryMapper.categoryDtoToCategory(categoryDtoToUpdate));
    return categoryMapper.categoryToCategoryDto(categorySaved);
  }
}
