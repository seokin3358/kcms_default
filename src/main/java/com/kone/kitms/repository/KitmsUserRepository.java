package com.kone.kitms.repository;

import com.kone.kitms.domain.KitmsUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the KitmsUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KitmsUserRepository extends JpaRepository<KitmsUser, Long> {
    @Query(
        "select m.userId from KitmsUser m " +
        "where m.userName= :userName and m.userEmail = :userEmail and " +
        "(m.userTel = :userTel or m.userMobile = :userTel)"
    )
    String findUserId(@Param("userName") String userName, @Param("userTel") String userTel, @Param("userEmail") String userEmail);

    @Query(
        "select concat(m.userId,'_',if(substr(m.userMobile, -4)='',substr(m.userTel,-4),substr(m.userMobile, -4)),'@') from KitmsUser m " +
        "where m.userId= :userId and m.userName= :userName and m.userEmail = :userEmail and " +
        "(m.userTel = :userTel or m.userMobile = :userTel)"
    )
    String findUserPwd(
        @Param("userId") String userId,
        @Param("userName") String userName,
        @Param("userTel") String userTel,
        @Param("userEmail") String userEmail
    );

    KitmsUser findUserNoByUserId(String userId);

    @Query("select m.userName from KitmsUser m " + "where m.userId= :userId ")
    Optional<String> findUserNameByUserId(String userId);

    Optional<KitmsUser> findKitmsUserByUserId(String userId);

    boolean existsByUserEmail(String userEmail);

    boolean existsByUserIdIgnoreCase(String userId);
}
