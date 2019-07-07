package com.scfs.domain;

/**
 * Created by Administrator on 2016/9/23.
 * 所有result继承BaseResult
 */
public class BaseResult {

    /**是否成功*/
    private boolean isSuccess = true;
    /**出错信息*/
    private String msg;
    //是否登陆
    private boolean isLogin;
    /**重定向页面*/
    private String redirectURL;

    //是否有权限
    private boolean isPermission = true;
    /**成功信息*/
    private String successMsg;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
        isSuccess = false;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public boolean isPermission() {
        return isPermission;
    }

    public void setPermission(boolean permission) {
        isPermission = permission;
    }

	public String getSuccessMsg() {
		return successMsg;
	}

	public void setSuccessMsg(String successMsg) {
		this.successMsg = successMsg;
	}

    public String getRedirectURL() {
        return redirectURL;
    }

    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }
}
