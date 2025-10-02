package com.kone.kitms.domain;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.ZonedDateTime;

@StaticMetamodel(AdminMenu.class)
@Generated("org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
public abstract class AdminMenu_ {

	
	/**
	 * @see com.kone.kitms.domain.AdminMenu#menuNo
	 **/
	public static volatile SingularAttribute<AdminMenu, Long> menuNo;
	
	/**
	 * @see com.kone.kitms.domain.AdminMenu#updatedBy
	 **/
	public static volatile SingularAttribute<AdminMenu, String> updatedBy;
	
	/**
	 * @see com.kone.kitms.domain.AdminMenu#menuIcon
	 **/
	public static volatile SingularAttribute<AdminMenu, String> menuIcon;
	
	/**
	 * @see com.kone.kitms.domain.AdminMenu#menuName
	 **/
	public static volatile SingularAttribute<AdminMenu, String> menuName;
	
	/**
	 * @see com.kone.kitms.domain.AdminMenu#isVisible
	 **/
	public static volatile SingularAttribute<AdminMenu, Boolean> isVisible;
	
	/**
	 * @see com.kone.kitms.domain.AdminMenu#isActive
	 **/
	public static volatile SingularAttribute<AdminMenu, Boolean> isActive;
	
	/**
	 * @see com.kone.kitms.domain.AdminMenu#createdAt
	 **/
	public static volatile SingularAttribute<AdminMenu, ZonedDateTime> createdAt;
	
	/**
	 * @see com.kone.kitms.domain.AdminMenu#menuOrder
	 **/
	public static volatile SingularAttribute<AdminMenu, Integer> menuOrder;
	
	/**
	 * @see com.kone.kitms.domain.AdminMenu#menuDescription
	 **/
	public static volatile SingularAttribute<AdminMenu, String> menuDescription;
	
	/**
	 * @see com.kone.kitms.domain.AdminMenu#menuUrl
	 **/
	public static volatile SingularAttribute<AdminMenu, String> menuUrl;
	
	/**
	 * @see com.kone.kitms.domain.AdminMenu#createdBy
	 **/
	public static volatile SingularAttribute<AdminMenu, String> createdBy;
	
	/**
	 * @see com.kone.kitms.domain.AdminMenu
	 **/
	public static volatile EntityType<AdminMenu> class_;
	
	/**
	 * @see com.kone.kitms.domain.AdminMenu#updatedAt
	 **/
	public static volatile SingularAttribute<AdminMenu, ZonedDateTime> updatedAt;

	public static final String MENU_NO = "menuNo";
	public static final String UPDATED_BY = "updatedBy";
	public static final String MENU_ICON = "menuIcon";
	public static final String MENU_NAME = "menuName";
	public static final String IS_VISIBLE = "isVisible";
	public static final String IS_ACTIVE = "isActive";
	public static final String CREATED_AT = "createdAt";
	public static final String MENU_ORDER = "menuOrder";
	public static final String MENU_DESCRIPTION = "menuDescription";
	public static final String MENU_URL = "menuUrl";
	public static final String CREATED_BY = "createdBy";
	public static final String UPDATED_AT = "updatedAt";

}

