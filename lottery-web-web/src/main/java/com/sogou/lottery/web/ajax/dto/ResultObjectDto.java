package com.sogou.lottery.web.ajax.dto;

public class ResultObjectDto<T> extends ResultBaseDto<T> {
	
	private static final long serialVersionUID = 3674776343700493683L;
	
	private T object;
	
	public ResultObjectDto() {
	
	}
	
	public ResultObjectDto(int retcode, String retdesc) {
	
		super(retcode, retdesc);
	}
	
	public ResultObjectDto(int retcode, String retdesc, T object) {
	
		super(retcode, retdesc);
		this.object = object;
	}
	
	public ResultObjectDto(T object) {
	
		super();
		this.object = object;
	}
	
	public T getObject() {
	
		return object;
	}
	
	public void setObject(T object) {
	
		this.object = object;
	}
	
}
