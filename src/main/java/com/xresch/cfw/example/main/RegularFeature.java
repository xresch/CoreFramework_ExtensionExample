package com.xresch.cfw.example.main;

import com.xresch.cfw._main.CFW;
import com.xresch.cfw._main.CFWAppFeature;
import com.xresch.cfw._main.CFWApplicationExecutor;
import com.xresch.cfw.response.bootstrap.MenuItem;


public class RegularFeature extends CFWAppFeature {

	@Override
	public void register() {
		// TODO Auto-generated method stub
		//----------------------------------
    	// Register Menu
		
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
