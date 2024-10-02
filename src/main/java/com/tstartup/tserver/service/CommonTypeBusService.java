package com.tstartup.tserver.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tstartup.tserver.common.response.ApiResult;
import com.tstartup.tserver.persistence.dataobject.TCommonType;
import com.tstartup.tserver.persistence.service.TCommonTypeService;
import com.tstartup.tserver.util.DateUtil;
import com.tstartup.tserver.web.dto.CommonIdDto;
import com.tstartup.tserver.web.dto.CommonTypeItemDto;
import com.tstartup.tserver.web.dto.CommonTypeQryDto;
import com.tstartup.tserver.web.dto.CommonTypeUpdateDto;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName CommonTypeService
 * @Description
 * @Author zhang
 * @Date 2024/8/17 10:22
 * @Version 1.0
 */
@Service
public class CommonTypeBusService {

    @Resource
    private TCommonTypeService commonTypeService;


    public ApiResult update(CommonTypeUpdateDto updateDto) {
        Integer id = updateDto.getId();
        if (Objects.nonNull(id)) {
            TCommonType tCommonType = commonTypeService.getById(id);
            if (Objects.isNull(tCommonType)) {
                return ApiResult.newParamError();
            }
            tCommonType.setName(updateDto.getName());
            tCommonType.setDescription(updateDto.getDesc());
            tCommonType.setTypeIdentity(updateDto.getTypeIdentity());
            tCommonType.setUpdateTime(DateUtil.getNowSeconds());
            tCommonType.setIsHot(updateDto.getIsHot());

            commonTypeService.updateById(tCommonType);

            return ApiResult.newSuccess();
        } else {
            TCommonType tCommonType = new TCommonType();

            tCommonType.setName(updateDto.getName());
            tCommonType.setDescription(updateDto.getDesc());
            tCommonType.setTypeIdentity(updateDto.getTypeIdentity());
            tCommonType.setCreateTime(DateUtil.getNowSeconds());
            tCommonType.setIsHot(updateDto.getIsHot());

            commonTypeService.save(tCommonType);

            return ApiResult.newSuccess();
        }
    }

    public ApiResult delete(CommonIdDto idDto) {
        commonTypeService.removeById(idDto.getId());
        return ApiResult.newSuccess();
    }

    public ApiResult<List<CommonTypeItemDto>> list(CommonTypeQryDto typeQryDto) {
        String busIdentity = typeQryDto.getTypeIdentity();

        List<TCommonType> tCommonTypeList = commonTypeService.list(Wrappers.<TCommonType>lambdaQuery()
                .eq(TCommonType::getTypeIdentity, busIdentity)
                .orderByDesc(TCommonType::getCreateTime));
        if (!CollectionUtils.isEmpty(tCommonTypeList)) {
            List<CommonTypeItemDto> typeItemDtoList = tCommonTypeList.stream().map(tCommonType -> {
                CommonTypeItemDto typeItemDto = new CommonTypeItemDto();
                BeanUtils.copyProperties(tCommonType, typeItemDto);
                typeItemDto.setDesc(tCommonType.getDescription());

                return typeItemDto;
            }).collect(Collectors.toList());
            return ApiResult.newSuccess(typeItemDtoList);

        }


        return ApiResult.newSuccess();
    }
}
