package com.quark.rest.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quark.common.dao.PostsDao;
import com.quark.common.dao.ReplyDao;
import com.quark.common.entity.Notification;
import com.quark.common.entity.Posts;
import com.quark.common.entity.Reply;
import com.quark.common.entity.User;
import com.quark.common.exception.ServiceProcessException;
import com.quark.rest.service.NotificationService;
import com.quark.rest.service.ReplyService;
import com.quark.rest.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;

/**
 * @Author LHR
 * Create By 2017/8/29
 */
@Service
public class ReplyServiceImpl extends ServiceImpl<ReplyDao, Reply> implements ReplyService {

    @Autowired
    private PostsDao postsDao;
    @Autowired
    private ReplyDao replyDao;
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private WebSocketService webSocketService;

    @Override
    public Page<Reply> getReplyByPage(Integer postsId, int pageNo, int length) {
        Page<Reply> page = new Page<>(pageNo, length);
        page = replyDao.findByPageById(page,postsId);
        return page;
    }


    @Transactional
    @Override
    public void saveReply(Reply reply, Integer postsId, User user) {
        try {
            Posts posts = postsDao.selectById(postsId);

            if (posts == null) throw new ServiceProcessException("帖子不存在!");

            //帖子回复数+1
            int count = posts.getReplyCount();
            posts.setReplyCount(++count);
            postsDao.insert(posts);

            //添加回复
            reply.setInit_time(new Date());
            reply.setUser(user);
            reply.setPosts(posts);
            replyDao.insert(reply);

            //判断是否是自问自回，如果是则不通知
            if (posts.getUser().getId()!=user.getId()) {
                //向消息表中增加信息
                Notification notification = new Notification();
                notification.setPosts(posts);
                notification.setFromuser(user);
                notification.setTouser(posts.getUser());
                notification.setInit_time(new Date());
                notificationService.save(notification);
                // 使用WebSocket进行1对1通知
                webSocketService.sendToOne(posts.getUser().getId());
            }
        } catch (ServiceProcessException e) {
            throw e;
        } catch (Exception e) {
            // 所有编译期异常转换为运行期异常
            throw new ServiceProcessException("发布回复失败!");
        }
    }
}
