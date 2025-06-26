package Software.Portal.web.mpping;

import org.mapstruct.Mapper;
import Software.Portal.web.dto.CategoryDto;
import Software.Portal.web.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapping {

    Category toCategory(CategoryDto categoryDto);
}
