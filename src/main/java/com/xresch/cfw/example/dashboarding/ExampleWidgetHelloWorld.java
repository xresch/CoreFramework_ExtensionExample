package com.xresch.cfw.example.dashboarding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.xresch.cfw._main.CFW;
import com.xresch.cfw.caching.FileDefinition;
import com.xresch.cfw.caching.FileDefinition.HandlingType;
import com.xresch.cfw.datahandling.CFWField;
import com.xresch.cfw.datahandling.CFWField.FormFieldType;
import com.xresch.cfw.datahandling.CFWObject;
import com.xresch.cfw.example.contextsettings.ExampleEnvironment;
import com.xresch.cfw.example.contextsettings.ExampleEnvironmentManagement;
import com.xresch.cfw.features.core.AutocompleteList;
import com.xresch.cfw.features.core.AutocompleteResult;
import com.xresch.cfw.features.core.CFWAutocompleteHandler;
import com.xresch.cfw.features.dashboard.WidgetDefinition;
import com.xresch.cfw.response.JSONResponse;
import com.xresch.cfw.response.bootstrap.AlertMessage.MessageType;
import com.xresch.cfw.validation.LengthValidator;
import com.xresch.cfw.validation.NotNullOrEmptyValidator;

public class ExampleWidgetHelloWorld extends WidgetDefinition {

	@Override
	public String getWidgetType() {return "cfwexample_helloworld";}

	@Override
	public CFWObject getSettings() {
		return new CFWObject()
				// Create a factory class for common fields to have everything in one place
		       .addField(ExampleSettingsFactory.createExampleEnvironmentSelectorField())
		       
				.addField(CFWField.newString(FormFieldType.TEXT, "name")
								.setLabel("{!cfw_widget_helloworld_name!}")
								.setDescription("{!cfw_widget_helloworld_name_desc!}")
								.addValidator(new LengthValidator(2, 25))
								.setValue("Jane Doe")
				)
				.addField(CFWField.newTagsSelector("JSON_HOBBIES_SELECTOR")
							.setLabel("Hobbies")
							.setAutocompleteHandler(new CFWAutocompleteHandler(5) {
								
								public AutocompleteResult getAutocompleteData(HttpServletRequest request, String inputValue) {
									AutocompleteList list = new AutocompleteList();
									LinkedHashMap<Object, Object>  array = new LinkedHashMap<Object, Object>() ;
									for(int i = 0; i < 25; i++ ) {
										String tag = inputValue+"_"+i;
										list.addItem("key_"+tag, "Hobby: "+tag);
									}
									return new AutocompleteResult(list);
								}
							})
				)
				.addField(CFWField.newInteger(FormFieldType.NUMBER, "number")
						.addValidator(new NotNullOrEmptyValidator())
						.setValue(1)
				)	
				.addField(CFWField.newBoolean(FormFieldType.BOOLEAN, "dosave")
						.setLabel("Do Save")
						.setValue(true)
				)
		;
	}

	@Override
	public void fetchData(HttpServletRequest request, JSONResponse response, JsonObject settings) { 
		//int number = settings.get("number").getAsInt();
		String number = settings.get("number").getAsString();
		
		response.getContent()
			.append("\"")
			.append("<p>{!cfw_widget_helloworld_serverside!} "+number+"<p>");
		
		//---------------------------------
		// Get Environment
		JsonElement environmentElement = settings.get("environment");
		if(environmentElement.isJsonNull()) {
			return;
		}
		
		ExampleEnvironment environment = ExampleEnvironmentManagement.getEnvironment(environmentElement.getAsInt());
		if(environment == null) {
			CFW.Context.Request.addAlertMessage(MessageType.WARNING, "Example Widget: The chosen environment seems not configured correctly.");
			return;
		}
		
		response.getContent().append("<p><b>Your environment is:&nbsp;</b> "+environment.getDefaultObject().name()+"</p>");
		response.getContent().append("<p><b>Environment URL:&nbsp;</b> "+environment.getExampleUrl()+"</p>");
		
		response.getContent()
		.append("\"");
	}

	@Override
	public ArrayList<FileDefinition> getJavascriptFiles() {
		ArrayList<FileDefinition> array = new ArrayList<FileDefinition>();
		FileDefinition js = new FileDefinition(HandlingType.JAR_RESOURCE, FeatureDashboardingExample.RESOURCE_PACKAGE, "cfwexamples_widget_helloworld.js");
		array.add(js);
		return array;
	}

	@Override
	public ArrayList<FileDefinition> getCSSFiles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<Locale, FileDefinition> getLocalizationFiles() {
		HashMap<Locale, FileDefinition> map = new HashMap<Locale, FileDefinition>();
		map.put(Locale.ENGLISH, new FileDefinition(HandlingType.JAR_RESOURCE, FeatureDashboardingExample.RESOURCE_PACKAGE, "lang_en_widget_helloworld.properties"));
		map.put(Locale.GERMAN, new FileDefinition(HandlingType.JAR_RESOURCE, FeatureDashboardingExample.RESOURCE_PACKAGE, "lang_de_widget_helloworld.properties"));
		return map;
	}

	@Override
	public boolean hasPermission() {
		return true;
	}

}
