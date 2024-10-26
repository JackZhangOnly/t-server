package com.tstartup.tserver.persistence.service.impl;

import com.tstartup.tserver.persistence.dataobject.TUser;
import com.tstartup.tserver.persistence.mapper.TUserMapper;
import com.tstartup.tserver.persistence.service.TUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Administrator
 * @since 2024-10-26
 */
@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser> implements TUserService {

}
