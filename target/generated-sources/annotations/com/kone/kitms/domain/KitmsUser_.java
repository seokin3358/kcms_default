package com.kone.kitms.domain;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@StaticMetamodel(KitmsUser.class)
@Generated("org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
public abstract class KitmsUser_ {

	
	/**
	 * @see com.kone.kitms.domain.KitmsUser#createUserId
	 **/
	public static volatile SingularAttribute<KitmsUser, String> createUserId;
	
	/**
	 * @see com.kone.kitms.domain.KitmsUser#lastPassModDt
	 **/
	public static volatile SingularAttribute<KitmsUser, ZonedDateTime> lastPassModDt;
	
	/**
	 * @see com.kone.kitms.domain.KitmsUser#authCode
	 **/
	public static volatile SingularAttribute<KitmsUser, String> authCode;
	
	/**
	 * @see com.kone.kitms.domain.KitmsUser#onsiteBranchNo
	 **/
	public static volatile SingularAttribute<KitmsUser, Long> onsiteBranchNo;
	
	/**
	 * @see com.kone.kitms.domain.KitmsUser#userNo
	 **/
	public static volatile SingularAttribute<KitmsUser, Long> userNo;
	
	/**
	 * @see com.kone.kitms.domain.KitmsUser#organNo
	 **/
	public static volatile SingularAttribute<KitmsUser, Long> organNo;
	
	/**
	 * @see com.kone.kitms.domain.KitmsUser#userPwd
	 **/
	public static volatile SingularAttribute<KitmsUser, String> userPwd;
	
	/**
	 * @see com.kone.kitms.domain.KitmsUser#vacStartDate
	 **/
	public static volatile SingularAttribute<KitmsUser, LocalDate> vacStartDate;
	
	/**
	 * @see com.kone.kitms.domain.KitmsUser#userTel
	 **/
	public static volatile SingularAttribute<KitmsUser, String> userTel;
	
	/**
	 * @see com.kone.kitms.domain.KitmsUser#userName
	 **/
	public static volatile SingularAttribute<KitmsUser, String> userName;
	
	/**
	 * @see com.kone.kitms.domain.KitmsUser#userId
	 **/
	public static volatile SingularAttribute<KitmsUser, String> userId;
	
	/**
	 * @see com.kone.kitms.domain.KitmsUser#createDt
	 **/
	public static volatile SingularAttribute<KitmsUser, ZonedDateTime> createDt;
	
	/**
	 * @see com.kone.kitms.domain.KitmsUser#vacEndDate
	 **/
	public static volatile SingularAttribute<KitmsUser, LocalDate> vacEndDate;
	
	/**
	 * @see com.kone.kitms.domain.KitmsUser#userEtc
	 **/
	public static volatile SingularAttribute<KitmsUser, String> userEtc;
	
	/**
	 * @see com.kone.kitms.domain.KitmsUser#userMobile
	 **/
	public static volatile SingularAttribute<KitmsUser, String> userMobile;
	
	/**
	 * @see com.kone.kitms.domain.KitmsUser#enable
	 **/
	public static volatile SingularAttribute<KitmsUser, Boolean> enable;
	
	/**
	 * @see com.kone.kitms.domain.KitmsUser#userEmail
	 **/
	public static volatile SingularAttribute<KitmsUser, String> userEmail;
	
	/**
	 * @see com.kone.kitms.domain.KitmsUser#firstFlag
	 **/
	public static volatile SingularAttribute<KitmsUser, Boolean> firstFlag;
	
	/**
	 * @see com.kone.kitms.domain.KitmsUser#userRole
	 **/
	public static volatile SingularAttribute<KitmsUser, String> userRole;
	
	/**
	 * @see com.kone.kitms.domain.KitmsUser
	 **/
	public static volatile EntityType<KitmsUser> class_;

	public static final String CREATE_USER_ID = "createUserId";
	public static final String LAST_PASS_MOD_DT = "lastPassModDt";
	public static final String AUTH_CODE = "authCode";
	public static final String ONSITE_BRANCH_NO = "onsiteBranchNo";
	public static final String USER_NO = "userNo";
	public static final String ORGAN_NO = "organNo";
	public static final String USER_PWD = "userPwd";
	public static final String VAC_START_DATE = "vacStartDate";
	public static final String USER_TEL = "userTel";
	public static final String USER_NAME = "userName";
	public static final String USER_ID = "userId";
	public static final String CREATE_DT = "createDt";
	public static final String VAC_END_DATE = "vacEndDate";
	public static final String USER_ETC = "userEtc";
	public static final String USER_MOBILE = "userMobile";
	public static final String ENABLE = "enable";
	public static final String USER_EMAIL = "userEmail";
	public static final String FIRST_FLAG = "firstFlag";
	public static final String USER_ROLE = "userRole";

}

