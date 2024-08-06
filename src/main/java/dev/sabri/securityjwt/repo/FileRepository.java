package dev.sabri.securityjwt.repo;

import dev.sabri.securityjwt.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {

}
