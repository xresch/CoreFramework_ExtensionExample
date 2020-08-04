package com.xresch.cfw.example.main;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xresch.cfw.datahandling.CFWField;
import com.xresch.cfw.datahandling.CFWField.FormFieldType;
import com.xresch.cfw.datahandling.CFWForm;
import com.xresch.cfw.datahandling.CFWFormHandler;
import com.xresch.cfw.datahandling.CFWObject;
import com.xresch.cfw.logging.CFWLog;
import com.xresch.cfw.response.HTMLResponse;
import com.xresch.cfw.response.JSONResponse;
import com.xresch.cfw.response.bootstrap.AlertMessage.MessageType;
import com.xresch.cfw.tests.assets.mockups.CFWObjectMockup;

public class FormExampleServlet extends HttpServlet
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Logger logger = CFWLog.getLogger(FormExampleServlet.class.getName());

	@Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
		
		CFWLog log = new CFWLog(logger).method("doGet");
		
		HTMLResponse html = new HTMLResponse("Test Page");
		StringBuffer content = html.getContent();
		  
		//------------------------------
		// Regular Form
		// sends post to same servlet
		//------------------------------
        CFWForm form = new CFWForm("cfwExampleDirectForm", "Save");
        form.addChild(CFWField.newString(FormFieldType.TEXT, "Firstname"));
        form.addChild(CFWField.newString(FormFieldType.TEXT, "Lastname"));
        
        content.append("<h2>Direct use of BTForm</h2>");
        content.append(form.getHTML());
        
        
		//------------------------------
		// Form with Custom Handler
        // posts request to /cfw/formhandler
		//------------------------------
        content.append("<h2>Map Requests and Validate</h2>");
        CFWForm handledForm2 = new FormExampleCFWObject().toForm("cfwExampleHandlerForm", "Handle Again!!!");
        
        handledForm2.setFormHandler(new CFWFormHandler() {
			@Override
			public void handleForm(HttpServletRequest request, HttpServletResponse response, CFWForm form, CFWObject origin) {
				// TODO Auto-generated method stub
				String formID = request.getParameter(CFWForm.FORM_ID);
				origin.mapRequestParameters(request);
				JSONResponse json = new JSONResponse();
		    	json.addAlert(MessageType.SUCCESS, "BTFormHandler: Post recieved from "+formID+"!!!");
		    	json.addAlert(MessageType.SUCCESS, origin.dumpFieldsAsKeyValueHTML());
		    	form.mapRequestParameters(request);
			}
		});
        content.append(handledForm2.getHTML());
    }
	
	
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
    	    	
    	String formID = request.getParameter(CFWForm.FORM_ID);
    	
    	JSONResponse json = new JSONResponse();
    	json.addAlert(MessageType.SUCCESS, "Post recieved from "+formID+"!!!");
    	
    }
	
}