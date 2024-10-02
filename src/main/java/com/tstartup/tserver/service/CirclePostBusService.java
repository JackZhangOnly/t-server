package com.tstartup.tserver.service;

import com.tstartup.tserver.persistence.service.TCirclePostService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CirclePostBusService {
    @Resource
    private TCirclePostService circlePostService;
}
