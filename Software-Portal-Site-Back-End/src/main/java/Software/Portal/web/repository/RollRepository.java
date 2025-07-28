package Software.Portal.web.repository;

import Software.Portal.web.constant.Roles;
import Software.Portal.web.entity.Roll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import javax.management.relation.Role;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface RollRepository extends JpaRepository<Roll,Long> {

    Roll getByRollId(Long rollId);

    Optional<Roll> findByRollName(Roles role);

    Roll getByRollName(Roles roles);
}
