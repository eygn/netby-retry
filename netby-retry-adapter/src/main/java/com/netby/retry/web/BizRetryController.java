package com.netby.retry.web;

import com.alibaba.cola.dto.Response;
import com.netby.common.util.JsonUtil;
import com.netby.core.annotation.LogPrinter;
import com.netby.retry.api.BizRetryServiceI;
import com.netby.retry.dto.BizRetryAddCmd;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class BizRetryController {

    @Autowired
    private BizRetryServiceI bizRetryService;

    @LogPrinter
    @PostMapping(value = "/bizretry")
    public Response addBizRetry(@RequestBody BizRetryAddCmd bizRetryAddCmd) {
        log.info("[addBizRetry]入参:{}", JsonUtil.writeValueAsString(bizRetryAddCmd));
        return bizRetryService.addBizRetry(bizRetryAddCmd);
    }
}
