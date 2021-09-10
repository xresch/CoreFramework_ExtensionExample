package com.xresch.cfw.example._main;

import com.xresch.cfw._main.CFW;
import com.xresch.cfw.spi.CFWAppInterface;

public class _MainForTesting {
			
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

