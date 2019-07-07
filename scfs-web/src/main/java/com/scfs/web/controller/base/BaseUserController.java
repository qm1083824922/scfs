package com.scfs.web.controller.base;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.code.kaptcha.Producer;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BaseUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.base.dto.req.BaseUserReqDto;
import com.scfs.domain.base.dto.resp.BaseUserResDto;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.project.dto.req.ProjectSearchReqDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.base.user.BaseUserService;
import com.scfs.service.common.SysCaptchaService;
import com.scfs.web.controller.BaseController;

@Controller
public class BaseUserController extends BaseController {

	private final static Logger LOGGER = LoggerFactory.getLogger(BaseUserController.class);

	@Autowired
	private BaseUserService baseUserService;
	@Autowired
	private SysCaptchaService sysCaptchaService;
	@Autowired
    private Producer producer;
	@Value("${scfs.url}")
	private String redirectUrl;

	/**
	 * 查询用户列表
	 *
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.QUERYUSER, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BaseUserResDto> queryUser(BaseUserReqDto baseUserReqDto) {
		PageResult<BaseUserResDto> result = new PageResult<BaseUserResDto>();
		try {
			result = baseUserService.queryBaseUserList(baseUserReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("查询异常，请稍后重试");
			LOGGER.error("查询异常，请稍后重试,", e);
		}
		return result;
	}

	/**
	 * 根据用户ID查询用户列表
	 *
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.DETAILUSER, method = RequestMethod.POST)
	@ResponseBody
	public Result<BaseUserResDto> queryUserById(BaseUserReqDto baseUserReqDto) {
		Result<BaseUserResDto> result = new Result<BaseUserResDto>();
		try {
			return baseUserService.queryBaseUser(baseUserReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 根据用户ID查询用户列表
	 *
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.EDITUSER, method = RequestMethod.POST)
	@ResponseBody
	public Result<BaseUserResDto> editUser(BaseUserReqDto baseUserReqDto) {
		Result<BaseUserResDto> result = new Result<BaseUserResDto>();
		try {
			return baseUserService.queryBaseUser(baseUserReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("编辑用户异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 添加用户
	 *
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.ADDUSER, method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> addUser(BaseUser baseUser) {
		/** 用户状态 1-待提交 2-已完成 3-已锁定 **/
		Result<Integer> result = new Result<Integer>();
		try {
			baseUser.setStatus(BaseConsts.THREE);
			result = baseUserService.addBaseUser(baseUser);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 更新用户
	 *
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.UPDATEUSER, method = RequestMethod.POST)
	@ResponseBody
	public Object updateUser(BaseUser baseUser) {
		Result<BaseUser> result = new Result<BaseUser>();
		try {
			result = baseUserService.updateBaseUser(baseUser);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 提交操作
	 *
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.SUBMITUSER, method = RequestMethod.POST)
	@ResponseBody
	public Object submit(BaseUser baseUser) {

		Result<BaseUser> result = new Result<BaseUser>();
		try {
			result = baseUserService.submit(baseUser);
			return result;
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg("提交失败，请重试");
		}
		return result;
	}

	/**
	 * 锁定操作
	 *
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.LOCKUSER, method = RequestMethod.POST)
	@ResponseBody
	public Object lock(BaseUser baseUser) {
		BaseResult br = new BaseResult();
		try {
			baseUserService.lock(baseUser);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			br.setSuccess(false);
			br.setMsg("锁定失败，请重试");
		}
		return br;
	}

	/**
	 * 重置操作
	 *
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.RESETPSW, method = RequestMethod.POST)
	@ResponseBody
	public Object resetPsw(BaseUser baseUser) {
		BaseResult br = new BaseResult();
		try {
			baseUserService.resetPsw(baseUser);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			br.setSuccess(false);
			br.setMsg("锁定失败，请重试");
		}
		return br;
	}

	/**
	 * 解锁操作
	 *
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.UNLOCKUSER, method = RequestMethod.POST)
	@ResponseBody
	public Object unlock(BaseUser baseUser) {
		BaseResult br = new BaseResult();
		try {
			baseUserService.unlock(baseUser);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			br.setSuccess(false);
			br.setMsg("解锁失败，请重试");
		}
		return br;
	}

	/**
	 * 删除操作
	 *
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.DELETEUSER, method = RequestMethod.POST)
	@ResponseBody
	public Object delete(BaseUser baseUser) {
		BaseResult result = new BaseResult();
		try {
			baseUserService.delete(baseUser);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试");
			LOGGER.error("", e);
		}
		return result;
	}

	@RequestMapping(value = BaseUrlConsts.LOGIN_INNER_REDIRECT, method = RequestMethod.GET)
	public String redirectInnerLogin() {
		return baseUserService.redirectInnerLogin();
	}

	@RequestMapping(value = BaseUrlConsts.API_UPDATE_INNER, method = RequestMethod.POST)
	@ResponseBody
	public String updateInnnerUser(HttpServletRequest request) {
		return baseUserService.updateInnnerUser(request);
	}

	@RequestMapping(value = BaseUrlConsts.LOGIN_INNER, method = RequestMethod.GET)
	public String innnerLogin(HttpServletRequest request, HttpServletResponse response) {
		return baseUserService.innerLogin(request, response);
	}
	
	/**
	 * 验证码
	 */
	@RequestMapping(value = BaseUrlConsts.CAPTCHA, method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> captcha(HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");

		//生成文字验证码
        String code = producer.createText();
        //获取uuid
        Map<String, Object> map = sysCaptchaService.getUuid(code);
		//获取图片验证码
        BufferedImage image = producer.createImage(code);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(image, "jpg", outputStream);
		
		// 对字节数组Base64编码  
		map.put("img", com.xiaoleilu.hutool.lang.Base64.encode(outputStream.toByteArray()));
		IOUtils.closeQuietly(outputStream);
		return map;
	}


	@RequestMapping(value = BaseUrlConsts.LOGIN, method = RequestMethod.POST)
	@ResponseBody
	public Result<String> login(@RequestParam String username, @RequestParam String pwd, @RequestParam String uuid,
			@RequestParam String captcha, HttpServletRequest request, HttpServletResponse response) {
		Result<String> result = new Result<String>();
		try {
			/**
			if (StringUtils.isBlank(uuid) || sysCaptchaService.isExpired(uuid)) {
				result.setMsg("验证码已过期");
				return result;
			}
			boolean validateFlag = sysCaptchaService.validate(uuid, captcha);
			if (!validateFlag){
				result.setMsg("验证码不正确");
				return result;
			}
			 **/
			if (StringUtils.isEmpty(pwd) || StringUtils.isEmpty(username)) {
				result.setMsg("用户名或密码不能为空");
				return result;
			}
			result = baseUserService.login(username, pwd, request, response);
			//sysCaptchaService.clearCaptcha(uuid);
		} catch (Exception e) {
			result.setMsg("登陆系统异常，请稍后重试");
			LOGGER.error("登陆系统异常", e);
		}
		return result;
	}

	@RequestMapping(value = BaseUrlConsts.LOGOUT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult logout(HttpServletRequest request, HttpServletResponse response) {
		BaseResult result;
		try {
			result = baseUserService.logout(request, response);
		} catch (Exception e) {
			result = new BaseResult();
			result.setMsg("登出系统异常，请稍后重试");
			LOGGER.error("登出系统异常", e);
		}
		return result;
	}

	@RequestMapping(value = BaseUrlConsts.UPDATEPWD, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult modifyPwd(String oldPwd, String newPwd) {
		BaseResult result;
		try {
			result = baseUserService.updatePwd(oldPwd, newPwd);
		} catch (BaseException e) {
			result = new BaseResult();
			result.setMsg(e.getMsg());
			LOGGER.error("更新密码异常", e);
		} catch (Exception e) {
			result = new BaseResult();
			result.setMsg("更新密码异常系统异常，请稍后重试");
			LOGGER.error("更新密码异常异常", e);
		}
		return result;
	}

	/**
	 * 查询角色下用户列表
	 *
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.QUERYUSERROLEBYID, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BaseUserResDto> queryUserByProject(BaseUserReqDto baseUserReqDto) {
		PageResult<BaseUserResDto> result = new PageResult<BaseUserResDto>();
		try {
			result = baseUserService.queryBaseUserListByRoleId(baseUserReqDto);
		} catch (Exception e) {
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 查询项目下用户列表
	 *
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.QUERYPROJECTUSER, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BaseUserResDto> queryUserByProjectId(ProjectSearchReqDto projectSearchReqDto) {
		PageResult<BaseUserResDto> result = new PageResult<BaseUserResDto>();
		try {
			result = baseUserService.queryBaseUserListByProjectId(projectSearchReqDto);
		} catch (Exception e) {
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 查询项目下未分配用户列表
	 *
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.QUERYUNDIVIDEPROJECTUSER, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BaseUserResDto> queryUndivideUserByProjectId(BaseUserReqDto baseUserReqDto) {
		PageResult<BaseUserResDto> result = new PageResult<BaseUserResDto>();
		try {
			result = baseUserService.queryUndivideUserByProjectId(baseUserReqDto);
		} catch (Exception e) {
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

}
