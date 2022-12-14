package com.xresch.cfw.example.hierarchy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.xresch.cfw.datahandling.CFWField;
import com.xresch.cfw.datahandling.CFWHierarchyConfig;
import com.xresch.cfw.datahandling.CFWField.FormFieldType;
import com.xresch.cfw.datahandling.CFWObject;
import com.xresch.cfw.features.api.APIDefinition;
import com.xresch.cfw.features.api.APIDefinitionFetch;
import com.xresch.cfw.validation.EmailValidator;
import com.xresch.cfw.validation.LengthValidator;

/**************************************************************************************************************
 * 
 * @author Reto Scheiwiller
 **************************************************************************************************************/
public class HierarchicalPerson extends CFWObject {
	
	public static String TABLE_NAME = "EXAMPLE_HIERARCHICAL_PERSON";
	
	public enum HierarchicalPersonFields{
		PK_ID, 
		FIRSTNAME, 
		LASTNAME, 
		LEVEL,
		LOCATION,
		EMAIL, 
		LIKES_TIRAMISU
	}
	
//	public enum HierarchicalPersonLevel {
//		EMPEROR,
//		MASTER,
//		SUBORDINATE,
//	}
	
		private static CFWHierarchyConfig hierarchyConfig = 
				new CFWHierarchyConfig(
						  HierarchicalPerson.class
						, new Object[] {HierarchicalPersonFields.FIRSTNAME, HierarchicalPersonFields.LASTNAME}
						, new Object[] {HierarchicalPersonFields.PK_ID, HierarchicalPersonFields.FIRSTNAME, HierarchicalPersonFields.LASTNAME}
						) {
		
		@Override
		public boolean canBeReordered(CFWObject targetParent, CFWObject sortedElement) {
			return true;
		}
		
		@Override
		public boolean canAccessHierarchy(String rootElementID) {
			return true;
		}
	};
	
	private CFWField<Integer> id = CFWField.newInteger(FormFieldType.HIDDEN, HierarchicalPersonFields.PK_ID)
								   .setPrimaryKeyAutoIncrement(this)
								   .setDescription("The user id.")
								   .apiFieldType(FormFieldType.NUMBER)
								   .setValue(null);
	
	private CFWField<String> firstname = CFWField.newString(FormFieldType.TEXT, HierarchicalPersonFields.FIRSTNAME)
			.setColumnDefinition("VARCHAR(255)")
			.setDescription("The firstname of the user.")
			.addValidator(new LengthValidator(-1, 255));
	
//	private CFWField<String> level = CFWField.newString(FormFieldType.SELECT, HierarchicalPersonFields.LEVEL)
//			.setColumnDefinition("VARCHAR(255)")
//			.setDescription("The level of the job the person is executing.")
//			.addValidator(new LengthValidator(-1, 255));
	
	private CFWField<String> lastname = CFWField.newString(FormFieldType.TEXT, HierarchicalPersonFields.LASTNAME)
			.setColumnDefinition("VARCHAR(255)")
			.setDescription("The lastname of the user.")
			.addValidator(new LengthValidator(-1, 255));
	
	private CFWField<String> email = CFWField.newString(FormFieldType.EMAIL, HierarchicalPersonFields.EMAIL)
			.setColumnDefinition("VARCHAR(255)")
			.setDescription("The user email address.")
			.addValidator(new LengthValidator(-1, 255))
			.addValidator(new EmailValidator());
	
	private CFWField<String> location = CFWField.newString(FormFieldType.TEXT, HierarchicalPersonFields.LOCATION)
			.setColumnDefinition("VARCHAR(255)")
			.setDescription("The lastname of the user.")
			.addValidator(new LengthValidator(-1, 255));
	
	private CFWField<Boolean> likesTiramisu = CFWField.newBoolean(FormFieldType.BOOLEAN, HierarchicalPersonFields.LIKES_TIRAMISU)
					.setDescription("Foreign users are managed by other authentication providers like LDAP. Password in database is ignored when a foreign authentication provider is used.")
					.setValue(false);
	

	public HierarchicalPerson() {
		initializeFields();
	}
	
	public HierarchicalPerson(String username) {
		initializeFields();
	}
	
	public HierarchicalPerson(ResultSet result) throws SQLException {
		initializeFields();
		this.mapResultSet(result);
	}
		
	private void initializeFields() {
		this.setTableName(TABLE_NAME);
		
		this.setHierarchyConfig(hierarchyConfig);
		
		this.addFields(id, 
				firstname, 
				lastname, 
				email,
				location,
				//level,
				likesTiramisu
				);
	}
	
	/**************************************************************************************
	 * 
	 **************************************************************************************/
	public void initDB() {

	}
	
	/**************************************************************************************
	 * 
	 **************************************************************************************/
	public ArrayList<APIDefinition> getAPIDefinitions() {
		ArrayList<APIDefinition> apis = new ArrayList<APIDefinition>();
		
		String[] inputFields = 
				new String[] {
						HierarchicalPersonFields.PK_ID.toString(), 
						HierarchicalPersonFields.EMAIL.toString(),
						HierarchicalPersonFields.FIRSTNAME.toString(),
						HierarchicalPersonFields.LASTNAME.toString(),
						HierarchicalPersonFields.LOCATION.toString(),
						//HierarchicalPersonFields.LEVEL.toString(),
						HierarchicalPersonFields.LIKES_TIRAMISU.toString()
				};
		
		String[] outputFields = 
				new String[] {
						HierarchicalPersonFields.PK_ID.toString(), 
						HierarchicalPersonFields.EMAIL.toString(),
						HierarchicalPersonFields.FIRSTNAME.toString(),
						HierarchicalPersonFields.LASTNAME.toString(),
						HierarchicalPersonFields.LOCATION.toString(),
						//HierarchicalPersonFields.LEVEL.toString(),
						HierarchicalPersonFields.LIKES_TIRAMISU.toString()
				};

		//----------------------------------
		// fetchJSON
		APIDefinitionFetch fetchDataAPI = 
				new APIDefinitionFetch(
						this.getClass(),
						this.getClass().getSimpleName(),
						"fetchData",
						inputFields,
						outputFields
				);
		
		apis.add(fetchDataAPI);
		
		return apis;
	}
	
	public Integer id() {
		return id.getValue();
	}
	
	public HierarchicalPerson id(Integer id) {
		this.id.setValue(id);
		return this;
	}
	
	public String email() {
		return email.getValue();
	}
	
	public HierarchicalPerson email(String email) {
		this.email.setValue(email);
		return this;
	}
	
	public String firstname() {
		return firstname.getValue();
	}
	
	public HierarchicalPerson firstname(String firstname) {
		this.firstname.setValue(firstname);
		return this;
	}
	
	public String lastname() {
		return lastname.getValue();
	}
	
	public HierarchicalPerson lastname(String lastname) {
		this.lastname.setValue(lastname);
		return this;
	}
	
	public String location() {
		return location.getValue();
	}
	
	public HierarchicalPerson location(String location) {
		this.location.setValue(location);
		return this;
	}
	
	public boolean likesTiramisu() {
		return likesTiramisu.getValue();
	}
	
	public HierarchicalPerson likesTiramisu(boolean isForeign) {
		this.likesTiramisu.setValue(isForeign);
		return this;
	}
	
}
