package com.tstartup.tserver.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import com.tstartup.tserver.common.response.ApiResponse;
import com.tstartup.tserver.persistence.dataobject.TCirclePost;
import com.tstartup.tserver.persistence.dataobject.TUser;
import com.tstartup.tserver.persistence.service.TCirclePostService;
import com.tstartup.tserver.persistence.service.TUserService;
import com.tstartup.tserver.web.dto.circle.CirclePostDto;
import com.tstartup.tserver.web.dto.circle.PostQryDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
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

        List<Integer> postUidList = postList.stream().map(TCirclePost::getUid).collect(Collectors.toList());
        Map<Integer, String> uidNameMap;
        if (!CollectionUtils.isEmpty(postUidList)) {
            List<TUser> tUserList = tUserService.list(Wrappers.<TUser>lambdaQuery().in(TUser::getId, postUidList));
            uidNameMap = tUserList.stream().collect(Collectors.toMap(TUser::getId, TUser::getNickName, (k1, k2) -> k1));
        } else {
            uidNameMap = Maps.newHashMap();
        }

        List<CirclePostDto> postDtoList = postList.stream().map(tCirclePost -> {
            Integer uid = tCirclePost.getUid();

            CirclePostDto circlePostDto = new CirclePostDto();
            BeanUtils.copyProperties(tCirclePost, circlePostDto);

            circlePostDto.setAuthor(uidNameMap.get(uid));

            return circlePostDto;
        }).collect(Collectors.toList());

        return ApiResponse.newSuccess(postDtoList);
    }
}
