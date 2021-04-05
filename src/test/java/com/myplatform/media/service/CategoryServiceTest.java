package com.myplatform.media.service;

import com.myplatform.media.dto.CategoryDto;
import com.myplatform.media.entity.Category;
import com.myplatform.media.exception.CategoryAlreadyExistException;
import com.myplatform.media.exception.CategoryNotFoundException;
import com.myplatform.media.mappers.CategoryMapperImpl;
import com.myplatform.media.repository.CategoryRepository;
import com.myplatform.media.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class CategoryServiceTest {
  @Spy CategoryMapperImpl categoryMapper;
  @MockBean CategoryRepository categoryRepository;
  @InjectMocks CategoryServiceImpl categoryService;

  @Test
  public void getCategoryByLabel_Should_Return_Category() throws CategoryNotFoundException {
    final String label = "action";
    final Category category = new Category("1", label);
    Mockito.when(categoryRepository.findByLabel(label)).thenReturn(Optional.of(category));
    Mockito.when(categoryMapper.categoryToCategoryDto(category)).thenCallRealMethod();
    final CategoryDto categoryDto = categoryService.getCategoryByLabel(label);
    assertNotNull(categoryDto);
    assertEquals(categoryDto.getLabel(), label);
    verify(categoryRepository, times(1)).findByLabel(label);
  }

  @Test
  public void getCategoryByLabel_Should_Throw_CategoryNotFoundException() {
    final String label = "unknown";
    Mockito.when(categoryRepository.findByLabel(label)).thenReturn(Optional.ofNullable(null));
    assertThrows(
        CategoryNotFoundException.class,
        () -> {
          categoryService.getCategoryByLabel(label);
        });
  }

  @Test
  public void getCategoryById_Should_Return_Category() throws CategoryNotFoundException {
    final String id = "1";
    final Category category = new Category(id, "action");
    Mockito.when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
    Mockito.when(categoryMapper.categoryToCategoryDto(category)).thenCallRealMethod();
    final CategoryDto categoryDto = categoryService.getCategoryById(id);
    assertNotNull(categoryDto);
    assertEquals(categoryDto.getId(), id);
    verify(categoryRepository, times(1)).findById(id);
  }

  @Test
  public void getCategoryById_Should_Throw_CategoryNotFoundException() {
    final String id = "000";
    Mockito.when(categoryRepository.findById(id)).thenReturn(Optional.ofNullable(null));
    assertThrows(
        CategoryNotFoundException.class,
        () -> {
          categoryService.getCategoryById(id);
        });
  }

  @Test
  public void insertCategory_Should_Return_Inserted_Category_Id()
      throws CategoryAlreadyExistException {
    final String label = "action";
    final CategoryDto categoryDto = new CategoryDto("0", label);
    final Category category = new Category("1", label);
    when(categoryRepository.findByLabel(label)).thenReturn(Optional.ofNullable(null));
    when(categoryMapper.categoryDtoToCategory(categoryDto)).thenReturn(category);
    when(categoryMapper.categoryToCategoryDto(category)).thenCallRealMethod();
    when(categoryRepository.insert(category)).thenReturn(category);
    final String categoryDtoSaved = categoryService.insertCategory(categoryDto);
    assertEquals(category.getId(), categoryDtoSaved);
    verify(categoryRepository, times(1)).insert(category);
  }

  @Test
  public void insertCategory_Should_Throw_CategoryAlreadyExist() {
    final String label = "action";
    final Category category = new Category("1", label);
    final CategoryDto categoryDto = new CategoryDto("0", label);
    when(categoryRepository.findByLabel(label)).thenReturn(Optional.of(category));
    assertThrows(
        CategoryAlreadyExistException.class, () -> categoryService.insertCategory(categoryDto));
  }

  @Test
  public void updateCategory_Should_Return_Updated_Category() throws CategoryNotFoundException {
    final String label = "action";
    final String updatedLabel = "adventure";
    final String id = "1";
    final Category categoryFound = new Category(id, label);
    final CategoryDto categoryDtoFound = new CategoryDto(id, label);
    final Category category = new Category(id, updatedLabel);
    when(categoryRepository.findById(id)).thenReturn(Optional.of(categoryFound));
    when(categoryMapper.categoryToCategoryDto(category)).thenReturn(categoryDtoFound);
    when(categoryMapper.categoryDtoToCategory(categoryDtoFound)).thenReturn(category);
    when(categoryMapper.categoryToCategoryDto(category)).thenCallRealMethod();
    when(categoryRepository.save(any())).thenReturn(category);
    CategoryDto categorySaved = categoryService.updateCategory(categoryDtoFound);
    assertNotNull(categorySaved);
    assertEquals(updatedLabel, categorySaved.getLabel());
  }

  @Test
  public void updateCategory_Should_Throw_CategoryNotFoundException() {
    final String updatedLabel = "adventure";
    final String id = "1";
    final CategoryDto categoryDto = new CategoryDto(id, updatedLabel);
    when(categoryRepository.findById(id)).thenReturn(Optional.ofNullable(null));
    assertThrows(CategoryNotFoundException.class, () -> categoryService.updateCategory(categoryDto));
  }

}
