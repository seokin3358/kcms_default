package com.kone.kitms.domain;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.ZonedDateTime;

@StaticMetamodel(KitmsAttach.class)
@Generated("org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
public abstract class KitmsAttach_ {

	
	/**
	 * @see com.kone.kitms.domain.KitmsAttach#createUserId
	 **/
	public static volatile SingularAttribute<KitmsAttach, String> createUserId;
	
	/**
	 * @see com.kone.kitms.domain.KitmsAttach#attachFile
	 **/
	public static volatile SingularAttribute<KitmsAttach, byte[]> attachFile;
	
	/**
	 * @see com.kone.kitms.domain.KitmsAttach#attachTableName
	 **/
	public static volatile SingularAttribute<KitmsAttach, String> attachTableName;
	
	/**
	 * @see com.kone.kitms.domain.KitmsAttach#attachFilePath
	 **/
	public static volatile SingularAttribute<KitmsAttach, String> attachFilePath;
	
	/**
	 * @see com.kone.kitms.domain.KitmsAttach#attachTablePk
	 **/
	public static volatile SingularAttribute<KitmsAttach, Long> attachTablePk;
	
	/**
	 * @see com.kone.kitms.domain.KitmsAttach#attachFileSize
	 **/
	public static volatile SingularAttribute<KitmsAttach, Long> attachFileSize;
	
	/**
	 * @see com.kone.kitms.domain.KitmsAttach#isThumbnail
	 **/
	public static volatile SingularAttribute<KitmsAttach, Boolean> isThumbnail;
	
	/**
	 * @see com.kone.kitms.domain.KitmsAttach#attachNo
	 **/
	public static volatile SingularAttribute<KitmsAttach, Long> attachNo;
	
	/**
	 * @see com.kone.kitms.domain.KitmsAttach
	 **/
	public static volatile EntityType<KitmsAttach> class_;
	
	/**
	 * @see com.kone.kitms.domain.KitmsAttach#createDt
	 **/
	public static volatile SingularAttribute<KitmsAttach, ZonedDateTime> createDt;
	
	/**
	 * @see com.kone.kitms.domain.KitmsAttach#attachFileName
	 **/
	public static volatile SingularAttribute<KitmsAttach, String> attachFileName;

	public static final String CREATE_USER_ID = "createUserId";
	public static final String ATTACH_FILE = "attachFile";
	public static final String ATTACH_TABLE_NAME = "attachTableName";
	public static final String ATTACH_FILE_PATH = "attachFilePath";
	public static final String ATTACH_TABLE_PK = "attachTablePk";
	public static final String ATTACH_FILE_SIZE = "attachFileSize";
	public static final String IS_THUMBNAIL = "isThumbnail";
	public static final String ATTACH_NO = "attachNo";
	public static final String CREATE_DT = "createDt";
	public static final String ATTACH_FILE_NAME = "attachFileName";

}

