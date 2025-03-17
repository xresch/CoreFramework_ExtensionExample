package com.xresch.cfw.example.forms;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xresch.cfw._main.CFW;
import com.xresch.cfw.datahandling.CFWField;
import com.xresch.cfw.datahandling.CFWField.FormFieldType;
import com.xresch.cfw.datahandling.CFWForm;
import com.xresch.cfw.datahandling.CFWFormHandler;
import com.xresch.cfw.datahandling.CFWObject;
import com.xresch.cfw.logging.CFWLog;
import com.xresch.cfw.response.HTMLResponse;
import com.xresch.cfw.response.JSONResponse;
import com.xresch.cfw.response.bootstrap.CFWHTMLItemAlertMessage.MessageType;

public class FormExampleServlet extends HttpServlet
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Logger logger = CFWLog.getLogger(FormExampleServlet.class.getName());

	@Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
				
		HTMLResponse html = new HTMLResponse("Test Page");
		StringBuilder content = html.getContent();
		  
		//------------------------------
		// Regular Form
		// sends post to same servlet
		//------------------------------
        CFWForm form = new CFWForm("cfwExampleDirectForm", "Save");
        form.addChild(CFWField.newString(FormFieldType.TEXT, "firstname"));
        form.addChild(CFWField.newString(FormFieldType.TEXT, "lastname"));
        
        content.append("<h2>Direct use of CFWForm</h2>");
        content.append(form.getHTML());
        content.append("<br />");
        
		//------------------------------
		// Form with Custom Handler
        // posts request to /cfw/formhandler
		//------------------------------
        content.append("<h2>Map Requests and Validate</h2>");
        CFWForm handledForm2 = new FormExampleCFWObject().toForm("cfwExampleHandlerForm", "Handle Again!!!");
        
        handledForm2.setFormHandler(new CFWFormHandler() {
			@Override
			public void handleForm(HttpServletRequest request, HttpServletResponse response, CFWForm form, CFWObject origin) {

				String formID = request.getParameter(CFWForm.FORM_ID);
				
				// mapRequestParameters() will validate and create error messages
				boolean isValid = origin.mapRequestParameters(request);
				
				if(isValid) {
					//Do your thing
				}else {
					//ignore or add some additional message
				}
				
				//------------------------------
				// Test output
				JSONResponse json = new JSONResponse();
				CFW.Messages.addSuccessMessage("CFWFormHandler: Post recieved from "+formID+"!!!");
				CFW.Messages.addInfoMessage(origin.dumpFieldsAsKeyValueHTML());

			}
		});
        content.append(handledForm2.getHTML());
    }
	
	
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
    	    	
    	String formID = request.getParameter(CFWForm.FORM_ID);
    	
    	JSONResponse json = new JSONResponse();
    	json.addAlert(MessageType.SUCCESS, "Post recieved from "+formID+"!!!");
    	json.addAlert(MessageType.INFO, "Firstname: "+request.getParameter("firstname"));
    	json.addAlert(MessageType.INFO, "Laststname: "+request.getParameter("lastname"));
    }
	
}