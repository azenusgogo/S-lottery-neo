package com.sogou.lottery.web.service.qianbao.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sogou.lottery.base.vo.qianbao.AccountVo;
import com.sogou.lottery.dao.qianbao.QianBaoAccountDao;

@Service
public class QianBaoAccountService {
	
	final Log LOG = LogFactory.getLog(getClass());
	public final static String ACCOUNT_SOGOU = "SOGOU";
	
	@Autowired
	private QianBaoAccountDao accountDao;
	
	public List<AccountVo> getByAccountId(String accountId) {
	
		return accountDao.getByAccount(accountId);
	}
	
	public AccountVo getSogouMiddle() {
	
		return getAccount(getSogouPlatform(), AccountVo.TYPE_SG_MIDDLE);
	}
	
	public AccountVo getSogouPromo() {
	
		return getAccount(getSogouPlatform(), AccountVo.TYPE_SG_PROMO);
	}
	
	public AccountVo getSogouProfit() {
	
		return getAccount(getSogouPlatform(), AccountVo.TYPE_SG_PROFIT);
	}
	
	public AccountVo getPlatformReceipt(String platformId) {
	
		return getAccount(platformId, AccountVo.TYPE_PT_RECEIVE);
	}
	
	private AccountVo getAccount(String platformId, int type) {
	
		List<AccountVo> list = accountDao.getByPlatform(platformId);
		Collections.sort(list, new Comparator<AccountVo>() {
			
			@Override
			public int compare(AccountVo o1, AccountVo o2) {
			
				return o1.getCreateTime().compareTo(o2.getCreateTime());
			}
		});
		for (AccountVo vo : list) {
			if (type == vo.getType()) {
				return vo;
			}
		}
		throw new IllegalArgumentException("Not Found Sogou Account " + type);
	}
	
	private String getSogouPlatform() {
	
		return ACCOUNT_SOGOU;
	}
}
