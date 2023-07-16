package com.netby.retry;

import com.alibaba.cola.catchlog.CatchAndLog;
import com.netby.common.vo.PageResult;
import com.netby.common.vo.Response;
import com.netby.retry.api.BizRetryServiceI;
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
public class BizRetryServiceImpl implements BizRetryServiceI {

    @Resource
    private BizRetryAddCmdExe bizRetryAddCmdExe;

    @Resource
    private BizRetryUpdateCmdExe bizRetryUpdateCmdExe;

    @Resource
    private BizRetryListQueryExe bizRetryListQueryExe;

    public Response addBizRetry(BizRetryAddCmd bizRetryAddCmd) {
        return bizRetryAddCmdExe.execute(bizRetryAddCmd);
    }

    @Override
    public Response updateRetry(BizRetryUpdateCmd bizRetryUpdateCmd) {
        return bizRetryUpdateCmdExe.execute(bizRetryUpdateCmd);
    }

    @Override
    public PageResult<BizRetryDTO> listByBizType(BizRetryListQuery bizRetryListQuery) {
        return bizRetryListQueryExe.execute(bizRetryListQuery);
    }


}