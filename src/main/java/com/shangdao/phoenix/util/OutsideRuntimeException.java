package com.shangdao.phoenix.util;

import com.shangdao.phoenix.enums.ExceptionResultEnum;

public class OutsideRuntimeException extends RuntimeException {
	
	private int code;
	private Object error;
	
	public OutsideRuntimeException(int code,Object error){
		super(error.toString());
		this.error = error;
		this.code = code;
	}
	
	public OutsideRuntimeException(ExceptionResultEnum exceptionResultEnum){
	    this.code = exceptionResultEnum.getCode();
	    this.error = exceptionResultEnum.getMessage();
	}
	
	public OutsideRuntimeException(ExceptionResultEnum exceptionResultEnum,String message){
	    this.code = exceptionResultEnum.getCode();
	    this.error = exceptionResultEnum.getMessage() + message;
	}
	
	public OutsideRuntimeException(String message,ExceptionResultEnum exceptionResultEnum){
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
