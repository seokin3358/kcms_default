package com.kone.kitms.repository;

import com.kone.kitms.domain.KitmsLoginLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the KitmsLoginLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KitmsLoginLogRepository extends JpaRepository<KitmsLoginLog, Long> {}
