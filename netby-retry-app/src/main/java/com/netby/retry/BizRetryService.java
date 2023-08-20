package com.netby.retry;

import com.netby.common.vo.PageResult;
import com.netby.common.vo.Response;
import com.netby.core.lock.NetbyLock;
import com.netby.retry.domain.retry.annotation.SyncRetry;
import com.netby.retry.dto.BizRetryAddCmd;
import com.netby.retry.dto.BizRetryListQuery;
import com.netby.retry.dto.BizRetryUpdateCmd;
import com.netby.retry.dto.data.BizRetryDTO;
import com.netby.retry.executor.BizRetryAddCmdExe;
import com.netby.retry.executor.BizRetryListQueryExe;
import com.netby.retry.executor.BizRetryUpdateCmdExe;
import com.netby.retry.integration.ThirdLogFacadeIntegration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @author Elvan.bai
 * @date 2023/7/16 09:49
 */
@Slf4j
@Service
public class BizRetryService {

    @Resource
    private BizRetryAddCmdExe bizRetryAddCmdExe;

    @Resource
    private BizRetryUpdateCmdExe bizRetryUpdateCmdExe;

    @Resource
    private BizRetryListQueryExe bizRetryListQueryExe;

    @Resource
    private ThirdLogFacadeIntegration thirdLogFacadeIntegration;

    @NetbyLock("#bizRetryAddCmd.bizRetryDTO.bizNo+'-'+#bizRetryAddCmd.bizRetryDTO.retryType")
    public Response addBizRetry(BizRetryAddCmd bizRetryAddCmd) {
//        return thirdLogFacadeIntegration.addBizRetry(bizRetryAddCmd);
        return bizRetryAddCmdExe.execute(bizRetryAddCmd);
    }

    @NetbyLock("#bizRetryAddCmd.bizRetryDTO.id")
    public Response updateRetry(BizRetryUpdateCmd bizRetryUpdateCmd) {
        return bizRetryUpdateCmdExe.execute(bizRetryUpdateCmd);
    }

    @SyncRetry(label = "queryList")
    public PageResult<BizRetryDTO> queryList(BizRetryListQuery bizRetryListQuery) {
        if ("syncRetry".equals(bizRetryListQuery.getBizType())) {
            log.error("{}", 1 / 0);
        }
        return bizRetryListQueryExe.execute(bizRetryListQuery);
    }


}