package com.kone.kitms.repository;

import com.kone.kitms.domain.KitmsNotice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the KitmsNotice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KitmsNoticeRepository extends JpaRepository<KitmsNotice, Long> {}
