package com.xresch.cfw.example.dashboarding;

import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.JsonObject;
import com.xresch.cfw.datahandling.CFWField;
import com.xresch.cfw.datahandling.CFWTimeframe;
import com.xresch.cfw.features.parameter.ParameterDefinition;

/***************************************************************
 * An example on how to add a custom parameter to the dashboard.
 * This class is registered by FeatureDashboardingExample using
 * CFW.Registry.Parameters.add(new ParameterDefinitionExampleEnvironment()); 
 ***************************************************************/
public class ParameterDefinitionExampleEnvironment extends ParameterDefinition {

	public static final String LABEL = "Example Environment";
	
	/***************************************************************
	 * Return the Unique Label for this parameter.
	 ***************************************************************/
	@Override
	public String getParamUniqueName() { return LABEL; }

	/***************************************************************
	 * Return the field displayed in the edit parameter window.
	 ***************************************************************/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public CFWField getFieldForSettings(HttpServletRequest request, String dashboardid, Object fieldValue) {
		CFWField settingsField = ExampleSettingsFactory.createExampleEnvironmentSelectorField()
													   .allowHTML(true); // do not sanitize HTML here to allow dashboard editors to do shenanigans
				
		if(fieldValue != null) {
			settingsField.setValueConvert(fieldValue, true);
		}
	
		return settingsField;
	}
	
	@Override
	public String descriptionShort() {
		return "This parameter allows the user to select an example environment.";
	}

	@Override
	public String descriptionHTML() {
		return "<p>Put here your verbose description for the manual.</p>";
	}
	
	/***************************************************************
	 * If another field should be displayed in the widget "Parameter"
	 * for this type of parameter, return a different field here.
	 * Else just return the same as for getFieldForWidget();
	 ***************************************************************/
	@SuppressWarnings({ "rawtypes" })
	@Override
	public CFWField getFieldForWidget(HttpServletRequest request, String dashboardid, Object parameterValue, CFWTimeframe timeframe, JsonObject userSelectedParamValues) {

		return getFieldForSettings(request, dashboardid, parameterValue).allowHTML(false); //you might want to sanitize here as it is user input
	}
	
	/***************************************************************
	 * 
	 ***************************************************************/
	@Override
	public boolean isDynamic() {
		return false;
	}

}
