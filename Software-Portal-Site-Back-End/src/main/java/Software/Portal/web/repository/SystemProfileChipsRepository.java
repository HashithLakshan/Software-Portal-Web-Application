package Software.Portal.web.repository;

import Software.Portal.web.entity.SystemProfileChips;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
@EnableJpaRepositories
public interface SystemProfileChipsRepository extends JpaRepository<SystemProfileChips,Long> {

    List<SystemProfileChips> findBySystemProfileSystemProfilesId(Long systemProfilesId);


}
