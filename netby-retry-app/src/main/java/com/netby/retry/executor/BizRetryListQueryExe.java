package com.netby.retry.executor;

import com.alibaba.cola.dto.MultiResponse;
import com.netby.retry.domain.retry.gateway.BizRetryGateway;
import com.netby.retry.dto.BizRetryListQuery;
import com.netby.retry.dto.data.BizRrtryDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class BizRetryListQueryExe {

    @Getter
    private final BizRetryGateway bizRetryGateway;

    public MultiResponse<BizRrtryDTO> execute(BizRetryListQuery cmd) {
        // TODO
        return MultiResponse.of(null);
    }
}
