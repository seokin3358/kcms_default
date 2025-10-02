package com.kone.kitms.domain;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.LocalDateTime;

@StaticMetamodel(CmsContent.class)
@Generated("org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
public abstract class CmsContent_ {

	
	/**
	 * @see com.kone.kitms.domain.CmsContent#updatedBy
	 **/
	public static volatile SingularAttribute<CmsContent, String> updatedBy;
	
	/**
	 * @see com.kone.kitms.domain.CmsContent#pageTitle
	 **/
	public static volatile SingularAttribute<CmsContent, String> pageTitle;
	
	/**
	 * @see com.kone.kitms.domain.CmsContent#previewMetaKeywords
	 **/
	public static volatile SingularAttribute<CmsContent, String> previewMetaKeywords;
	
	/**
	 * @see com.kone.kitms.domain.CmsContent#previewUpdatedAt
	 **/
	public static volatile SingularAttribute<CmsContent, LocalDateTime> previewUpdatedAt;
	
	/**
	 * @see com.kone.kitms.domain.CmsContent#metaDescription
	 **/
	public static volatile SingularAttribute<CmsContent, String> metaDescription;
	
	/**
	 * @see com.kone.kitms.domain.CmsContent#previewContent
	 **/
	public static volatile SingularAttribute<CmsContent, String> previewContent;
	
	/**
	 * @see com.kone.kitms.domain.CmsContent#pageCode
	 **/
	public static volatile SingularAttribute<CmsContent, String> pageCode;
	
	/**
	 * @see com.kone.kitms.domain.CmsContent#createdAt
	 **/
	public static volatile SingularAttribute<CmsContent, LocalDateTime> createdAt;
	
	/**
	 * @see com.kone.kitms.domain.CmsContent#metaKeywords
	 **/
	public static volatile SingularAttribute<CmsContent, String> metaKeywords;
	
	/**
	 * @see com.kone.kitms.domain.CmsContent#createdBy
	 **/
	public static volatile SingularAttribute<CmsContent, String> createdBy;
	
	/**
	 * @see com.kone.kitms.domain.CmsContent#previewMetaTitle
	 **/
	public static volatile SingularAttribute<CmsContent, String> previewMetaTitle;
	
	/**
	 * @see com.kone.kitms.domain.CmsContent#metaTitle
	 **/
	public static volatile SingularAttribute<CmsContent, String> metaTitle;
	
	/**
	 * @see com.kone.kitms.domain.CmsContent#previewMetaDescription
	 **/
	public static volatile SingularAttribute<CmsContent, String> previewMetaDescription;
	
	/**
	 * @see com.kone.kitms.domain.CmsContent#id
	 **/
	public static volatile SingularAttribute<CmsContent, Long> id;
	
	/**
	 * @see com.kone.kitms.domain.CmsContent#pageContent
	 **/
	public static volatile SingularAttribute<CmsContent, String> pageContent;
	
	/**
	 * @see com.kone.kitms.domain.CmsContent
	 **/
	public static volatile EntityType<CmsContent> class_;
	
	/**
	 * @see com.kone.kitms.domain.CmsContent#status
	 **/
	public static volatile SingularAttribute<CmsContent, String> status;
	
	/**
	 * @see com.kone.kitms.domain.CmsContent#updatedAt
	 **/
	public static volatile SingularAttribute<CmsContent, LocalDateTime> updatedAt;

	public static final String UPDATED_BY = "updatedBy";
	public static final String PAGE_TITLE = "pageTitle";
	public static final String PREVIEW_META_KEYWORDS = "previewMetaKeywords";
	public static final String PREVIEW_UPDATED_AT = "previewUpdatedAt";
	public static final String META_DESCRIPTION = "metaDescription";
	public static final String PREVIEW_CONTENT = "previewContent";
	public static final String PAGE_CODE = "pageCode";
	public static final String CREATED_AT = "createdAt";
	public static final String META_KEYWORDS = "metaKeywords";
	public static final String CREATED_BY = "createdBy";
	public static final String PREVIEW_META_TITLE = "previewMetaTitle";
	public static final String META_TITLE = "metaTitle";
	public static final String PREVIEW_META_DESCRIPTION = "previewMetaDescription";
	public static final String ID = "id";
	public static final String PAGE_CONTENT = "pageContent";
	public static final String STATUS = "status";
	public static final String UPDATED_AT = "updatedAt";

}

