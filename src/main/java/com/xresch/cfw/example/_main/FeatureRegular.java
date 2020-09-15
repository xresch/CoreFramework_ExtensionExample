package com.xresch.cfw.example._main;

import com.xresch.cfw._main.CFW;
import com.xresch.cfw._main.CFWAppFeature;
import com.xresch.cfw._main.CFWApplicationExecutor;
import com.xresch.cfw.response.bootstrap.MenuItem;

/**************************************************************************************
 * This feature is manually registered with CFW.Registry.Features.addFeature().
 **************************************************************************************/
public class FeatureRegular extends CFWAppFeature {

	@Override
	public void register() {

		//----------------------------------
    	// Register Menu
		// as no permissions are set, also 
		// none logged in users will see 
		// the menu entry.
		
		CFW.Registry.Components.addRegularMenuItem(
				(MenuItem)new MenuItem("Regular Feature")
				.faicon("fas fa-seedling"),
				null
		);
		
		CFW.Registry.Components.addRegularMenuItem(
				(MenuItem)new MenuItem("Show Support Info")
					.faicon("fas fa-info")
					.onclick("cfw_showSupportInfoModal();")
				, "Regular Feature");
		
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
