package Software.Portal.web.repository;

import Software.Portal.web.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface SubCategoryRepository extends JpaRepository<SubCategory,Long> {
    SubCategory getBySubCategoryId(Long subCategoryId);
}
