package com.fordece.forum.utils;

public class Const {
    public static final String JWT_BLACK_LIST = "jwt:blacklist:";
    public static final String VERIFY_EMAIL_LIMIT = "verify:email:limit:";
    public static final String VERIFY_EMAIL_DATA = "verify:email:data:";

    public static final String FLOW_LIMIT_COUNTER = "flow:counter:";
    public static final String FLOW_LIMIT_BLOCK = "flow:block:";

    public static final String STAR_COUNTER = "star:counter:"; // 用户点赞记录，每天30个，凌晨清空

    public static final int ORDER_LIMIT = -101;

    public static final int ORDER_CORS = -102;

}
