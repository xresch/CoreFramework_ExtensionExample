package com.xresch.cfw.example.dashboarding;

import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

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
		CFWField settingsField = ExampleSettingsFactory.createExampleEnvironmentSelectorField();
				
		if(fieldValue != null) {
			settingsField.setValueConvert(fieldValue, true);
		}
	
		return settingsField;
	}
	
	/***************************************************************
	 * If another field should be displayed in the widget "Parameter"
	 * for this type of parameter, return a different field here.
	 * Else just return the same as for getFieldForWidget();
	 ***************************************************************/
	@SuppressWarnings({ "rawtypes" })
	@Override
	public CFWField getFieldForWidget(HttpServletRequest request, String dashboardid, Object parameterValue, CFWTimeframe timeframe) {

		return getFieldForSettings(request, dashboardid, parameterValue);
	}
	
	/***************************************************************
	 * 
	 ***************************************************************/
	@Override
	public boolean isDynamic() {
		return false;
	}
	/***************************************************************
	 * Check for which widget types this widget is available.
	 * Return true if it is available for the given widget, else 
	 * return false. (Tip: use a unique naming convention for your widgets).
	 * 
	 ***************************************************************/
	@Override
	public boolean isAvailable(HashSet<String> widgetTypesArray) {
		
		for(String type : widgetTypesArray) {
			if(type.startsWith("cfwexample_")) {
				return true;
			}
			
		}
		return false;
	}

}
