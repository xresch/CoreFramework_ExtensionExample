package com.xresch.cfw.example.dashboarding;

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
public class FeatureDashboardingExample extends CFWAppFeature {
		
	public static final String RESOURCE_PACKAGE = "com.xresch.cfw.example.dashboarding.resources";

	@Override
	public void register() {
		//----------------------------------
		// Register Package
		CFW.Files.addAllowedPackage(RESOURCE_PACKAGE);

		
    	//----------------------------------
    	// Register Widgets
		CFW.Registry.Widgets.add(new ExampleWidgetHelloWorld());
		
		
    	//----------------------------------
    	// Register Parameters
		CFW.Registry.Parameters.add(new ParameterDefinitionExampleEnvironment());
		
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
