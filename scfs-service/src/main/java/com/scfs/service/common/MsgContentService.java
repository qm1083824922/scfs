package com.scfs.service.common;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.dao.base.entity.BaseUserDao;
import com.scfs.dao.common.MsgContentDao;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.common.MailTemplateOne;
import com.scfs.domain.common.MailTemplateTwo;
import com.scfs.domain.common.entity.MsgContent;
import com.scfs.domain.wechat.entity.WechatUser;
import com.scfs.service.base.user.UserWechatService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Maps;

/**
 * Created by Administrator on 2016/12/15.
 */
@Service
public class MsgContentService {
	@Autowired
	private MsgContentDao msgContentDao;
	@Autowired
	private BaseUserDao baseUserDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private UserWechatService userWechatService;

	/**
	 * 根据用户ID添加消息
	 *
	 * @param userId 用户id
	 * @param title 消息头
	 * @param content 消息内容
	 * @param msgType 消息类型，1表示RTX,2表示email,3表示sms短信，4表示微信，5表示语音电话
	 */
	public void addMsgContentByUserId(int userId, String title, String content, int msgType) {
		BaseUser baseUser = cacheService.getUserByid(userId);
		String acc = getAccount(baseUser, msgType);
		addMsgContent(acc, title, content, msgType);
	}

	/**
	 * @param roleName 角色名称
	 * @param title 消息头
	 * @param content 消息内容
	 * @param msgType 消息类型，1表示RTX,2表示email,3表示sms短信，4表示微信，5表示语音电话
	 */
	public void addMsgContentByRoleName(String roleName, String title, String content, int msgType) {
		List<BaseUser> userList = baseUserDao.queryUserListByRoleName(roleName);
		if (CollectionUtils.isNotEmpty(userList)) {
			StringBuilder sb = new StringBuilder();
			for (BaseUser baseUser : userList) {
				String acc = getAccount(baseUser, msgType);
				sb.append(acc).append(",");
			}
			addMsgContent(sb.toString(), title, content, msgType);
		}
	}

	/**
	 * 根据角色发送微信系统消息
	 * 
	 * @param roleName
	 * @param title
	 * @param content
	 */
	public void addWebcatMsgByRoleName(String roleName, String title, String url, String msg, String remark) {
		List<WechatUser> wechatUsers = userWechatService.queryBindWechatsByRoleName(roleName);
		if (!CollectionUtils.isEmpty(wechatUsers)) {
			StringBuilder acc = new StringBuilder();
			for (WechatUser wechatUser : wechatUsers) {
				acc.append(wechatUser.getOpenid()).append(",");
			}
			if (StringUtils.isNotBlank(acc)) {
				Map<String, String> map = Maps.newHashMap();
				map.put("template_id", "ANI1ZfU8RWCkJAZ1i3AtG1MpIDICD-o8Yt9mytggFak");
				map.put("first", title);
				map.put("url", url);
				map.put("name", msg);
				map.put("time", DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, new Date()));
				map.put("remark", remark);
				String content = JSONObject.toJSONString(map);
				addMsgContent(acc.toString(), title, content, BaseConsts.FOUR);
			}
		}
	}

	/**
	 * 根据账号添加消息
	 *
	 * @param toAccounts 账号，多个账号用逗号隔开
	 * @param title 消息头
	 * @param content 消息内容
	 * @param msgType 消息类型，1表示RTX,2表示email,3表示sms短信，4表示微信，5表示语音电话
	 */
	public void addMsgContents(String toAccounts, String title, String content, int msgType) {
		addMsgContent(toAccounts, title, content, msgType);
	}

	public List<MsgContent> querySendMsgList() {
		return msgContentDao.querySendMsgList();
	}

	public int updateMsgById(MsgContent msgContent) {
		return msgContentDao.updateById(msgContent);
	}

	private String getAccount(BaseUser baseUser, int msgType) {
		switch (msgType) {
		case BaseConsts.ONE:
			return baseUser.getRtxCode();
		case BaseConsts.TWO:
			return baseUser.getEmail();
		case BaseConsts.THREE:
			return baseUser.getMobilePhone();
		case BaseConsts.FIVE:
			return baseUser.getMobilePhone();
		default:
			return null;
		}
	}

	public void addMsgContent(String toAccounts, String title, String content, int msgType) {
		//TODO 仅支持发送邮件(RTX、短信需对接rtx和运营商，微信需开通微信公众号)
		if (msgType == BaseConsts.TWO && StringUtils.isNotBlank(toAccounts)) {
			MsgContent msgContent = new MsgContent();
			msgContent.setToAccounts(toAccounts);
			msgContent.setMsgTitle(title);
			msgContent.setMsgContent(content);
			msgContent.setMsgType(msgType);
			msgContent.setIsSend(BaseConsts.ONE);
			msgContent.setSendCount(BaseConsts.ZERO);
			msgContent.setCreateAt(new Date());
			if (ServiceSupport.getUser() != null) {
				msgContent.setCreator(ServiceSupport.getUser().getChineseName());
				msgContent.setCreatorId(ServiceSupport.getUser().getId());
			}
			msgContent.setIsDelete(BaseConsts.ZERO);
			msgContentDao.insert(msgContent);
		}
	}

	/**
	 * 邮件内容转换，模板一(mail.html)的内容转换
	 * 
	 * @param contentTitle 内容标题 
	 * @param templateOnes 内容
	 * @param bottom 合计
	 * @return
	 */
	public String convertMailOneContent(final String contentTitle, final List<MailTemplateOne> templateOnes,
			String bottom) {
		StringBuilder sb = new StringBuilder();
		if (CollectionUtils.isNotEmpty(templateOnes)) {
			for (MailTemplateOne one : templateOnes) {
				sb.append("<tr><td style='width:30%;text-align: right;background:#eee;'>" + one.getColumnOne() + "</td>"
						+ "<td>" + one.getColumnTwo() + "</td></tr>");
			}
		}
		Date d = new Date();
		String content = "<div id='mail-box' style='width:700px;margin:auto;'>" + "            <style>"
				+ "                table{" + "                    border-collapse:collapse;"
				+ "                    width:100%;" + "                    font-size:14px;" + "                }"
				+ "                td{" + "                    border:1px solid #ccc;"
				+ "                    padding:5px;" + "                }" + "                a{"
				+ "                    text-decoration:none;" + "                }" + "            </style>"
				+ "            <table>" + "                <tr>"
				+ "                    <td colspan='2' style='border:0 none;'>"
				+ "                        <h1 style='text-align:center;font-size:30px;'>" + contentTitle + "</h1>"
				+ "                    </td>" + "                </tr>" + sb + (bottom != null ? bottom : "")
				+ "<tr><td colspan='2' style='line-height:20px;'>该邮件为SCFS系统自动发送，请勿回复。如有疑问，请联系系统管理员。<br>日期："
				+ DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, d) + "</td></tr></table></div>";
		return content;
	}

	/**
	 * 邮件模板二(mail2.html)的内容转换
	 * 
	 * @param contentTitle 内容标题
	 * @param tableTitle 表格标题
	 * @param lists 内容 
	 * @param bottom 合计
	 * @return
	 */
	public String convertMailTwoContent(final String contentTitle, final String[] tableTitle,
			final List<List<MailTemplateTwo>> lists, String bottom) {
		StringBuilder tableTitleStr = new StringBuilder();
		int length = 0;
		if (tableTitle != null && tableTitle.length > 0) {
			length = tableTitle.length;
			tableTitleStr.append("<tr>");
			for (int i = 0; i < tableTitle.length; i++) {
				tableTitleStr.append(" <th style='text-align: center;background:#eee;'>" + tableTitle[i] + "</th>");
			}
			tableTitleStr.append("</tr>");
		} else {
			length = 0;
		}

		StringBuilder contSb = new StringBuilder();
		if (CollectionUtils.isNotEmpty(lists)) {
			for (List<MailTemplateTwo> columns : lists) {
				contSb.append("<tr>");
				for (MailTemplateTwo column : columns) {
					contSb.append(" <td style='text-align: " + column.getAlign() + ";color:" + column.getColor()
							+ ";font-size:" + column.getFontSize() + ";'>" + column.getContent() + "</td>");
				}
				contSb.append("</tr>");
			}
		}
		Date d = new Date();
		String content = "<div id='mail-box' style='width:700px;margin:auto;'>" + "            <style>"
				+ "                table{" + "                    border-collapse:collapse;"
				+ "                    width:100%;" + "                    font-size:14px;" + "                }"
				+ "                th,td{" + "                    border:1px solid #ccc;"
				+ "                    padding:5px;" + "                }" + "                a{"
				+ "                    text-decoration:none;" + "                }" + "            </style>"
				+ "            <table><tr><td colspan=" + length + " style='border:0 none;text-align: center;'>"
				+ " <h1 style='text-align:center;font-size:30px;'>" + contentTitle + "</h1></td></tr>" + tableTitleStr
				+ contSb + (bottom != null ? bottom : "") + "<tr><td colspan='" + length
				+ "' style='line-height:20px;'>该邮件为SCFS系统自动发送，请勿回复。如有疑问，请联系系统管理员。<br>日期："
				+ DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, d) + "</td></tr></table></div>";

		return content;
	}

	/**
	 * @param map key值必须严格和微信消息模板官网一致
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String createWechatTempMsg(Map<String, String> map) {
		Map<String, Object> m1 = Maps.newHashMap();
		m1.put("touser", map.get("touser"));
		map.remove("touser");
		// 要使用的消息模板
		m1.put("template_id", map.get("template_id"));
		map.remove("template_id");
		m1.put("url", map.get("url"));
		map.remove("url");
		Iterator it = map.entrySet().iterator();
		// 第二层，主要data数据
		Map<String, Object> m2 = Maps.newHashMap();
		while (it.hasNext()) {
			Map<String, String> mm = Maps.newHashMap();
			Map.Entry entry = (Map.Entry) it.next();
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			if (value == null) {
				value = "";
			}
			mm.put("value", value);
			m2.put(key, mm);
		}
		m1.put("data", m2);
		return JSONObject.toJSONString(m1);
	}
}
