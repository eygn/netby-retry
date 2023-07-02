package com.netby.biz.retry.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.netby.biz.retry.annotation.LogPrinter;
import com.netby.biz.retry.entity.BizRetryDO;
import com.netby.biz.retry.service.BizRetryService;
import com.netby.common.vo.Response;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * 服务重试表(BizRetry)表控制层
 *
 * @author: byg
 * @since 2023-06-17 17:09:45
 */
@LogPrinter
@RestController
@RequestMapping("bizRetry")
public class BizRetryController {
    /**
     * 服务对象
     */
    @Resource
    private BizRetryService bizRetryService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param bizRetryDO 查询实体
     * @return 所有数据
     */
    @GetMapping
    public Response selectAll(Page<BizRetryDO> page, BizRetryDO bizRetryDO) {
        return Response.success(this.bizRetryService.page(page, new QueryWrapper<>(bizRetryDO)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public Response selectOne(@PathVariable Serializable id) {
        return Response.success(this.bizRetryService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param bizRetryDO 实体对象
     * @return 新增结果
     */
    @PostMapping
    public Response insert(@RequestBody BizRetryDO bizRetryDO) {
        return Response.success(this.bizRetryService.save(bizRetryDO));
    }

    /**
     * 修改数据
     *
     * @param bizRetryDO 实体对象
     * @return 修改结果
     */
    @PutMapping
    public Response update(@RequestBody BizRetryDO bizRetryDO) {
        return Response.success(this.bizRetryService.updateById(bizRetryDO));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public Response delete(@RequestParam("idList") List<Long> idList) {
        return Response.success(this.bizRetryService.removeByIds(idList));
    }
}

