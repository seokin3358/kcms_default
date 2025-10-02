package com.kone.kitms.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QKitmsUser is a Querydsl query type for KitmsUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QKitmsUser extends EntityPathBase<KitmsUser> {

    private static final long serialVersionUID = -1356111083L;

    public static final QKitmsUser kitmsUser = new QKitmsUser("kitmsUser");

    public final StringPath authCode = createString("authCode");

    public final DateTimePath<java.time.ZonedDateTime> createDt = createDateTime("createDt", java.time.ZonedDateTime.class);

    public final StringPath createUserId = createString("createUserId");

    public final BooleanPath enable = createBoolean("enable");

    public final BooleanPath firstFlag = createBoolean("firstFlag");

    public final DateTimePath<java.time.ZonedDateTime> lastPassModDt = createDateTime("lastPassModDt", java.time.ZonedDateTime.class);

    public final NumberPath<Long> onsiteBranchNo = createNumber("onsiteBranchNo", Long.class);

    public final NumberPath<Long> organNo = createNumber("organNo", Long.class);

    public final StringPath userEmail = createString("userEmail");

    public final StringPath userEtc = createString("userEtc");

    public final StringPath userId = createString("userId");

    public final StringPath userMobile = createString("userMobile");

    public final StringPath userName = createString("userName");

    public final NumberPath<Long> userNo = createNumber("userNo", Long.class);

    public final StringPath userPwd = createString("userPwd");

    public final StringPath userRole = createString("userRole");

    public final StringPath userTel = createString("userTel");

    public final DatePath<java.time.LocalDate> vacEndDate = createDate("vacEndDate", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> vacStartDate = createDate("vacStartDate", java.time.LocalDate.class);

    public QKitmsUser(String variable) {
        super(KitmsUser.class, forVariable(variable));
    }

    public QKitmsUser(Path<? extends KitmsUser> path) {
        super(path.getType(), path.getMetadata());
    }

    public QKitmsUser(PathMetadata metadata) {
        super(KitmsUser.class, metadata);
    }

}

