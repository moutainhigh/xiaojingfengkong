package com.shangdao.phoenix.controller;

import com.shangdao.phoenix.enums.ExceptionResultEnum;
import com.shangdao.phoenix.service.SmsService;
import com.shangdao.phoenix.util.HTTPResponse;
import com.shangdao.phoenix.util.OutsideRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author duyiting
 * @date 2018/03/22
 */
@RestController
@RequestMapping(value = "/Sms")
public class SmsController {

    @Autowired
    SmsService smsService;

    @RequestMapping(value = "/verificationCode")
    public HTTPResponse registerSms(HttpServletRequest httpServletRequest, String username) {
        if (username == null) {
            throw new OutsideRuntimeException(ExceptionResultEnum.NOMOBILE);
        }
        smsService.sendSms(httpServletRequest.getRemoteAddr(), username);
        return new HTTPResponse();
    }



}
