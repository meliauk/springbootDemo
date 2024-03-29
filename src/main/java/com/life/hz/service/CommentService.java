package com.life.hz.service;

import com.life.hz.dto.CommentDTO;
import com.life.hz.enums.CommentTypeEnum;
import com.life.hz.enums.NOtificationStatusEnum;
import com.life.hz.enums.NOtificationTypeEnum;
import com.life.hz.exception.CustomizeException;
import com.life.hz.exception.CustomizeExceptionCode;
import com.life.hz.mapper.*;
import com.life.hz.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private NotificationMapper notificationMapper;


//  事务  错了就不执行这个方法..
    @Transactional
    public void insert(Comment comment, User commentator){

        if(comment.getParentId() == null || comment.getParentId() == 0){
            throw new CustomizeException(CustomizeExceptionCode.TARGET_PARAM_NOT_FOUND);
        }
        if(comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeExceptionCode.TYPE_PARAM_WORNG);
        }
        if(comment.getType() == CommentTypeEnum.COMMENT.getType() ){
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if(dbComment == null ){
                throw new CustomizeException(CustomizeExceptionCode.COMMENT_NOT_FOUND);
            }

            //回复问题
            Question question = questionMapper.selectByPrimaryKey(dbComment.getParentId());
            if(question == null ){
                throw new CustomizeException(CustomizeExceptionCode.QUESTION_NOT_FOUND);
            }

            commentMapper.insert(comment);
            //增加评论数
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComment);
//          创建通知
            createNotiy(comment, dbComment.getCommentator(), commentator.getName(), question.getTitle(), NOtificationTypeEnum.REPLY_COMMENT,question.getId());

        }else {
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if(question == null ){
                throw new CustomizeException(CustomizeExceptionCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
            //创建通知
            createNotiy(comment, question.getCreator(), commentator.getName(),question.getTitle(),NOtificationTypeEnum.REPLY_QUESTION,question.getId());
        }
    }

    private void createNotiy(Comment comment, Long commentator, String notifierName, String outerTitle, NOtificationTypeEnum nOtificationTypeEnum,Long outerId) {
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(nOtificationTypeEnum.getType());
//          设置通知 的 评论问题
        notification.setOuterid(outerId);
//          xxx  回复了 什么什么问题
//          通知人 xxx
        notification.setNotifier(comment.getCommentator());
        notification.setStatus(NOtificationStatusEnum.UNREAD.getStatus());
//          接收人 登录的用户
        notification.setReceive(commentator);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }

    public List<CommentDTO> ListByTargetId(Long id, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andParentIdEqualTo(id).andTypeEqualTo(type.getType());
//      setOrderByClause 传递2个值 第二个值为排序顺序 （ASC,DESC）  空格 隔开
        commentExample.setOrderByClause("gmt_modified desc");
        List<Comment> commentList = commentMapper.selectByExample(commentExample);

        if(commentList.size() == 0 ){
            return new ArrayList<>();
        }
//      获取去掉重复的评论人
        Set<Long> commentCtator = commentList.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList<>();
        userIds.addAll(commentCtator);

//      获取评论人 并转换未 Map
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

//      转换 comment 为 commentDTO
        List<CommentDTO> commentDTOS = commentList.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOS;
    }
}
