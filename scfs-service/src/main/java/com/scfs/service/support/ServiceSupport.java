package com.scfs.service.support;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.entity.BasePermission;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.entity.BaseRole;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.base.entity.BaseUserRoles;
import com.scfs.domain.base.entity.MatterManage;
import com.scfs.domain.common.entity.MonitorLog;
import com.scfs.domain.report.entity.ProfitTarget;
import com.scfs.rpc.cache.ObjectRedisTemplate;
import com.scfs.service.base.exchangeRate.BaseExchangeRateService;
import com.scfs.service.util.ApplicationContextHolder;

/**
 * Created by Administrator on 2016/9/28.
 */
@Service
public class ServiceSupport {
	private final static Logger LOGGER = LoggerFactory.getLogger(ServiceSupport.class);
	private static ObjectRedisTemplate objectRedisTemplate;
	private static CacheService cacheService;
	public static ThreadLocal<BaseUser> loginUserContext = new ThreadLocal<>();
	public static ThreadLocal<MonitorLog> moniter = new ThreadLocal<>();
	public static String localIp;
	private static BaseExchangeRateService baseExchangeRateService;
	private static boolean defaultLoginUserOpen = false;
	private static int defaultUserId = 1;

	@PostConstruct
	private void init() {
		objectRedisTemplate = ApplicationContextHolder.getBean("objectRedisTemplate", ObjectRedisTemplate.class);
		baseExchangeRateService = ApplicationContextHolder.getBean("baseExchangeRateService",
				BaseExchangeRateService.class);
		cacheService = ApplicationContextHolder.getBean("cacheService", CacheService.class);
		getIpAddr();
		try {
			Properties properties = PropertiesLoaderUtils.loadAllProperties("props/application.properties");
			String defaultLoginUser = properties.getProperty("default.login.open");
			if (defaultLoginUser != null) {
				defaultLoginUserOpen = Boolean.valueOf(defaultLoginUser.trim());
			}
			String userId = properties.getProperty("default.login.userId");
			if (userId != null) {
				defaultUserId = Integer.valueOf(userId);
			}

		} catch (IOException e) {
		}
	}

	/**
	 * 币种和金额转换
	 *
	 * @param amount 币种金额
	 * @param curreny 代转换人民币币种
	 * @param date 日期为空，默认为当天
	 * @return
	 */
	public static BigDecimal amountToRMB(BigDecimal amount, int curreny, Date date) {
		if (curreny == BaseConsts.ONE) {
			return amount;
		}
		if (date == null) {
			date = new Date();
		}
		BigDecimal rate = baseExchangeRateService.convertCurrency(String.valueOf(BaseConsts.TWO),
				String.valueOf(BaseConsts.ONE), String.valueOf(curreny), date);
		return DecimalUtil.multiply(amount, rate);
	}
	
	/**
	 * 汇率转换
	 * @param amount
	 * @param curreny
	 * @param date
	 * @return
	 */
	public static BigDecimal amountNewToRMB(BigDecimal amount, int curreny, Date date) {
		return amountNewToRMB(amount, curreny, date, false);
	}
	
	/**
	 * 币种和金额转换(获取财务汇率)
	 * @param amount 币种金额
	 * @param curreny 代转换人民币币种
	 * @param date 日期为空，默认为当天
	 * @param isNew 是否新汇率
	 * @return
	 */
	public static BigDecimal amountNewToRMB(BigDecimal amount, int curreny, Date date, boolean isNew) {
		if (isNew == true) {
			if (curreny == BaseConsts.ONE) {
				return amount;
			}
			if (date == null) {
				date = new Date();
			}
			BigDecimal rate = baseExchangeRateService.convertCurrency(curreny, date);
			return DecimalUtil.multiply(amount, rate);
		} else {
			return amountToRMB(amount, curreny, date); 
		}
	}

	/**
	 * @param operList 根据状态得到的操作列表,编辑，浏览，锁定，解锁等操作
	 * @param permMap 所有操作列表集合，key 名称 value RUL，比如,key:编辑,value:/permission/edit
	 * @return
	 */
	public static List<CodeValue> getOperListByPermission(List<String> operList, Map<String, String> permMap) {
		List<CodeValue> oprResult = Lists.newArrayList();
		if (!CollectionUtils.isEmpty(operList)) {
			for (String operName : operList) {
				String url = permMap.get(operName);
				boolean isPermed = isAllowPerm(url);
				if (isPermed) {
					CodeValue codeValue = new CodeValue(url, operName);
					oprResult.add(codeValue);
				}
			}
		}
		return oprResult;
	}

	/**
	 * 判断用户是否拥有此URL权限
	 *
	 * @param url
	 * @return
	 */
	public static boolean isAllowPerm(String url) {
		return isAllowPerm(url, null);
	}

	public static boolean isAllowPerm(String url, Integer userId) {
		if (url == null) {
			return false;
		}
		if (null == userId) {
			if (null == getUser()) {
				return false;
			}
			userId = getUser().getId();
		}

		Object obj = objectRedisTemplate.boundHashOps(CacheKeyConsts.USER_PERMISSIONS).get(String.valueOf(userId));
		if (obj != null) {
			List<BasePermission> permissionList = (List<BasePermission>) obj;
			for (BasePermission permission : permissionList) {
				if (url.equalsIgnoreCase(permission.getUrl()) && permission.getIsDelete() == BaseConsts.ZERO) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 根据业务code和code获取对应的value
	 *
	 * @param bizCode
	 * @param code
	 * @return
	 */
	public static CodeValue getValueByBizAndCode(String bizCode, String code) {
		CodeValue codeValue = new CodeValue();
		if (bizCode == null || code == null || "null".equals(code)) {
			return codeValue;
		}
		codeValue.setCode(code);
		Object obj = objectRedisTemplate.boundHashOps(CacheKeyConsts.BIZ_CONSTANTS).get(bizCode);
		if (obj != null) {
			List<CodeValue> bizConstantList = (List<CodeValue>) obj;
			for (CodeValue cv : bizConstantList) {
				if (code.equalsIgnoreCase(cv.getCode())) {
					codeValue.setValue(cv.getValue());
					return codeValue;
				}
			}
		}
		return codeValue;
	}

	public static String getValueByBizCode(String bizCode, String code) {
		String value = "";
		if (code == null) {
			return value;
		}
		Object obj = objectRedisTemplate.boundHashOps(CacheKeyConsts.BIZ_CONSTANTS).get(bizCode);
		if (obj != null) {
			List<CodeValue> bizConstantList = (List<CodeValue>) obj;
			for (CodeValue cv : bizConstantList) {
				if (code.equalsIgnoreCase(cv.getCode())) {
					value = cv.getValue();
				}
			}
		}
		return value;
	}

	public static List<CodeValue> getCvListByBizCode(String bizCode) {
		Object obj = objectRedisTemplate.boundHashOps(CacheKeyConsts.BIZ_CONSTANTS).get(bizCode);
		if (obj != null) {
			List<CodeValue> bizConstantList = (List<CodeValue>) obj;
			return bizConstantList;
		} else {
			return null;
		}
	}

	/**
	 * 根据BIZ_CODE和value获取code
	 *
	 * @param bizCode
	 * @param value
	 * @return
	 */
	public static String getCodeByBizValue(String bizCode, String value) {
		if (bizCode == null || value == null) {
			return null;
		}
		Object obj = objectRedisTemplate.boundHashOps(CacheKeyConsts.BIZ_CONSTANTS).get(bizCode);
		if (obj != null) {
			List<CodeValue> bizConstantList = (List<CodeValue>) obj;
			if (!CollectionUtils.isEmpty(bizConstantList)) {
				for (CodeValue cv : bizConstantList) {
					if (value.equalsIgnoreCase(cv.getValue())) {
						return cv.getCode();
					}
				}
			}
		}
		return null;
	}

	/**
	 * 判断是否是管理，如果是管理员 拥有所有权限
	 *
	 * @return
	 */
	public static boolean isAdminUser(Integer userId) {
		if (userId == null || userId <= 0) {
			userId = getUser().getId();
		}
		Object object = objectRedisTemplate.boundHashOps(CacheKeyConsts.USER_ROLES).get(String.valueOf(userId));
		if (object != null) {
			List<BaseUserRoles> urList = (List<BaseUserRoles>) object;
			for (BaseUserRoles userRole : urList) { // 用户下的角色列表
				if (userRole.getIsDelete() == BaseConsts.ZERO && userRole.getStatus() != null
						&& userRole.getStatus() == BaseConsts.ONE) {// 关系没有删除
					Object obj = objectRedisTemplate.boundHashOps(CacheKeyConsts.ADMIN_ROLE)
							.get(String.valueOf(userRole.getRoleId()));
					if (obj != null) {
						BaseRole baseRole = (BaseRole) obj;
						if (baseRole.getState() == BaseConsts.TWO && baseRole.getIsDelete() == BaseConsts.ZERO) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	/**
	 * 获取当前登录用户
	 *
	 * @return
	 */
	public static BaseUser getUser() {
		BaseUser user = loginUserContext.get();
		if (defaultLoginUserOpen) {
			if (user == null) {// 注释掉filter,用于微信免登陆，默认登陆账号，线上应该注释掉下面代码
				return cacheService.getUserByid(defaultUserId);
			}
		}
		return user;
	}

	/**
	 * 获取法务审核
	 *
	 * @return
	 */
	public static BaseUser getLawDepartUser(Integer projectId) {
		BaseProject project = cacheService.getProjectById(projectId);
		return cacheService.getUserByid(project.getLawId());
	}

	/**
	 * 获取业务审核
	 *
	 * @return
	 */
	public static BaseUser getBusDepartUser(Integer projectId) {
		BaseProject project = cacheService.getProjectById(projectId);
		return cacheService.getUserByid(project.getBizManagerId());
	}

	/**
	 * 业务目标值获取业务审核
	 *
	 * @return
	 */
	public static BaseUser getProfitBusDepartUser(Integer profitId) {
		ProfitTarget profit = cacheService.getProfitTarget(profitId);
		return cacheService.getUserByid(profit.getBusiId());
	}

	/**
	 * 业务目标值获取部门主管审核
	 *
	 * @return
	 */
	public static BaseUser getProfitBusDeptManageUser(Integer profitId) {
		ProfitTarget profit = cacheService.getProfitTarget(profitId);
		return cacheService.getUserByid(profit.getDeptManageId());
	}

	/**
	 * 获取财务主管审核
	 *
	 * @return
	 */
	public static BaseUser getFinanceDepartUser(Integer projectId) {
		BaseProject project = cacheService.getProjectById(projectId);
		return cacheService.getUserByid(project.getFinanceManagerId());
	}

	/**
	 * 获取财务主管审核 根据经营单位
	 * 
	 * @return
	 */
	public static BaseUser getFinanceUserByBusi(Integer busi) {
		BaseSubject baseSubject = cacheService.getBusiUnitById(busi);
		if (baseSubject == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "经营单位主键为【" + busi + "】,未查询到经营列表");
		}
		if (baseSubject.getFinanceManagerId() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "经营单位主键为【" + busi + "】,未设置财务主管审核");
		}
		return cacheService.getUserByid(baseSubject.getFinanceManagerId());
	}

	/**
	 * 获取财务专员审核
	 *
	 * @return
	 */
	public static BaseUser getFinanceSpecialDepartUser(Integer projectId) {
		BaseProject project = cacheService.getProjectById(projectId);
		return cacheService.getUserByid(project.getFinanceSpecialId());
	}

	/**
	 * 获取风控主管审核
	 *
	 * @return
	 */
	public static BaseUser getRiskDepartUser(Integer projectId) {
		BaseProject project = cacheService.getProjectById(projectId);
		return cacheService.getUserByid(project.getRiskManagerId());
	}

	/**
	 * 获取风控专员审核
	 * 
	 * @param projectId
	 * @return
	 */
	public static BaseUser getRiskSpecialDepartUser(Integer projectId) {
		BaseProject project = cacheService.getProjectById(projectId);
		return cacheService.getUserByid(project.getRiskSpecialId());
	}

	/**
	 * 获取部门主管审核
	 *
	 * @return
	 */
	public static BaseUser getDeptManageDepartUser(Integer projectId) {
		BaseProject project = cacheService.getProjectById(projectId);
		return cacheService.getUserByid(project.getDepartmentManagerId());
	}

	/**
	 * 根据经营单位查询部门主管审核
	 * 
	 * @param busi
	 * @return
	 */
	public static BaseUser getDepartManageUserByBusi(Integer busi) {
		BaseSubject baseSubject = cacheService.getBusiUnitById(busi);
		if (baseSubject == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "经营单位主键为【" + busi + "】,未查询到经营列表");
		}
		if (baseSubject.getDepartmentManagerId() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "经营单位主键为【" + busi + "】,未设置部门主管审核");
		}
		return cacheService.getUserByid(baseSubject.getDepartmentManagerId());
	}

	/**
	 * 获取总经理审核
	 *
	 * @return
	 */
	public static BaseUser getBossUser(Integer projectId) {
		BaseProject project = cacheService.getProjectById(projectId);
		return cacheService.getUserByid(project.getBossId());
	}

	/**
	 * 获取商务主管审核
	 *
	 * @return
	 */
	public static BaseUser getOfficalUser(Integer projectId) {
		BaseProject project = cacheService.getProjectById(projectId);
		return cacheService.getUserByid(project.getBusinessManagerId());
	}

	/**
	 * 事项管理获取业务审核
	 * 
	 * @return
	 */
	public static BaseUser getMatterBusiUser(Integer matterId) {
		MatterManage matter = cacheService.getMatterService(matterId);
		return cacheService.getUserByid(matter.getBizManagerId());
	}

	/**
	 * 事项管理获取商务审核
	 * 
	 * @return
	 */
	public static BaseUser getMatterOfficalUser(Integer matterId) {
		MatterManage matter = cacheService.getMatterService(matterId);
		return cacheService.getUserByid(matter.getBusinessManagerId());
	}

	/**
	 * 事项管理获取部门主管审核
	 * 
	 * @return
	 */
	public static BaseUser getMatterDepartUser(Integer matterId) {
		MatterManage matter = cacheService.getMatterService(matterId);
		return cacheService.getUserByid(matter.getDepartmentId());
	}

	/**
	 * 事项管理获取法务审核
	 * 
	 * @return
	 */
	public static BaseUser getMatterJusticeUser(Integer matterId) {
		MatterManage matter = cacheService.getMatterService(matterId);
		return cacheService.getUserByid(matter.getJusticeId());
	}

	/**
	 * 事项管理获取财务主管审核
	 * 
	 * @return
	 */
	public static BaseUser getMatterFinanceUser(Integer matterId) {
		MatterManage matter = cacheService.getMatterService(matterId);
		return cacheService.getUserByid(matter.getFinanceManagerId());
	}

	/**
	 * 事项管理获取分控主管审核
	 * 
	 * @return
	 */
	public static BaseUser getMatterRiskUser(Integer matterId) {
		MatterManage matter = cacheService.getMatterService(matterId);
		return cacheService.getUserByid(matter.getRiskManagerId());
	}

	public static boolean isLoginSuccess(HttpServletRequest request, boolean isWxRequest) {
		String username = null;
		String token = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (int i = 0; i < cookies.length; i++) {
				if (BaseConsts.LOGOIN_USER_NAME.equalsIgnoreCase(cookies[i].getName())) {
					username = cookies[i].getValue();
				}
				if (BaseConsts.LOGOIN_USER_TOKEN.equalsIgnoreCase(cookies[i].getName())) {
					token = cookies[i].getValue();
				}

			}
			if (username == null || token == null) {
				return false;
			}
			try {
				username = new String(Base64.decodeBase64(URLDecoder.decode(username, "utf-8")), "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			// 是否是微信请求
			String userNameCacheKey = username;
			if (isWxRequest) {
				userNameCacheKey = BaseConsts.WEI_XIN + username;
			}
			String redisToken = cacheService.getLoginToken(userNameCacheKey);
			if (redisToken != null && redisToken.equalsIgnoreCase(token)) {
				BaseUser user = cacheService.getUserByUsername(username);// 当前登陆用户
				ServiceSupport.loginUserContext.set(user);
				cacheService.setLoginToken(userNameCacheKey, redisToken);
				return true;
			}
		}
		return false;
	}

	public static boolean isLogin(String username) {
		String redisToken = cacheService.getLoginToken(username);
		if (redisToken == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 获取本机IP
	 *
	 * @return
	 */
	private String getIpAddr() {
		Enumeration<NetworkInterface> interfaces;
		try {
			interfaces = NetworkInterface.getNetworkInterfaces();
		} catch (Throwable e) {
			throw new RuntimeException("获取本机IP异常");
		}
		if (interfaces != null) {
			while (interfaces.hasMoreElements()) {
				NetworkInterface network = (NetworkInterface) interfaces.nextElement();
				Enumeration<InetAddress> addresses = network.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress address = (InetAddress) addresses.nextElement();
					if (!(address instanceof Inet6Address)) {
						if (isValidAddress(address)) {
							localIp = address.getHostAddress();
							break;
						}
					}
				}
			}
		}
		LOGGER.info("获取本机IP:" + localIp);
		return localIp;
	}

	public static boolean isValidAddress(InetAddress address) {
		if ((address == null) || (address.isLoopbackAddress())) {
			return false;
		}
		String ip = address.getHostAddress();
		return (ip != null) && (!"127.0.0.1".equals(ip)) && (!"0.0.0.0".equals(ip)) && (isValidIPv4(ip));
	}

	public static boolean isValidIPv4(String address) {
		if ((address == null) || (address.length() == 0)) {
			return false;
		}
		int octets = 0;
		String temp = address + ".";

		int start = 0;
		int pos;
		while ((start < temp.length()) && ((pos = temp.indexOf('.', start)) > start)) {
			if (octets == 4) {
				return false;
			}
			int octet;
			try {
				octet = Integer.parseInt(temp.substring(start, pos));
			} catch (NumberFormatException ex) {
				return false;
			}
			if ((octet < 0) || (octet > 255)) {
				return false;
			}
			start = pos + 1;
			octets++;
		}
		return octets == 4;
	}

	public void setDefaultLoginOpen(boolean defaultLoginOpen) {
		this.defaultLoginUserOpen = defaultLoginOpen;
	}
}
