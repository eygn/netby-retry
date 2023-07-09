package com.netby.retry.biz.log;

import java.util.Map;

/**
 * @author: byg
 */
public interface LogPrinterFunction {
    /**
     * 对象转 map
     *
     * @param object 需要打印的对象
     * @return 转换后的 Map
     */
    Map<String, Object> convert2Map(Object object);

    /**
     * 需要打印的日志
     *
     * @param jsonMap jsonMap
     * @return 日志
     */
    String toString(Map<String, Object> jsonMap);
}
