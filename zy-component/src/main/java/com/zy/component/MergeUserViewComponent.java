package com.zy.component;

import com.zy.common.model.query.Page;
import com.zy.common.util.BeanUtils;
import com.zy.common.util.DateUtil;
import com.zy.entity.mergeusr.MergeUserView;
import com.zy.entity.sys.SystemCode;
import com.zy.model.dto.UserDto;
import com.zy.model.query.MergeUserViewQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.service.MergeUserViewService;
import com.zy.service.SystemCodeService;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.zy.vo.MergeUserViewVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

@Component
public class MergeUserViewComponent {

	@Autowired
	private CacheComponent cacheComponent;

	@Autowired
	private MergeUserViewService mergeUserViewService;

	@Autowired
	private SystemCodeService systemCodeService;

	public MergeUserViewVo buildVo(MergeUserView mergeUserView) {
		MergeUserViewVo mergeUserViewVo = new MergeUserViewVo();
		BeanUtils.copyProperties(mergeUserView, mergeUserViewVo);
		mergeUserViewVo.setAvatarThumbnail(GcUtils.getThumbnail(mergeUserView.getAvatar()));
//		if (mergeUserView.getInviterId() != null) {
//			mergeUserViewVo.setInviter(VoHelper.buildMergeUserViewSimpleVo(mergeUserViewService.findOne(mergeUserView.getInviterId()));
//		}
		if(mergeUserView.getParentId() != null) {
			mergeUserViewVo.setParent(VoHelper.buildMergeUserViewSimpleVo(mergeUserViewService.findOne(mergeUserView.getParentId())));
		}
		mergeUserViewVo.setUserRankLabel(GcUtils.getUserRankLabel(mergeUserView.getUserRank()));
//		Long bossId = mergeUserView.getBossId();
//		if (bossId != null) {
//			mergeUserViewVo.setBoss(VoHelper.buildMergeUserViewSimpleVo(mergeUserViewService.findOne(mergeUserView.getBossId())));
//		}
		Long presidentId = mergeUserView.getPresidentId();
		if(presidentId != null){
			mergeUserViewVo.setPresident(VoHelper.buildMergeUserViewSimpleVo(mergeUserViewService.findOne(mergeUserView.getParentId())));
		}
		if(mergeUserView.getV4Id() != null){
			mergeUserViewVo.setV4Parent(VoHelper.buildMergeUserViewSimpleVo(mergeUserViewService.findOne(mergeUserView.getV4Id())));
		}
		Integer largeArea = mergeUserView.getLargearea();
		if(largeArea != null){
			SystemCode largeAreaType = systemCodeService.findByTypeAndValue("LargeAreaType", largeArea+"");
			if(largeAreaType == null){
				mergeUserViewVo.setLargeareaLabel("");
			}else {
				mergeUserViewVo.setLargeareaLabel(largeAreaType.getSystemName());
			}
		}
		return mergeUserViewVo;
	}

	/**
	 * 计算 活动占比
	 * @param userId
	 * @return
	 */
	public String activeProportion(Long userId) {
		MergeUserViewQueryModel mergeUserViewQueryModel = new MergeUserViewQueryModel();
		mergeUserViewQueryModel.setRegisterTimeLT(DateUtil.getBeforeMonthEnd(new Date(),1,0));
		mergeUserViewQueryModel.setRegisterTimeGTE(DateUtil.getBeforeMonthBegin(new Date(),-2,0));
		long total = mergeUserViewService.countByActive(mergeUserViewQueryModel);
		mergeUserViewQueryModel.setParentIdNL(userId);
		long mytotal = mergeUserViewService.countByActive(mergeUserViewQueryModel);
		DecimalFormat df = new DecimalFormat("###0.00");
		if (total==0){
			return "0.00";
		}else{
			return df.format((double)mytotal/(double)total*100);
		}
	}

	/**
	 * 查询  所有 用户 信息
	 * @param userQueryModel
	 * @return
	 */
	public Page<UserDto> findUserAll(UserQueryModel userQueryModel) {
		List<UserDto> userList = mergeUserViewService.findUserAll(userQueryModel);
		long total = mergeUserViewService.countUserAll(userQueryModel);
		Page<UserDto> page = new Page<>();
		page.setPageNumber(userQueryModel.getPageNumber());
		page.setPageSize(userQueryModel.getPageSize());
		page.setData(userList);
		page.setTotal(total);
		return page;
	}

}
