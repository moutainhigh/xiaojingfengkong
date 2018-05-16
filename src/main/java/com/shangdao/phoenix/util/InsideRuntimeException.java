package com.shangdao.phoenix.util;

import com.shangdao.phoenix.enums.ExceptionResultEnum;

public class InsideRuntimeException extends RuntimeException {
	
	private int code;
	private Object error;
	
	public InsideRuntimeException(int code,Object error){
		super(error.toString());
		this.code = code;
		this.error = error;
	}
	public InsideRuntimeException(Object error){
		super(error.toString());
		this.code = 500;
		this.error = error;
	}
	
	public InsideRuntimeException(ExceptionResultEnum exceptionResultEnum){
	    this.code = exceptionResultEnum.getCode();
	    this.error = exceptionResultEnum.getMessage();
	}
	
	public InsideRuntimeException(ExceptionResultEnum exceptionResultEnum,String message){
	    this.code = exceptionResultEnum.getCode();
	    this.error = exceptionResultEnum.getMessage() + message;
	}
	
	public InsideRuntimeException(String message , ExceptionResultEnum exceptionResultEnum){
	    this.code = exceptionResultEnum.getCode();
	    this.error = message + exceptionResultEnum.getMessage();
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public Object getError() {
		return error;
	}
	public void setError(Object error) {
		this.error = error;
	}


}
