package com.xresch.cfw.example.dashboarding;

import java.util.Locale;

import com.xresch.cfw._main.CFW;
import com.xresch.cfw._main.CFWApplicationExecutor;
import com.xresch.cfw.caching.FileDefinition;
import com.xresch.cfw.caching.FileDefinition.HandlingType;
import com.xresch.cfw.features.dashboard.FeatureDashboard;
import com.xresch.cfw.features.manual.FeatureManual;
import com.xresch.cfw.spi.CFWAppFeature;

/**************************************************************************************************************
 * 
 * @author Reto Scheiwiller, (c) Copyright 2019 
 * @license MIT-License
 **************************************************************************************************************/
public class FeatureDashboardingExample extends CFWAppFeature {
		
	public static final String PACKAGE_RESOURCES = "com.xresch.cfw.example.dashboarding.resources";
	
	public static final String WIDGET_CATEGORY_EXTENSION_EXAMPLES = "Extension Examples";

	@Override
	public void register() {
		//----------------------------------
		// Register Package
		CFW.Files.addAllowedPackage(PACKAGE_RESOURCES);
		
		//----------------------------------
		// Register Languages 
		// if you use properties in widgets in global locale files, you can add them with the following code		
//		FileDefinition english = new FileDefinition(HandlingType.JAR_RESOURCE, PACKAGE_RESOURCES, "lang_en_customGlobal.properties");
//		CFW.Localization.registerLocaleFile(Locale.ENGLISH, FeatureDashboard.URI_DASHBOARD_VIEW, english);
//		CFW.Localization.registerLocaleFile(Locale.ENGLISH, FeatureDashboard.URI_DASHBOARD_VIEW_PUBLIC, english);
//		CFW.Localization.registerLocaleFile(Locale.ENGLISH, FeatureManual.URI_MANUAL, english);
		
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
