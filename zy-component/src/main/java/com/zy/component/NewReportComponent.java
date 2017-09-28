package com.zy.component;

import com.zy.entity.sys.SystemCode;
import com.zy.entity.usr.User;
import com.zy.service.SystemCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by it001 on 2017-09-28.
 */
@Component
public class NewReportComponent {
    @Autowired
    private LocalCacheComponent localCacheComponent;

    @Autowired
    private SystemCodeService systemCodeService;

    /**
     * 处理查询大区数据
     * @param type
     * @return
     */
    public Map<String,Object> disposeTeam(String type) {
        //过滤出所有的V4成员 且分过大区的 有大区总裁的
        List<User> users = localCacheComponent.getUsers().stream().filter(user -> user.getUserRank()==User.UserRank.V4).filter(user -> user.getLargearea()!=null).collect(Collectors.toList());
        List<SystemCode> largeAreaTypes = systemCodeService.findByType("LargeAreaType");//查询说有大区
        List<User>presidentUserList =users.stream().filter(user -> user.getIsPresident()!=null).collect(Collectors.toList());//查询所有大区总裁

        return null;
    }

    //处理团队人数
    private List<Map<String,Integer>> disposeTeamNumber(String type,List<User> userList,List<SystemCode> largeAreaTypes,List<User>presidentUserList){
        List<Map<String,Integer>> resultList = new ArrayList<>();
        Map<Integer,List<User>> userMap = userList.stream().collect( Collectors.groupingBy(User::getLargearea));
        if ("0".equals(type)){//公司不需要 过滤大区总裁
            for (SystemCode systemCode :largeAreaTypes){
                Map<String,Integer> naemap= new HashMap<>();
                naemap.put(systemCode.getSystemName(),userMap.get(systemCode.getSystemValue())!=null?userMap.get(systemCode.getSystemValue()).size():0);
                resultList.add(naemap);
            }
          }else{//统计各个大区的分布
           List<User> areatUserList = userMap.get(type);//获取大区的所有人
            Map<Long,List<User>> areatMap = areatUserList.stream().collect(Collectors.groupingBy(User::getPresidentId));
            for(User user :presidentUserList){
                Map<String,Integer> naemap= new HashMap<>();
                naemap.put(user.getNickname(),areatMap.get(user.getId())!=null?userMap.get(user.getId()).size():0);
                resultList.add(naemap);
            }
         }
       return resultList;
    }

  //  private List

}
