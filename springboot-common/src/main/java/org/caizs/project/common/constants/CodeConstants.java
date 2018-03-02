package org.caizs.project.common.constants;

/**
 * 异常错误码类 大写命名，用下划线'_'做分割 映射的property文件的key用点'.'做分割
 */
public class CodeConstants implements BaseCode {
    // 服务端业务异常, code区间
    // 120001 - 121000   //前1000公用，其他同理
    
    // 121001 - 122000
    // 122001 - 123000
    // 123001 - 124000
    // 124001 - 125000
    // 125001 - 126000  
    // 126001 - 127000  
    // 127001 - 128000  
    // 128001 - 129000  
    // 129001 - 129999  
    
    //BIZ COMMON
    public static final int OBJECT_UNEXIST = SERVER_ERROR + BIZ + 1;

    /* SECURITY 开始 */
    // 用户不存在
    public static final int PLT_SECURITY_LOGIN_UNKNOW = SERVER_ERROR + SECURITY + 1;
    // 账号被锁定
    public static final int PLT_SECURITY_LOGIN_LOCKED = SERVER_ERROR + SECURITY + 2;
    // 账号被禁用
    public static final int PLT_SECURITY_LOGIN_DISABLED = SERVER_ERROR + SECURITY + 3;
    // 密码错误
    public static final int PLT_SECURITY_LOGIN_PASSWORD_ERROR = SERVER_ERROR + SECURITY + 4;
    // 用户名或密码错误
    public static final int PLT_SECURITY_LOGIN_ACCOUNT_ERROR = SERVER_ERROR + SECURITY + 5;
    // 用户不存在
    public static final int USER_UNEXIST_ERROR = SERVER_ERROR + SECURITY + 6;
    // 护理单元不存在
    public static final int NURSEUNIT_UNEXIST_ERROR = SERVER_ERROR + SECURITY + 7;
    // 登入超时错误
    public static final int PLT_SESSION_TIMEOUT = SERVER_ERROR + SECURITY + 8;
    /* SECURITY 结束 */
 
}
