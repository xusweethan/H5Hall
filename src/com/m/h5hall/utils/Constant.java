package com.m.h5hall.utils;


public class Constant {
	//HouseHoder & Teacher
	public final static int APP_ROLE_HOUSEHODER = Const.APP_TSXT_PARENT;
	public final static int APP_ROLE_TEACHER = Const.APP_TSXT_TEACHER;
	
	public final static int APP_PUBLISH_SWITCH = Const.APP_PUBLISH_SWITCH;
	
	public final static String GOTO_CONTACT_DETAIL_INTENT_FROM_FLAG = "ContactDetailFromFlag";
	public final static String GOTO_INTENT_ACTIVITY_FLAG = "intent_flag";
	
	public final static String GOTO_CONTACT_DETAIL_INTENT_TEACHER_TO_TEACHER = "teacher_to_teacher";
	public final static String GOTO_CONTACT_DETAIL_INTENT_TEACHER_TO_HOUSEHODER = "teacher_to_househoder";
	public final static String GOTO_CONTACT_DETAIL_INTENT_HOUSEHODER_TO_TEACHER = "househoder_to_teacher";
	
	public final static String GOTO_INTENT_ACTIVITY_TITLE_FLAG = "activity_title_flag";
	
	public final static String BAIDU_PUSH_API_KEY = (APP_PUBLISH_SWITCH == APP_ROLE_TEACHER) ? "uVGsuf0qWr3qVSXBNdiOrihR":"2Mi8XOvETmK2IgQPynhRSwsk";
	
	public final static String APPLICATION_PKG_NAME = (APP_PUBLISH_SWITCH == APP_ROLE_TEACHER) ? "com.xiepei.tsxt_teacher":"com.xiepei.tsxt_parent";
	
	public static final String EM_CLASS = (APP_PUBLISH_SWITCH == APP_ROLE_TEACHER) ? "com.xiepei.tsxt_teacher.utils.SmileUtils" : "com.xiepei.tsxt_parent.utils.SmileUtils";
	public static final String CHAT_ACTIVITY = (APP_PUBLISH_SWITCH == APP_ROLE_TEACHER) ? "com.xiepei.tsxt_teacher.index.chat.ChatActivity":"com.xiepei.tsxt_parent.index.chat.ChatActivity";
}
