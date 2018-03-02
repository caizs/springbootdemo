package org.caizs.project.exception;

public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    /**
     * 错误信息定义类
     */
    protected ErrorMessage msgDef;

    public BaseException(String message) {
        super(message);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(ErrorMessage msgDef) {
        super(msgDef.getErrMsg());
        setMsgDef(msgDef);
    }

    public BaseException(ErrorMessage msgDef, Throwable cause) {
        super(msgDef.getErrMsg(), cause);
        setMsgDef(msgDef);
    }

    public ErrorMessage getMsgDef() {
        return msgDef;
    }

    public void setMsgDef(ErrorMessage msgDef) {
        this.msgDef = msgDef;
    }


}