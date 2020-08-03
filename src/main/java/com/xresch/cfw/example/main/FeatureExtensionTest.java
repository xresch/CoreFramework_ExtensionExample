package com.xresch.cfw.example.main;

import com.xresch.cfw._main.CFW;
import com.xresch.cfw._main.CFWAppFeature;
import com.xresch.cfw._main.CFWApplicationExecutor;
import com.xresch.cfw._main.CFWExtensionFeature;
import com.xresch.cfw.response.bootstrap.MenuItem;

@CFWExtensionFeature
public class FeatureExtensionTest extends CFWAppFeature {

	@Override
	public void register() {

		//-------------------------------------
    	// Register Menus that needs permission
		// to access
		CFW.Registry.Components.addRegularMenuItem(
				(MenuItem)new MenuItem("Extension Example")
				.faicon("fas fa-flask")
				.addPermission(ExampleExtensionApplication.PERMISSION_CFWSAMPLES),
				null
		);
		
		CFW.Registry.Components.addRegularMenuItem(
				(MenuItem)new MenuItem("Open Google")
					.faicon("fab fa-google")
					.addPermission(ExampleExtensionApplication.PERMISSION_CFWSAMPLES)
					.href("http://www.google.ch")
					.addAttribute("target", "_blank")
				, "Extension Example");
	}

	@Override
	public void initializeDB() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addFeature(CFWApplicationExecutor app) {
		// TODO Auto-generated method stub

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
