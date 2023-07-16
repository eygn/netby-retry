
package com.netby.retry.executor;

import com.alibaba.cola.dto.Response;
import com.netby.common.vo.BaseVO;
import com.netby.retry.domain.retry.BizRetry;
import com.netby.retry.domain.retry.gateway.BizRetryGateway;
import com.netby.retry.dto.BizRetryAddCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 行为命令模式：增加retry
 * @author Elvan.baiang
 * @date 2023/7/15 10:52
 */
@Component
@RequiredArgsConstructor
public class BizRetryAddCmdExe {

    private final BizRetryGateway bizRetryGateway;

    public Response execute(BizRetryAddCmd cmd) {
        bizRetryGateway.add(BaseVO.copyTo(cmd.getBizRetryDTO(), BizRetry.class));
        return Response.buildSuccess();
    }

}
