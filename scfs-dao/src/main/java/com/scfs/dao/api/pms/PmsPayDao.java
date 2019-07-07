package com.scfs.dao.api.pms;

import java.util.List;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;
import com.scfs.domain.api.pms.entity.PmsPay;
import com.scfs.domain.api.pms.model.PmsPayModel;
import com.scfs.domain.interf.dto.PmsDistributionSearchReqDto;

@Repository
public interface PmsPayDao {
	/**
	 * 删除数据
	 * 
	 * @param id
	 * @return
	 */
	int deleteById(Integer id);

	/**
	 * 添加数据
	 * 
	 * @param pmsPay
	 * @return
	 */
	int insert(PmsPay pmsPay);

	/**
	 * 获取详情
	 * 
	 * @param id
	 * @return
	 */
	PmsPay queryEntityById(Integer id);

	/**
	 * 通过流水号获取
	 * 
	 * @param pmsPay
	 * @return
	 */
	PmsPay queryEntityByParam(PmsPay pmsPay);

	/**
	 * 修改数据
	 * 
	 * @param pmsPay
	 * @return
	 */
	int updateById(PmsPay pmsPay);

	/**
	 * 获取流水号数据
	 * 
	 * @param pmsPay
	 * @return
	 */
	List<PmsPay> queryPmsPayGroupDao(PmsPay pmsPay);

	List<PmsPay> queryPmsPayWaitGroupDao(PmsPay pmsPay);

	/**
	 * 获取所有未处理待付款信息
	 * 
	 * @return
	 */
	List<PmsPay> queryPmsPayWaitDao();

	/**
	 * 获取所有未处理驳回信息
	 * 
	 * @return
	 */
	List<PmsPay> queryPmsPayRebutDao();

	/**
	 * 根据PMS铺货ID查询PMS请款单数据
	 * 
	 * @param reqDto
	 * @param rowBounds
	 * @return
	 */
	List<PmsPayModel> queryPayBySeries(PmsDistributionSearchReqDto reqDto, RowBounds rowBounds);

	/**
	 * 通过参数获取数据
	 * 
	 * @param pmsPay
	 * @return
	 */
	List<PmsPay> queryPmsPayByParam(PmsPay pmsPay);

	/**
	 * 根据PMS流水号查询当前请款单失败的数据
	 * 
	 * @param reqDto
	 * @return
	 */
	List<PmsPay> queryPayFailure(PmsDistributionSearchReqDto reqDto);

	/**
	 * 根据PMS流水号查询当前请款单驳回失败的数据
	 * 
	 * @param reqDto
	 * @return
	 */
	List<PmsPay> queryPayFailureByStatu(PmsDistributionSearchReqDto reqDto);

	/**
	 * 根据PMS流水号查询当前请款单待请款失败的数据
	 * 
	 * @param reqDto
	 * @return
	 */
	List<PmsPay> queryPayFailureByStatuBy(PmsDistributionSearchReqDto reqDto);
}