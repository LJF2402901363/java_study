package com.quark.rest.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quark.common.dao.LabelDao;
import com.quark.common.entity.Label;
import com.quark.rest.service.LabelService;
import org.springframework.stereotype.Service;

/**
 * @Author LHR
 * Create By 2017/8/27
 */
@Service
public class LabelServiceImpl extends ServiceImpl<LabelDao,Label> implements LabelService{
}
