package com.kone.kitms.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QKitmsLoginLog is a Querydsl query type for KitmsLoginLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QKitmsLoginLog extends EntityPathBase<KitmsLoginLog> {

    private static final long serialVersionUID = 511990117L;

    public static final QKitmsLoginLog kitmsLoginLog = new QKitmsLoginLog("kitmsLoginLog");

    public final DateTimePath<java.time.ZonedDateTime> loginDt = createDateTime("loginDt", java.time.ZonedDateTime.class);

    public final StringPath loginIp = createString("loginIp");

    public final StringPath loginReason = createString("loginReason");

    public final BooleanPath loginSuccess = createBoolean("loginSuccess");

    public final NumberPath<Long> logNo = createNumber("logNo", Long.class);

    public final StringPath userId = createString("userId");

    public QKitmsLoginLog(String variable) {
        super(KitmsLoginLog.class, forVariable(variable));
    }

    public QKitmsLoginLog(Path<? extends KitmsLoginLog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QKitmsLoginLog(PathMetadata metadata) {
        super(KitmsLoginLog.class, metadata);
    }

}

