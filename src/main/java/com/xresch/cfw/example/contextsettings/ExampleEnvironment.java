package com.xresch.cfw.example.contextsettings;

import com.xresch.cfw.datahandling.CFWField;
import com.xresch.cfw.datahandling.CFWField.FormFieldType;
import com.xresch.cfw.features.contextsettings.AbstractContextSettings;

/**************************************************************************************************************
 * 
 * @author Reto Scheiwiller, (c) Copyright 2019 
 * @license MIT-License
 **************************************************************************************************************/
public class ExampleEnvironment extends AbstractContextSettings {
	
	public static final String SETTINGS_TYPE = "Example Environment";
	private String apiURL = null;
	
	public enum PrometheusEnvironmentFields{
		HOST,
		PORT,
		USE_HTTPS
	}
			
	private CFWField<String> host = CFWField.newString(FormFieldType.TEXT, PrometheusEnvironmentFields.HOST)
			.setDescription("The hostname of the this example instance.");
	
	private CFWField<Integer> port = CFWField.newInteger(FormFieldType.NUMBER, PrometheusEnvironmentFields.PORT)
			.setDescription("The port for this example environment.");
	
	private CFWField<Boolean> useHttps = CFWField.newBoolean(FormFieldType.BOOLEAN, PrometheusEnvironmentFields.USE_HTTPS)
			.setDescription("Use HTTPS for this example environment.");
	
	public ExampleEnvironment() {
		initializeFields();
	}
		
	private void initializeFields() {
		this.addFields(host, port, useHttps);
	}
		
		
	@Override
	public boolean isDeletable(int settingsID) {
		// add your check here if required
		return true;
	}
	
	public boolean isDefined() {
		if(host.getValue() != null
		&& port.getValue() != null) {
			return true;
		}
		
		return false;
	}
	
	
	public String getExampleUrl() {
		
		if(apiURL == null) {
			StringBuilder builder = new StringBuilder();
			
			if(useHttps.getValue()) {
				builder.append("https://");
			}else {
				builder.append("http://");
			}
			builder.append(host.getValue())
				.append(":")
				.append(port.getValue())
				.append("/api/v1");
			
			apiURL = builder.toString();
		}
		
		return apiURL;
	}
	
	public String host() {
		return host.getValue();
	}
	
	public ExampleEnvironment host(String value) {
		this.host.setValue(value);
		return this;
	}
		
	public int port() {
		return port.getValue();
	}
	
	public ExampleEnvironment port(int value) {
		this.port.setValue(value);
		return this;
	}
	
	public boolean useHttps() {
		return useHttps.getValue();
	}
	
	public ExampleEnvironment useHttps(boolean value) {
		this.useHttps.setValue(value);
		return this;
	}
		
}
