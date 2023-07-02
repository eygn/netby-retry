package com.netby.biz.retry.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.netby.biz.retry.context.BusinessContext;

/**
 * 日志统一替换
 *
 * @author: byg
 */
@SuppressWarnings("unused")
public class IdConvert extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent iLoggingEvent) {
        return BusinessContext.getLogTrace();
    }
}
