package com.netby.biz.retry.log.sensitive;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: byg
 */
@Getter
@Setter
@ConfigurationProperties(prefix = LogSensitiveProperties.PREFIX)
public class LogSensitiveProperties {

    static final String PREFIX = "netby.log.sensitive";

    /**
     * 校验规则文件路径
     */
    private String rulePath;
    /**
     * 是否开启脱敏，默认关闭(false）,注意要配合logback.xml配置
     */
    private Boolean enable = false;

    /**
     * 日志最大长度，超出长度，日志会被截断，<=0为不限制
     */
    private Integer logMaxLength;
}
