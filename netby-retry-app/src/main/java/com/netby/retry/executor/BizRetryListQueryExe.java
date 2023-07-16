package com.netby.retry.executor;

import com.alibaba.cola.dto.MultiResponse;
import com.netby.common.vo.BaseVO;
import com.netby.retry.domain.retry.BizRetry;
import com.netby.retry.domain.retry.gateway.BizRetryGateway;
import com.netby.retry.dto.BizRetryListQuery;
import com.netby.retry.dto.data.BizRetryDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @author Elvan.bai
 * @date 2023/7/16 10:08
 */
@Component
@RequiredArgsConstructor
public class BizRetryListQueryExe {

    @Getter
    private final BizRetryGateway bizRetryGateway;

    public MultiResponse<BizRetryDTO> execute(BizRetryListQuery cmd) {
        List<BizRetry> bizRetryList = bizRetryGateway.listByBizType(cmd.getBizType());
        return MultiResponse.of(BaseVO.copyListTo(bizRetryList, BizRetryDTO.class));
    }
}
