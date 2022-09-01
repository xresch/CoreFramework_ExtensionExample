package com.xresch.cfw.example.query;

import com.xresch.cfw._main.CFW;
import com.xresch.cfw._main.CFWApplicationExecutor;
import com.xresch.cfw.features.query.commands.CFWQueryCommandSource;
import com.xresch.cfw.spi.CFWAppFeature;

public class FeatureQueryExamples extends CFWAppFeature {
	
	public static final String PACKAGE_MANUAL = "com.xresch.cfw.example.query.manual";
	
	@Override
	public void register() {
		//----------------------------------
		// Register Package
		CFW.Files.addAllowedPackage(PACKAGE_MANUAL);
		
		//----------------------------------
		// Register Sources
		CFW.Registry.Query.registerSource(new CFWQuerySourceHelloWorld(null));
	
		//----------------------------------
		// Register Commands
		CFW.Registry.Query.registerCommand(new CFWQueryCommandTiramisu(null));
	}

	@Override
	public void initializeDB() {
		
	}

	@Override
	public void addFeature(CFWApplicationExecutor executor) {

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
