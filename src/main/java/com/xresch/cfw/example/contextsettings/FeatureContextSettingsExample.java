package com.xresch.cfw.example.contextsettings;

import com.xresch.cfw._main.CFW;
import com.xresch.cfw._main.CFWAppFeature;
import com.xresch.cfw._main.CFWApplicationExecutor;
import com.xresch.cfw._main.CFWExtensionFeature;
import com.xresch.cfw.example._main.ExampleExtensionApplication;
import com.xresch.cfw.example.javascript.ServletJavascriptExamples;
import com.xresch.cfw.response.bootstrap.MenuItem;

/**************************************************************************************************************
 * 
 * @author Reto Scheiwiller, (c) Copyright 2020
 * @license MIT-License
 **************************************************************************************************************/
@CFWExtensionFeature
public class FeatureContextSettingsExample extends CFWAppFeature {
	
	public static final String PACKAGE_RESOURCE = "com.xresch.cfw.example.contextsettings.resources";
	
	@Override
	public void register() {
		
		//----------------------------------
		// Register Package
		CFW.Files.addAllowedPackage(PACKAGE_RESOURCE);
		
		//----------------------------------
		// Register Context Settings
		//IMPORTANT: Make sure to enable CFW.AppSettings.setEnableContextSettings(true) in the Application
		CFW.Registry.ContextSettings.register(ExampleEnvironment.SETTINGS_TYPE, ExampleEnvironment.class);
    
		//----------------------------------
		// Register Context Settings
		ExampleExtensionApplication.EXTENSION_MENU_ROOT.addChild(
				new MenuItem("Context Settings Chooser")
					.faicon("fas fa-cogs")
					.addPermission(ExampleExtensionApplication.PERMISSION_CFWSAMPLES)
					.href("/app/contextsettingschooser")
			);
	}

	@Override
	public void initializeDB() {
		ExampleEnvironmentManagement.initialize();
	}

	@Override
	public void addFeature(CFWApplicationExecutor app) {	
		app.addAppServlet(ServletContextSettingsChooser.class, "/contextsettingschooser");
	}

	@Override
	public void startTasks() {
		/* do nothing */
	}

	@Override
	public void stopFeature() {
		/* do nothing */
	}

}
