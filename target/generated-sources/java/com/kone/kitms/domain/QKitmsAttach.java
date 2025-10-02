package com.kone.kitms.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QKitmsAttach is a Querydsl query type for KitmsAttach
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QKitmsAttach extends EntityPathBase<KitmsAttach> {

    private static final long serialVersionUID = 1876081423L;

    public static final QKitmsAttach kitmsAttach = new QKitmsAttach("kitmsAttach");

    public final ArrayPath<byte[], Byte> attachFile = createArray("attachFile", byte[].class);

    public final StringPath attachFileName = createString("attachFileName");

    public final StringPath attachFilePath = createString("attachFilePath");

    public final NumberPath<Long> attachFileSize = createNumber("attachFileSize", Long.class);

    public final NumberPath<Long> attachNo = createNumber("attachNo", Long.class);

    public final StringPath attachTableName = createString("attachTableName");

    public final NumberPath<Long> attachTablePk = createNumber("attachTablePk", Long.class);

    public final DateTimePath<java.time.ZonedDateTime> createDt = createDateTime("createDt", java.time.ZonedDateTime.class);

    public final StringPath createUserId = createString("createUserId");

    public final BooleanPath isThumbnail = createBoolean("isThumbnail");

    public QKitmsAttach(String variable) {
        super(KitmsAttach.class, forVariable(variable));
    }

    public QKitmsAttach(Path<? extends KitmsAttach> path) {
        super(path.getType(), path.getMetadata());
    }

    public QKitmsAttach(PathMetadata metadata) {
        super(KitmsAttach.class, metadata);
    }

}

