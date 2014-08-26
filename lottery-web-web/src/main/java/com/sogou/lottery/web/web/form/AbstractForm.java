package com.sogou.lottery.web.web.form;

import java.io.Serializable;


public abstract class AbstractForm implements Serializable {
	
	private static final long serialVersionUID = -6093235712766201380L;
	
	public AbstractForm() {

		super();
	}
	
	public abstract boolean setFormData(String paraStr);
	
	
}
