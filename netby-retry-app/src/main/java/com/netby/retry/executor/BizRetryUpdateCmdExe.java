
package com.netby.retry.executor;

import com.netby.common.vo.BaseVO;
import com.netby.common.vo.Response;
import com.netby.retry.domain.retry.BizRetry;
import com.netby.retry.domain.retry.gateway.BizRetryGateway;
import com.netby.retry.dto.BizRetryUpdateCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Elvan.bai
 * @date 2023/7/16 11:10
 */
@Component
@RequiredArgsConstructor
public class BizRetryUpdateCmdExe {

    private final BizRetryGateway bizRetryGateway;

    public Response execute(BizRetryUpdateCmd cmd) {
        bizRetryGateway.update(BaseVO.copyTo(cmd.getBizRetryDTO(), BizRetry.class));
        return Response.success();
    }

}
