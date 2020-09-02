package com.xresch.cfw.example.dashboardwidget;

import com.xresch.cfw._main.CFW;
import com.xresch.cfw._main.CFWAppFeature;
import com.xresch.cfw._main.CFWApplicationExecutor;
import com.xresch.cfw._main.CFWExtensionFeature;
import com.xresch.cfw.features.manual.ManualPage;

/**************************************************************************************************************
 * 
 * @author Reto Scheiwiller, (c) Copyright 2019 
 * @license MIT-License
 **************************************************************************************************************/
@CFWExtensionFeature
public class FeatureWidgetExample extends CFWAppFeature {
		
	public static final String RESOURCE_PACKAGE = "com.xresch.cfw.example.dashboardwidget.resources";

	
	public static final ManualPage ROOT_MANUAL_PAGE = CFW.Registry.Manual.addManualPage(null, new ManualPage("Dashboard").faicon("fas fa-tachometer-alt"));
	
	@Override
	public void register() {
		//----------------------------------
		// Register Package
		CFW.Files.addAllowedPackage(RESOURCE_PACKAGE);

		
    	//----------------------------------
    	// Register Widgets
		CFW.Registry.Widgets.add(new ExampleWidgetHelloWorld());
		
	}

	@Override
	public void initializeDB() {

	}

	@Override
	public void addFeature(CFWApplicationExecutor app) {	

	}

	@Override
	public void startTasks() {

	}

	@Override
	public void stopFeature() {
		// TODO Auto-generated method stub
		
	}
	
}
