package com.tstartup.tserver.persistence.service.impl;

import com.tstartup.tserver.persistence.dataobject.TCity;
import com.tstartup.tserver.persistence.mapper.TCityMapper;
import com.tstartup.tserver.persistence.service.TCityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Administrator
 * @since 2024-08-17
 */
@Service
public class TCityServiceImpl extends ServiceImpl<TCityMapper, TCity> implements TCityService {

}
