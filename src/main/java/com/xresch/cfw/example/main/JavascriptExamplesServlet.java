package com.xresch.cfw.example.main;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xresch.cfw._main.CFW;
import com.xresch.cfw.caching.FileDefinition.HandlingType;
import com.xresch.cfw.response.HTMLResponse;
import com.xresch.cfw.response.JSONResponse;
import com.xresch.cfw.response.bootstrap.AlertMessage.MessageType;

/**************************************************************************************************************
 * 
 * @author Reto Scheiwiller, (c) Copyright 2020
 **************************************************************************************************************/
public class JavascriptExamplesServlet extends HttpServlet
{

	private static final long serialVersionUID = 1L;
	
	public JavascriptExamplesServlet() {
	
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

				html.addJSFileBottom(HandlingType.JAR_RESOURCE, ExampleExtensionApplication.RESOURCE_PACKAGE, "jsexamples.js");
				

				html.addJavascriptCode("jsexamples_initialDraw();");
				
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
					case "mydashboards": 		jsonResponse.getContent().append(CFW.DB.Dashboards.getUserDashboardListAsJSON());
	  											break;
	  											
					case "shareddashboards": 	jsonResponse.getContent().append(CFW.DB.Dashboards.getSharedDashboardListAsJSON());
												break;	
												
					case "admindashboards": 	jsonResponse.getContent().append(CFW.DB.Dashboards.getAdminDashboardListAsJSON());
												break;									
	  																					
					default: 					CFW.Context.Request.addAlertMessage(MessageType.ERROR, "The value of item '"+item+"' is not supported.");
												break;
				}
				break;
			

			case "delete": 			
				switch(item.toLowerCase()) {

					case "dashboards": 	deleteDashboards(jsonResponse, IDs);
										break;  
										
					default: 			CFW.Context.Request.addAlertMessage(MessageType.ERROR, "The value of item '"+item+"' is not supported.");
										break;
				}
				break;	
			case "getform": 			
				switch(item.toLowerCase()) {
					case "edit": 		createEditForm(jsonResponse, ID);
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
	private void deleteDashboards(JSONResponse jsonResponse, String IDs) {
		// TODO Auto-generated method stub
	}
	
	
	/******************************************************************
	 *
	 ******************************************************************/
	private void createForms() {
				
//		CFWForm createDashboardForm = new Dashboard().toForm("cfwCreateDashboardForm", "{!cfw_dashboard_create!}");
//		
//		createDashboardForm.setFormHandler(new CFWFormHandler() {
//			
//			@Override
//			public void handleForm(HttpServletRequest request, HttpServletResponse response, CFWForm form, CFWObject origin) {
//								
//				if(origin != null) {
//					
//					origin.mapRequestParameters(request);
//					Dashboard dashboard = (Dashboard)origin;
//					dashboard.foreignKeyUser(CFW.Context.Request.getUser().id());
//					if( CFW.DB.Dashboards.create(dashboard) ) {
//						CFW.Context.Request.addAlertMessage(MessageType.SUCCESS, "Dashboard created successfully!");
//					}
//				}
//				
//			}
//		});
//		
	}
	
	/******************************************************************
	 *
	 ******************************************************************/
	private void createEditForm(JSONResponse json, String ID) {

//		Dashboard dashboard = CFW.DB.Dashboards.selectByID(Integer.parseInt(ID));
//		
//		if(dashboard != null) {
//			
//			CFWForm editDashboardForm = dashboard.toForm("cfwEditDashboardForm"+ID, "Update Dashboard");
//			
//			editDashboardForm.setFormHandler(new CFWFormHandler() {
//				
//				@Override
//				public void handleForm(HttpServletRequest request, HttpServletResponse response, CFWForm form, CFWObject origin) {
//					
//					if(origin.mapRequestParameters(request)) {
//						
//						if(CFW.DB.Dashboards.update((Dashboard)origin)) {
//							CFW.Context.Request.addAlertMessage(MessageType.SUCCESS, "Updated!");
//						}
//							
//					}
//					
//				}
//			});
//			
//			editDashboardForm.appendToPayload(json);
//			json.setSuccess(true);	
//		}

	}
}