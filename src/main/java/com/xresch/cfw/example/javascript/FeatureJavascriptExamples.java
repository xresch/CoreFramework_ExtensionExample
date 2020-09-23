package com.xresch.cfw.example.javascript;

import com.xresch.cfw._main.CFW;
import com.xresch.cfw._main.CFWAppFeature;
import com.xresch.cfw._main.CFWApplicationExecutor;
import com.xresch.cfw._main.CFWExtensionFeature;
import com.xresch.cfw.example._main.ExampleExtensionApplication;
import com.xresch.cfw.example.datahandling.Person;
import com.xresch.cfw.example.datahandling.PersonDBMethods;
import com.xresch.cfw.response.bootstrap.MenuItem;
import com.xresch.cfw.utils.CFWRandom;

@CFWExtensionFeature
public class FeatureJavascriptExamples extends CFWAppFeature {
	
	public static final String RESOURCE_PACKAGE = "com.xresch.cfw.example.javascript.resources";
	
	@Override
	public void register() {
		//----------------------------------
		// Register Package
		CFW.Files.addAllowedPackage(RESOURCE_PACKAGE);
		
		//-------------------------------------
    	// Register Menus that needs permission
		// to access
		ExampleExtensionApplication.EXTENSION_MENU_ROOT.addChild(
			new MenuItem("Javascript Examples")
				.faicon("fab fa-js")
				.addPermission(ExampleExtensionApplication.PERMISSION_CFWSAMPLES)
				.href("/app/jsexamples")
		);
		
		//-------------------------------------
    	// Register Objects
		CFW.Registry.Objects.addCFWObject(Person.class);		
	}

	@Override
	public void initializeDB() {
		
		
		//-------------------------------------
    	// Create Testdata
		if(PersonDBMethods.getCount() == 0) {
			
			for(int i = 0; i < 321; i++) {
				
				String firstname = CFWRandom.randomFirstnameOfGod();
				String lastname = CFWRandom.randomLastnameSweden();
				String location = CFWRandom.randomMythicalLocation();
				String email = firstname.toLowerCase() + "." + lastname.toLowerCase() + "@"+location.replace(" ", "-").toLowerCase() + ".com";
				
				PersonDBMethods.create(
					new Person()
						.firstname(firstname)
						.lastname(lastname)
						.email(email)
						.location(location)
						.likesTiramisu(CFWRandom.getBoolean())
				);
					
				
			}
		}

	}

	@Override
	public void addFeature(CFWApplicationExecutor executor) {
		
		executor.addAppServlet(JavascriptExamplesServlet.class, "/jsexamples");

	}

	@Override
	public void startTasks() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopFeature() {
		// TODO Auto-generated method stub

	}

}
