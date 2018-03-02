package org.caizs.project.common.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.caizs.project.common.constants.CodeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

public final class ErrorCodeUtil {

    private static Map<Integer, String> codeByMessageMap = new HashMap<Integer, String>();
    
    private static MessageSource messageSource = SpringContextHolder.getBean(MessageSource.class);

    private static final Locale LANGUAGE = Locale.CHINESE;// 这里默认指定为中文

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorCodeUtil.class);

    static {
        Object o = new CodeConstants();
        java.lang.reflect.Field[] fs = o.getClass().getFields();
        try {
            for (int i = 0; i < fs.length; i++) {
                String fieldName = "";
                fieldName = fs[i].getName().replaceAll("_", ".").toLowerCase();
                if (fs[i].getType() == int.class) {
                    codeByMessageMap.put(Integer.valueOf(fs[i].getInt(o)), fieldName);
                }
            }
        } catch (Exception e) {
            LOGGER.error("", e);
        }
    }

    public static String getMessageByCode(int code, Object... args) {
        return messageSource.getMessage(codeByMessageMap.get(code), args, LANGUAGE);
    }

}
