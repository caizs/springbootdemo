package org.caizs.project.exception;

import org.caizs.project.common.constants.CodeConstants;
import org.caizs.project.common.utils.ErrorCodeUtil;

public class ProjectException extends BaseException {

    private static final long serialVersionUID = 1L;

    public ProjectException() {
        super(new ErrorMessage(CodeConstants.SERVER_UNKNOW, ErrorCodeUtil.getMessageByCode(CodeConstants.SERVER_UNKNOW)));
    }

    public ProjectException(int code, Throwable cause) {
        super(new ErrorMessage(code, ErrorCodeUtil.getMessageByCode(code)), cause);
    }

    public ProjectException(Throwable cause) {
        super(cause);
    }

    public ProjectException(int code) {
        super(new ErrorMessage(code, ErrorCodeUtil.getMessageByCode(code)));
    }

    /**
     * 支持传入参数，替换错误信息中的占位符
     */
    public ProjectException(int code, Throwable cause, Object... args) {
        super(new ErrorMessage(code, ErrorCodeUtil.getMessageByCode(code, args)), cause);
    }

    /**
     * 支持传入参数，替换错误信息中的占位符
     */
    public ProjectException(int code, Object... args) {
        super(new ErrorMessage(code, ErrorCodeUtil.getMessageByCode(code, args)));
    }


}
