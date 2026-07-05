package org.cognizant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Country {
	private static final Logger logger=LoggerFactory.getLogger(Country.class);
	
	private String name;
	private String code;
	
	public String getName() {
		logger.debug("inside the getName()");
		return name;
	}

	public void setName(String name) {
		logger.debug("inside the setName()");
		this.name = name;
	}

	public String getCode() {
		logger.debug("inside the getcode()");
		return code;
	}

	public void setCode(String code) {
		logger.debug("inside the setCode()");
		this.code = code;
	}

	@Override
	public String toString() {
		return "Country [name=" + name + ", code=" + code + "]";
	}

	public Country() {
		logger.debug("inside  country constructor");
	}

}
