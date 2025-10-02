package com.kone.kitms.domain;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.ZonedDateTime;

@StaticMetamodel(KitmsNewsroom.class)
@Generated("org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
public abstract class KitmsNewsroom_ {

	
	/**
	 * @see com.kone.kitms.domain.KitmsNewsroom#newsroomNo
	 **/
	public static volatile SingularAttribute<KitmsNewsroom, Long> newsroomNo;
	
	/**
	 * @see com.kone.kitms.domain.KitmsNewsroom#createdAt
	 **/
	public static volatile SingularAttribute<KitmsNewsroom, ZonedDateTime> createdAt;
	
	/**
	 * @see com.kone.kitms.domain.KitmsNewsroom#updatedBy
	 **/
	public static volatile SingularAttribute<KitmsNewsroom, String> updatedBy;
	
	/**
	 * @see com.kone.kitms.domain.KitmsNewsroom#newsroomUrl
	 **/
	public static volatile SingularAttribute<KitmsNewsroom, String> newsroomUrl;
	
	/**
	 * @see com.kone.kitms.domain.KitmsNewsroom#createdBy
	 **/
	public static volatile SingularAttribute<KitmsNewsroom, String> createdBy;
	
	/**
	 * @see com.kone.kitms.domain.KitmsNewsroom#newsroomTitle
	 **/
	public static volatile SingularAttribute<KitmsNewsroom, String> newsroomTitle;
	
	/**
	 * @see com.kone.kitms.domain.KitmsNewsroom#newsroomImage
	 **/
	public static volatile SingularAttribute<KitmsNewsroom, String> newsroomImage;
	
	/**
	 * @see com.kone.kitms.domain.KitmsNewsroom#newsroomStatus
	 **/
	public static volatile SingularAttribute<KitmsNewsroom, String> newsroomStatus;
	
	/**
	 * @see com.kone.kitms.domain.KitmsNewsroom
	 **/
	public static volatile EntityType<KitmsNewsroom> class_;
	
	/**
	 * @see com.kone.kitms.domain.KitmsNewsroom#updatedAt
	 **/
	public static volatile SingularAttribute<KitmsNewsroom, ZonedDateTime> updatedAt;

	public static final String NEWSROOM_NO = "newsroomNo";
	public static final String CREATED_AT = "createdAt";
	public static final String UPDATED_BY = "updatedBy";
	public static final String NEWSROOM_URL = "newsroomUrl";
	public static final String CREATED_BY = "createdBy";
	public static final String NEWSROOM_TITLE = "newsroomTitle";
	public static final String NEWSROOM_IMAGE = "newsroomImage";
	public static final String NEWSROOM_STATUS = "newsroomStatus";
	public static final String UPDATED_AT = "updatedAt";

}

