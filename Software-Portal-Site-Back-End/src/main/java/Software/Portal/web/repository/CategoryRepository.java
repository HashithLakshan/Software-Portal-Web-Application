package Software.Portal.web.repository;

import Software.Portal.web.constant.CommonStatus;
import Software.Portal.web.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface CategoryRepository extends JpaRepository<Category,Long> {

    Category getByCategoryId(Long categoryId);

    Page<Category> findByCommonStatus(CommonStatus commonStatusEnum, Pageable pageable);

    Optional<Category> findByCategoryNameAndCommonStatus(String categoryName, CommonStatus commonStatus);
}
