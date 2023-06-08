package com.ape.mapper;

import com.ape.dto.CategoryDTO;
import com.ape.model.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDTO categoryToCategoryDTO(Category category);

    List<CategoryDTO> categoryListToCategoryDTOList(List<Category> categories);
}
