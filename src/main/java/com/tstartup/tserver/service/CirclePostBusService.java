package com.tstartup.tserver.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.api.client.util.Strings;
import com.google.common.collect.Maps;
import com.tstartup.tserver.common.response.ApiResponse;
import com.tstartup.tserver.persistence.dataobject.TCirclePost;
import com.tstartup.tserver.persistence.dataobject.TUser;
import com.tstartup.tserver.persistence.service.TCirclePostService;
import com.tstartup.tserver.persistence.service.TUserService;
import com.tstartup.tserver.util.DateUtil;
import com.tstartup.tserver.web.dto.circle.CirclePostDto;
import com.tstartup.tserver.web.dto.circle.PostAddDto;
import com.tstartup.tserver.web.dto.circle.PostListRecentlyQry;
import com.tstartup.tserver.web.dto.circle.PostQryDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CirclePostBusService {
    @Resource
    private TCirclePostService circlePostService;

    @Resource
    private TUserService tUserService;



    public ApiResponse<List<CirclePostDto>> list(PostQryDto qryDto) {

        List<TCirclePost> postList = circlePostService.list(Wrappers.<TCirclePost>lambdaQuery()
                .eq(TCirclePost::getCircleId, qryDto.getCircleId())
                .orderByDesc(TCirclePost::getCreateTime));

        List<Integer> postUidList = postList.stream().map(TCirclePost::getUid).distinct().collect(Collectors.toList());
        Map<Integer, TUser> userMap;
        if (!CollectionUtils.isEmpty(postUidList)) {
            List<TUser> tUserList = tUserService.list(Wrappers.<TUser>lambdaQuery().in(TUser::getId, postUidList));
            userMap = tUserList.stream().collect(Collectors.toMap(TUser::getId, Function.identity(), (k1, k2) -> k1));
        } else {
            userMap = Maps.newHashMap();
        }

        List<CirclePostDto> postDtoList = postList.stream().map(tCirclePost -> {
            Integer uid = tCirclePost.getUid();

            CirclePostDto circlePostDto = new CirclePostDto();
            BeanUtils.copyProperties(tCirclePost, circlePostDto);

            TUser tUser = userMap.get(uid);
            if (Objects.nonNull(tUser)) {
                circlePostDto.setAuthor(Strings.isNullOrEmpty(tUser.getNickName()) ? tUser.getUsername() : tUser.getNickName());
            }

            return circlePostDto;
        }).collect(Collectors.toList());

        return ApiResponse.newSuccess(postDtoList);
    }

    public ApiResponse<List<CirclePostDto>> listRecently(PostListRecentlyQry qryDto) {
        int size = Objects.nonNull(qryDto) && qryDto.getSize() > 0 ? qryDto.getSize() : 10;
        List<TCirclePost> postList = circlePostService.list(Wrappers.<TCirclePost>lambdaQuery()
                .orderByDesc(TCirclePost::getId).last("limit " + size));

        List<Integer> postUidList = postList.stream().map(TCirclePost::getUid).collect(Collectors.toList());
        Map<Integer, TUser> userMap;
        if (!CollectionUtils.isEmpty(postUidList)) {
            List<TUser> tUserList = tUserService.list(Wrappers.<TUser>lambdaQuery().in(TUser::getId, postUidList));
            userMap = tUserList.stream().collect(Collectors.toMap(TUser::getId, Function.identity(), (k1, k2) -> k1));
        } else {
            userMap = Maps.newHashMap();
        }

        List<CirclePostDto> postDtoList = postList.stream().map(tCirclePost -> {
            Integer uid = tCirclePost.getUid();

            CirclePostDto circlePostDto = new CirclePostDto();
            BeanUtils.copyProperties(tCirclePost, circlePostDto);

            TUser tUser = userMap.get(uid);
            if (Objects.nonNull(tUser)) {
                circlePostDto.setAuthor(Strings.isNullOrEmpty(tUser.getNickName()) ? tUser.getUsername() : tUser.getNickName());
            }
            return circlePostDto;
        }).collect(Collectors.toList());

        return ApiResponse.newSuccess(postDtoList);
    }

    public ApiResponse add(Integer uid, PostAddDto postAddDto) {
        TCirclePost circlePost = new TCirclePost();
        BeanUtils.copyProperties(postAddDto, circlePost);

        circlePost.setUid(uid);
        circlePost.setCreateTime(DateUtil.getNowSeconds());

        circlePostService.save(circlePost);

        return ApiResponse.newSuccess();
    }
}
