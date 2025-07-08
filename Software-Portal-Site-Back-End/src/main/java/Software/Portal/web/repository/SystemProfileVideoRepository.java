package Software.Portal.web.repository;

import Software.Portal.web.entity.SystemProfileVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface SystemProfileVideoRepository extends JpaRepository<SystemProfileVideo,Long> {
    Optional<SystemProfileVideo> findBySystemProfileSystemProfilesId(Long systemProfileId);

    long countBySystemProfileSystemProfilesId(Long systemProfileId);
}
