package com.netby.biz.retry.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.netby.biz.retry.mapper.BizRetryMapper;
import com.netby.biz.retry.entity.BizRetryDO;
import com.netby.biz.retry.service.BizRetryService;
import org.springframework.stereotype.Service;

/**
 * 服务重试表(BizRetry)表服务实现类
 *
 * @author: byg
 * @since 2023-06-17 17:09:52
 */
@Service("bizRetryService")
public class BizRetryServiceImpl extends ServiceImpl<BizRetryMapper, BizRetryDO> implements BizRetryService {

}

