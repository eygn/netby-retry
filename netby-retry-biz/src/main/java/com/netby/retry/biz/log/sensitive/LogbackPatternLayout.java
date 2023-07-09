package com.netby.retry.biz.log.sensitive;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import com.netby.common.util.JsonUtil;
import com.netby.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.CollectionUtils;

/**
 * @author: byg
 */
@Slf4j
public class LogbackPatternLayout extends PatternLayout {

    /**
     * 正则替换规则
     */
    private LogbackReplaces replaces;
    /**
     * 是否开启脱敏，默认关闭(false）
     */
    private Boolean enable;
    /**
     * 日志最大长度
     */
    private Integer logMaxLength;
    /**
     * 不限制长度
     */
    public static final int NO_LIMIT = -1;


    public LogbackPatternLayout(String rulePath, Boolean enable, Integer maxLength) {
        super();
        logMaxLengthHandle(maxLength);
        sensitiveHandle(enable);
        if (logOpenSensitive() && StringUtil.isNotBlank(rulePath)) {
            try {
                ClassPathResource resource = new ClassPathResource(rulePath);
                BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
                String jsonRegex = br.lines().collect(Collectors.joining("\n"));
                List<RegexReplacement> replacementList = JsonUtil.readAsList(jsonRegex, RegexReplacement.class);
                replaces = new LogbackReplaces();
                replaces.setReplace(replacementList);
            } catch (FileNotFoundException ignored) {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void sensitiveHandle(Boolean openSensitive) {
        this.enable = openSensitive;
        if (openSensitive == null) {
            this.enable = Boolean.FALSE;
        }
    }

    private void logMaxLengthHandle(Integer maxLength) {
        this.logMaxLength = maxLength;
        if (maxLength == null || maxLength < NO_LIMIT) {
            this.logMaxLength = NO_LIMIT;
        }
        //长度为0,也特殊处理为不限制
        if (this.logMaxLength == 0) {
            this.logMaxLength = NO_LIMIT;
        }
    }

    /**
     * 格式化日志信息
     *
     * @param event ILoggingEvent
     * @return 日志信息
     */
    @Override
    public String doLayout(ILoggingEvent event) {
        // 占位符填充
        String msg = super.doLayout(event);
        // 脱敏处理
        return this.buildSensitiveMsg(msg, event.getLevel());
    }

    /**
     * 根据配置对日志进行脱敏
     *
     * @param msg 消息
     * @return 脱敏后的日志信息
     */
    @SuppressWarnings("AlibabaAvoidComplexCondition")
    public String buildSensitiveMsg(String msg, Level level) {
        if (StringUtil.isBlank(msg)) {
            return msg;
        }
        if (!logOpenSensitive()) {
            // 未开启脱敏
            return msg;
        }
        if (this.replaces == null || CollectionUtils.isEmpty(this.replaces.getReplace())) {
            return msg;
        }

        //长度处理 ,error级别打印完整日志
        if ((level == null || level != Level.ERROR) && (this.logMaxLength != NO_LIMIT && msg.length() > logMaxLength)) {
            msg = new StringBuilder().append(msg, 0, logMaxLength).append("...").toString();
        }

        String sensitiveMsg = msg;
        for (RegexReplacement replace : this.replaces.getReplace()) {
            // 脱敏
            sensitiveMsg = replace.format(sensitiveMsg);
        }
        return sensitiveMsg;
    }

    public Boolean logOpenSensitive() {
        return enable != null && enable;
    }
}
