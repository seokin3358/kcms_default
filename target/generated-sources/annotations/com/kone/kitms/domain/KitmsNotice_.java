package com.kone.kitms.domain;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.ZonedDateTime;

@StaticMetamodel(KitmsNotice.class)
@Generated("org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
public abstract class KitmsNotice_ {

	
	/**
	 * @see com.kone.kitms.domain.KitmsNotice#noticeContent
	 **/
	public static volatile SingularAttribute<KitmsNotice, String> noticeContent;
	
	/**
	 * @see com.kone.kitms.domain.KitmsNotice#createUserId
	 **/
	public static volatile SingularAttribute<KitmsNotice, String> createUserId;
	
	/**
	 * @see com.kone.kitms.domain.KitmsNotice#noticeNo
	 **/
	public static volatile SingularAttribute<KitmsNotice, Long> noticeNo;
	
	/**
	 * @see com.kone.kitms.domain.KitmsNotice#staticFlag
	 **/
	public static volatile SingularAttribute<KitmsNotice, Boolean> staticFlag;
	
	/**
	 * @see com.kone.kitms.domain.KitmsNotice
	 **/
	public static volatile EntityType<KitmsNotice> class_;
	
	/**
	 * @see com.kone.kitms.domain.KitmsNotice#createDt
	 **/
	public static volatile SingularAttribute<KitmsNotice, ZonedDateTime> createDt;
	
	/**
	 * @see com.kone.kitms.domain.KitmsNotice#noticeTitle
	 **/
	public static volatile SingularAttribute<KitmsNotice, String> noticeTitle;

	public static final String NOTICE_CONTENT = "noticeContent";
	public static final String CREATE_USER_ID = "createUserId";
	public static final String NOTICE_NO = "noticeNo";
	public static final String STATIC_FLAG = "staticFlag";
	public static final String CREATE_DT = "createDt";
	public static final String NOTICE_TITLE = "noticeTitle";

}

