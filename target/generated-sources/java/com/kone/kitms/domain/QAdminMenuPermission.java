package com.kone.kitms.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAdminMenuPermission is a Querydsl query type for AdminMenuPermission
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAdminMenuPermission extends EntityPathBase<AdminMenuPermission> {

    private static final long serialVersionUID = -2090641813L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAdminMenuPermission adminMenuPermission = new QAdminMenuPermission("adminMenuPermission");

    public final DateTimePath<java.time.ZonedDateTime> createdAt = createDateTime("createdAt", java.time.ZonedDateTime.class);

    public final StringPath createdBy = createString("createdBy");

    public final BooleanPath isGranted = createBoolean("isGranted");

    public final QAdminMenu menu;

    public final NumberPath<Long> menuNo = createNumber("menuNo", Long.class);

    public final NumberPath<Long> permissionNo = createNumber("permissionNo", Long.class);

    public final DateTimePath<java.time.ZonedDateTime> updatedAt = createDateTime("updatedAt", java.time.ZonedDateTime.class);

    public final StringPath updatedBy = createString("updatedBy");

    public final QKitmsUser user;

    public final StringPath userId = createString("userId");

    public QAdminMenuPermission(String variable) {
        this(AdminMenuPermission.class, forVariable(variable), INITS);
    }

    public QAdminMenuPermission(Path<? extends AdminMenuPermission> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAdminMenuPermission(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAdminMenuPermission(PathMetadata metadata, PathInits inits) {
        this(AdminMenuPermission.class, metadata, inits);
    }

    public QAdminMenuPermission(Class<? extends AdminMenuPermission> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.menu = inits.isInitialized("menu") ? new QAdminMenu(forProperty("menu")) : null;
        this.user = inits.isInitialized("user") ? new QKitmsUser(forProperty("user")) : null;
    }

}

