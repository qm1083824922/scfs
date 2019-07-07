package com.scfs.dao.wechat;

import com.scfs.domain.base.dto.req.BaseUserReqDto;
import com.scfs.domain.wechat.entity.WechatUser;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WechatUserDao {

	int insert(WechatUser wechatUser);

	WechatUser queryWechatUserById(@Param("id") int id);

	List<WechatUser> queryWechatUserByUserId(Integer userId, RowBounds rowBounds);

	List<WechatUser> queryBindWechatsByUserId(Integer userId);

	List<WechatUser> queryWechatUserByCon(BaseUserReqDto baseUserReqDto, RowBounds rowBounds);

	WechatUser queryWechatUserByOpenId(String openId);

	int update(WechatUser wechatUser);

	int unbindUser(WechatUser wechatUser);

	int bindUser(WechatUser wechatUser);

	List<WechatUser> queryBindWechatsByRoleName(@Param("roleName")String roleName);
}
