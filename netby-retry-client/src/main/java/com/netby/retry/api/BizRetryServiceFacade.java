package com.netby.retry.api;

import com.netby.common.vo.PageResult;
import com.netby.common.vo.Response;
import com.netby.retry.dto.BizRetryAddCmd;
import com.netby.retry.dto.BizRetryListQuery;
import com.netby.retry.dto.BizRetryUpdateCmd;
import com.netby.retry.dto.data.BizRetryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * @author Elvan.bai
 * @date 2023/7/16 09:49
 */
@FeignClient(value = "netby-retry", contextId = "bizRetryServiceFacade")
public interface BizRetryServiceFacade {

    /**
     * 新增重试
     *
     * @param bizRetryAddCmd
     * @return
     */
    @PostMapping("/bizRetry/add")
    Response addBizRetry(BizRetryAddCmd bizRetryAddCmd);

    /**
     * 更新重试
     *
     * @param bizRetryUpdateCmd
     * @return
     */
    @PutMapping("/bizRetry/update")
    Response updateRetry(BizRetryUpdateCmd bizRetryUpdateCmd);

    /**
     * 根据业务类型查询
     *
     * @param bizRetryListQuery
     * @return
     */
    @PostMapping("/bizRetry/queryList")
    PageResult<BizRetryDTO> queryList(BizRetryListQuery bizRetryListQuery);

}
