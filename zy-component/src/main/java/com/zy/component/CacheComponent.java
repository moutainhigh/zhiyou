package com.zy.component;

import com.zy.common.support.cache.CacheSupport;
import com.zy.entity.act.Activity;
import com.zy.entity.cms.Notice;
import com.zy.entity.fnc.BankCard;
import com.zy.entity.usr.Address;
import com.zy.entity.usr.Job;
import com.zy.entity.usr.User;
import com.zy.model.dto.AreaDto;
import com.zy.model.query.NoticeQueryModel;
import com.zy.service.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static com.zy.common.util.ValidateUtils.*;
import static com.zy.model.Constants.*;

@Component
public class CacheComponent {

	final Logger logger = LoggerFactory.getLogger(CacheComponent.class);

	@Autowired
	private CacheSupport cacheSupport;

	@Autowired
	private UserService userService;

	@Autowired
	private BankCardService bankCardService;

	@Autowired
	private JobService jobService;

	@Autowired
	private NoticeService noticeService;

	@Autowired
	private AreaService areaService;

	@Autowired
	private AddressService addressService;

	@Autowired
	private ActivityService activityService;
	
	private static final int DEFAULT_EXPIRE = 600;

	public User getUser(Long userId) {
		validate(userId, NOT_NULL, "cache key user id is null");
		User user = null;
		try {
			user = (User) cacheSupport.get(CACHE_NAME_USER, userId + "");
		} catch (Exception e) {
			logger.error("缓存异常", e);
		}
		if (user == null) {
			user = userService.findOne(userId);
			if (user != null) {
				try {
					cacheSupport.set(CACHE_NAME_USER, userId + "", user, DEFAULT_EXPIRE);
				} catch (Exception e) {
					logger.error("缓存异常", e);
				}
			}
		}
		return user;
	}

	public void setUser(User user) {
		validate(user, NOT_NULL, "cache user is null");
		validate(user.getId(), NOT_NULL, "cache key user id is null");
		try {
			cacheSupport.set(CACHE_NAME_USER, user.getId() + "", user, DEFAULT_EXPIRE);
		} catch (Exception e) {
			logger.error("缓存异常", e);
		}
	}

	public void deleteUser(Long userId) {
		validate(userId, NOT_NULL, "cache key user id is null");
		try {
			cacheSupport.delete(CACHE_NAME_USER, userId + "");
		} catch (Exception e) {
			logger.error("缓存异常", e);
		}
	}

	public List<Notice> getNotice(Notice.NoticeType[] noticeTypes) {
		validate(noticeTypes, NOT_EMPTY, "cache key notice types is null");
		List<Notice> notices = null;
		String key = Arrays.stream(noticeTypes).sorted().map(v -> String.valueOf(v.ordinal())).reduce("", (u, v) -> u + "-" + v);
		try {
			notices = cacheSupport.get(CACHE_NAME_NOTICE, key);
		} catch (Exception e) {
			logger.error("缓存异常", e);
		}
		if (notices == null || notices.isEmpty()) {
			NoticeQueryModel noticeQueryModel = new NoticeQueryModel();
			noticeQueryModel.setNoticeTypeIN(noticeTypes);
			notices = noticeService.findAll(noticeQueryModel);
			if (notices != null) {

				try {
					cacheSupport.set(CACHE_NAME_NOTICE, key, notices, DEFAULT_EXPIRE);
				} catch (Exception e) {
					logger.error("缓存异常", e);
				}
			}
		}
		return notices;
	}

	public AreaDto getAreaDto(Long areaId) {
		validate(areaId, NOT_NULL, "cache key areaId id is null");
		AreaDto areaDto = null;
		try {
			areaDto = (AreaDto) cacheSupport.get(CACHE_NAME_AREA, areaId + "");
		} catch (Exception e) {
			logger.error("缓存异常", e);
		}
		if (areaDto == null) {
			areaDto = areaService.findOneDto(areaId);
			if (areaDto != null) {
				try {
					cacheSupport.set(CACHE_NAME_AREA, areaId + "", areaDto, DEFAULT_EXPIRE);
				} catch (Exception e) {
					logger.error("缓存异常", e);
				}
			}
		}
		return areaDto;
	}

	public BankCard getBankCard(Long bankCardId) {
		validate(bankCardId, NOT_NULL, "cache key bank cardId id is null");
		BankCard bankCard = null;
		try {
			bankCard = (BankCard) cacheSupport.get(CACHE_NAME_BANK_CARD, bankCardId + "");
		} catch (Exception e) {
			logger.error("缓存异常", e);
		}
		if (bankCard == null) {
			bankCard = bankCardService.findOne(bankCardId);
			if (bankCard != null) {
				try {
					cacheSupport.set(CACHE_NAME_BANK_CARD, bankCardId + "", bankCard, DEFAULT_EXPIRE);
				} catch (Exception e) {
					logger.error("缓存异常", e);
				}
			}
		}
		return bankCard;
	}

	public List<Address> getAddress(Long userId) {
		validate(userId, NOT_EMPTY, "cache key user id is null");
		List<Address> addresses = null;
		try {
			addresses = cacheSupport.get(CACHE_NAME_ADDRESS, String.valueOf(userId));
		} catch (Exception e) {
			logger.error("缓存异常", e);
		}
		if (addresses == null || addresses.isEmpty()) {
			addresses = addressService.findByUserId(userId);
			if (addresses != null) {

				try {
					cacheSupport.set(CACHE_NAME_ADDRESS, String.valueOf(userId), addresses, DEFAULT_EXPIRE);
				} catch (Exception e) {
					logger.error("缓存异常", e);
				}
			}
		}
		return addresses;
	}

	public Job getJob(Long jobId) {
		validate(jobId, NOT_EMPTY, "cache key user id is null");
		Job job = null;
		try {
			job = cacheSupport.get(CACHE_NAME_JOB, String.valueOf(jobId));
		} catch (Exception e) {
			logger.error("缓存异常", e);
		}
		if (job == null) {
			job = jobService.findOne(jobId);
			if (job != null) {

				try {
					cacheSupport.set(CACHE_NAME_JOB, String.valueOf(jobId), job, DEFAULT_EXPIRE);
				} catch (Exception e) {
					logger.error("缓存异常", e);
				}
			}
		}
		return job;
	}

	public void deleteAddress(Long userId) {
		validate(userId, NOT_NULL, "cache key user id is null");
		try {
			cacheSupport.delete(CACHE_NAME_ADDRESS, String.valueOf(userId));
		} catch (Exception e) {
			logger.error("缓存异常", e);
		}
	}
	
	public Activity getActivity(Long activityId) {
		validate(activityId, NOT_NULL, "cache key activity id is null");
		Activity activity = null;
		try {
			activity = cacheSupport.get(CACHE_NAME_ACTIVITY, String.valueOf(activityId));
		} catch (Exception e) {
			logger.error("缓存异常", e);
		}
		if (activity == null) {
			activity = activityService.findOne(activityId);
			if (activity != null) {

				try {
					cacheSupport.set(CACHE_NAME_ACTIVITY, String.valueOf(activityId), activity, DEFAULT_EXPIRE);
				} catch (Exception e) {
					logger.error("缓存异常", e);
				}
			}
		}
		return activity;
	}
}
