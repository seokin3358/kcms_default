package com.kone.kitms.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCmsContent is a Querydsl query type for CmsContent
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCmsContent extends EntityPathBase<CmsContent> {

    private static final long serialVersionUID = -907171198L;

    public static final QCmsContent cmsContent = new QCmsContent("cmsContent");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath createdBy = createString("createdBy");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath metaDescription = createString("metaDescription");

    public final StringPath metaKeywords = createString("metaKeywords");

    public final StringPath metaTitle = createString("metaTitle");

    public final StringPath pageCode = createString("pageCode");

    public final StringPath pageContent = createString("pageContent");

    public final StringPath pageTitle = createString("pageTitle");

    public final StringPath previewContent = createString("previewContent");

    public final StringPath previewMetaDescription = createString("previewMetaDescription");

    public final StringPath previewMetaKeywords = createString("previewMetaKeywords");

    public final StringPath previewMetaTitle = createString("previewMetaTitle");

    public final DateTimePath<java.time.LocalDateTime> previewUpdatedAt = createDateTime("previewUpdatedAt", java.time.LocalDateTime.class);

    public final StringPath status = createString("status");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final StringPath updatedBy = createString("updatedBy");

    public QCmsContent(String variable) {
        super(CmsContent.class, forVariable(variable));
    }

    public QCmsContent(Path<? extends CmsContent> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCmsContent(PathMetadata metadata) {
        super(CmsContent.class, metadata);
    }

}

