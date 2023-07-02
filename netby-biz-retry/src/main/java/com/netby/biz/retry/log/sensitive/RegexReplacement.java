package com.netby.biz.retry.log.sensitive;


import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: byg
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegexReplacement {

    /**
     * 脱敏匹配正则
     */
    private Pattern regex;

    /**
     * 替换正则
     */
    private String replacement;

    /**
     * @param msg 信息
     * @return 加密
     */
    public String format(final String msg) {
        return regex.matcher(msg).replaceAll(replacement);
    }

    public void setRegex(String regex) {
        this.regex = Pattern.compile(regex);
    }


    public static RegexReplacement getEntity(String regex, String replacement) {
        return new RegexReplacement(Pattern.compile(regex), replacement);
    }
}
