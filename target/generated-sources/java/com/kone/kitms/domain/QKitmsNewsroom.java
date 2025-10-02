package com.kone.kitms.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QKitmsNewsroom is a Querydsl query type for KitmsNewsroom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QKitmsNewsroom extends EntityPathBase<KitmsNewsroom> {

    private static final long serialVersionUID = -115004296L;

    public static final QKitmsNewsroom kitmsNewsroom = new QKitmsNewsroom("kitmsNewsroom");

    public final DateTimePath<java.time.ZonedDateTime> createdAt = createDateTime("createdAt", java.time.ZonedDateTime.class);

    public final StringPath createdBy = createString("createdBy");

    public final StringPath newsroomImage = createString("newsroomImage");

    public final NumberPath<Long> newsroomNo = createNumber("newsroomNo", Long.class);

    public final StringPath newsroomStatus = createString("newsroomStatus");

    public final StringPath newsroomTitle = createString("newsroomTitle");

    public final StringPath newsroomUrl = createString("newsroomUrl");

    public final DateTimePath<java.time.ZonedDateTime> updatedAt = createDateTime("updatedAt", java.time.ZonedDateTime.class);

    public final StringPath updatedBy = createString("updatedBy");

    public QKitmsNewsroom(String variable) {
        super(KitmsNewsroom.class, forVariable(variable));
    }

    public QKitmsNewsroom(Path<? extends KitmsNewsroom> path) {
        super(path.getType(), path.getMetadata());
    }

    public QKitmsNewsroom(PathMetadata metadata) {
        super(KitmsNewsroom.class, metadata);
    }

}

