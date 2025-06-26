package Software.Portal.web.repository;

import Software.Portal.web.entity.Roll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface RollRepository extends JpaRepository<Roll,Long> {

    Roll getByRollId(Long rollId);
}
