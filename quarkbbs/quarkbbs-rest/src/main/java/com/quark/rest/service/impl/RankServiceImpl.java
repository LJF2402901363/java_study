package com.quark.rest.service.impl;

import com.quark.common.dao.PostsDao;
import com.quark.common.dao.UserDao;
import com.quark.common.entity.Posts;
import com.quark.common.entity.User;
import com.quark.common.utils.DateUtil;
import com.quark.rest.service.RankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author LHR
 * Create By 2017/8/31
 */
@Service
public class RankServiceImpl  implements RankService{
    @Autowired
    private PostsDao postsDao;

    @Autowired
    private UserDao userDao;

    @Override
    public List<Object> findPostsRank() {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusDays(30L);
        String endTimeStr = DateUtil.dateToString(endTime);
        String startTimeStr = DateUtil.dateToString(startTime);
        List<User> userList = userDao.findNewUser(startTimeStr,endTimeStr);
        List<Posts> hot = postsDao.findHot(startTimeStr,endTimeStr);
        List<Object> list = new ArrayList<>();
        if (Objects.isNull(hot) || hot.size() == 0){
            return list;
        }

        hot.forEach(e->{
            list.add(e);
        });
        return list;
    }

    @Override
    public List<Object> findUserRank() {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusDays(30L);
        String endTimeStr = DateUtil.dateToString(endTime);
        String startTimeStr = DateUtil.dateToString(startTime);
        List<User> userList = userDao.findNewUser(startTimeStr,endTimeStr);
        List<Object> list = new ArrayList<>();
        if (Objects.isNull(userList) || userList.size() == 0){
            return list;
        }

        userList.forEach(e->{
            list.add(e);
        });
        return list;
    }
}
