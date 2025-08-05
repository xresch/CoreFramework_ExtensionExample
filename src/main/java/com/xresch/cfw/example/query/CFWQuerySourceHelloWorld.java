package com.xresch.cfw.example.query;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.text.ParseException;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.xresch.cfw._main.CFW;
import com.xresch.cfw.datahandling.CFWField;
import com.xresch.cfw.datahandling.CFWObject;
import com.xresch.cfw.datahandling.CFWField.FormFieldType;
import com.xresch.cfw.example._main.ExampleExtensionApplication;
import com.xresch.cfw.features.analytics.FeatureSystemAnalytics;
import com.xresch.cfw.features.core.AutocompleteResult;
import com.xresch.cfw.features.query.CFWQuery;
import com.xresch.cfw.features.query.CFWQueryAutocompleteHelper;
import com.xresch.cfw.features.query.CFWQuerySource;
import com.xresch.cfw.features.query.EnhancedJsonObject;
import com.xresch.cfw.features.query.FeatureQuery;
import com.xresch.cfw.features.usermgmt.User;
import com.xresch.cfw.logging.CFWAuditLogDBMethods;
import com.xresch.cfw.utils.ResultSetUtils.ResultSetAsJsonReader;
import com.xresch.cfw.utils.json.JsonTimerangeChecker;
	
/**************************************************************************************************************
 * 
 * @author Reto Scheiwiller, (c) Copyright 2021 
 * @license MIT-License
 **************************************************************************************************************/
public class CFWQuerySourceHelloWorld extends CFWQuerySource {


	/******************************************************************
	 *
	 ******************************************************************/
	public CFWQuerySourceHelloWorld(CFWQuery parent) {
		super(parent);
	}

	
	/******************************************************************
	 *
	 ******************************************************************/
	@Override
	public String uniqueName() {
		return "helloworld";
	}

	/******************************************************************
	 *
	 ******************************************************************/
	@Override
	public String descriptionShort() {
		return "Creates example data based on hello world.";
	}
	
	/******************************************************************
	 *
	 ******************************************************************/
	@Override
	public String descriptionTime() {
		return "Time range is automatically taken from time picker.";
	}
	
	/******************************************************************
	 * The returned String will be added to the manual page under the 
	 * "Usage" section for this source.
	 ******************************************************************/
	@Override
	public String descriptionHTML() {
		return CFW.Files.readPackageResource(FeatureQueryExamples.PACKAGE_MANUAL, "source_helloworld.html");
	}
	/******************************************************************
	 *
	 ******************************************************************/
	@Override
	public String descriptionRequiredPermission() {
		return ExampleExtensionApplication.PERMISSION_CFWSAMPLES;
	}

	/******************************************************************
	 *
	 ******************************************************************/
	@Override
	public boolean hasPermission(User user) {
		return user.hasPermission(ExampleExtensionApplication.PERMISSION_CFWSAMPLES);
	}
	
	/***********************************************************************************************
	 * 
	 ***********************************************************************************************/
	@Override
	public void autocomplete(AutocompleteResult result, CFWQueryAutocompleteHelper helper) {
		// do nothing
	}
	
	/******************************************************************
	 * Create a CFWObject and add CFWFields to define the parameters.
	 * Those are the parameters the user can define for this source.
	 ******************************************************************/
	@Override
	public CFWObject getParameters() {
		
		return new CFWObject()
				.addField(
					CFWField.newInteger(FormFieldType.NUMBER, "records")
						.setDescription("Number of records to generate. (Default: 42)")
						.setValue(42)
				)
				.addField(
						CFWField.newInteger(FormFieldType.NUMBER, "min")
							.setDescription("The minimum for the random number.(Default: 0)")
							.setValue(0)
					)
				.addField(
						CFWField.newInteger(FormFieldType.NUMBER, "max")
							.setDescription("The maximum for the random number. (Default: 100)")
							.setValue(100)
					)
				.addField(
						CFWField.newString(FormFieldType.NUMBER, "name")
						.setDescription("The name used in the hi_there message. (Default: null, use random name)")
						.setValue(null)
						)
			;
	}
	
	/******************************************************************
	 *
	 ******************************************************************/
	@Override
	public void parametersPermissionCheck(CFWObject parameters) throws ParseException {
		//do nothing
	}
	
	
	/******************************************************************
	 *
	 ******************************************************************/
	@Override
	public void execute(CFWObject parameters, LinkedBlockingQueue<EnhancedJsonObject> outQueue, long earliestMillis, long latestMillis, int limit) throws Exception {
		
		//---------------------------------------------------------------
		// Get the parameters that are defined under getParameters()
		int records = (int)parameters.getField("records").getValue();
		int min = (int)parameters.getField("min").getValue();
		int max = (int)parameters.getField("max").getValue();
		String name = (String)parameters.getField("name").getValue();

		//---------------------------------------------------------------
		// Timerange from the time picker is given by the method parameters
		long earliest = earliestMillis;
		long latest = latestMillis;
		long diff = latest - earliest;
		long diffStep = diff / records;
		
		//-------------------------------
    	// Generate records up to records limit
		for(int i = 0; i < records; i++) {
			
			//-------------------------------
        	// Create the record data
			EnhancedJsonObject currentObject = new EnhancedJsonObject();
			
			currentObject.addProperty("index", i );
			currentObject.addProperty("time", earliest +(i * diffStep));
        	
			currentObject.addProperty("type", (CFW.Random.bool()) ? "Hello" : "World" );
			currentObject.addProperty("helloworld", "Hello World!" );
			
			if(name != null) {
				currentObject.addProperty("hi_there", "Hi "+name+"!" );
			}else {
				currentObject.addProperty("hi_there", "Hi "+CFW.Random.firstnameOfGod()+"!" );
			}
			
			currentObject.addProperty("lucky_number", CFW.Random.integer(min, max));
			
			//-------------------------------
        	// Add result to the output queue
        	outQueue.add( currentObject );
        	
        	//-------------------------------
        	// check if the source limit is reached
			if( isLimitReached(limit, i)) { break; }
		}
		
		
	}
}
