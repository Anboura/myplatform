package com.myplatform.media.mappers;

import com.myplatform.media.dto.CategoryDto;
import com.myplatform.media.entity.Category;
import org.mapstruct.Mapper;

@Mapper
public interface CategoryMapper {
    CategoryDto categoryToCategoryDto(Category category);
    Category categoryDtoToCategory(CategoryDto categoryDto);
}
