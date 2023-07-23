package com.netby.retry;

import com.alibaba.cola.catchlog.CatchAndLog;
import com.netby.common.vo.PageResult;
import com.netby.common.vo.Response;
import com.netby.core.lock.NetbyLock;
import com.netby.retry.api.BizRetryServiceFacade;
import com.netby.retry.dto.BizRetryAddCmd;
import com.netby.retry.dto.BizRetryListQuery;
import com.netby.retry.dto.BizRetryUpdateCmd;
import com.netby.retry.dto.data.BizRetryDTO;
import com.netby.retry.executor.BizRetryUpdateCmdExe;
import org.springframework.stereotype.Service;

import com.netby.retry.executor.BizRetryAddCmdExe;
import com.netby.retry.executor.BizRetryListQueryExe;

import javax.annotation.Resource;


/**
 * @author Elvan.bai
 * @date 2023/7/16 09:49
 */
@Service
@CatchAndLog
public class BizRetryServiceImpl implements BizRetryServiceFacade {

    @Resource
    private BizRetryAddCmdExe bizRetryAddCmdExe;

    @Resource
    private BizRetryUpdateCmdExe bizRetryUpdateCmdExe;

    @Resource
    private BizRetryListQueryExe bizRetryListQueryExe;

    @Override
    @NetbyLock("#bizRetryAddCmd.bizRetryDTO.bizNo+'-'+#bizRetryAddCmd.bizRetryDTO.retryType")
    public Response addBizRetry(BizRetryAddCmd bizRetryAddCmd) {
        return bizRetryAddCmdExe.execute(bizRetryAddCmd);
    }

    @Override
    @NetbyLock("#bizRetryAddCmd.bizRetryDTO.id")
    public Response updateRetry(BizRetryUpdateCmd bizRetryUpdateCmd) {
        return bizRetryUpdateCmdExe.execute(bizRetryUpdateCmd);
    }

    @Override
    public PageResult<BizRetryDTO> queryList(BizRetryListQuery bizRetryListQuery) {
        return bizRetryListQueryExe.execute(bizRetryListQuery);
    }


}