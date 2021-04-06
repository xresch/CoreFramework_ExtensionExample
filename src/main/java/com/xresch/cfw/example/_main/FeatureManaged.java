package com.xresch.cfw.example._main;

import com.xresch.cfw._main.CFWAppFeature;
import com.xresch.cfw._main.CFWApplicationExecutor;
import com.xresch.cfw.response.bootstrap.MenuItem;

/**************************************************************************************
 * This feature is manually registered with CFW.Registry.Features.addFeature().
 * Override the following methods to define your managed feature:
 *   - getNameForFeatureManagement()
 *   - getDescriptionForFeatureManagement()
 *   - activeByDefault()
 *   
 **************************************************************************************/
public class FeatureManaged extends CFWAppFeature {

	/************************************************************************************
	 * Override to make it managed and return something else then null.
	 ************************************************************************************/
	@Override
	public String getNameForFeatureManagement() {
		return "Managed Feature";
	};
	
	/************************************************************************************
	 * Register a description for the feature management.
	 ************************************************************************************/
	@Override
	public String getDescriptionForFeatureManagement() {
		return "Example of a feature that can be enabled/disabled through the user interface(needs restart of application to take effect).";
	};
	
	/************************************************************************************
	 * Return if the feature is active by default or if the admin has to enable it.
	 ************************************************************************************/
	public boolean activeByDefault() {
		return true;
	};
	
	@Override
	public void register() {

		//----------------------------------
    	// Register Menu
		// as no permissions are set, also 
		// none logged in users will see 
		// the menu entry.
				
		ExampleExtensionApplication.EXTENSION_MENU_ROOT.addChild(
				(MenuItem)new MenuItem("Managed Feature")
					.faicon("fas fa-star")
					.onclick("CFW.ui.showModal('Managed Example', 'This Feature can be disabled in the Feature management.');")
		);
		
		
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
