package com.netby.retry.biz.job;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.netby.retry.biz.constants.ContextConstants;
import com.netby.retry.biz.context.BusinessContext;
import com.netby.retry.biz.entity.BizRetryDO;
import com.netby.retry.biz.enums.RetryBizTypeEnum;
import com.netby.retry.biz.service.BizRetryService;
import com.netby.common.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;

/**
 * 风控决策重试任务，每五分钟执行一次
 *
 * @author: byg
 * @date 2023/2/15 19:34
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BizRetryTask {

    private static final int RETRY_COUNT_MAX = 3;

    private final BizRetryService bizRetryService;

//    @XxlJob("bizRetryTask")
    public void execute(String params) throws Exception {
        log.info("[riskDecisionRetryTask - execute] 开始执行风控决策重试任务! params:{}", params);

        process(params);
    }

    private void process(String params) {
        Integer pageNum = 0;
        Integer pageSize = 50;
        Page page = PageDTO.of(pageNum, pageSize);

        QueryWrapper<BizRetryDO> queryWrapper = new QueryWrapper<>();
        String bizNo = null;
        boolean forceRetry = false;
        if (StringUtils.isNotBlank(params)) {
            String[] arr = params.split(",");
            bizNo = arr[0];
            if (arr.length > 1) {
                forceRetry = "true".equals(arr[1]);
            }
        }
        if (StringUtil.isNotBlank(bizNo)) {
            queryWrapper.eq("biz_no", bizNo);
        } else {
            queryWrapper.eq("retry_status", 0);
        }
        queryWrapper.orderByAsc("created");

        long count = bizRetryService.getBaseMapper().selectCount(queryWrapper);
        long totalPage = count / pageSize;
        if (count % pageSize != 0) {
            totalPage++;
        }
        log.info("开始执行决策重试,任务数:{},forceRetry:{}", count, forceRetry);
        for (int i = 0; i < totalPage; i++) {
            page.setCurrent(i);
            IPage<BizRetryDO> resultPage = bizRetryService.getBaseMapper().selectPage(page, queryWrapper);
            if (CollectionUtils.isNotEmpty(resultPage.getRecords())) {
                List<BizRetryDO> records = resultPage.getRecords();
                for (BizRetryDO record : records) {
                    if (record.getRetryCount() >= RETRY_COUNT_MAX && record.getRetryStatus() == 0) {
                        record.setRetryStatus(2);
                        bizRetryService.getBaseMapper().updateById(record);
                    }
                    if (record.getRetryStatus() == 1) {
                        if (!forceRetry) {
                            continue;
                        }
                    }
                    doRetry(record);
                }
            }
        }
        log.info("决策重试完成");

        // 删除3个月以前的重试成功数据
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 180);
            QueryWrapper<BizRetryDO> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.eq("retry_status", 1).le("modified", calendar.getTime());
            long removeCount = bizRetryService.getBaseMapper().selectCount(deleteWrapper);
            bizRetryService.getBaseMapper().delete(deleteWrapper);
            log.info("删除决策重试数据完成,共删除数据:{}", removeCount);
        } catch (Exception e) {
            log.error("删除决策重试数据异常", e);
        }
    }

    private void doRetry(BizRetryDO riskDecisionRetryDO) {
        BusinessContext.putSystem(ContextConstants.ROUTE_ID, "RETRY_" + riskDecisionRetryDO.getBizNo());
        log.info("doRetry,bizNo:{},bizType:{}", riskDecisionRetryDO.getBizNo(), riskDecisionRetryDO.getBizType());
        try {
            String bizParams = riskDecisionRetryDO.getBizParams();
            if (RetryBizTypeEnum.DECISION.getCode().equals(riskDecisionRetryDO.getBizType())) {
                // TODO
            } else if (RetryBizTypeEnum.NOTIFY.getCode().equals(riskDecisionRetryDO.getBizType())) {
                // TODO
            }
        } catch (Exception e) {
            log.error("重试失败,params:{}", JSON.toJSONString(riskDecisionRetryDO), e);
        }
    }
}
