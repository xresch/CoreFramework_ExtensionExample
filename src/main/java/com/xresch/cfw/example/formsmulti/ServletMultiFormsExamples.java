package com.xresch.cfw.example.formsmulti;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xresch.cfw._main.CFW;
import com.xresch.cfw._main.CFWMessages;
import com.xresch.cfw.caching.FileDefinition.HandlingType;
import com.xresch.cfw.datahandling.CFWMultiForm;
import com.xresch.cfw.datahandling.CFWMultiFormHandlerDefault;
import com.xresch.cfw.datahandling.CFWObject;
import com.xresch.cfw.db.CFWSQL;
import com.xresch.cfw.example._main.ExampleExtensionApplication;
import com.xresch.cfw.example.datahandling.Person;
import com.xresch.cfw.response.HTMLResponse;
import com.xresch.cfw.response.JSONResponse;

/**************************************************************************************************************
 * 
 * @author Reto Scheiwiller, (c) Copyright 2020
 **************************************************************************************************************/
public class ServletMultiFormsExamples extends HttpServlet
{

	private static final long serialVersionUID = 1L;
	
	public ServletMultiFormsExamples() {
	
	}
	
	/******************************************************************
	 *
	 ******************************************************************/
	@Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
		HTMLResponse html = new HTMLResponse("Javascript Examples");
		
		if(CFW.Context.Request.hasPermission(ExampleExtensionApplication.PERMISSION_CFWSAMPLES)) {
						
			String action = request.getParameter("action");
			
			if(action == null) {

				html.addJSFileBottom(HandlingType.JAR_RESOURCE, FeatureMultiFormExamples.RESOURCE_PACKAGE, "multiform_examples.js");
				
				html.addJavascriptCode("multiformexamples_initialDraw();");
				
		        response.setContentType("text/html");
		        response.setStatus(HttpServletResponse.SC_OK);
			}else {
				handleDataRequest(request, response);
			}
		}else {
			CFWMessages.accessDenied();
		}
        
    }
	
	/******************************************************************
	 *
	 ******************************************************************/
	private void handleDataRequest(HttpServletRequest request, HttpServletResponse response) {
		
		String action = request.getParameter("action");
		String item = request.getParameter("item");
		//int	userID = CFW.Context.Request.getUser().id();
			
		JSONResponse jsonResponse = new JSONResponse();		

		switch(action.toLowerCase()) {
				
			case "getform": 			
				switch(item.toLowerCase()) {
					case "basicmultiform": 	createMultiForm(jsonResponse);
											break;
					
					default: 			CFW.Messages.itemNotSupported(item);
										break;
				}
				break;
						
			default: 			CFW.Messages.actionNotSupported(action);
								break;
								
		}
	}
	
	
	
	/******************************************************************
	 *
	 ******************************************************************/
	private void createMultiForm(JSONResponse json) {

		ArrayList<CFWObject> personList = new CFWSQL(new Person())
				.select()
				.limit(15)
				.getAsObjectList();
		
		if(personList.size() != 0) {
			
			CFWMultiForm editPersonForm = new CFWMultiForm("cfwMultiFormExample"+CFW.Random.randomStringAlphaNumerical(12), "Save", personList);
			
			editPersonForm.setMultiFormHandler(new CFWMultiFormHandlerDefault());
			
			editPersonForm.appendToPayload(json);
			json.setSuccess(true);	
		}
	}
}