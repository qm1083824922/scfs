package com.scfs.web.controller.base;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.base.dto.req.BaseUserReqDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.wechat.entity.WechatUser;
import com.scfs.service.base.user.UserWechatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2016/12/5.
 */
@Controller
public class UserWechatController {

	private final static Logger LOGGER = LoggerFactory.getLogger(UserWechatController.class);
	@Autowired
	private UserWechatService wechatService;

	@RequestMapping(value = BusUrlConsts.USER_WECHAT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<WechatUser> queryBindInfo(BaseUserReqDto baseUserReqDto) {
		PageResult<WechatUser> result = new PageResult<WechatUser>();
		try {
			result = wechatService.querybindWechat(baseUserReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询绑定微信信息异常[{}]", JSONObject.toJSON(baseUserReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询绑定微信信息异常[{}]", JSONObject.toJSON(baseUserReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.USER_WECHAT_UNBIND_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<WechatUser> queryUnBindInfo(BaseUserReqDto baseUserReqDto) {
		PageResult<WechatUser> result = new PageResult<WechatUser>();
		try {
			result = wechatService.queryUnbind(baseUserReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询未绑定微信信息异常[{}]", JSONObject.toJSON(baseUserReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询未绑定微信信息异常[{}]", JSONObject.toJSON(baseUserReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.USER_WECHAT_BIND, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult wechatBind(WechatUser wechatUser) {
		BaseResult result = new BaseResult();
		try {
			wechatService.bindWechat(wechatUser);
		} catch (BaseException e) {
			LOGGER.error(" 绑定微信信息异常[{}]", JSONObject.toJSON(wechatUser.getUserId()), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("绑定微信信息异常[{}]", JSONObject.toJSON(wechatUser.getUserId()), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.USER_WECHAT_UNBIND, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult wechatUnBind(WechatUser wechatUser) {
		BaseResult result = new BaseResult();
		try {
			wechatService.unbindWechat(wechatUser);
		} catch (BaseException e) {
			LOGGER.error("解绑微信信息异常[{}]", JSONObject.toJSON(wechatUser.getUserId()), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("解绑微信信息异常[{}]", JSONObject.toJSON(wechatUser.getUserId()), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}
}
