package com.xresch.cfw.example.javascript;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xresch.cfw._main.CFW;
import com.xresch.cfw._main.CFWMessages;
import com.xresch.cfw.caching.FileDefinition.HandlingType;
import com.xresch.cfw.datahandling.CFWForm;
import com.xresch.cfw.datahandling.CFWFormHandler;
import com.xresch.cfw.datahandling.CFWObject;
import com.xresch.cfw.example._main.ExampleExtensionApplication;
import com.xresch.cfw.example.datahandling.Person;
import com.xresch.cfw.example.datahandling.PersonDBMethods;
import com.xresch.cfw.response.HTMLResponse;
import com.xresch.cfw.response.JSONResponse;
import com.xresch.cfw.response.bootstrap.AlertMessage.MessageType;

/**************************************************************************************************************
 * 
 * @author Reto Scheiwiller, (c) Copyright 2020
 **************************************************************************************************************/
public class ServletJavascriptExamples extends HttpServlet
{

	private static final long serialVersionUID = 1L;
	
	public ServletJavascriptExamples() {
	
	}
	
	/******************************************************************
	 *
	 ******************************************************************/
	@Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
		HTMLResponse html = new HTMLResponse("Javascript Examples");
		
		if(CFW.Context.Request.hasPermission(ExampleExtensionApplication.PERMISSION_CFWSAMPLES)) {
			
			createForms();
			
			String action = request.getParameter("action");
			
			if(action == null) {

				html.addJSFileBottom(HandlingType.JAR_RESOURCE, FeatureJavascriptExamples.RESOURCE_PACKAGE, "jsexamples.js");
				

				html.addJavascriptCode("jsexamples_initialDraw();");
				
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
		String ID = request.getParameter("id");
		String IDs = request.getParameter("ids");
		//int	userID = CFW.Context.Request.getUser().id();
			
		JSONResponse jsonResponse = new JSONResponse();		

		switch(action.toLowerCase()) {
		
			case "fetch": 			
				switch(item.toLowerCase()) {
					case "personlist": 		jsonResponse.getContent().append(PersonDBMethods.getPersonListAsJSON());
	  										break;
	  										
					default: 				CFW.Messages.itemNotSupported(item);
											break;
				}
				break;
			
			case "fetchpartial": 			
				switch(item.toLowerCase()) {
					case "personlist": 		String pagesize = request.getParameter("pagesize");
											String pagenumber = request.getParameter("pagenumber");
											String filterquery = request.getParameter("filterquery");
											String sortby = request.getParameter("sortby");
											String isAscendingString = request.getParameter("isascending");
											boolean isAscending = (isAscendingString == null || isAscendingString.equals("true")) ? true : false;
											
											jsonResponse.getContent().append(PersonDBMethods.getPartialPersonListAsJSON(pagesize, pagenumber, filterquery, sortby, isAscending));
	  										break;
	  										
					default: 				CFW.Messages.itemNotSupported(item);
											break;
				}
				break;	
			
			case "delete": 			
				switch(item.toLowerCase()) {

					case "person": 		deletePerson(jsonResponse, ID);
										break;  
										
					default: 			CFW.Messages.itemNotSupported(item);
										break;
				}
				break;	
				
			case "duplicate": 			
				switch(item.toLowerCase()) {

					case "person": 	 	duplicatePerson(jsonResponse, ID);
										break;  
										
					default: 			CFW.Messages.itemNotSupported(item);
										break;
				}
				break;	
				
			case "getform": 			
				switch(item.toLowerCase()) {
					case "editperson": 	createEditForm(jsonResponse, ID);
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
	private void deletePerson(JSONResponse jsonResponse, String ID) {
		PersonDBMethods.deleteByID(Integer.parseInt(ID));
	}
	
	
	/******************************************************************
	 *
	 ******************************************************************/
	private void duplicatePerson(JSONResponse jsonResponse, String id) {
		PersonDBMethods.duplicateByID(id);
	}
	
	/******************************************************************
	 *
	 ******************************************************************/
	private void createForms() {
				
		CFWForm createPersonForm = new Person().toForm("cfwCreatePersonForm", "Create Person");
		
		createPersonForm.setFormHandler(new CFWFormHandler() {
			
			@Override
			public void handleForm(HttpServletRequest request, HttpServletResponse response, CFWForm form, CFWObject origin) {
								
				if(origin != null) {
					if(origin.mapRequestParameters(request)) {
						Person Person = (Person)origin;
						
						if(PersonDBMethods.create(Person) ) {
							CFW.Context.Request.addAlertMessage(MessageType.SUCCESS, "Person created successfully!");
						}
					}
				}
				
			}
		});
		
	}
	
	/******************************************************************
	 *
	 ******************************************************************/
	private void createEditForm(JSONResponse json, String ID) {

		Person Person = PersonDBMethods.selectByID(Integer.parseInt(ID));
		
		if(Person != null) {
			
			CFWForm editPersonForm = Person.toForm("cfwEditPersonForm"+ID, "Update Person");
			
			editPersonForm.setFormHandler(new CFWFormHandler() {
				
				@Override
				public void handleForm(HttpServletRequest request, HttpServletResponse response, CFWForm form, CFWObject origin) {
					
					if(origin.mapRequestParameters(request)) {
						
						if(PersonDBMethods.update((Person)origin)) {
							CFW.Context.Request.addAlertMessage(MessageType.SUCCESS, "Updated!");
						}
							
					}
					
				}
			});
			
			editPersonForm.appendToPayload(json);
			json.setSuccess(true);	
		}

	}
}