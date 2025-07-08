package Software.Portal.web.repository;

import Software.Portal.web.entity.SystemProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface
SystemProfileImageRepository extends JpaRepository<SystemProfileImage,Long> {
    List<SystemProfileImage> findAllBySystemProfileSystemProfilesId(Long systemProfilesId);

    long countBySystemProfileSystemProfilesId(Long systemProfilesId);


}
