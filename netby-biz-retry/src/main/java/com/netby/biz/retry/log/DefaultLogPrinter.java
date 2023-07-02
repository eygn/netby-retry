package com.netby.biz.retry.log;

import com.netby.common.util.JsonUtil;

import java.util.Map;

/**
 * @author: byg
 */
public class DefaultLogPrinter implements LogPrinterFunction {
    @Override
    public Map<String, Object> convert2Map(Object object) {
        return JsonUtil.convertToMap(object);
    }

    @Override
    public String toString(Map<String, Object> jsonMap) {
        return JsonUtil.writeValueAsString(jsonMap);
    }
}
