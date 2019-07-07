package com.scfs.service.common;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.dao.common.SequenceDao;
import com.scfs.dao.project.ProjectNoSeqDao;
import com.scfs.domain.common.entity.SeqManage;
import com.scfs.domain.project.entity.ProjectNoSeq;

/**
 * <pre>
 *
 *  File: SequenceServiceImpl.java
 *  Description:
 *  TODO
 *  Date,					Who,
 *  2016年9月27日				Administrator
 *
 * </pre>
 */
@Service
public class SequenceService {

	private static final String DEFAULT_PADDING = "0";

	@Autowired
	private SequenceDao sequenceDao;
	@Autowired
	private ProjectNoSeqDao projectNoSeqDao;

	/**
	 * 编号一直增长，根据指定的序列名获取编号长度
	 * 
	 * @param seqPre
	 *            编号前缀，比如采购单PO,序列默认左填充为'0'
	 * @param seqName
	 *            序列名称 PURCHASE_ORDER_NO
	 * @param length
	 *            编号总长度 13
	 * @return PO161029 00003
	 */
	public synchronized String getNumIncByBusName(String seqPre, String seqName, int length) {
		SeqManage seqManage = sequenceDao.getSeqByName(seqName);
		int seqVal = seqManage.getSeqVal(); // 当前序列
		seqManage.setSeqVal(seqVal + 1);
		sequenceDao.updateSeqValByName(seqManage); // 下一个序列
		int seqLen = length - seqPre.length();
		String seq = StringUtils.leftPad(seqVal + "", seqLen, DEFAULT_PADDING);
		return seqPre + seq;
	}

	/**
	 * 编号每天从1开始，根据指定的序列名获取编号长度
	 *
	 * @param seqPre
	 *            编号前缀，比如采购单PO,序列默认左填充为'0'
	 * @param seqName
	 *            序列名称 PURCHASE_ORDER_NO
	 * @param length
	 *            编号总长度 13
	 * @return PO161029 00003
	 */
	public synchronized String getNumDateByBusName(String seqPre, String seqName, int length) {
		SeqManage seqManage = sequenceDao.getSeqByName(seqName);
		int seqVal = seqManage.getSeqVal(); // 当前序列
		seqManage.setSeqVal(seqVal + 1);
		Date d = new Date();
		String dateStr = DateFormatUtils.format(DateFormatUtils.YYMMDD, d);
		String oldDateStr = DateFormatUtils.format(DateFormatUtils.YYMMDD, seqManage.getUpdateAt());
		// 每天都从1开始
		if (!dateStr.equalsIgnoreCase(oldDateStr)) {
			seqVal = 1;
			seqManage.setUpdateAt(new Date());
			seqManage.setSeqVal(2);// 下一个序列开始
		}
		sequenceDao.updateSeqValByName(seqManage); // 下一个序列
		int seqLen = length - seqPre.length() - dateStr.length();
		String seq = StringUtils.leftPad(seqVal + "", seqLen, DEFAULT_PADDING);
		return seqPre + dateStr + seq;
	}

	/**
	 * 获取项目编号
	 * 
	 * @param length
	 * @param industrial
	 * @param projectType
	 * @param busiUnit
	 * @return
	 */
	public synchronized String getProjectNo(int length, String industrial, String projectNoType, String busiUnit) {
		ProjectNoSeq projectNoSeq = projectNoSeqDao.getSeqByType(projectNoType);
		if (null == projectNoSeq) {
			projectNoSeq = new ProjectNoSeq();
			projectNoSeq.setProjectNoType(projectNoType);
			projectNoSeq.setSeqVal(1);
			projectNoSeqDao.insert(projectNoSeq);
			projectNoSeq = projectNoSeqDao.getSeqByType(projectNoType);
		}
		int seqVal = projectNoSeq.getSeqVal(); // 当前序列
		projectNoSeq.setSeqVal(seqVal + 1);
		projectNoSeqDao.updateSeqValByType(projectNoSeq); // 下一个序列
		String seqValStr = String.valueOf(seqVal);
		int bit = seqValStr.length();
		int seqLen = length - bit;
		if (seqLen < 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "项目流水号超过最大范围[001~999]");
		}
		String seq = StringUtils.leftPad(seqVal + "", length, DEFAULT_PADDING);
		return industrial + seq + projectNoType + "." + busiUnit;
	}

}
