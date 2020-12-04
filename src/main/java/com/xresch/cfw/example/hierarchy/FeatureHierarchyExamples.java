package com.xresch.cfw.example.hierarchy;

import com.xresch.cfw._main.CFW;
import com.xresch.cfw._main.CFWAppFeature;
import com.xresch.cfw._main.CFWApplicationExecutor;
import com.xresch.cfw._main.CFWExtensionFeature;
import com.xresch.cfw.example._main.ExampleExtensionApplication;
import com.xresch.cfw.response.bootstrap.MenuItem;
import com.xresch.cfw.utils.CFWRandom;

@CFWExtensionFeature
public class FeatureHierarchyExamples extends CFWAppFeature {
	
	public static final String RESOURCE_PACKAGE = "com.xresch.cfw.example.hierarchy.resources";
	
	@Override
	public void register() {
		//----------------------------------
		// Register Package
		CFW.Files.addAllowedPackage(RESOURCE_PACKAGE);
		
		//-------------------------------------
    	// Register Menus that needs permission
		// to access
		ExampleExtensionApplication.EXTENSION_MENU_ROOT.addChild(
			new MenuItem("Hierarchy Examples")
				.faicon("fas fa-sitemap")
				.addPermission(ExampleExtensionApplication.PERMISSION_CFWSAMPLES)
				.href("/app/hierarchyexamples")
		);
		
		//-------------------------------------
    	// Register Objects
		CFW.Registry.Objects.addCFWObject(HierarchicalPerson.class);		
	}

	@Override
	public void initializeDB() {
				
		//-------------------------------------
		// Create Testdata
		if(HierarchicalPersonDBMethods.getCount() == 0) {
		
			//----------------------------------
			// Create 3 hierarchy root elements
			for(int i = 0; i < 3; i++) {
				String firstname = CFWRandom.randomFirstnameOfGod();
				String lastname = CFWRandom.randomLastnameSweden();
				String location = CFWRandom.randomMythicalLocation();
				String email = firstname.toLowerCase() + "." + lastname.toLowerCase() + "@"+location.replace(" ", "-").toLowerCase() + ".com";
				
				HierarchicalPerson ceo = HierarchicalPersonDBMethods.createGetObject(
						new HierarchicalPerson()
							.firstname(firstname)
							.lastname(lastname)
							.email(email)
							.location(location)
							.likesTiramisu(CFWRandom.getBoolean())		
				);
				
				//-----------------------------
				// Create Subordinates
				createSubordinates(ceo, 6, 6, 0, 4);
			}
			

		}
				
	}

	private void createSubordinates(HierarchicalPerson parent, int minSubordinates, int maxSubordinates, int currentDepth, int maxDepth) {
		
		//-----------------------------------------
		// 
		//-----------------------------------------
		int max = CFWRandom.randomIntegerInRange(minSubordinates, maxSubordinates);

		for(int i = 0; i < max; i++) {

			String firstname = CFWRandom.randomFirstnameOfGod();
			String lastname = CFWRandom.randomLastnameSweden();
			String location = CFWRandom.randomMythicalLocation();
			String email = firstname.toLowerCase() + "." + lastname.toLowerCase() + "@"+location.replace(" ", "-").toLowerCase() + ".com";
			
			HierarchicalPerson person = new HierarchicalPerson()
				.firstname(firstname)
				.lastname(lastname)
				.email(email)
				.location(location)
				.likesTiramisu(CFWRandom.getBoolean());
			
			person.setParent(parent);
			
			HierarchicalPerson personFromDB = HierarchicalPersonDBMethods.createGetObject(person);
			
			//-----------------------------
			// Create Subordinates
			if(currentDepth < maxDepth) {
				int newMin = (minSubordinates-2 > 0) ? minSubordinates-2 : 0;
				createSubordinates(personFromDB, newMin, maxSubordinates-1, currentDepth+1, maxDepth);
			}
		}
	}

	@Override
	public void addFeature(CFWApplicationExecutor executor) {
		
		executor.addAppServlet(HierarchyExamplesServlet.class, "/hierarchyexamples");

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
