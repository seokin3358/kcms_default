package com.kone.kitms.domain;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.ZonedDateTime;

@StaticMetamodel(AdminMenuPermission.class)
@Generated("org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
public abstract class AdminMenuPermission_ {

	
	/**
	 * @see com.kone.kitms.domain.AdminMenuPermission#createdAt
	 **/
	public static volatile SingularAttribute<AdminMenuPermission, ZonedDateTime> createdAt;
	
	/**
	 * @see com.kone.kitms.domain.AdminMenuPermission#menuNo
	 **/
	public static volatile SingularAttribute<AdminMenuPermission, Long> menuNo;
	
	/**
	 * @see com.kone.kitms.domain.AdminMenuPermission#updatedBy
	 **/
	public static volatile SingularAttribute<AdminMenuPermission, String> updatedBy;
	
	/**
	 * @see com.kone.kitms.domain.AdminMenuPermission#createdBy
	 **/
	public static volatile SingularAttribute<AdminMenuPermission, String> createdBy;
	
	/**
	 * @see com.kone.kitms.domain.AdminMenuPermission#isGranted
	 **/
	public static volatile SingularAttribute<AdminMenuPermission, Boolean> isGranted;
	
	/**
	 * @see com.kone.kitms.domain.AdminMenuPermission#permissionNo
	 **/
	public static volatile SingularAttribute<AdminMenuPermission, Long> permissionNo;
	
	/**
	 * @see com.kone.kitms.domain.AdminMenuPermission#menu
	 **/
	public static volatile SingularAttribute<AdminMenuPermission, AdminMenu> menu;
	
	/**
	 * @see com.kone.kitms.domain.AdminMenuPermission
	 **/
	public static volatile EntityType<AdminMenuPermission> class_;
	
	/**
	 * @see com.kone.kitms.domain.AdminMenuPermission#userId
	 **/
	public static volatile SingularAttribute<AdminMenuPermission, String> userId;
	
	/**
	 * @see com.kone.kitms.domain.AdminMenuPermission#user
	 **/
	public static volatile SingularAttribute<AdminMenuPermission, KitmsUser> user;
	
	/**
	 * @see com.kone.kitms.domain.AdminMenuPermission#updatedAt
	 **/
	public static volatile SingularAttribute<AdminMenuPermission, ZonedDateTime> updatedAt;

	public static final String CREATED_AT = "createdAt";
	public static final String MENU_NO = "menuNo";
	public static final String UPDATED_BY = "updatedBy";
	public static final String CREATED_BY = "createdBy";
	public static final String IS_GRANTED = "isGranted";
	public static final String PERMISSION_NO = "permissionNo";
	public static final String MENU = "menu";
	public static final String USER_ID = "userId";
	public static final String USER = "user";
	public static final String UPDATED_AT = "updatedAt";

}

