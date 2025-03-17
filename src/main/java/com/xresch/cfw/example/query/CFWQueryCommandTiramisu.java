package com.xresch.cfw.example.query;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import com.google.gson.JsonElement;
import com.xresch.cfw._main.CFW;
import com.xresch.cfw.features.core.AutocompleteResult;
import com.xresch.cfw.features.query.CFWQuery;
import com.xresch.cfw.features.query.CFWQueryAutocompleteHelper;
import com.xresch.cfw.features.query.CFWQueryCommand;
import com.xresch.cfw.features.query.CFWQuerySource;
import com.xresch.cfw.features.query.EnhancedJsonObject;
import com.xresch.cfw.features.query._CFWQueryCommon;
import com.xresch.cfw.features.query.parse.CFWQueryParser;
import com.xresch.cfw.features.query.parse.QueryPart;
import com.xresch.cfw.features.query.parse.QueryPartArray;
import com.xresch.cfw.features.query.parse.QueryPartAssignment;
import com.xresch.cfw.features.query.parse.QueryPartValue;
import com.xresch.cfw.logging.CFWLog;
import com.xresch.cfw.pipeline.PipelineActionContext;

public class CFWQueryCommandTiramisu extends CFWQueryCommand {
	
	private static final Logger logger = CFWLog.getLogger(CFWQueryCommandTiramisu.class.getName());
	
	CFWQuerySource source = null;
	HashSet<String> fieldnames = new HashSet<>();
	
	private boolean doRandomDessert = true;
	
	HashSet<String> encounters = new HashSet<>();
	
	/***********************************************************************************************
	 * 
	 ***********************************************************************************************/
	public CFWQueryCommandTiramisu(CFWQuery parent) {
		super(parent);
	}

	/***********************************************************************************************
	 * Return the command name and aliases.
	 * The first entry in the array will be used as the main name, under which the documentation can
	 * be found in the manual. All other will be used as aliases.
	 ***********************************************************************************************/
	@Override
	public String[] uniqueNameAndAliases() {
		return new String[] {"tiramisu", "dessertification"};
	}
	
	/***********************************************************************************************
	 * 
	 ***********************************************************************************************/
	@Override
	public TreeSet<String> getTags(){
		TreeSet<String> tags = new TreeSet<>();
		tags.add(_CFWQueryCommon.TAG_STRINGS);
		tags.add("desserts");
		return tags;
	}


	/***********************************************************************************************
	 * 
	 ***********************************************************************************************/
	@Override
	public String descriptionShort() {
		return "Turns the value of a random field into Italian dessert.";
	}

	/***********************************************************************************************
	 * 
	 ***********************************************************************************************/
	@Override
	public String descriptionSyntax() {
		return "distinct <fieldname> [, <fieldname> ...] [random=<boolean>]";
	}
	
	/***********************************************************************************************
	 * 
	 ***********************************************************************************************/
	@Override
	public String descriptionSyntaxDetailsHTML() {
		return "<p><b>fieldname:&nbsp;</b> Names of the fields that should be considered to be changed into Italian dessert.</p>"
		 	  +"<p><b>random:&nbsp;</b> Toogle if the Italian desserts should be random. If false, only uses tiramisu. (Default: true)</p>";
	}

	/***********************************************************************************************
	 * 
	 ***********************************************************************************************/
	@Override
	public String descriptionHTML() {
		return CFW.Files.readPackageResource(FeatureQueryExamples.PACKAGE_MANUAL, "command_tiramisu.html");
	}

	/***********************************************************************************************
	 * 
	 ***********************************************************************************************/
	@Override
	public void setAndValidateQueryParts(CFWQueryParser parser, ArrayList<QueryPart> parts) throws ParseException {
		
		//------------------------------------------
		// Get Fieldnames
		for(QueryPart part : parts) {
			
			//------------------------------------------
			// Get Fieldnames
			if(part instanceof QueryPartAssignment) {
				
				QueryPartAssignment parameter = (QueryPartAssignment)part;
				String paramName = parameter.getLeftSide().determineValue(null).getAsString();
				
				if(paramName != null && paramName.equals("random")) {
					QueryPartValue paramValue = parameter.getRightSide().determineValue(null);
					if(paramValue.isBoolOrBoolString()) {
						this.doRandomDessert = paramValue.getAsBoolean();
					}else {
						parser.throwParseException("tiramisu: Expected a boolean value for parameter 'random'.", part);
					}
				}
				
			}else if(part instanceof QueryPartArray) {
				// as fieldnames might be comma separated, which results in a QueryPartArray, iterate array contents
				QueryPartArray array = (QueryPartArray)part;

				for(JsonElement element : array.getAsJsonArray(null, true)) {
					
					if(!element.isJsonNull() && element.isJsonPrimitive()) {
						fieldnames.add(element.getAsString());
					}
				}
			}else {
				// Every other part is evaluated and converted to a string
				QueryPartValue value = part.determineValue(null);
				if(!value.isNull()) {
					fieldnames.add(value.getAsString());
				}
			}
		}
			
	}
	
	/***********************************************************************************************
	 * 
	 ***********************************************************************************************/
	@Override
	public void autocomplete(AutocompleteResult result, CFWQueryAutocompleteHelper helper) {
		result.setHTMLDescription(
				"<b>Hint:&nbsp;</b>Specify the fieldnames of the fields that should be considered for dessert manipulation.<br>"
				+"<b>Syntax:&nbsp;</b>"+CFW.Security.escapeHTMLEntities(this.descriptionSyntax())
			);
	}

	/***********************************************************************************************
	 * This method will be executed during the pipline.
	 * IMPORTANT: 
	 *   This method will be called multiple times if the while(keepPolling()) is used.
	 *   Whenever the inQueue gets empty, the loop be exited and the pipeline will wait until there 
	 *   are more items in the inQueue before calling this method again.
	 *   So it is important to not store any information in local variables if the info should
	 *   persist over multiple calls. 
	 ***********************************************************************************************/
	@Override
	public void execute(PipelineActionContext context) throws Exception {
		
		while(keepPolling()) {
			
			EnhancedJsonObject record = inQueue.poll();
			
			//---------------------------------
			// Get field to choose from
			Set<String> finalFields = fieldnames;
			if(finalFields.isEmpty()) {
				finalFields = record.keySet();
			}
			
			if(finalFields == null || finalFields.isEmpty()) {
				continue; 
			}

			String randomField = CFW.Random.randomFromSet(0, finalFields);

			//---------------------------------
			// Get Dessert
			String dessert = "Tiramisu";
			if(doRandomDessert) {
				dessert = CFW.Random.randomItalianDessert();
			}
			
			//---------------------------------
			// Modify record and add to outQueue
			record.addProperty(randomField, dessert);
			
			outQueue.add(record);
		}
		this.setDoneIfPreviousDone();
		
	}



}
