package com.netby.retry.web;

import com.netby.common.vo.PageResult;
import com.netby.common.vo.Response;
import com.netby.core.annotation.LogPrinter;
import com.netby.retry.BizRetryService;
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
@RequestMapping("/bizRetry")
@RequiredArgsConstructor
public class BizRetryController {

    private final BizRetryService bizRetryService;

    @LogPrinter
    @PostMapping("/add")
    public Response addBizRetry(@RequestBody BizRetryAddCmd bizRetryAddCmd) {
        return bizRetryService.addBizRetry(bizRetryAddCmd);
    }

    @LogPrinter
    @PutMapping("/update")
    public Response updateBizRetry(@RequestBody BizRetryUpdateCmd bizRetryUpdateCmd) {
        return bizRetryService.updateRetry(bizRetryUpdateCmd);
    }

    @LogPrinter(ignoreResp = {"records"})
    @PostMapping("/queryList")
    public PageResult<BizRetryDTO> queryList(@RequestBody BizRetryListQuery bizRetryListQuery) {
        return bizRetryService.queryList(bizRetryListQuery);
    }

}
