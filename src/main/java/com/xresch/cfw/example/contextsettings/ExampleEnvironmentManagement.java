package com.xresch.cfw.example.contextsettings;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

import com.xresch.cfw._main.CFW;
import com.xresch.cfw.features.contextsettings.AbstractContextSettings;
import com.xresch.cfw.features.contextsettings.ContextSettings;
import com.xresch.cfw.features.contextsettings.ContextSettingsChangeListener;
import com.xresch.cfw.logging.CFWLog;
import com.xresch.cfw.response.bootstrap.AlertMessage.MessageType;


public class ExampleEnvironmentManagement {
	
	private static Logger logger = CFWLog.getLogger(ExampleEnvironmentManagement.class.getName());
	
	private static boolean isInitialized = false;

	// Contains ContextSettings id and the associated  example environment
	private static HashMap<Integer, ExampleEnvironment> environments = new HashMap<Integer, ExampleEnvironment>();
	
	private ExampleEnvironmentManagement() {
		// hide public constructor
	}
	/************************************************************************
	 * 
	 ************************************************************************/
	public static void initialize() {
	
		//-----------------------------------------
		// Add Listener to react to changes
		ContextSettingsChangeListener listener = 
				new ContextSettingsChangeListener(ExampleEnvironment.SETTINGS_TYPE) {
			
			@Override
			public void onChange(AbstractContextSettings setting, boolean isNew) {
				ExampleEnvironment env = (ExampleEnvironment)setting;
				ExampleEnvironmentManagement.createEnvironment(env);
			}
			
			@Override
			public void onDelete(AbstractContextSettings typeSettings) {
				environments.remove(typeSettings.getDefaultObject().id());
			}
		};
		
		CFW.DB.ContextSettings.addChangeListener(listener);
		
		createEnvironments();
		isInitialized = true;
		
		//-----------------------------------------
		// Create Testdata
		// Note: Normally you won't do that in code,
		// will only be created through UI

		
		CFW.DB.ContextSettings.oneTimeCreate(
				new ContextSettings()
					.name("DEV")
					.description("The development environment.")
					.type(ExampleEnvironment.SETTINGS_TYPE)
					.settings(new ExampleEnvironment()
									.host("localhost")
									.port(7007)
									.useHttps(false)
									.toJSON()	
							)
			);
		
		CFW.DB.ContextSettings.oneTimeCreate(
				new ContextSettings()
					.name("TEST")
					.description("The test environment.")
					.type(ExampleEnvironment.SETTINGS_TYPE)
					.settings(new ExampleEnvironment()
									.host("localhost")
									.port(8008)
									.useHttps(true)
									.toJSON()	
							)
			);
		
		CFW.DB.ContextSettings.oneTimeCreate(
				new ContextSettings()
					.name("PROD")
					.description("The productive environment.")
					.type(ExampleEnvironment.SETTINGS_TYPE)
					.settings(new ExampleEnvironment()
									.host("localhost")
									.port(9009)
									.useHttps(true)
									.toJSON()	
							)
			);
	}
	
	/************************************************************************
	 * 
	 ************************************************************************/
	private static void createEnvironments() {
		// Clear environments
		environments = new HashMap<Integer, ExampleEnvironment>();
		
		ArrayList<AbstractContextSettings> settingsArray = CFW.DB.ContextSettings.getContextSettingsForType(ExampleEnvironment.SETTINGS_TYPE);

		for(AbstractContextSettings settings : settingsArray) {
			ExampleEnvironment current = (ExampleEnvironment)settings;
			createEnvironment(current);
		}
	}
	
	/************************************************************************
	 * 
	 ************************************************************************/
	private static void createEnvironment(ExampleEnvironment environment) {

		environments.remove(environment.getDefaultObject().id());

		InetSocketAddress address = new InetSocketAddress(environment.host(), environment.port());
		if(address.isUnresolved()) {
			CFW.Context.Request.addAlertMessage(MessageType.ERROR, "The URL could not be resolved: "+environment.host()+":"+environment.port());
			return;
		};
		
		environments.put(environment.getDefaultObject().id(), environment);
		
	}
	
	/************************************************************************
	 * 
	 ************************************************************************/
	public static LinkedHashMap<Integer, String> getEnvironmentsAsSelectOptions() {
		if(!isInitialized) { initialize(); }
		LinkedHashMap<Integer,String> options = new LinkedHashMap<Integer,String>();
		
		for(ExampleEnvironment env : environments.values()) {
			options.put(env.getDefaultObject().id(), env.getDefaultObject().name());
		}
		
		return options;
	}
	
	/************************************************************************
	 * 
	 ************************************************************************/
	public static ExampleEnvironment getEnvironment(int id) {
		if(!isInitialized) { initialize(); }
		return environments.get(id);
	}
	
}
