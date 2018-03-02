package org.caizs.project.common.constants;

public interface BaseCode {

    /**
     * 出错，下面对错误状态码的定义，错误状态码数值>=100000
     * <p>
     * <p>
     * <p>
     * 每个error code由 6 位的整型数字，分 3 段有含义的数值表示；具体定义方式后面有举例
     * <p>
     * 第1段 由第1位数字表示，其代表error的出处 1:server，2:android，3:web page，4:pc client，9:other;
     * <p>
     * 如 1XXXXX，为每个类别定义起始位
     * <p>
     * 第2段 由第2～3位数字表示，其代表error的类别
     * <p>
     * 第3段 由第4～6位数字表示，自然增长，定义是＋
     */

    // 第1段定义

    public static final int SERVER_ERROR = 100000;
    public static final int ANDROID_ERROR = 200000;
    public static final int WEB_PAGE_ERROR = 300000;
    public static final int PC_CLIENT_ERROR = 400000;
    public static final int OTHER_ERROR = 900000;

    // 第2段定义，此段数字单独没有意义，必需与第1段及具体错误码结合使用，
    // 前后端共用类别
    public static final int DB = 91000; // DB访问错误
    public static final int DS = 92000; // I/O文件类访问错误
    public static final int CACHE = 93000; // 缓存类访问错误
    public static final int THIRD = 94000; // 特定第三方Local api调用错误，如文件指文计算，文件压缩，IP转城市等
    public static final int BS = 95000; // 访问其它应用出错，如其它服务化接口
    public static final int SECURITY = 98000; // 安全类出错，试图操作未授权资源

    // 服务端
    public static final int WEB = 10000; // web代码层出错
    public static final int BIZ = 20000; // service代码层出错
    public static final int DAO = 30000; // dao代码层出错

    // 手机端或PC端
    public static final int UI = 50000; // 手机app层出错或PC app层出错
    public static final int HTTP = 60000; // 手机服务访问出错

    // Web页面
    public static final int JS = 80000; // web html／js前端页面处理出错

    // server服务端错务码定义
    public static final int SERVER_UNKNOW = SERVER_ERROR + 0; // 100000 表示为服务端未知错误unknow，可视为无定义时的默认值undefine
 
}