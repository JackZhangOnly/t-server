package com.tstartup.tserver.service;

import com.tstartup.tserver.persistence.service.TCircleCommentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CircleCommentBusService {
    @Resource
    private TCircleCommentService circleCommentService;
}
