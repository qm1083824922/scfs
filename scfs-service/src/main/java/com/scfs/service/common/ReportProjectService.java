package com.scfs.service.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.dao.common.ReportProjectDao;
import com.scfs.domain.common.dto.req.ReportProjectReqDto;
import com.scfs.domain.common.entity.ReportProject;

/**
 * Created by Administrator on 2017年6月9日.
 */
@Service
public class ReportProjectService {
	@Autowired
	private ReportProjectDao reportProjectDao;

	public void addReportProject(ReportProjectReqDto reportProjectReqDto) {
		if (reportProjectReqDto.getProjectId() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误，请检查");
		}

		Integer projectId = reportProjectReqDto.getProjectId();
		List<Integer> reportTypeList = reportProjectReqDto.getReportTypeList();
		reportProjectDao.deleteByProjectId(projectId);
		for (Integer reportType : reportTypeList) {
			ReportProject reportProject = new ReportProject();
			reportProject.setProjectId(projectId);
			reportProject.setReportType(reportType);
			reportProjectDao.insert(reportProject);
		}
	}

	public List<ReportProject> queryReportProject(ReportProjectReqDto reportProjectReqDto) {
		if (reportProjectReqDto.getProjectId() == null) {
			return null;
		}

		List<ReportProject> reportProjectList = reportProjectDao
				.queryEntityByProjectId(reportProjectReqDto.getProjectId());
		return reportProjectList;
	}

	/**
	 * 根据报表类型查询需过滤项目列表
	 * 
	 * @param reportType
	 *            报表类型
	 * @return
	 */
	public List<Integer> queryReportProject(Integer reportType) {
		List<Integer> projectIdList = reportProjectDao.queryEntityByReportType(reportType);
		return projectIdList;
	}

}
