package com.scfs.domain.base.dto.resp;

import com.scfs.domain.base.model.UserPermission;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/11/18.
 */
public class UserPermissionResDto implements Serializable {

	private static final long serialVersionUID = 8785398536423924185L;

	private List<UserPermission> userPermissionList;

	public List<UserPermission> getUserPermissionList() {
		return userPermissionList;
	}

	public void setUserPermissionList(List<UserPermission> userPermissionList) {
		this.userPermissionList = userPermissionList;
	}
}
