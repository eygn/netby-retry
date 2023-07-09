package com.netby.retry.biz.log;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: byg
 */
@Getter
@Setter
@ConfigurationProperties(prefix = LogPrinterProperties.PREFIX)
public class LogPrinterProperties {

    static final String PREFIX = "netby.log";

    /**
     * 是否开启切面日志记录
     */
    private Boolean enable = false;
    /**
     * 日志忽略字段提示文本
     */
    private String ignoreText;

    /**
     * 全局忽略请求字段
     */
    private Set<String> ignoreReq;

    /**
     * 全局忽略返回字段
     */
    private Set<String> ignoreResp;

    /**
     * 包扫描路径，支持多个包路径用逗号分割
     */
    private String packagePath;
}
