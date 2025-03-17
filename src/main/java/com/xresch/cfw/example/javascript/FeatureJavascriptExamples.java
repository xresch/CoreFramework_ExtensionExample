package com.xresch.cfw.example.javascript;

import com.xresch.cfw._main.CFW;
import com.xresch.cfw._main.CFWApplicationExecutor;
import com.xresch.cfw.example._main.ExampleExtensionApplication;
import com.xresch.cfw.example.datahandling.Person;
import com.xresch.cfw.response.bootstrap.CFWHTMLItemMenuItem;
import com.xresch.cfw.spi.CFWAppFeature;

public class FeatureJavascriptExamples extends CFWAppFeature {
	
	public static final String RESOURCE_PACKAGE = "com.xresch.cfw.example.javascript.resources";
	
	@Override
	public void register() {
		//----------------------------------
		// Register Package
		CFW.Files.addAllowedPackage(RESOURCE_PACKAGE);
		
		//-------------------------------------
    	// Register Menus that needs permission
		// to access
		ExampleExtensionApplication.EXTENSION_MENU_ROOT.addChild(
			new CFWHTMLItemMenuItem("Javascript Examples")
				.faicon("fab fa-js")
				.addPermission(ExampleExtensionApplication.PERMISSION_CFWSAMPLES)
				.href("/app/jsexamples")
		);
		
		//-------------------------------------
    	// Register Objects
		CFW.Registry.Objects.addCFWObject(Person.class);		
	}

	@Override
	public void initializeDB() {
		
	}

	@Override
	public void addFeature(CFWApplicationExecutor executor) {
		
		executor.addAppServlet(ServletJavascriptExamples.class, "/jsexamples");

	}

	@Override
	public void startTasks() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopFeature() {
		// TODO Auto-generated method stub

	}

}
