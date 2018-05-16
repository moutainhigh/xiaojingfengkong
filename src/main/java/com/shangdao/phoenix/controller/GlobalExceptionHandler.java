package com.shangdao.phoenix.controller;

import com.shangdao.phoenix.util.HTTPResponse;
import com.shangdao.phoenix.util.InsideRuntimeException;
import com.shangdao.phoenix.util.OutsideRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;


@ControllerAdvice
public class GlobalExceptionHandler {

	private final static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(java.lang.RuntimeException.class)
	@ResponseBody
	public HTTPResponse handle(java.lang.RuntimeException ex) {
		ex.printStackTrace();
		if (ex instanceof OutsideRuntimeException) {
			if(((OutsideRuntimeException)ex).getCode() == 1000 ){
				return new HTTPResponse(((OutsideRuntimeException)ex).getCode(), "格式错误",((OutsideRuntimeException)ex).getError());
			}else{
				return new HTTPResponse(((OutsideRuntimeException)ex).getCode(), "外部错误",((OutsideRuntimeException)ex).getError());
			}
		}else{
			if (!(ex instanceof InsideRuntimeException)) {
				logger.warn(ex.getMessage());
			}
			return new HTTPResponse(500, "内部错误",Collections.EMPTY_SET);
		}
	}
}