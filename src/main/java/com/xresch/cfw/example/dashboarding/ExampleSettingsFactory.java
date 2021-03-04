package com.xresch.cfw.example.dashboarding;

import com.xresch.cfw._main.CFW;
import com.xresch.cfw.datahandling.CFWField;
import com.xresch.cfw.datahandling.CFWField.FormFieldType;
import com.xresch.cfw.example.contextsettings.ExampleEnvironment;

public class ExampleSettingsFactory {
	
	/************************************************************************************
	 * Returns the dynatrace environment selector field.
	 * 
	 * @return
	 ************************************************************************************/
	public static CFWField<?> createExampleEnvironmentSelectorField(){
		return CFWField.newString(FormFieldType.SELECT, "environment")
				.setLabel("Example Environment")
				.setDescription("Choose an enviromnent from the list.")
				.setOptions(CFW.DB.ContextSettings.getSelectOptionsForTypeAndUser(ExampleEnvironment.SETTINGS_TYPE));
	}
		

}
