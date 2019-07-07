package com.scfs.common.exception;

import com.scfs.common.consts.ExcMsgEnum;
import org.slf4j.helpers.MessageFormatter;

/**
 * 
 * @author Administrator
 *
 */
public class BaseException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5505548905336543093L;

	public final static ThreadLocal<String> msgContext = new ThreadLocal<String>();

    public BaseException(ExcMsgEnum msgEnum,Object... args){
        super();
        String formattedMessage = MessageFormatter.arrayFormat(msgEnum.getMsg(), args).getMessage();
        msgContext.set(formattedMessage);
    }

	public BaseException(Throwable cause) {
		super(cause);
	}
	
	public BaseException(ExcMsgEnum msgEnum, Throwable cause,Object... args) {
		super(cause);
        String formattedMessage = MessageFormatter.arrayFormat(msgEnum.getMsg(), args).getMessage();
        msgContext.set(formattedMessage);
	}
	
    public String getMsg() {
    	if (null != msgContext.get()) {
        	return msgContext.get();
    	} else {
    		return "系统异常，请稍后重试";
    	}
    }
}
