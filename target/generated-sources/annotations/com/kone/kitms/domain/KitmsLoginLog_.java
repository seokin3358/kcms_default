package com.kone.kitms.domain;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.ZonedDateTime;

@StaticMetamodel(KitmsLoginLog.class)
@Generated("org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
public abstract class KitmsLoginLog_ {

	
	/**
	 * @see com.kone.kitms.domain.KitmsLoginLog#logNo
	 **/
	public static volatile SingularAttribute<KitmsLoginLog, Long> logNo;
	
	/**
	 * @see com.kone.kitms.domain.KitmsLoginLog#loginSuccess
	 **/
	public static volatile SingularAttribute<KitmsLoginLog, Boolean> loginSuccess;
	
	/**
	 * @see com.kone.kitms.domain.KitmsLoginLog#loginReason
	 **/
	public static volatile SingularAttribute<KitmsLoginLog, String> loginReason;
	
	/**
	 * @see com.kone.kitms.domain.KitmsLoginLog#loginIp
	 **/
	public static volatile SingularAttribute<KitmsLoginLog, String> loginIp;
	
	/**
	 * @see com.kone.kitms.domain.KitmsLoginLog
	 **/
	public static volatile EntityType<KitmsLoginLog> class_;
	
	/**
	 * @see com.kone.kitms.domain.KitmsLoginLog#userId
	 **/
	public static volatile SingularAttribute<KitmsLoginLog, String> userId;
	
	/**
	 * @see com.kone.kitms.domain.KitmsLoginLog#loginDt
	 **/
	public static volatile SingularAttribute<KitmsLoginLog, ZonedDateTime> loginDt;

	public static final String LOG_NO = "logNo";
	public static final String LOGIN_SUCCESS = "loginSuccess";
	public static final String LOGIN_REASON = "loginReason";
	public static final String LOGIN_IP = "loginIp";
	public static final String USER_ID = "userId";
	public static final String LOGIN_DT = "loginDt";

}

