package com.kone.kitms.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QKitmsNotice is a Querydsl query type for KitmsNotice
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QKitmsNotice extends EntityPathBase<KitmsNotice> {

    private static final long serialVersionUID = -2051316830L;

    public static final QKitmsNotice kitmsNotice = new QKitmsNotice("kitmsNotice");

    public final DateTimePath<java.time.ZonedDateTime> createDt = createDateTime("createDt", java.time.ZonedDateTime.class);

    public final StringPath createUserId = createString("createUserId");

    public final StringPath noticeContent = createString("noticeContent");

    public final NumberPath<Long> noticeNo = createNumber("noticeNo", Long.class);

    public final StringPath noticeTitle = createString("noticeTitle");

    public final BooleanPath staticFlag = createBoolean("staticFlag");

    public QKitmsNotice(String variable) {
        super(KitmsNotice.class, forVariable(variable));
    }

    public QKitmsNotice(Path<? extends KitmsNotice> path) {
        super(path.getType(), path.getMetadata());
    }

    public QKitmsNotice(PathMetadata metadata) {
        super(KitmsNotice.class, metadata);
    }

}

