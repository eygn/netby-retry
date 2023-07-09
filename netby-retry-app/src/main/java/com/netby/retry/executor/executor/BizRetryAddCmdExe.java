
package com.netby.retry.executor.executor;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.exception.BizException;
import com.netby.common.vo.BaseVO;
import com.netby.retry.domain.retry.BizRetry;
import com.netby.retry.domain.retry.gateway.BizRetryGateway;
import com.netby.retry.dto.BizRetryAddCmd;
import com.netby.retry.dto.data.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class BizRetryAddCmdExe {

    private final BizRetryGateway bizRetryGateway;

    public Response execute(BizRetryAddCmd cmd) {
        bizRetryGateway.add(BaseVO.copyTo(cmd.getBizRrtryDTO(), BizRetry.class));
        return Response.buildSuccess();
    }

}
