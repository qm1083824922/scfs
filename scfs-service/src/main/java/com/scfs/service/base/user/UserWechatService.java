package com.scfs.service.base.user;

import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.wechat.WechatUserDao;
import com.scfs.domain.base.dto.req.BaseUserReqDto;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.wechat.entity.WechatUser;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

@Service
public class UserWechatService {

	@Autowired
	private WechatUserDao wechatUserDao;
	@Autowired
	private CacheService cacheService;

	public PageResult<WechatUser> queryUnbind(BaseUserReqDto baseUserReqDto) {
		PageResult<WechatUser> result = new PageResult<WechatUser>();
		int offSet = PageUtil.getOffSet(baseUserReqDto.getPage(), baseUserReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, baseUserReqDto.getPer_page());
		List<WechatUser> wechatList = wechatUserDao.queryWechatUserByCon(baseUserReqDto, rowBounds);
		List<WechatUser> wechatLists = convertToRes(wechatList);
		result.setItems(wechatLists);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), baseUserReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(baseUserReqDto.getPage());
		result.setPer_page(baseUserReqDto.getPer_page());
		return result;
	}

	public PageResult<WechatUser> querybindWechat(BaseUserReqDto baseUserReqDto) {
		PageResult<WechatUser> result = new PageResult<WechatUser>();
		int offSet = PageUtil.getOffSet(baseUserReqDto.getPage(), baseUserReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, baseUserReqDto.getPer_page());
		List<WechatUser> wechatList = wechatUserDao.queryWechatUserByUserId(baseUserReqDto.getUserId(), rowBounds);
		List<WechatUser> wechatLists = convertToRes(wechatList);
		result.setItems(wechatLists);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), baseUserReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(baseUserReqDto.getPage());
		result.setPer_page(baseUserReqDto.getPer_page());
		return result;
	}

	private List<WechatUser> convertToRes(List<WechatUser> wechatList) {
		// TODO Auto-generated method stub
		for (int i = 0; i < wechatList.size(); i++) {
			wechatList.get(i)
					.setSexName(ServiceSupport.getValueByBizCode(BizCodeConsts.SEX, wechatList.get(i).getSex() + ""));
			if (StringUtils.isNotBlank(wechatList.get(i).getNickname())) {
				wechatList.get(i).setNickname(new String(Base64.decodeBase64(wechatList.get(i).getNickname())));
			}
		}
		return wechatList;
	}

	public void bindWechat(WechatUser wechatUser) {
		Integer result = wechatUserDao.update(wechatUser);
		if (result < 0) {
			throw new BaseException(ExcMsgEnum.WECHAT_UPDATE_ERROR);
		}
	}

	public void unbindWechat(WechatUser wechatUser) {
		for (int i = 0; i < wechatUser.getIds().size(); i++) {
			WechatUser user = new WechatUser();
			user.setId(wechatUser.getIds().get(i));
			// 删除绑定缓存信息，在解绑
			WechatUser wc = wechatUserDao.queryWechatUserById(user.getId());
			BaseUser baseUser = cacheService.getUserByid(wc.getUserId());
			cacheService.delLoginToken(BaseConsts.WEI_XIN + baseUser.getUserName());

			Integer result = wechatUserDao.unbindUser(user);
			if (result < 0) {
				throw new BaseException(ExcMsgEnum.WECHAT_UPDATE_ERROR);
			}
		}

	}

	public List<WechatUser> queryBindWechatsByUserId(int userId) {
		return wechatUserDao.queryBindWechatsByUserId(userId);
	}

	public List<WechatUser> queryBindWechatsByRoleName(String roleName) {
		return wechatUserDao.queryBindWechatsByRoleName(roleName);
	}
}
