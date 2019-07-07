package com.scfs.service.api.pms;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.consts.BaseConsts;
import com.scfs.dao.api.pms.PmsStoreOutDao;
import com.scfs.domain.api.pms.dto.res.PmsHttpBillStoreOutResDto;
import com.scfs.domain.api.pms.entity.PmsSeries;
import com.scfs.domain.pay.entity.PmsStoreOut;

/**
 * <pre>
 * 	PMS出库明细相关业务处理
 *  File: PmsStoreOutService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年05月02日			Administrator
 *
 * </pre>
 */
@Service
public class PmsStoreOutService {
	@Autowired
	PmsStoreOutDao pmsStoreOutDao;

	/**
	 * 新增当前PMS同步出库单明细数据
	 * 
	 * @param storeOut
	 */
	public List<PmsHttpBillStoreOutResDto> createStoreOutByCon(List<PmsStoreOut> storeOut, PmsSeries pmsSeries,
			List<PmsHttpBillStoreOutResDto> pmsHttpResDto) {
		for (PmsStoreOut pmsStoreOut : storeOut) {
			try {
				pmsStoreOut.setCreate_at(new Date());// 当前时间
				pmsStoreOut.setPmsSeriesId(pmsSeries.getId());// PMS铺货业务流水表id
																// 待修改
				pmsStoreOutDao.createPmsStoreOut(pmsStoreOut);
			} catch (Exception e) {
				for (PmsHttpBillStoreOutResDto pmsHttpBillStoreOutResDto : pmsHttpResDto) {
					if (pmsStoreOut.getSku_id().equals(pmsHttpBillStoreOutResDto.getSku_id())) {
						pmsHttpBillStoreOutResDto.setFlag(BaseConsts.FLAG_NO);
						pmsHttpBillStoreOutResDto.setMsg(pmsHttpBillStoreOutResDto.getMsg() + "插入失败");
					}

				}
			}
		}
		return pmsHttpResDto;
	}

}
