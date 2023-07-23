package com.netby.retry;

import com.netby.common.state.CommonStateCode;
import com.netby.common.vo.PageResult;
import com.netby.common.vo.Response;
import com.netby.core.lock.NetbyLock;
import com.netby.core.mock.RpcInvokeWrapper;
import com.netby.retry.dto.BizRetryAddCmd;
import com.netby.retry.dto.BizRetryListQuery;
import com.netby.retry.dto.BizRetryUpdateCmd;
import com.netby.retry.dto.data.BizRetryDTO;
import com.netby.retry.executor.BizRetryAddCmdExe;
import com.netby.retry.executor.BizRetryListQueryExe;
import com.netby.retry.executor.BizRetryUpdateCmdExe;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @author Elvan.bai
 * @date 2023/7/16 09:49
 */
@Service
public class BizRetryService {

    @Resource
    private BizRetryAddCmdExe bizRetryAddCmdExe;

    @Resource
    private BizRetryUpdateCmdExe bizRetryUpdateCmdExe;

    @Resource
    private BizRetryListQueryExe bizRetryListQueryExe;

    @NetbyLock("#bizRetryAddCmd.bizRetryDTO.bizNo+'-'+#bizRetryAddCmd.bizRetryDTO.retryType")
    public Response addBizRetry(BizRetryAddCmd bizRetryAddCmd) {

        return new RpcInvokeWrapper<BizRetryDTO>(){
            @Override
            public String getKey() {
                return "/bizRetry/add";
            }

            @Override
            public Response<BizRetryDTO> doInvoke() {
                return Response.failed(CommonStateCode.FAILED);
            }
        }.supportDevMode(BizRetryDTO.class).invoke();

//        return bizRetryAddCmdExe.execute(bizRetryAddCmd);
    }

    @NetbyLock("#bizRetryAddCmd.bizRetryDTO.id")
    public Response updateRetry(BizRetryUpdateCmd bizRetryUpdateCmd) {
        return bizRetryUpdateCmdExe.execute(bizRetryUpdateCmd);
    }

    public PageResult<BizRetryDTO> queryList(BizRetryListQuery bizRetryListQuery) {
        return bizRetryListQueryExe.execute(bizRetryListQuery);
    }


}