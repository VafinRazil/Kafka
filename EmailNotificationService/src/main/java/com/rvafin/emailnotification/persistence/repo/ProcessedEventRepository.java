package com.rvafin.emailnotification.persistence.repo;

import com.rvafin.emailnotification.persistence.entity.ProcessedEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProcessedEventRepository extends JpaRepository<ProcessedEventEntity, Long> {

     Optional<ProcessedEventEntity> findByMessageId(String messageId);
}
