package com.xresch.cfw.example.main;

import com.xresch.cfw._main.CFW;
import com.xresch.cfw._main.CFWAppInterface;
import com.xresch.cfw._main.CFWApplicationExecutor;
import com.xresch.cfw._main.CFWExtensionApplication;
import com.xresch.cfw.features.usermgmt.Permission;
import com.xresch.cfw.response.bootstrap.MenuItem;

/**********************************************************************************
 * Using @CFWExtensionApplication marks this class as the application that should
 * be loaded on startup. 
 * @author Reto Scheiwiller, (c) Copyright 2019 
 *
 **********************************************************************************/
@CFWExtensionApplication
public class ExampleExtensionApplication implements CFWAppInterface {

	public static final String RESOURCE_PACKAGE = "com.xresch.cfw.example.main.resources";
	
	public static final String PERMISSION_CFWSAMPLES = "CFW Extension Samples";
	
	public static final MenuItem EXTENSION_MENU_ROOT = new MenuItem("Extension Examples")
			.faicon("fas fa-flask")
			.addPermission(ExampleExtensionApplication.PERMISSION_CFWSAMPLES);
	@Override
	public void settings() {
		CFW.AppSettings.setEnableDashboarding(true);
	}

	@Override
	public void register() {
		//----------------------------------
		// Register Package
		CFW.Files.addAllowedPackage(RESOURCE_PACKAGE);
		
		//----------------------------------
		// Register Features
		CFW.Registry.Features.addFeature(FeatureRegular.class);
		
		//----------------------------------
		// Register Menu Entry
		CFW.Registry.Components.addRegularMenuItem(EXTENSION_MENU_ROOT, null);
	}

	@Override
	public void initializeDB() {
		
		//-----------------------------------------
		// 
		//-----------------------------------------
		CFW.DB.Permissions.oneTimeCreate(
				new Permission(PERMISSION_CFWSAMPLES, "user")
					.description("View and use the CFW Samples."),
				true,
				true);
									
	}

	@Override
	public void startApp(CFWApplicationExecutor executor) {
		 executor.setDefaultURL("/dashboard/list", true);
	     
	}

	@Override
	public void startTasks() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopApp() {
		// TODO Auto-generated method stub

	}

}
