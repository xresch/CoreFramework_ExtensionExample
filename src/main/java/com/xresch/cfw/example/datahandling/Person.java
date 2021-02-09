package com.xresch.cfw.example.datahandling;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.xresch.cfw.datahandling.CFWField;
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
public class Person extends CFWObject {
	
	public static String TABLE_NAME = "EXAMPLE_PERSON";
	
	public enum PersonFields{
		PK_ID, 
		FIRSTNAME, 
		LASTNAME, 
		LOCATION,
		EMAIL, 
		LIKES_TIRAMISU
	}
	
	private CFWField<Integer> id = CFWField.newInteger(FormFieldType.HIDDEN, PersonFields.PK_ID)
								   .setPrimaryKeyAutoIncrement(this)
								   .setDescription("The user id.")
								   .apiFieldType(FormFieldType.NUMBER)
								   .setValue(null);
	
	
	private CFWField<String> firstname = CFWField.newString(FormFieldType.TEXT, PersonFields.FIRSTNAME)
			.setColumnDefinition("VARCHAR(255)")
			.setDescription("The firstname of the user.")
			.addValidator(new LengthValidator(-1, 255));
	
	private CFWField<String> lastname = CFWField.newString(FormFieldType.TEXT, PersonFields.LASTNAME)
			.setColumnDefinition("VARCHAR(255)")
			.setDescription("The lastname of the user.")
			.addValidator(new LengthValidator(-1, 255));
	
	private CFWField<String> email = CFWField.newString(FormFieldType.EMAIL, PersonFields.EMAIL)
			.setColumnDefinition("VARCHAR(255)")
			.setDescription("The user email address.")
			.addValidator(new LengthValidator(-1, 255))
			.addValidator(new EmailValidator());
	
	private CFWField<String> location = CFWField.newString(FormFieldType.TEXT, PersonFields.LOCATION)
			.setColumnDefinition("VARCHAR(255)")
			.setDescription("The lastname of the user.")
			.addValidator(new LengthValidator(-1, 255));
	
	private CFWField<Boolean> likesTiramisu = CFWField.newBoolean(FormFieldType.BOOLEAN, PersonFields.LIKES_TIRAMISU)
					.setDescription("Define if the person loves tiramisu.")
					.setValue(false);
	

	public Person() {
		initializeFields();
	}
	
	public Person(String username) {
		initializeFields();
	}
	
	public Person(ResultSet result) throws SQLException {
		initializeFields();
		this.mapResultSet(result);

	}
		
	private void initializeFields() {
		this.setTableName(TABLE_NAME);
		this.enableFulltextSearch();
		
		this.addFields(id, 
				firstname, 
				lastname, 
				email,
				location,
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
						PersonFields.PK_ID.toString(), 
						PersonFields.EMAIL.toString(),
						PersonFields.FIRSTNAME.toString(),
						PersonFields.LASTNAME.toString(),
						PersonFields.LOCATION.toString(),
						PersonFields.LIKES_TIRAMISU.toString()
				};
		
		String[] outputFields = 
				new String[] {
						PersonFields.PK_ID.toString(), 
						PersonFields.EMAIL.toString(),
						PersonFields.FIRSTNAME.toString(),
						PersonFields.LASTNAME.toString(),
						PersonFields.LOCATION.toString(),
						PersonFields.LIKES_TIRAMISU.toString()
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
	
	public Person id(Integer id) {
		this.id.setValue(id);
		return this;
	}
	
	public String email() {
		return email.getValue();
	}
	
	public Person email(String email) {
		this.email.setValue(email);
		return this;
	}
	
	public String firstname() {
		return firstname.getValue();
	}
	
	public Person firstname(String firstname) {
		this.firstname.setValue(firstname);
		return this;
	}
	
	public String lastname() {
		return lastname.getValue();
	}
	
	public Person lastname(String lastname) {
		this.lastname.setValue(lastname);
		return this;
	}
	
	public String location() {
		return location.getValue();
	}
	
	public Person location(String location) {
		this.location.setValue(location);
		return this;
	}
	
	public boolean likesTiramisu() {
		return likesTiramisu.getValue();
	}
	
	public Person likesTiramisu(boolean isForeign) {
		this.likesTiramisu.setValue(isForeign);
		return this;
	}
	
}
