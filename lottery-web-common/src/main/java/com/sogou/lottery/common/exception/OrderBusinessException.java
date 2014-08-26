package com.sogou.lottery.common.exception;

public class OrderBusinessException extends BusinessException {
	
	private static final long serialVersionUID = -5310992155224201978L;
	
	public OrderBusinessException() {

		super();
	}
	
	public OrderBusinessException(String message, Throwable cause) {

		super(message, cause);
	}
	
	public OrderBusinessException(String message) {

		super(message);
	}
	
	public OrderBusinessException(Throwable cause) {

		super(cause);
	}
}
