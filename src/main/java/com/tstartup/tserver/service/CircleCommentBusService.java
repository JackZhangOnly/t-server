package com.tstartup.tserver.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import com.tstartup.tserver.common.response.ApiResponse;
import com.tstartup.tserver.persistence.dataobject.TCircleComment;
import com.tstartup.tserver.persistence.dataobject.TCirclePost;
import com.tstartup.tserver.persistence.dataobject.TUser;
import com.tstartup.tserver.persistence.service.TCircleCommentService;
import com.tstartup.tserver.persistence.service.TUserService;
import com.tstartup.tserver.util.DateUtil;
import com.tstartup.tserver.web.dto.circle.CircleCommentDto;
import com.tstartup.tserver.web.dto.circle.CirclePostDto;
import com.tstartup.tserver.web.dto.circle.CommentAddDto;
import com.tstartup.tserver.web.dto.circle.CommentQryDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CircleCommentBusService {
    @Resource
    private TCircleCommentService circleCommentService;

    @Resource
    private TUserService tUserService;


    public ApiResponse<List<CircleCommentDto>> list(CommentQryDto qryDto) {

        List<TCircleComment> circleCommentList = circleCommentService.list(Wrappers.<TCircleComment>lambdaQuery()
                .eq(TCircleComment::getPostId, qryDto.getPostId())
                .orderByDesc(TCircleComment::getCreateTime));

        List<Integer> uidList = circleCommentList.stream().map(TCircleComment::getUid).collect(Collectors.toList());
        Map<Integer, String> uidNameMap;
        if (!CollectionUtils.isEmpty(uidList)) {
            List<TUser> tUserList = tUserService.list(Wrappers.<TUser>lambdaQuery().in(TUser::getId, uidList));
            uidNameMap = tUserList.stream().collect(Collectors.toMap(TUser::getId, TUser::getNickName, (k1, k2) -> k1));
        } else {
            uidNameMap = Maps.newHashMap();
        }

        List<CircleCommentDto> commentDtoList = circleCommentList.stream().map(tCircleComment -> {
            Integer uid = tCircleComment.getUid();

            CircleCommentDto commentDto = new CircleCommentDto();
            BeanUtils.copyProperties(tCircleComment, commentDto);

            commentDto.setAuthor(uidNameMap.get(uid));

            return commentDto;
        }).collect(Collectors.toList());

        return ApiResponse.newSuccess(commentDtoList);
    }

    public ApiResponse add(Integer uid, CommentAddDto commentAddDto) {
        TCircleComment tCircleComment = new TCircleComment();

        BeanUtils.copyProperties(commentAddDto, tCircleComment);

        tCircleComment.setUid(uid);
        tCircleComment.setCreateTime(DateUtil.getNowSeconds());
        circleCommentService.save(tCircleComment);

        return ApiResponse.newSuccess();
    }
}
