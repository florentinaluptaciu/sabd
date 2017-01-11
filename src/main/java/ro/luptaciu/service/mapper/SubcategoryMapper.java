package ro.luptaciu.service.mapper;

import ro.luptaciu.domain.*;
import ro.luptaciu.service.dto.SubcategoryDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Subcategory and its DTO SubcategoryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SubcategoryMapper {

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.categoryName", target = "categoryCategoryName")
    SubcategoryDTO subcategoryToSubcategoryDTO(Subcategory subcategory);

    List<SubcategoryDTO> subcategoriesToSubcategoryDTOs(List<Subcategory> subcategories);

    @Mapping(source = "categoryId", target = "category")
    Subcategory subcategoryDTOToSubcategory(SubcategoryDTO subcategoryDTO);

    List<Subcategory> subcategoryDTOsToSubcategories(List<SubcategoryDTO> subcategoryDTOs);

    default Category categoryFromId(Long id) {
        if (id == null) {
            return null;
        }
        Category category = new Category();
        category.setId(id);
        return category;
    }
}
