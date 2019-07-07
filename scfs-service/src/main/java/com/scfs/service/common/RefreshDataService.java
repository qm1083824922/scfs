package com.scfs.service.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.scfs.common.consts.BaseConsts;
import com.scfs.dao.base.entity.BaseProjectDao;
import com.scfs.dao.base.entity.BaseSubjectDao;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.entity.BaseSubject;

/**
 * @Description: TODO (这里用一句话描述这个方法的作用)
 * @author Administrator
 * @date:2017年10月25日下午6:10:13
 * 
 */
@Service
public class RefreshDataService {
	@Autowired
	private BaseProjectDao baseProjectDao;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private BaseSubjectDao baseSubjectDao;

	public void refreshProjectNo() {
		List<BaseProject> list = baseProjectDao.queryAllProject(null);
		if (!CollectionUtils.isEmpty(list)) {
			for (BaseProject baseProject : list) {
				String industrial = String.valueOf(baseProject.getIndustrial());
				String projectNoType = baseProject.getProjectNoType();
				BaseSubject baseSubject = baseSubjectDao.queryEntityById(baseProject.getBusinessUnitId());
				int subjectNoLength = baseSubject.getSubjectNo().length();

				String busiUnit = baseSubject.getSubjectNo().substring(subjectNoLength - 2, subjectNoLength);
				String projectNo = sequenceService.getProjectNo(BaseConsts.THREE, industrial, projectNoType, busiUnit);
				BaseProject updateBaseProject = new BaseProject();
				updateBaseProject.setId(baseProject.getId());
				updateBaseProject.setProjectNo(projectNo);
				baseProjectDao.updateById(updateBaseProject);
			}
		}
	}
}
