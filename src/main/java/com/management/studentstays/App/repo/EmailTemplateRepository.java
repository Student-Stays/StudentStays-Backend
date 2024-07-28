package com.management.studentstays.App.repo;

import com.management.studentstays.App.entity.EmailTemplate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Long> {
  Optional<EmailTemplate> findByName(String name);
}
