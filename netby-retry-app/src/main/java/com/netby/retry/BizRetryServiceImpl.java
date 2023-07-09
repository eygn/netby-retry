package com.netby.retry;

import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.catchlog.CatchAndLog;
import com.netby.retry.api.BizRetryServiceI;
import com.netby.retry.dto.BizRetryAddCmd;
import com.netby.retry.dto.BizRetryListQuery;
import com.netby.retry.dto.BizRetryUpdateCmd;
import com.netby.retry.dto.data.BizRrtryDTO;
import com.netby.retry.executor.executor.BizRetryUpdateCmdExe;
import lombok.Getter;
import org.springframework.stereotype.Service;

import com.netby.retry.executor.executor.BizRetryAddCmdExe;
import com.netby.retry.executor.executor.query.BizRetryListQueryExe;

import javax.annotation.Resource;


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
    public void updateRetry(BizRetryUpdateCmd bizRetryUpdateCmd) {
        bizRetryUpdateCmdExe.execute(bizRetryUpdateCmd);
    }


}