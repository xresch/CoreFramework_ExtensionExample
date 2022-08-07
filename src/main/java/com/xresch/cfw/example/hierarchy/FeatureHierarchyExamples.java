package com.xresch.cfw.example.hierarchy;

import com.xresch.cfw._main.CFW;
import com.xresch.cfw._main.CFWApplicationExecutor;
import com.xresch.cfw.datahandling.CFWHierarchy;
import com.xresch.cfw.example._main.ExampleExtensionApplication;
import com.xresch.cfw.response.bootstrap.MenuItem;
import com.xresch.cfw.spi.CFWAppFeature;
import com.xresch.cfw.utils.CFWRandom;

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
				
				
				HierarchicalPerson ceo = 
						new HierarchicalPerson()
							.firstname(firstname)
							.lastname(lastname)
							.email(email)
							.location(location)
							.likesTiramisu(CFWRandom.randomBoolean());
				
				//Make sure to set parent to root so you get proper insert order(affects DB column H_POS)
				Integer ceoID = CFWHierarchy.create(null, ceo);
				
				if(ceoID == null) {
					return; // error
				}
				//-----------------------------
				// Create Subordinates
				createSubordinates(ceoID, 6, 6, 0, 4);
			}
			

		}
				
	}

	private void createSubordinates(int parentID, int minSubordinates, int maxSubordinates, int currentDepth, int maxDepth) {
		
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
				.likesTiramisu(CFWRandom.randomBoolean());
			
			Integer createdPersonID = CFWHierarchy.create(parentID, person);
			
			if(createdPersonID == null) {
				return; // error
			}

			//-----------------------------
			// Create Subordinates
			if(currentDepth < maxDepth) {
				int newMin = (minSubordinates-2 > 0) ? minSubordinates-2 : 0;
				createSubordinates(createdPersonID, newMin, maxSubordinates-1, currentDepth+1, maxDepth);
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
