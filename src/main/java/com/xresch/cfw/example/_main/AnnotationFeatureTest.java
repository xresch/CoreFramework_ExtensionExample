package com.xresch.cfw.example._main;

import com.xresch.cfw._main.CFWApplicationExecutor;
import com.xresch.cfw.example.forms.FormExampleServlet;
import com.xresch.cfw.response.bootstrap.CFWHTMLItemMenuItem;
import com.xresch.cfw.spi.CFWAppFeature;

public class AnnotationFeatureTest extends CFWAppFeature {

	@Override
	public void register() {

		//-------------------------------------
    	// Register Menus that needs permission
		// to access
				
		ExampleExtensionApplication.EXTENSION_MENU_ROOT.addChild(
			new CFWHTMLItemMenuItem("Form Example")
				.faicon("fas fa-th-large")
				.addPermission(ExampleExtensionApplication.PERMISSION_CFWSAMPLES)
				.href("/app/formexample")
		);
		
		ExampleExtensionApplication.EXTENSION_MENU_ROOT.addChild(
			new CFWHTMLItemMenuItem("Open Google")
				.faicon("fab fa-google")
				.addPermission(ExampleExtensionApplication.PERMISSION_CFWSAMPLES)
				.href("http://www.google.ch")
				.addAttribute("target", "_blank")
		);
	}

	@Override
	public void initializeDB() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addFeature(CFWApplicationExecutor executor) {
		
		executor.addAppServlet(FormExampleServlet.class, "/formexample");

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
