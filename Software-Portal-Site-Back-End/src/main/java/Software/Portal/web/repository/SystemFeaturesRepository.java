package Software.Portal.web.repository;

import Software.Portal.web.entity.SystemFeatures;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface SystemFeaturesRepository extends JpaRepository<SystemFeatures,Long> {
    List<SystemFeatures> findBySystemProfileSystemProfilesId(Long systemProfilesId);
}
