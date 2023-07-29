package com.netby.retry.integration;

import com.netby.common.state.CommonStateCode;
import com.netby.common.vo.Response;
import com.netby.retry.dto.BizRetryAddCmd;
import org.springframework.stereotype.Component;

/**
 * 记录三方日志外调接口
 *
 * @author: Elvan.bai
 * @date: 2023/7/29 17:03
 */
@Component
public class ThirdLogFacadeIntegration {

    public Response addBizRetry(BizRetryAddCmd bizRetryAddCmd) {
        return Response.failed(CommonStateCode.INNER_SERVER_ERROR);
    }
}
