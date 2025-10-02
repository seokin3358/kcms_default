package com.kone.kitms.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAdminMenu is a Querydsl query type for AdminMenu
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAdminMenu extends EntityPathBase<AdminMenu> {

    private static final long serialVersionUID = 1672470396L;

    public static final QAdminMenu adminMenu = new QAdminMenu("adminMenu");

    public final DateTimePath<java.time.ZonedDateTime> createdAt = createDateTime("createdAt", java.time.ZonedDateTime.class);

    public final StringPath createdBy = createString("createdBy");

    public final BooleanPath isActive = createBoolean("isActive");

    public final BooleanPath isVisible = createBoolean("isVisible");

    public final StringPath menuDescription = createString("menuDescription");

    public final StringPath menuIcon = createString("menuIcon");

    public final StringPath menuName = createString("menuName");

    public final NumberPath<Long> menuNo = createNumber("menuNo", Long.class);

    public final NumberPath<Integer> menuOrder = createNumber("menuOrder", Integer.class);

    public final StringPath menuUrl = createString("menuUrl");

    public final DateTimePath<java.time.ZonedDateTime> updatedAt = createDateTime("updatedAt", java.time.ZonedDateTime.class);

    public final StringPath updatedBy = createString("updatedBy");

    public QAdminMenu(String variable) {
        super(AdminMenu.class, forVariable(variable));
    }

    public QAdminMenu(Path<? extends AdminMenu> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAdminMenu(PathMetadata metadata) {
        super(AdminMenu.class, metadata);
    }

}

