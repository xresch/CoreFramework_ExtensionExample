package com.xresch.cfw.example.main;

import java.util.logging.Logger;

import com.xresch.cfw._main.CFW;
import com.xresch.cfw._main.CFWAppInterface;
import com.xresch.cfw.logging.CFWLog;

public class _MainForTesting {
		
	public static Logger logger = CFWLog.getLogger(_MainForTesting.class.getName());
	protected static CFWLog log = new CFWLog(logger);
	
    public static void main( String[] args ) throws Exception
    {
    	
    	//------------------------------------
    	// Load application Extension
    	CFWAppInterface app = CFW.loadExtentionApplication();

    	//------------------------------------
    	// Start Application
    	CFW.initializeApp(app, args);

    }

}

