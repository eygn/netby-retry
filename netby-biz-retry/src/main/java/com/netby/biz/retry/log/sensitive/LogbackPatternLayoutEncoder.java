package com.netby.biz.retry.log.sensitive;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/***
 * @author: byg
 * 日志输出编码
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LogbackPatternLayoutEncoder extends PatternLayoutEncoder {

    /**
     * 校验规则文件路径
     */
    private String rulePath;
    /**
     * 是否开启脱敏，默认关闭(false）'
     */
    private Boolean enable = false;

    private Integer logMaxLength;

    /**
     * 使用自定义 格式化输出
     */
    @Override
    public void start() {
        LogbackPatternLayout patternLayout = new LogbackPatternLayout(rulePath, enable, logMaxLength);
        patternLayout.setContext(context);
        patternLayout.setPattern(this.getPattern());
        patternLayout.setOutputPatternAsHeader(outputPatternAsHeader);
        patternLayout.start();
        this.layout = patternLayout;
        started = true;
    }

}