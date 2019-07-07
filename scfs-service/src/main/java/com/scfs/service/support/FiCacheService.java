package com.scfs.service.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.stereotype.Service;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.dao.fi.AccountBookDao;
import com.scfs.dao.fi.AccountLineDao;
import com.scfs.domain.fi.entity.AccountBook;
import com.scfs.domain.fi.entity.AccountLine;
import com.scfs.rpc.cache.ObjectRedisTemplate;

/**
 * <pre>
 * 
 *  File: FiCacheService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月25日			Administrator
 *
 * </pre>
 */
@Service
public class FiCacheService {
	@Autowired
	private ObjectRedisTemplate objectRedisTemplate;
	@Autowired
	private AccountBookDao accountBookDao;
	@Autowired
	private AccountLineDao accountLineDao;

	public AccountBook getAbById(int id) {
		BoundHashOperations accountBookCache = objectRedisTemplate.boundHashOps(CacheKeyConsts.ACCOUNTBOOK);
		Object obj = accountBookCache.get(String.valueOf(id));
		if (obj == null) {
			AccountBook accountBook = accountBookDao.queryEntityById(id);
			if (accountBook != null) {
				accountBookCache.put(String.valueOf(id), accountBook);
				return accountBook;
			}
		}
		return (AccountBook) obj;
	}

	public AccountLine getAlById(int id) {
		BoundHashOperations accountLineCache = objectRedisTemplate.boundHashOps(CacheKeyConsts.ACCOUNTLINE);
		Object obj = accountLineCache.get(String.valueOf(id));
		if (obj == null) {
			AccountLine accountLine = accountLineDao.queryEntityById(id);
			if (accountLine != null) {
				accountLineCache.put(String.valueOf(id), accountLine);
				return accountLine;
			}
		}
		return (AccountLine) obj;
	}

	public String getAbNameById(int id) {
		AccountBook accountBook = getAbById(id);
		if (accountBook == null) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		builder.append(accountBook.getAccountBookNo());
		builder.append(BaseConsts.CONJUNCTION_FLAG);
		builder.append(accountBook.getAccountBookName());
		return builder.toString();
	}

	public String getAbNameById(AccountBook accountBook) {
		if (accountBook == null) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		builder.append(accountBook.getAccountBookNo());
		builder.append(BaseConsts.CONJUNCTION_FLAG);
		builder.append(accountBook.getAccountBookName());
		return builder.toString();
	}

	public String getAlNameById(int id) {
		AccountLine accountLine = getAlById(id);
		if (accountLine == null) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		builder.append(accountLine.getAccountLineNo());
		builder.append(BaseConsts.CONJUNCTION_FLAG);
		builder.append(accountLine.getAccountLineName());
		return builder.toString();
	}

	public String getAlNameById(AccountLine accountLine) {
		if (accountLine == null) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		builder.append(accountLine.getAccountLineNo());
		builder.append(BaseConsts.CONJUNCTION_FLAG);
		builder.append(accountLine.getAccountLineName());
		return builder.toString();
	}
}
