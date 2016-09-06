package com.andframe.exception;

/**
 * ToastException 
 * 1.用于在预计之内会导致的异常而像用户提醒内容
 * 2.直接提醒用户可理解的Message不夹杂其他技术错误内容
 * 3.不会计入异常通知，ToastException 代表的是已知异常，或者可接受异常
 * @author 树朾
 */
@SuppressWarnings("unused")
public class AfToastException extends AfException{
	private static final long serialVersionUID = -4134318918697162517L;

	public AfToastException() {
		super();
	}

	public AfToastException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public AfToastException(String detailMessage) {
		super(detailMessage);
	}

	public AfToastException(Throwable throwable) {
		super(throwable);
	}

	
}
