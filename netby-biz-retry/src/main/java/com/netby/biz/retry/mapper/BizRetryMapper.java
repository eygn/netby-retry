package com.netby.biz.retry.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.netby.biz.retry.entity.BizRetryDO;

/**
 * 服务重试表(BizRetry)表数据库访问层
 *
 * @author: byg
 * @since 2023-06-17 17:09:46
 */
@Mapper
public interface BizRetryMapper extends BaseMapper<BizRetryDO> {

/**
* 批量新增数据（MyBatis原生foreach方法）
*
* @param entities List<BizRetry> 实例对象列表
* @return 影响行数
*/
int insertBatch(@Param("entities") List<BizRetryDO> entities);

/**
* 批量新增或按主键更新数据（MyBatis原生foreach方法）
*
* @param entities List<BizRetry> 实例对象列表
* @return 影响行数
* @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
*/
int insertOrUpdateBatch(@Param("entities") List<BizRetryDO> entities);

}

