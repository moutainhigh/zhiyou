package com.zy.service.impl;

import com.zy.common.model.query.Page;
import com.zy.entity.cms.Matter;
import com.zy.entity.cms.MatterCollect;
import com.zy.entity.usr.User;
import com.zy.mapper.MatterCollectMapper;
import com.zy.mapper.MatterMapper;
import com.zy.model.query.MatterQueryModel;
import com.zy.service.MatterService;
import com.zy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
@Validated
public class MatterServiceImpl implements MatterService {

	@Autowired
	private MatterMapper matterMapper;

	@Autowired
	private MatterCollectMapper matterCollectMapper;

	@Autowired
	private UserService userService;

	/**
	 * 资源列表查询
	 * @param matterQueryModel
	 * @return
     */
	@Override
	public Page<Matter> findPage(@NotNull MatterQueryModel matterQueryModel) {
		if(matterQueryModel.getPageNumber() == null)
			matterQueryModel.setPageNumber(0);
		if(matterQueryModel.getPageSize() == null)
			matterQueryModel.setPageSize(20);
		long total = matterMapper.count(matterQueryModel);
		List<Matter> data = matterMapper.findAll(matterQueryModel);
		for (Matter matter:data) {
			User user = userService.findOne(matter.getUploadUserId());
			matter.setAuthor(user.getNickname());
		}
		Page<Matter> page = new Page<>();
		page.setPageNumber(matterQueryModel.getPageNumber());
		page.setPageSize(matterQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	/**
	 *
	 * @param matterQueryModel
	 * @return
     */
	@Override
	public Page<Matter> mobileFindPage(@NotNull MatterQueryModel matterQueryModel) {
		if(matterQueryModel.getPageNumber() == null)
			matterQueryModel.setPageNumber(0);
		if(matterQueryModel.getPageSize() == null)
			matterQueryModel.setPageSize(20);
		long total = matterMapper.mobileCount(matterQueryModel);
		List<Matter> data = matterMapper.mobileFindAll(matterQueryModel);
		for (Matter matter:data) {
			User user = userService.findOne(matter.getUploadUserId());
			matter.setAuthor(user.getNickname());
		}
		Page<Matter> page = new Page<>();
		page.setPageNumber(matterQueryModel.getPageNumber());
		page.setPageSize(matterQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	/**
	 * 查询资源
	 * @param
	 * @return
     */
	@Override
	public Matter findOne(@NotNull MatterQueryModel matterQueryModel) {
		Matter matter = matterMapper.findOne(matterQueryModel.getId());
		//查看我是否关注
		MatterCollect matterCollect = matterCollectMapper.queryMatterCollect(matterQueryModel);
		if(matterCollect == null){
			matter.setIsCollected(false);
		}else{
			matter.setIsCollected(true);
		}
		return matter;
	}

	/**
	 * 更改资源状态
	 * @param id
	 * @param status
     */
	@Override
	public void updateStatus(@NotNull Long id,@NotNull Integer status) {
		Matter newMatter = new Matter();
		newMatter.setId(id);
		newMatter.setStatus(status);

		matterMapper.update(newMatter);
	}

	/**
	 * 修改资源
	 * @param matter
     */
	@Override
	public void updateMatter(Matter matter) {
		matterMapper.updateMatter(matter);
	}

	/**
	 * 新增资源
	 * @param matter
     */
	@Override
	public void create(@NotNull Matter matter) {
		matterMapper.insert(matter);
	}

	/**
	 * 点击量
	 * @param id
	 */
	@Override
	public void click(@NotNull Long id) {
		matterMapper.click(id);
	}

	/**
	 * 关注
	 * @param id
	 * @param userId
     */
	@Override
	public void collect(@NotNull Long id,@NotNull Long userId) {
		MatterQueryModel matterQueryModel = new MatterQueryModel();
		matterQueryModel.setId(id);
		matterQueryModel.setUserId(userId);
		matterCollectMapper.insert(matterQueryModel);

		matterMapper.collect(id);
	}

	/**
	 * 取消关注
	 * @param id
	 * @param userId
     */
	@Override
	public void uncollect(@NotNull Long id,@NotNull Long userId) {
		MatterQueryModel matterQueryModel = new MatterQueryModel();
		matterQueryModel.setId(id);
		matterQueryModel.setUserId(userId);
		matterCollectMapper.delete(matterQueryModel);

		//关注记录-1
		matterMapper.updateNum(id);
	}
}
