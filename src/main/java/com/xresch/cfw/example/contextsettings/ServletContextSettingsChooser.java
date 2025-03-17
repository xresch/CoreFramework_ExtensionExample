package com.xresch.cfw.example.contextsettings;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Strings;
import com.xresch.cfw._main.CFW;
import com.xresch.cfw.datahandling.CFWField;
import com.xresch.cfw.datahandling.CFWField.FormFieldType;
import com.xresch.cfw.datahandling.CFWForm;
import com.xresch.cfw.logging.CFWLog;
import com.xresch.cfw.response.HTMLResponse;
import com.xresch.cfw.response.JSONResponse;
import com.xresch.cfw.response.bootstrap.CFWHTMLItemAlertMessage.MessageType;

public class ServletContextSettingsChooser extends HttpServlet
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Logger logger = CFWLog.getLogger(ServletContextSettingsChooser.class.getName());

	@Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
				
		HTMLResponse html = new HTMLResponse("Example EnvironmentChooser");
		StringBuilder content = html.getContent();
		  
		//------------------------------------------------------------
		// An example Environment chooser field
        CFWForm form = new CFWForm("cfwExampleEnvironmentChooser", "Save");
        form.addChild(
        	CFWField.newString(FormFieldType.SELECT, "environment")
				.setLabel("Example Environment")
				.setDescription("Choose an enviromnent from the list")
				.setOptions(CFW.DB.ContextSettings.getSelectOptionsForTypeAndUser(ExampleEnvironment.SETTINGS_TYPE))
		);
        
        content.append("<h2>Form with Environment</h2>");
        content.append(form.getHTML());
        
    }
	
	
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
    	
    	try {
	    	JSONResponse json = new JSONResponse();
	    	String environmentID = request.getParameter("environment");
	    	if(Strings.isNullOrEmpty(environmentID)) {
	    		json.addAlert(MessageType.INFO, "Please choose an environment first.");
	    		return;
	    	}
	    	
	    	json.addAlert(MessageType.SUCCESS, "Post received!!!");
	    	json.addAlert(MessageType.INFO, "Environment ID: "+environmentID);
	    	
	    	ExampleEnvironment environment = ExampleEnvironmentManagement.getEnvironment(Integer.parseInt(environmentID) );
	    	if(environment != null) {
	    		json.addAlert(MessageType.INFO, "Name: "+environment.host());
	    		json.addAlert(MessageType.INFO, "Host: "+environment.host());
	    		json.addAlert(MessageType.INFO, "Port: "+environment.port());
	    		json.addAlert(MessageType.INFO, "Use HTTPs: "+environment.useHttps());
	    	}
    	}catch(Exception e) {
    		new CFWLog(logger).severe("Exception occured: ", e);
    	}
    	
    }
	
}