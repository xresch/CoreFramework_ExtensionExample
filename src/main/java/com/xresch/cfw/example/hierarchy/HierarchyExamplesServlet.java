package com.xresch.cfw.example.hierarchy;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xresch.cfw._main.CFW;
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
public class HierarchyExamplesServlet extends HttpServlet
{

	private static final long serialVersionUID = 1L;
	
	public HierarchyExamplesServlet() {
	
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

				html.addJSFileBottom(HandlingType.JAR_RESOURCE, FeatureHierarchyExamples.RESOURCE_PACKAGE, "hierarchy_examples.js");
				

				html.addJavascriptCode("hierarchyexamples_draw();");
				
		        response.setContentType("text/html");
		        response.setStatus(HttpServletResponse.SC_OK);
			}else {
				handleDataRequest(request, response);
			}
		}else {
			CFW.Context.Request.addMessageAccessDenied();
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
				}
				break;
			
			case "fetchpartial": 			
				switch(item.toLowerCase()) {
					case "personlist": 		String pagesize = request.getParameter("pagesize");
											String pagenumber = request.getParameter("pagenumber");
											String filterquery = request.getParameter("filterquery");
											jsonResponse.getContent().append(PersonDBMethods.getPartialPersonListAsJSON(pagesize, pagenumber, filterquery));
	  										break;
				}
				break;	
			
			case "delete": 			
				switch(item.toLowerCase()) {

					case "person": 		deletePerson(jsonResponse, ID);
										break;  
										
					default: 			CFW.Context.Request.addAlertMessage(MessageType.ERROR, "The value of item '"+item+"' is not supported.");
										break;
				}
				break;	
				
			case "duplicate": 			
				switch(item.toLowerCase()) {

					case "person": 	 	duplicatePerson(jsonResponse, ID);
										break;  
										
					default: 			CFW.Context.Request.addAlertMessage(MessageType.ERROR, "The value of item '"+item+"' is not supported.");
										break;
				}
				break;	
			case "getform": 			
				switch(item.toLowerCase()) {
					case "editperson": 	createEditForm(jsonResponse, ID);
					break;
					
					default: 			CFW.Context.Request.addAlertMessage(MessageType.ERROR, "The value of item '"+item+"' is not supported.");
										break;
				}
				break;
						
			default: 			CFW.Context.Request.addAlertMessage(MessageType.ERROR, "The action '"+action+"' is not supported.");
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
				
		CFWForm createPersonForm = new Person().toForm("cfwCreatePersonForm", "Create HierarchicalPerson");
		
		createPersonForm.setFormHandler(new CFWFormHandler() {
			
			@Override
			public void handleForm(HttpServletRequest request, HttpServletResponse response, CFWForm form, CFWObject origin) {
								
				if(origin != null) {
					if(origin.mapRequestParameters(request)) {
						Person Person = (Person)origin;
						
						if(PersonDBMethods.create(Person) ) {
							CFW.Context.Request.addAlertMessage(MessageType.SUCCESS, "HierarchicalPerson created successfully!");
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
			
			CFWForm editPersonForm = Person.toForm("cfwEditPersonForm"+ID, "Update HierarchicalPerson");
			
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