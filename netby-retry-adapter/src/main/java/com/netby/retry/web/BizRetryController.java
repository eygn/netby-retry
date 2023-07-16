package com.netby.retry.web;

import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.Response;
import com.netby.core.annotation.LogPrinter;
import com.netby.retry.api.BizRetryServiceI;
import com.netby.retry.dto.BizRetryAddCmd;
import com.netby.retry.dto.BizRetryListQuery;
import com.netby.retry.dto.BizRetryUpdateCmd;
import com.netby.retry.dto.data.BizRetryDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 重试服务
 *
 * @author Elvan.bai
 * @date 2023/7/16 09:45
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class BizRetryController {

    private final BizRetryServiceI bizRetryService;

    @LogPrinter
    @PostMapping(value = "/bizretry")
    public Response addBizRetry(@RequestBody BizRetryAddCmd bizRetryAddCmd) {
        return bizRetryService.addBizRetry(bizRetryAddCmd);
    }

    @LogPrinter
    @PutMapping(value = "/bizretry")
    public Response updateBizRetry(@RequestBody BizRetryUpdateCmd bizRetryUpdateCmd) {
        return bizRetryService.updateRetry(bizRetryUpdateCmd);
    }

    @LogPrinter
    @GetMapping(value = "/bizretry/listByBizType")
    MultiResponse<BizRetryDTO> listByBizType(BizRetryListQuery bizRetryListQuery) {
        return bizRetryService.listByBizType(bizRetryListQuery);
    }

}
