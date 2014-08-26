package com.sogou.lottery.web.service.user.service;

import com.sogou.lottery.amqp.AmqpTaskTopics;
import com.sogou.lottery.amqp.engine.MessageQueueLoaderEngine;
import com.sogou.lottery.base.constant.SystemConfigs;
import com.sogou.lottery.base.memcached.MemcachedConstantBase;
import com.sogou.lottery.base.memcached.MemcachedSupport;
import com.sogou.lottery.base.user.dto.UserNickNameDto;
import com.sogou.lottery.base.util.DateUtil;
import com.sogou.lottery.base.vo.SystemConfig;
import com.sogou.lottery.base.vo.user.User;
import com.sogou.lottery.base.vo.user.UserChannelVo;
import com.sogou.lottery.base.vo.user.UserLog;
import com.sogou.lottery.common.constant.LOG;
import com.sogou.lottery.common.constant.MemcachedConstant;
import com.sogou.lottery.dao.user.UserChannelDao;
import com.sogou.lottery.dao.user.UserDao;
import com.sogou.lottery.web.service.user.constant.UserAccountDomainEnum;
import com.sogou.lottery.web.service.user.dto.UserInfoDto;
import com.sogou.lottery.web.service.user.dto.UserOperDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class UserCacheService extends MemcachedSupport {
	
	public static final String CHANNEL = "channel_";
    private static Logger log = LOG.user;

    @Autowired
	private UserDao userDao;
	@Autowired
	private UserChannelDao userChannelDao;

    @Autowired(required = false)
    AmqpTemplate amqpTemplate;

    @Autowired
    UserService userService;
	
	public UserInfoDto queryUserInfoCache(String userId) {
	
		return this.queryUserInfoCache(userId, false);
	}
	
	/**
	 * 描述：刷新用户缓存
	 * 
	 * @param userId
	 * @param forceRefresh
	 * @return
	 */
	public UserInfoDto queryUserInfoCache(String userId, boolean forceRefresh) {
	
		UserInfoDto userInfoDto = null;
		// 1.非强制刷新，从memcached中读取
		if (!forceRefresh) {
            try{
            userInfoDto = (UserInfoDto) super.getMemcachedData(MemcachedConstantBase.MEMCACHED_USER, MemcachedConstant.getUserinfoKey(userId));
            }catch(Exception e){
                log.error("get memcache error:userId:"+userId+e,e);
            }
        }
		// 2.强制刷新或者从memcached中读取为null，则从数据库中获取
		if (forceRefresh || userInfoDto == null) {
			// 2.1从数据库中获取user
			User user = userDao.getByUserId(userId);
			userInfoDto = new UserInfoDto();
			userInfoDto.setUser(user);
			// 2.2new userInfoDto设置user，放入memcached
            super.setMemcachedData(MemcachedConstantBase.MEMCACHED_USER, MemcachedConstant.getUserinfoKey(userId), userInfoDto);
		}
        //设置第三方用户昵称
        this.setPartyUserNickName(userId,userInfoDto);
        return userInfoDto;
	}

    /**
     * 设置第三方用户昵称
     * @param userId
     * @param userInfoDto
     */
    public void setPartyUserNickName(String userId,UserInfoDto userInfoDto){
        //1.qq用户
        if (userService.getDomainEnum(userId) == UserAccountDomainEnum.QQ) {
            if (userInfoDto.getUser() == null) {//1.1用户未补全
                UserNickNameDto userNickNameDto = this.queryPartyUserNickName(userId);
                //1.1.1未获取到passport用户昵称,拼接用户id信息
                if(userNickNameDto.getNickName() == null){
                    userInfoDto.setPartyUserNickName("腾讯用户"+userId.substring(0,(userId.length() > 8) ? 8:userId.length()));
                }else{//1.1.2取到passport用户昵称，拼接该昵称
                    userInfoDto.setPartyUserNickName("腾讯用户"+userNickNameDto.getNickName());
                }
            } else {//1.2用户已补全，拼接站内昵称 暂时不需要
                userInfoDto.setPartyUserNickName(userInfoDto.getUser().getNickName());
            }
        }
    }

    /**
     * 描述：查询第三方用户昵称
     * @param userId
     * @return
     */
    public UserNickNameDto queryPartyUserNickName(String userId) {
        Integer timeout = Integer.parseInt(SystemConfigs.get("NICKNAME_QUERY_TIMEOUT", "3600000"));
        UserNickNameDto userNickNameDto = null;
        try {
            userNickNameDto = (UserNickNameDto) super.getMemcachedData(MemcachedConstantBase.MEMCACHED_USER, MemcachedConstant.getUserNickNameKey(userId));
        } catch (Exception e) {
            log.error("get memcache error:userId:" + userId + e, e);
        }
        //1.用户昵称不存在，发送消息更新用户昵称,更新memcached昵称为已设置
        if (userNickNameDto == null) {
            userNickNameDto = new UserNickNameDto(null, DateUtil.getCurrentDate());
            this.sendMessage(userId);
            super.setMemcachedData(MemcachedConstantBase.MEMCACHED_USER, MemcachedConstant.getUserNickNameKey(userId), userNickNameDto);
            //2.用户昵称时间差超过阀值，发送消息更新用户昵称，memcached存取昵称时间阀值更新
        } else if ((new Date().getTime() - userNickNameDto.getTimestamp().getTime()) > timeout) {
            userNickNameDto = new UserNickNameDto(userNickNameDto.getNickName(), DateUtil.getCurrentDate());
            this.sendMessage(userId);
            super.setMemcachedData(MemcachedConstantBase.MEMCACHED_USER, MemcachedConstant.getUserNickNameKey(userId), userNickNameDto);
        } else {
            //3.未超过时间阀值，则默认不处理
        }
        return userNickNameDto;
    }

    private void sendMessage(String userId) {
        log.info("sendMessage==userId:" + userId);
        if (amqpTemplate != null) {
            try {
                amqpTemplate.convertAndSend(AmqpTaskTopics.USER_CACHE_SYNC_PASSPORT_INFO, userId);
            } catch (Exception e) {
                log.error("sendMessage==userId:" + userId+",error:"+e, e);
            }
        }
    }

    // public User queryUserCache(String userId) {
	// return this.queryUserCache(userId, false);
	// }
	//
	// /**
	// * 描述：刷新用户缓存
	// *
	// * @param userId
	// * @param forceRefresh
	// * @return
	// */
	// public User queryUserCache(String userId, boolean forceRefresh) {
	// User user = null;
	// // 1.非强制刷新，从memcached中读取
	// if (!forceRefresh) {
	// user = (User)
	// super.getMemcachedData(MemcachedConstantBase.MEMCACHED_USER,
	// MemcachedConstant.getUserinfoKey(userId));
	// }
	// // 2.强制刷新或者从memcached中读取为null，则从数据库中获取
	// if (forceRefresh || user == null) {
	// // 2.1从数据库中获取user
	// user = userDao.getByUserId(userId);
	// // 2.1.1数据库中的user为null，则新建一个new user将其set到memcached中
	// if (user == null) {
	// super.setMemcachedData(MemcachedConstantBase.MEMCACHED_USER,
	// MemcachedConstant.getUserinfoKey(userId), new User());
	// return null;
	// } else {
	// // 2.2数据库中不为空，则直接放入memcached
	// super.setMemcachedData(MemcachedConstantBase.MEMCACHED_USER,
	// MemcachedConstant.getUserinfoKey(userId), user);
	//
	// }
	// } else {
	// // 3.如果从memcached中读取的user是new出来的user，说明本用户在数据库中不存在，需要返回null
	// if (StringUtils.isBlank(user.getUserId())) {
	// return null;
	// }
	// }
	// return user;
	// }
	
	/**
	 * @param userLog
	 * @param expire
	 *            过期的时间点
	 * @return
	 */
	public boolean setUserOper(UserLog userLog, Date expire) {
	
		Validate.notNull(userLog);
		Validate.notNull(expire);
		Validate.notNull(userLog.getUserId());
		// userLogDao.insert(userLog);
		
		Timestamp now = new Timestamp(System.currentTimeMillis());
		UserOperDto dto = getUserOper(userLog.getUserId(), userLog.getType());
		if (dto == null) {
			dto = new UserOperDto(userLog.getUserId(), (int) userLog.getType(), 0, new Timestamp(expire.getTime()));
		}
		long delay = expire.before(dto.getExpireTime()) ? expire.getTime() : dto.getExpireTime().getTime();
		delay = delay - now.getTime();
		if (delay < 0) {
			return false;
		}
		if (delay < 1001) {
			delay = 1001;
		}
		dto.setUpdateTime(now);
		dto.setTimes(dto.getTimes() + 1);
		String key = getUserOperKey(userLog.getUserId(), userLog.getType());
		return setMemcachedData(MemcachedConstantBase.MEMCACHED_USER, key, dto, new Date(delay));
	}
	
	public int getUserOperTimes(String userId, int operType) {
	
		UserOperDto dto = getUserOper(userId, operType);
		return dto == null ? 0 : dto.getTimes();
	}
	
	public void deleteUserOperTimes(String userId, int operType) {
	
		UserOperDto dto = getUserOper(userId, operType);
		if (dto != null) {
			String key = getUserOperKey(userId, operType);
			deletMemcachedeData(MemcachedConstantBase.MEMCACHED_USER, key);
		}
	}
	
	private UserOperDto getUserOper(String userId, int operType) {
	
		String key = getUserOperKey(userId, operType);
		UserOperDto userOper = (UserOperDto) getMemcachedData(MemcachedConstantBase.MEMCACHED_USER, key);
		return userOper;
	}
	
	public UserChannelVo queryUserChannel(String userId, boolean forceRefresh) {
	
		UserChannelVo vo = null;
		// 1.非强制刷新，从memcached中读取
		if (!forceRefresh) {
            try{
			vo = (UserChannelVo) super.getMemcachedData(MemcachedConstantBase.MEMCACHED_USER, getChannelKey(userId));
            }catch(Exception e){
                log.error("get memcache error:userId:"+userId+e,e);
            }
		}
		// 2.强制刷新或者从memcached中读取为null，则从数据库中获取
		if (forceRefresh || vo == null) {
			// 2.1从数据库中获取user
			vo = userChannelDao.getByUserId(userId);
			// 2.1.1数据库中的user为null，则新建一个new user将其set到memcached中
			if (vo == null) {
				super.setMemcachedData(MemcachedConstantBase.MEMCACHED_USER, getChannelKey(userId), new UserChannelVo());
				return null;
			} else {
				// 2.2数据库中不为空，则直接放入memcached
				super.setMemcachedData(MemcachedConstantBase.MEMCACHED_USER, getChannelKey(userId), vo);
			}
		} else {
			// 3.如果从memcached中读取的user是new出来的user，说明本用户在数据库中不存在，需要返回null
			if (StringUtils.isBlank(vo.getUserId())) {
				return null;
			}
		}
		return vo;
	}
	
	private String getChannelKey(String userId) {
	
		return CHANNEL + userId;
	}
	
	private String getUserOperKey(String userId, int operType) {
	
		return "oper" + "|" + userId + "|" + operType;
	}
}
