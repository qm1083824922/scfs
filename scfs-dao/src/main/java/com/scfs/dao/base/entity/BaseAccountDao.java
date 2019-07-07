package com.scfs.dao.base.entity;

import com.scfs.domain.base.entity.BaseAccount;
import com.scfs.domain.base.subject.dto.req.AddAccountDto;
import com.scfs.domain.base.subject.dto.req.QueryAccountReqDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BaseAccountDao {
	/**
	 * 添加账户信息
	 */
	int insertBaseAccount(AddAccountDto AddAccountDto);

	/**
	 * 根据主键ID获取账号信息
	 * 
	 * @param id
	 * @return
	 */
	BaseAccount loadAndLockEntityById(Integer id);

	/**
	 * 根据主键ID获取账号信息
	 * 
	 * @param id
	 * @return
	 */
	BaseAccount queryEntityById(Integer id);

	/**
	 * 查询账户信息
	 */
	List<BaseAccount> queryAccountBySubjectId(QueryAccountReqDto queryAccountReqDto, RowBounds rowBounds);

	List<BaseAccount> queryAccountBySubjectId(QueryAccountReqDto queryAccountReqDto);

	List<BaseAccount> queryFicBySubjectId(QueryAccountReqDto queryAccountReqDto);

	/**
	 * 查询所有主体正在使用的账号信息
	 * 
	 * @return
	 */
	List<BaseAccount> queryAllAccount(@Param("updateAt") String updateAt);

	/**
	 * 查询最后更新时间
	 * 
	 * @return
	 */
	Date queryLastUpdateAt();

	/**
	 * 修改账户信息
	 */
	int updateBaseAccountById(BaseAccount aseAccount);

	/**
	 * 作废信息
	 */
	int invalidAccountById(BaseAccount baseAccount);

	/**
	 * 根据开户账号查询账号信息
	 * 
	 * @param accountNo
	 * @return
	 */
	BaseAccount queryAccountByAccountNo(String accountNo);

}
