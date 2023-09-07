package com.xresch.cfw.example.query;

import java.util.ArrayList;
import java.util.TreeSet;

import com.google.common.base.Strings;
import com.xresch.cfw._main.CFW;
import com.xresch.cfw.features.query.CFWQueryContext;
import com.xresch.cfw.features.query.CFWQueryFunction;
import com.xresch.cfw.features.query.EnhancedJsonObject;
import com.xresch.cfw.features.query.parse.QueryPartValue;

public class CFWQueryFunctionCamelize extends CFWQueryFunction {

	
	public CFWQueryFunctionCamelize(CFWQueryContext context) {
		super(context);
	}

	/***********************************************************************************************
	 * 
	 ***********************************************************************************************/
	@Override
	public String uniqueName() {
		return "camelize";
	}
	
	/***********************************************************************************************
	 * 
	 ***********************************************************************************************/
	@Override
	public TreeSet<String> getTags(){
		// tags will be used on the manual page 'Query >> Functions' to create an overview of functions 
		// grouped by tags
		TreeSet<String> tags = new TreeSet<>();
		tags.add(CFWQueryFunction.TAG_STRINGS);
		tags.add("animalistic"); // you can also use custom tags
		return tags;
	}
	
	/***********************************************************************************************
	 * 
	 ***********************************************************************************************/
	@Override
	public String descriptionSyntax() {
		return "camelize(stringOrFieldname, lowercaseCount)";
	}
	/***********************************************************************************************
	 * 
	 ***********************************************************************************************/
	@Override
	public String descriptionShort() {
		return "Takes a string and hyper-morph-warp-ilates it into a camel(at least from an ascii perspective).";
	}
	
	/***********************************************************************************************
	 * 
	 ***********************************************************************************************/
	@Override
	public String descriptionSyntaxDetailsHTML() {
		return "<p><b>stringOrFieldname:&nbsp;</b>The string or or a fieldname that should be camelated.</p>"
			  +"<p><b>lowercaseCount:&nbsp;</b>Number of lowercase characters between uppercase characters(choose wisely, default is one).</p>"
			;
	}

	/***********************************************************************************************
	 * 
	 ***********************************************************************************************/
	@Override
	public String descriptionHTML() {
		return CFW.Files.readPackageResource(FeatureQueryExamples.PACKAGE_MANUAL, "function_camelize.html");
	}


	/***********************************************************************************************
	 * 
	 ***********************************************************************************************/
	@Override
	public boolean supportsAggregation() {
		return false;
	}

	/***********************************************************************************************
	 * 
	 ***********************************************************************************************/
	@Override
	public void aggregate(EnhancedJsonObject object,ArrayList<QueryPartValue> parameters) {
		// not supported for this example
	}

	/***********************************************************************************************
	 * 
	 ***********************************************************************************************/
	@Override
	public QueryPartValue execute(EnhancedJsonObject object, ArrayList<QueryPartValue> parameters) {
		
		int paramCount = parameters.size();
		
		//----------------------------------
		// Return CaCaCaRaMeL as default if no params are defined
		if(paramCount == 0) { 
			return QueryPartValue.newString("CaCaCaRaMeL!!!");
		}else { 
			
			//----------------------------------
			// Get String
			QueryPartValue initialValue = parameters.get(0);
			String initialString = initialValue.getAsString();
			
			if(Strings.isNullOrEmpty(initialString)) { 
				return initialValue; 
			}
			
			//----------------------------------
			// Get Interval
			Integer interval = 1;
			if(paramCount > 1 && parameters.get(1).isNumberOrNumberString()) {
				interval = parameters.get(1).getAsInteger();
				
				if(interval < 0) {
					interval = 1;
				}else if(interval >= initialString.length()) {
					interval = initialString.length();
				}
			}
			
			//----------------------------------
			// Form and Bake an ASCII Camel
			String lowercase = initialString.toLowerCase();
			StringBuilder builder = new StringBuilder( (lowercase.charAt(0)+"").toUpperCase() );
			
			for(int i = 1; i < initialString.length(); i++ ) {
				if( (i % (interval+1)) != 0) {
					builder.append(lowercase.charAt(i));
				}else {
					builder.append(
							(lowercase.charAt(i)+"").toUpperCase()
						);
				}
			}
			return QueryPartValue.newString(builder.toString());
		}
				

	}

}
