package com.kone.kitms.repository;

import com.kone.kitms.domain.KitmsAttach;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the KitmsAttach entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KitmsAttachRepository extends JpaRepository<KitmsAttach, Long> {
    void deleteByAttachTableNameAndAttachTablePk(String attachTableName, Long attachTablePk);

    List<KitmsAttach> findAllByAttachTableNameAndAttachTablePk(String attachTableName, Long attachTablePk);

    void deleteByAttachTableNameAndAttachNo(String attachTableName, Long attachNo);
    KitmsAttach findByAttachTableNameAndAttachNo(String attachTableName, Long attachNo);
    
    // 공지사항 이미지 조회
    List<KitmsAttach> findByAttachTableNameAndAttachTablePk(String attachTableName, Long attachTablePk);
}
