package com.zy.component;

import com.zy.common.model.tree.TreeHelper;
import com.zy.common.model.tree.TreeNode;
import com.zy.common.util.BeanUtils;
import com.zy.entity.mal.Order;
import com.zy.entity.sys.SystemCode;
import com.zy.entity.usr.User;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.zy.vo.SystemCodeAdminVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class SystemCodeComponent {

	@Autowired
	private CacheComponent cacheComponent;

	public SystemCodeAdminVo buildAdminVo(SystemCode systemCode) {
		SystemCodeAdminVo systemCodeAdminVo = new SystemCodeAdminVo();
		BeanUtils.copyProperties(systemCode,systemCodeAdminVo);
		Long createId = systemCode.getCreateBy();
		Long updateId = systemCode.getUpdateBy();
		if (createId != null) {
			User user1 = cacheComponent.getUser(createId);
			systemCodeAdminVo.setCreateUser(VoHelper.buildUserAdminSimpleVo(user1));
		}
		if (updateId != null) {
			User user2 = cacheComponent.getUser(updateId);
			systemCodeAdminVo.setUpdateUser(VoHelper.buildUserAdminSimpleVo(user2));
		}
		systemCodeAdminVo.setCreateTimeLabel(GcUtils.formatDate(systemCode.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
		systemCodeAdminVo.setUpdateTimeLabel(GcUtils.formatDate(systemCode.getUpdateDate(), "yyyy-MM-dd HH:mm:ss"));
		return systemCodeAdminVo;
	}

	/**
	 * 计算量  小工具
	 * @param userId
	 * @return
     */
	public String sealUtil(Long userId,LocalCacheComponent localCacheComponent,String data) {
		List<User> userList = localCacheComponent.getUsers();
		Map<Long ,User> mapUser= userList.stream().collect(Collectors.toMap(User::getId,u->u));
		List<Order> orderList = localCacheComponent.getOrders();
		String [] datas= data.split("-");
		SimpleDateFormat f = new SimpleDateFormat("YYYY-MM");
		orderList =orderList.stream().filter(v->v.getPaidTime()!=null).filter(v->{
			String[] sDate = f.format(v.getPaidTime()).split("-");
			if (Integer.parseInt(datas[0])==Integer.parseInt(sDate[0])&&Integer.parseInt(datas[1])==Integer.parseInt(sDate[1])){
				return true;
			}
			return  false;
		}).collect(Collectors.toList());
		List<User> children = TreeHelper.sortBreadth2(userList, userId.toString(), v -> {
			TreeNode treeNode = new TreeNode();
			treeNode.setId(v.getId().toString());
			treeNode.setParentId(v.getParentId() == null ? null : v.getParentId().toString());
			return treeNode;
		});

        List <User>isDirectorList =children.stream().filter( v -> v.getIsDirector() != null && v.getIsDirector())
				.filter(v->v.getId()!=userId).collect(Collectors.toList());//过滤出下级的所有董事
		List<User>isDirectorListChildren = new ArrayList<User>();
		for (User user :isDirectorList){
          boolean flae= true;
			long pIsDirector=-1;
			User u1=user;
			do {
			   User user1 = mapUser.get(u1.getParentId());
				if(user1!=null&&user1.getIsDirector() != null && user1.getIsDirector()){
					pIsDirector=user1.getId();
					flae= false;
				} else if(user1==null) {
					flae= false;
				}
				u1=user1;
		  }while (flae);
			if (pIsDirector==userId){
				isDirectorListChildren.add(user);
			}
		}

		long mysum = this.sealNumber(userId,orderList,userList);

		//其他团队
		long sum = 0;

		for (User user :isDirectorListChildren){
			sum += this.sealNumber(user.getId(),orderList,userList);
		}


		return (mysum-sum)+"";


	}


	private Long sealNumber(Long userId,List<Order> orderList,List<User>userList){

		List<User> children = TreeHelper.sortBreadth2(userList, userId.toString(), v -> {
			TreeNode treeNode = new TreeNode();
			treeNode.setId(v.getId().toString());
			treeNode.setParentId(v.getParentId() == null ? null : v.getParentId().toString());
			return treeNode;
		});

		Predicate<User> predicate = v -> {
			User.UserRank userRank = v.getUserRank();
			if (userRank == User.UserRank.V4) {
				return true;
			}
			return false;
		};
		List<User> v4Users = children.stream().filter(predicate).collect(Collectors.toList());//把所有特级过来出来
		Map<Long, Long> userInQuantityMap = orderList.stream().filter(order -> order.getOrderStatus() == Order.OrderStatus.已支付
				|| order.getOrderStatus() == Order.OrderStatus.已发货
				|| order.getOrderStatus() == Order.OrderStatus.已完成)
				.filter(v -> v.getBuyerUserRank() == User.UserRank.V4||(v.getQuantity()==3600&&v.getSellerId()==1))
				.collect(Collectors.toMap(Order::getUserId, Order::getQuantity, (x, y) -> x + y));

		  long sum = userInQuantityMap.get(userId)==null?0:userInQuantityMap.get(userId);
		for (User user :v4Users){
			sum +=userInQuantityMap.get(user.getId())==null?0:userInQuantityMap.get(user.getId());
		}
		return sum;
	}
}
