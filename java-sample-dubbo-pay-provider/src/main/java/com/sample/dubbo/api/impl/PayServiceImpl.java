package com.sample.dubbo.api.impl;

import com.sample.dubbo.api.PayService;

/**
 * Created by maozy on 2018/5/20.
 */
public class PayServiceImpl implements PayService {
    @Override
    public String doPay(String params) {
        return "Hello Dubbo ->" + params;
    }
}
