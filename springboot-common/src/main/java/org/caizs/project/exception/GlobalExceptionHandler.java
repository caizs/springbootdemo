package org.caizs.project.exception;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.caizs.project.common.constants.CodeConstants;
import org.caizs.project.common.utils.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;


@ControllerAdvice("com.lianfan")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(HttpServletRequest request, Exception ex, HttpServletResponse response) throws IOException {
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.BAD_REQUEST.value());// ajax根据http  // code判断请求是否失败
                                                          
        printTrace(request, ex); // 打印堆栈
        ProjectException numasEx = convertException(ex);
        if (htmlHasPriority(request.getHeader("accept"))) { // html或者jsp页面请求发生错误，跳转到错误页面
            return errorHtml(numasEx);
        } else { // 其他都返回json
            return errorJson(numasEx, response);
        }
    }

    private ModelAndView errorHtml( ProjectException numasEx) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("errMsg", numasEx.getMsgDef().getErrMsg());
        ModelAndView m = new ModelAndView("/errors/errPage", model);
        return m;
    }

    private ModelAndView errorJson(ProjectException numasEx, HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=utf-8");  
        response.setContentType("text/JavaScript; charset=utf-8");  
        response.getWriter().print(Json.toJson(numasEx.getMsgDef()));
        return null;
    }

    private ProjectException convertException(Exception ex) {
        ProjectException numasEx = null;
        if (!(ex instanceof ProjectException)) {
            if (StringUtils.isBlank(ex.getMessage())) {
                numasEx = new ProjectException(CodeConstants.SERVER_UNKNOW, ex, ex.getClass().getSimpleName());
            } else {
                numasEx = new ProjectException(CodeConstants.SERVER_UNKNOW, ex, ex.getClass().getSimpleName() + "(" + ex.getMessage() + ")");
            }
        } else {
            numasEx = (ProjectException) ex;
        }
        return numasEx;
    }

    /**
     * html为当前请求mime最高优先级
     * 
     * @param acceptString
     *            - HTTP accept header field, format according to HTTP spec:
     *            "mime1;quality1,mime2;quality2,mime3,mime4,..." (quality is
     *            optional)
     * @return true only if html is the MIME type with highest quality of all
     *         specified MIME types.
     */
    private boolean htmlHasPriority(String acceptString) {
        if (acceptString != null) {
            final String[] mimes = acceptString.split(",");
            Arrays.sort(mimes, new MimeQualityComparator());
            final String firstMime = mimes[0].split(";")[0];
            return firstMime.equals("text/html");
        }
        return false;
    }

    private static class MimeQualityComparator implements Comparator<String> {
        @Override
        public int compare(String mime1, String mime2) {
            final double m1Quality = getQualityofMime(mime1);
            final double m2Quality = getQualityofMime(mime2);
            return Double.compare(m1Quality, m2Quality) * -1;
        }
    }

    /**
     * @param mimeAndQuality
     *            - "mime;quality" pair from the accept header of a HTTP
     *            request, according to HTTP spec (missing mimeQuality means
     *            quality = 1).
     * @return quality of this pair according to HTTP spec.
     */
    private static Double getQualityofMime(String mimeAndQuality) {
        // split off quality factor
        final String[] mime = mimeAndQuality.split(";");
        if (mime.length <= 1) {
            return 1.0;
        } else {
            final String quality = mime[1].split("=")[1];
            return Double.parseDouble(quality);
        }
    }

    private void printTrace(HttpServletRequest request, Exception ex) {
        ByteArrayOutputStream body = (ByteArrayOutputStream) request.getAttribute("RequestBody");

        String bodyStr = "";

        if (body != null) {
            try {
                bodyStr = StringEscapeUtils.escapeJava(body.toString("utf-8"));
            } catch (UnsupportedEncodingException e1) {
                logger.error("UnsupportedEncoding", e1);
            }
        }

        logger.error("Call [{}:{}] failed! Client:[{}], Session:[{}], Parameter:[{}], Body:[{}].", request.getMethod(), request.getRequestURI(),
                request.getRemoteAddr(), request.getRequestedSessionId(), request.getQueryString(), body == null ? "" : bodyStr, ex);
    }
}
