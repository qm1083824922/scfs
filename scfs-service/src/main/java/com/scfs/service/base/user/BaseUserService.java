package com.scfs.service.base.user;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.PageUtil;
import com.scfs.common.utils.PwdUtils;
import com.scfs.common.utils.http.HttpInvoker;
import com.scfs.dao.base.entity.BaseUserDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.BaseResult;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.dto.req.BasePermissionGroupReqDto;
import com.scfs.domain.base.dto.req.BaseUserReqDto;
import com.scfs.domain.base.dto.resp.BaseUserResDto;
import com.scfs.domain.base.entity.BaseDepartment;
import com.scfs.domain.base.entity.BaseRole;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.project.dto.req.ProjectSearchReqDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

@Service
public class BaseUserService {
	private final static Logger LOGGER = LoggerFactory.getLogger(BaseUserService.class);

	@Autowired
	private BaseUserDao baseUserDao;

	@Value("${initUserPassword}")
	private String initUserPassword;
	@Value("${inner.login.url}")
	private String innerLoginUrl;
	@Value("${inner.check.url}")
	private String innerCheckUrl;
	@Value("${scfs.url}")
	private String scfsUrl;
	@Value("${scfs.inner.url}")
	private String scfsInnerLoginUrl;
	@Value("${scfs.login.msg.url}")
	private String scfsLoginMsgUrl;

	@Autowired
	private CacheService cacheService;

	public PageResult<BaseUserResDto> queryBaseUserList(BaseUserReqDto baseUserReqDto) {
		PageResult<BaseUserResDto> result = new PageResult<BaseUserResDto>();
		int offSet = PageUtil.getOffSet(baseUserReqDto.getPage(), baseUserReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, baseUserReqDto.getPer_page());
		//DTO转domain，
		BaseUser baseUser = convertToBaseUser(baseUserReqDto);
		//查询用户列表
		List<BaseUser> baseUserList = baseUserDao.queryBaseUserList(baseUser, rowBounds);
		List<BaseUserResDto> baseUserResDtoList = convertToResult(baseUserList);
		//用户集合
		result.setItems(baseUserResDtoList);
		//总页数
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), baseUserReqDto.getPer_page());
		//设置总页数
		result.setLast_page(totalPage);
		//设置总条数
		result.setTotal(CountHelper.getTotalRow());
		//设置当前页
		result.setCurrent_page(baseUserReqDto.getPage());
		//设置每页显示项目数
		result.setPer_page(baseUserReqDto.getPer_page());
		return result;
	}

	public PageResult<BaseUserResDto> queryUserByPermissionGroup(BasePermissionGroupReqDto basePermissionGroupReqDto) {
		PageResult<BaseUserResDto> result = new PageResult<BaseUserResDto>();
		int offSet = PageUtil.getOffSet(basePermissionGroupReqDto.getPage(), basePermissionGroupReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, basePermissionGroupReqDto.getPer_page());
		List<BaseUser> baseUserList = baseUserDao.queryUserByPermissionGroup(basePermissionGroupReqDto, rowBounds);
		List<BaseUserResDto> baseUserResDtoList = convertToResult(baseUserList);
		result.setItems(baseUserResDtoList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), basePermissionGroupReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(basePermissionGroupReqDto.getPage());
		result.setPer_page(basePermissionGroupReqDto.getPer_page());
		return result;
	}

	public Result<BaseUserResDto> queryBaseUser(BaseUserReqDto baseUserReqDto) {
		Result<BaseUserResDto> result = new Result<BaseUserResDto>();
		BaseUser user = convertToBaseUser(baseUserReqDto);
		BaseUser baseUser = baseUserDao.queryBaseUserByUser(user);
		BaseUserResDto baseUserResDto = convertToResDto(baseUser);
		result.setItems(baseUserResDto);
		return result;
	}

	public Result<Integer> addBaseUser(BaseUser baseUser) {
		Result<Integer> result = new Result<Integer>();
		// 校验用户名是否重复
		BaseUser user = new BaseUser();
		user.setUserName(baseUser.getUserName());
		BaseUser dbUser = baseUserDao.queryBaseUserByUser(user);
		if (dbUser != null) {
			result.setSuccess(false);
			result.setMsg("用户名已存在");
		} else {
			// 校验工号是否重复
			user = new BaseUser();
			user.setEmployeeNumber(baseUser.getEmployeeNumber());
			dbUser = baseUserDao.queryBaseUserByUser(user);
			if (dbUser != null) {
				result.setSuccess(false);
				result.setMsg("工号已存在");
			} else {
				baseUser.setPassword(PwdUtils.encryptPassword(initUserPassword));
				baseUser.setCreatorId(ServiceSupport.getUser().getId());
				baseUser.setCreator(ServiceSupport.getUser().getChineseName());
				baseUser.setUserProperty(BaseConsts.ONE);
				baseUserDao.insert(baseUser);
				result.setItems(baseUser.getId());
			}
		}
		return result;
	}

	public Result<BaseUser> updateBaseUser(BaseUser baseUser) {
		Result<BaseUser> result = new Result<BaseUser>();
		// 校验用户名是否重复
		BaseUser user = new BaseUser();
		user.setUserName(baseUser.getUserName());
		BaseUser dbUser = baseUserDao.queryBaseUserByUser(user);
		if (dbUser != null && !baseUser.getId().equals(dbUser.getId())) {
			result.setMsg("用户名已存在");
			result.setSuccess(false);
			return result;
		} else {
			// 校验工号是否重复
			user = new BaseUser();
			user.setEmployeeNumber(baseUser.getEmployeeNumber());
			dbUser = baseUserDao.queryBaseUserByUser(user);
			if (dbUser != null && !dbUser.getId().equals(baseUser.getId())) {
				result.setMsg("工号已存在");
				result.setSuccess(false);
				return result;
			}
		}
		baseUserDao.update(baseUser);
		return result;
	}

	private List<BaseUserResDto> convertToResult(List<BaseUser> baseUserList) {
		List<BaseUserResDto> baseUserResDtoList = new ArrayList<BaseUserResDto>();
		if (CollectionUtils.isEmpty(baseUserList)) {
			return baseUserResDtoList;
		}
		for (BaseUser baseUser : baseUserList) {
			BaseUserResDto baseUserResDto = convertToResDto(baseUser);
			baseUserResDto.setOpertaList(getOperList(baseUser.getStatus(), baseUser));
			baseUserResDtoList.add(baseUserResDto);
		}
		return baseUserResDtoList;
	}

	private BaseUserResDto convertToResDto(BaseUser baseUser) {
		BaseUserResDto baseUserResDto = new BaseUserResDto();
		baseUserResDto.setId(baseUser.getId());
		baseUserResDto.setEmployeeNumber(baseUser.getEmployeeNumber());
		baseUserResDto.setCreateAt(baseUser.getCreateAt());
		baseUserResDto.setCreator(baseUser.getCreator());
		baseUserResDto.setUserName(baseUser.getUserName());
		baseUserResDto.setPassword(baseUser.getPassword());
		baseUserResDto.setEnglishName(baseUser.getEnglishName());
		baseUserResDto.setChineseName(baseUser.getChineseName());
		baseUserResDto.setMobilePhone(baseUser.getMobilePhone());
		baseUserResDto.setRtxCode(baseUser.getRtxCode());
		baseUserResDto.setEmail(baseUser.getEmail());
		baseUserResDto.setDepartmentId(baseUser.getDepartmentId());
		baseUserResDto.setType(baseUser.getType());
		baseUserResDto.setUserProperty(baseUser.getUserProperty());
		baseUserResDto.setRoleName(getUserRoles(baseUser.getId()));
		if (baseUser.getUserProperty() != null) {
			baseUserResDto.setUserPropertyValue(
					ServiceSupport.getValueByBizCode(BizCodeConsts.USER_PROPERTY, baseUser.getUserProperty() + ""));
		}
		if (baseUser.getDepartmentId() != null) {
			BaseDepartment baseDepartment = cacheService.getBaseDepartmentById(baseUser.getDepartmentId());
			if (baseDepartment != null)
				baseUserResDto.setDepartmentName(baseDepartment.getNameNo());
		}
		if (baseUser.getStatus() != null) {
			baseUserResDto
					.setStatus(ServiceSupport.getValueByBizCode(BizCodeConsts.USERSTS, baseUser.getStatus() + ""));
		}
		if (baseUser.getOperater() != null) {
			baseUserResDto.setOperaterStr(
					ServiceSupport.getValueByBizCode(BizCodeConsts.WAREHOUSE_OPERATE, baseUser.getOperater() + ""));
		}
		return baseUserResDto;
	}

	// 获取用户角色字符串
	private String getUserRoles(Integer userId) {
		List<BaseRole> roleList = cacheService.getRolesByUserId(userId);
		StringBuilder roleNames = new StringBuilder();
		for (int i = 0; i < roleList.size(); i++) {
			roleNames.append(roleList.get(i).getName());
			if (i < roleList.size() - 1) {
				roleNames.append(",");
			}
		}
		return roleNames.toString();
	}

	private List<CodeValue> getOperList(Integer state, BaseUser baseUser) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState(state, baseUser);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				BaseUserResDto.Operate.operMap);
		return oprResult;
	}

	private List<String> getOperListByState(Integer state, BaseUser baseUser) {
		if (state == null) {
			return null;
		}
		List<String> opertaList = Lists.newArrayList(5);
		opertaList.add(OperateConsts.DETAIL);
		switch (state) {
		// 状态,1表示待提交，2表示已完成，3表示已锁定
		case BaseConsts.ZERO:
			if (baseUser.getId() != BaseConsts.SYSTEM_USER_ID) {
				opertaList.add(OperateConsts.EDIT);
				opertaList.add(OperateConsts.LOCK);
				opertaList.add(OperateConsts.RESET);
			} else {
				opertaList.add(OperateConsts.EDIT);
			}
			break;
		case BaseConsts.ONE:
			if (baseUser.getId() != BaseConsts.SYSTEM_USER_ID) {
				opertaList.add(OperateConsts.UNLOCK);
				opertaList.add(OperateConsts.RESET);
			}
			break;
		case BaseConsts.TWO:
			if (baseUser.getId() != BaseConsts.SYSTEM_USER_ID) {
				opertaList.add(OperateConsts.UNLOCK);
				opertaList.add(OperateConsts.RESET);
			}
			break;
		case BaseConsts.THREE:
			if (baseUser.getId() != BaseConsts.SYSTEM_USER_ID) {
				opertaList.add(OperateConsts.EDIT);
				opertaList.add(OperateConsts.SUBMIT);
				opertaList.add(OperateConsts.DELETE);
			} else {
				opertaList.add(OperateConsts.SUBMIT);
				opertaList.add(OperateConsts.EDIT);
			}
			break;
		}
		return opertaList;
	}

	private BaseUser convertToBaseUser(BaseUserReqDto baseUserReqDto) {
		BaseUser baseUser = new BaseUser();
		baseUser.setId(baseUserReqDto.getId());
		baseUser.setEmployeeNumber(baseUserReqDto.getEmployeeNumber());
		baseUser.setUserName(baseUserReqDto.getUserName());
		baseUser.setEnglishName(baseUserReqDto.getEnglishName());
		baseUser.setChineseName(baseUserReqDto.getChineseName());
		baseUser.setMobilePhone(baseUserReqDto.getMobilePhone());
		baseUser.setStatus(baseUserReqDto.getStatus());
		baseUser.setType(baseUserReqDto.getType());
		return baseUser;
	}

	public Result<BaseUser> submit(BaseUser baseUser) {
		Result<BaseUser> result = new Result<BaseUser>();
		BaseUser userResult = baseUserDao.queryBaseUserById(baseUser.getId());
		if (userResult.getStatus() == BaseConsts.THREE) {
			int i = baseUserDao.submit(baseUser);
			if (i <= 0) {
				throw new BaseException(ExcMsgEnum.USERSSUBMIT_EXCEPTION);
			}
		}
		return result;
	}

	public Result<BaseUser> lock(BaseUser baseUser) {
		Result<BaseUser> result = new Result<BaseUser>();
		// 查询用户状态加锁
		int i = baseUserDao.lock(baseUser);
		if (i <= 0) {
			throw new BaseException(ExcMsgEnum.USERSLOCK_EXCEPTION);
		}

		return result;
	}

	public Result<BaseUser> resetPsw(BaseUser baseUser) {
		Result<BaseUser> result = new Result<BaseUser>();
		// 更新密码
		baseUser.setPassword(PwdUtils.encryptPassword(initUserPassword));
		int i = baseUserDao.update(baseUser);
		if (i <= 0) {
			throw new BaseException(ExcMsgEnum.USERSRESET_EXCEPTION);
		}

		return result;
	}

	public Result<BaseUser> unlock(BaseUser baseUser) {
		Result<BaseUser> result = new Result<BaseUser>();
		int i = baseUserDao.unlock(baseUser);
		if (i < 0) {
			throw new BaseException(ExcMsgEnum.USERSUNLOCK_EXCEPTION);
		}
		return result;
	}

	public Result<BaseUser> delete(BaseUser baseUser) {
		Result<BaseUser> result = new Result<BaseUser>();
		int i = baseUserDao.delete(baseUser);
		if (i < 0) {
			throw new BaseException(ExcMsgEnum.USERSDELETE_EXCEPTION);
		}
		return result;
	}

	/**
	 * 跳转到用户中心，登陆
	 *
	 * @return
	 */
	public String redirectInnerLogin() {
		return "redirect:" + innerLoginUrl + "?struli=" + Base64.encodeBase64String(scfsInnerLoginUrl.getBytes())
				+ "&from=scfs";
	}

	/**
	 * 更新内部用户状态
	 * 
	 * @param request
	 * @return
	 */
	public String updateInnnerUser(HttpServletRequest request) {
		// FIXME 修改内部用户信息，在SCFS系统中，手动修改
		String data = request.getParameter("data");
		try {
			LOGGER.info("更新内部用户状态：[{}]", data);
			JSONObject dataJson = JSONObject.parseObject(data);
			JSONObject userJson = dataJson.getJSONObject("userinfo");// 修改内部用户信息
			String userName = userJson.getString("username");
			String isLock = userJson.getString("is_lock");
			BaseUser oldUser = cacheService.getUserByUsername(userName);
			if (oldUser != null) {
				BaseUser baseUser = new BaseUser();
				baseUser.setId(oldUser.getId());
				baseUser.setUserName(userName);
				baseUser.setMobilePhone(userJson.getString("phone"));
				baseUser.setEmail(userJson.getString("email"));
				baseUser.setRtxCode(userJson.getString("rtx_id"));
				if (StringUtils.isNotEmpty(isLock) && oldUser.getStatus() == BaseConsts.ZERO) {
					baseUser.setStatus(Integer.parseInt(isLock));
				}
				baseUserDao.update(baseUser);
			}
		} catch (Exception e) {
			LOGGER.info("[{}]更新内部用户状态异常：[{}]", data, e);
		}
		JSONObject json = new JSONObject();
		json.put("opr", "1");
		return json.toJSONString();
	}

	/**
	 * 内部用户登录回调
	 *
	 * @param request
	 * @param response
	 */
	public String innerLogin(HttpServletRequest request, HttpServletResponse response) {
		String sid = request.getParameter("sid");
		try {
			if (sid == null) {
				return redirectInnerLogin();
			}
			// String url = request.getParameter("url");
			// String username = request.getParameter("username");
			// String isadmin = request.getParameter("isadmin");
			// String key = request.getParameter("key");
			String data = HttpInvoker.get(innerCheckUrl + "?sid=" + sid);
			if (!data.equals("fbd")) {
				String json = new String(Base64.decodeBase64(data), "utf-8");
				JSONObject userInfo = JSONObject.parseObject(json);
				if (userInfo != null) {
					String userName = userInfo.getString("username");
					BaseUser baseUser = cacheService.getUserByUsername(userName);
					// 用户状态为正常登陆，登录成功，写入cookie
					if (baseUser.getStatus().equals(BaseConsts.ZERO)) {
						writeCacheAndCookie(baseUser, response);
					} else {
						return "redirect:" + scfsLoginMsgUrl;
					}
				}
			}
			// 跳转
		} catch (Exception e) {
			LOGGER.error("校验sid【{}】异常：", sid, e);
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, e, "校验sid出错，请联系用户中心！");
		}
		return "redirect:" + scfsUrl;
	}

	public Result<String> login(String userName, String pwd, HttpServletRequest request, HttpServletResponse response) {
		Result<String> result = new Result<String>();
		BaseUser baseUser = new BaseUser();
		baseUser.setStatus(BaseConsts.ZERO);
		baseUser.setUserName(userName.trim());
		baseUser.setPassword(PwdUtils.encryptPassword(pwd));
		BaseUser user = baseUserDao.queryBaseUserByUser(baseUser);
		// String loginIp = HttpUtils.getRemoteIpAddresses(request);//获取登陆IP
		if (user == null) {
			result.setMsg("用户名或密码错误");
			// 校验用户名是否存在
			baseUser.setPassword(null);
			BaseUser dbUser = baseUserDao.queryBaseUserByUser(baseUser);
			if (dbUser != null) {
				long count = cacheService.incrLoginErrorCount(BaseConsts.LOG_ERROR_COUNT + userName);
				if (count >= BaseConsts.FIVE) {
					result.setMsg("登陆错误" + count + "次,已达到上限，请联系管理员！");
					BaseUser lockUser = new BaseUser();
					lockUser.setId(dbUser.getId());
					lockUser.setStatus(BaseConsts.ONE); // 1-闲置锁定
					count = baseUserDao.update(lockUser);
					if (count != 1) {
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "锁定用户失败");
					}
					cacheService.delLoginToken(userName);
					return result;
				} else {
					result.setMsg("用户名或密码错误,还剩" + (BaseConsts.FIVE - count) + "次机会！");
				}
			}

		} else {// 登陆成功后，写缓存
			if (BaseConsts.ONE == user.getStatus()) {
				result.setMsg("该用户已锁定，请联系管理员！");
				return result;
			}
			writeCacheAndCookie(user, response);
			result.setItems(user.getChineseName());
		}
		return result;
	}

	/**
	 * 写入cookie
	 * 
	 * @param baseUser
	 * @param response
	 */
	private void writeCacheAndCookie(BaseUser baseUser, HttpServletResponse response) {
		String userName = baseUser.getUserName();
		boolean isLogin = ServiceSupport.isLogin(userName);
		boolean isAdmin = false;
		if (isLogin) {
			if ("admin".equalsIgnoreCase(baseUser.getUserName())) {
				isAdmin = true;
			} else {
				cacheService.delLoginToken(userName);
			}
		}
		String token;
		if (isAdmin) {// 是管理员
			token = cacheService.getLoginToken(userName);
		} else {
			token = PwdUtils.encryptPassword(userName + System.currentTimeMillis());
			cacheService.setLoginToken(userName, token);
		}
		try {
			Cookie nameCookie = createCookie(BaseConsts.LOGOIN_USER_NAME,
					URLEncoder.encode(Base64.encodeBase64String(userName.getBytes("utf-8")), "utf-8"));
			response.addCookie(nameCookie);
			String chineseName = baseUser.getChineseName();
			String base = Base64.encodeBase64String(chineseName.getBytes("utf-8"));
			Cookie chineseNameCookie = createCookie(BaseConsts.CHINESE_NAME, URLEncoder.encode(base, "utf-8"));
			response.addCookie(chineseNameCookie);
		} catch (UnsupportedEncodingException e) {
		}
		Cookie tokenCookie = createCookie(BaseConsts.LOGOIN_USER_TOKEN, token);
		response.addCookie(tokenCookie);
		cacheService.delLoginErrorCount(BaseConsts.LOG_ERROR_COUNT + userName);
	}

	public BaseResult logout(HttpServletRequest request, HttpServletResponse response) {
		BaseResult result = new BaseResult();
		String username = removeCookie(request, response);// 移除cookie
		try {
			username = new String(Base64.decodeBase64(URLDecoder.decode(username, "utf-8")), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		cacheService.delLoginToken(username);// 删除登陆token
		return result;
	}

	public BaseResult updatePwd(String oldPwd, String newPwd) {
		BaseResult result = new BaseResult();
		String username = ServiceSupport.getUser().getUserName();
		if (username == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "登陆已失效，请重新登陆");
		}
		BaseUser baseUser = new BaseUser();
		baseUser.setUserName(username);
		baseUser.setPassword(PwdUtils.encryptPassword(oldPwd));
		BaseUser user = baseUserDao.queryBaseUserByUser(baseUser);
		if (user == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "旧密码有误，请重新输入");
		}
		baseUser = new BaseUser();
		baseUser.setId(user.getId());
		baseUser.setPassword(PwdUtils.encryptPassword(newPwd));
		int count = baseUserDao.update(baseUser);
		if (count != 1) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "更新密码异常,请稍后重试");
		}
		return result;
	}

	public String getUsernameByRequest(HttpServletRequest request) {
		String userName = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (int i = 0; i < cookies.length; i++) {
				if (BaseConsts.LOGOIN_USER_NAME.equalsIgnoreCase(cookies[i].getName())) {
					return cookies[i].getValue();
				}
			}
		}
		return userName;
	}

	private String removeCookie(HttpServletRequest request, HttpServletResponse response) {
		String userName = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (int i = 0; i < cookies.length; i++) {
				if (BaseConsts.LOGOIN_USER_NAME.equalsIgnoreCase(cookies[i].getName())) {
					userName = cookies[i].getValue();
				}
				cookies[i].setMaxAge(0);
				cookies[i].setValue(null);
				response.addCookie(cookies[i]);
			}
		}
		return userName;
	}

	private Cookie createCookie(String key, String value) {
		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(-1);// 会话级cookie，关闭浏览器失效 60*60);//过期时间为1小时 0表示清除
		cookie.setPath("/");
		return cookie;
	}

	/**
	 * 通过角色获取用户信息
	 * 
	 * @param baseUserReqDto
	 * @return
	 */
	public PageResult<BaseUserResDto> queryBaseUserListByRoleId(BaseUserReqDto baseUserReqDto) {
		PageResult<BaseUserResDto> result = new PageResult<BaseUserResDto>();
		int offSet = PageUtil.getOffSet(baseUserReqDto.getPage(), baseUserReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, baseUserReqDto.getPer_page());
		List<BaseUser> baseUserList = baseUserDao.queryBaseUserListByRoleId(baseUserReqDto, rowBounds);
		List<BaseUserResDto> baseUserResDtoList = convertToResult(baseUserList);
		result.setItems(baseUserResDtoList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), baseUserReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(baseUserReqDto.getPage());
		result.setPer_page(baseUserReqDto.getPer_page());
		return result;
	}

	/**
	 * 通过项目获取用户信息
	 * 
	 * @param projectSearchReqDto
	 * @return
	 */
	public PageResult<BaseUserResDto> queryBaseUserListByProjectId(ProjectSearchReqDto projectSearchReqDto) {
		PageResult<BaseUserResDto> result = new PageResult<BaseUserResDto>();
		int offSet = PageUtil.getOffSet(projectSearchReqDto.getPage(), projectSearchReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, projectSearchReqDto.getPer_page());
		List<BaseUser> baseUserList = baseUserDao.queryBaseUserListByPorjectId(projectSearchReqDto, rowBounds);
		List<BaseUserResDto> baseUserResDtoList = convertToResult(baseUserList);
		result.setItems(baseUserResDtoList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), projectSearchReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(projectSearchReqDto.getPage());
		result.setPer_page(projectSearchReqDto.getPer_page());
		return result;
	}

	/**
	 * 通过项目查询未分配用户信息
	 * 
	 * @param baseUserReqDto
	 * @return
	 */
	public PageResult<BaseUserResDto> queryUndivideUserByProjectId(BaseUserReqDto baseUserReqDto) {
		PageResult<BaseUserResDto> result = new PageResult<BaseUserResDto>();
		int offSet = PageUtil.getOffSet(baseUserReqDto.getPage(), baseUserReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, baseUserReqDto.getPer_page());
		List<BaseUser> baseUserList = baseUserDao.queryUndivideUserByPorjectId(baseUserReqDto, rowBounds);
		List<BaseUserResDto> baseUserResDtoList = convertToResult(baseUserList);
		result.setItems(baseUserResDtoList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), baseUserReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(baseUserReqDto.getPage());
		result.setPer_page(baseUserReqDto.getPer_page());
		return result;
	}

	/**
	 * 通过主体获取用户信息
	 * 
	 * @param projectSearchReqDto
	 * @return
	 */
	public PageResult<BaseUserResDto> queryBaseUserListBySubjectId(BaseUserReqDto baseUserReqDto) {
		PageResult<BaseUserResDto> result = new PageResult<BaseUserResDto>();
		int offSet = PageUtil.getOffSet(baseUserReqDto.getPage(), baseUserReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, baseUserReqDto.getPer_page());
		List<BaseUser> baseUserList = baseUserDao.queryBaseUserListBySubjectId(baseUserReqDto, rowBounds);
		List<BaseUserResDto> baseUserResDtoList = convertToResult(baseUserList);
		result.setItems(baseUserResDtoList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), baseUserReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(baseUserReqDto.getPage());
		result.setPer_page(baseUserReqDto.getPer_page());
		return result;
	}

	/**
	 * 通过主体查询未分配用户信息
	 * 
	 * @param baseUserReqDto
	 * @return
	 */
	public PageResult<BaseUserResDto> queryUndivideUserBySubjectId(BaseUserReqDto baseUserReqDto) {
		PageResult<BaseUserResDto> result = new PageResult<BaseUserResDto>();
		int offSet = PageUtil.getOffSet(baseUserReqDto.getPage(), baseUserReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, baseUserReqDto.getPer_page());
		List<BaseUser> baseUserList = baseUserDao.queryUndivideUserBySubjectId(baseUserReqDto, rowBounds);
		List<BaseUserResDto> baseUserResDtoList = convertToResult(baseUserList);
		result.setItems(baseUserResDtoList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), baseUserReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(baseUserReqDto.getPage());
		result.setPer_page(baseUserReqDto.getPer_page());
		return result;
	}
}
