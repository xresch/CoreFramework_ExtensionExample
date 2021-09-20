package com.xresch.cfw.example.dashboarding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

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
import com.xresch.cfw.features.dashboard.DashboardWidget;
import com.xresch.cfw.features.dashboard.WidgetDefinition;
import com.xresch.cfw.features.jobs.CFWJobsAlertObject;
import com.xresch.cfw.features.jobs.CFWJobsAlertObject.AlertType;
import com.xresch.cfw.features.usermgmt.User;
import com.xresch.cfw.logging.CFWLog;
import com.xresch.cfw.response.JSONResponse;
import com.xresch.cfw.response.bootstrap.AlertMessage.MessageType;
import com.xresch.cfw.validation.LengthValidator;
import com.xresch.cfw.validation.NotNullOrEmptyValidator;

public class ExampleWidgetHelloWorld extends WidgetDefinition {
	
	private static final String NUMBER = "NUMBER";
	private static final String LIKES_TIRAMISU = "LIKES_TIRAMISU";
	private static final String MESSAGE = "MESSAGE";
	
	private static final Logger logger = CFWLog.getLogger(ExampleWidgetHelloWorld.class.getName());
	
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
	public void fetchData(HttpServletRequest request, JSONResponse response, CFWObject settings, JsonObject jsonSettings, long earliest, long latest) { 
		//int number = settings.get("number").getAsInt();
		String number = jsonSettings.get("number").getAsString();
		
		response.getContent()
			.append("\"")
			.append("<p>{!cfw_widget_helloworld_serverside!} "+number+"<p>")
			;
		
		//---------------------------------
		// Get Environment
		JsonElement environmentElement = jsonSettings.get("environment");
		if(environmentElement.isJsonNull()) {
			response.append("\"");
			return;
		}
		
		ExampleEnvironment environment = ExampleEnvironmentManagement.getEnvironment(environmentElement.getAsInt());
		if(environment == null) {
			CFW.Context.Request.addAlertMessage(MessageType.WARNING, "Example Widget: The chosen environment seems not configured correctly.");
			response.append("\"");
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
	public boolean hasPermission(User user) {
		return true;
	}
	
	
	//=========================================================================
	// Override the following methods if you want to add support for tasks
	// to your widget.
	//=========================================================================
	public boolean supportsTask() {
		return true;
	}
	

	public CFWObject getTasksParameters() {
		
		//Create Default Object for Alert Settings
		CFWJobsAlertObject alertObject = new CFWJobsAlertObject();
		
		//Add additional fields
		alertObject
				.addField(
					CFWField.newString(FormFieldType.TEXT, MESSAGE)
							.setDescription("Message to write to the log file.")
							.addValidator(new LengthValidator(5,500))
				)
				.addField(
						CFWField.newBoolean(FormFieldType.BOOLEAN, LIKES_TIRAMISU)
								.setDescription("Affinity of tiramisu to write to the log file")
				)
				.addField(
						CFWField.newInteger(FormFieldType.NUMBER, NUMBER)
								.setDescription("Number to write to the log")
				)
				;
		
		return alertObject;
	}
	

	public String getTaskDescription() {
		return "The task of this widget writes a message to the log file.";
	}

	public void executeTask(JobExecutionContext context, CFWObject taskParams, DashboardWidget widget, CFWObject widgetSettings) throws JobExecutionException {
		
		new CFWLog(logger)
			.custom("likesTiramisu", taskParams.getField(LIKES_TIRAMISU).getValue())
			.custom("chosenNumber", taskParams.getField(NUMBER).getValue())
			.info(taskParams.getField(MESSAGE).getValue().toString());
		
		//-----------------------------
		// Random Message for Testing
		MessageType[] types = MessageType.values();
		int randomIndex = CFW.Random.randomFromZeroToInteger(3);
		CFW.Messages.addMessage(types[randomIndex], "Hello World Task wrote a log message.");
		
		//-----------------------------
		// Alerting Example
		CFWJobsAlertObject alertObject = new CFWJobsAlertObject(context, this.getWidgetType());
		
		alertObject.mapJobExecutionContext(context);
		
		boolean randomCondition = CFW.Random.randomBoolean();
		CFW.Messages.addInfoMessage("Last Condition: "+randomCondition);
		
		AlertType type = alertObject.checkSendAlert(randomCondition, null);
		
		if(!type.equals(AlertType.NONE)) {

			String dashboardOrigin = widget.createWidgetOriginMessage();
			
			String message = "Hi There!\n\nThis is only a test, have a marvelous day!";
			String messageHTML = "<p>Hi There!<p></p>This is only a test, have a marvelous day!</p>"
								 +dashboardOrigin;
			
			if(type.equals(AlertType.RAISE)) {
				alertObject.doSendAlert("[TEST] Alert: A situation is occuring!", message, messageHTML);
			}
			
			if(type.equals(AlertType.RESOLVE)) {
				alertObject.doSendAlert("[TEST] Alert: A situation has resolved!.", message, messageHTML);
			}
		}
		
	}

}
