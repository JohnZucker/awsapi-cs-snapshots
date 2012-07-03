package com.cloud.bridge.util;

public class HeaderParam {

	protected String name;
	protected String value;
	
	public HeaderParam() {
		name  = null;
		value = null;
	}
	
	public HeaderParam (String name, String value) {
         this.name = name;
         this.name = value;     
	}
	
	public void setName( String name ) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setValue( String value ) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}
