package Software.Portal.web.repository;

import Software.Portal.web.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {



    Optional<Image> findByEmployeeEmployeeId(String employeeId);
}
