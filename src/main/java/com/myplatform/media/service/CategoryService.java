package com.myplatform.media.service;

import com.myplatform.media.dto.CategoryDto;
import com.myplatform.media.entity.Category;
import com.myplatform.media.exception.CategoryAlreadyExistException;
import com.myplatform.media.exception.CategoryNotFoundException;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Optional<Category> findCategoryByLabel(String label);
    CategoryDto getCategoryByLabel(String label) throws CategoryNotFoundException;
    Optional<Category> findCategoryById(String id);
    CategoryDto getCategoryById(String id) throws CategoryNotFoundException;
    List<CategoryDto> getCategories();
    String insertCategory(CategoryDto categoryDto) throws CategoryAlreadyExistException;
    CategoryDto updateCategory(CategoryDto categoryDto) throws CategoryNotFoundException;
}
