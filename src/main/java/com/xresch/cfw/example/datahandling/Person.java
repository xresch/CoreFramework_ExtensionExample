package com.xresch.cfw.example.datahandling;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.xresch.cfw._main.CFW;
import com.xresch.cfw.datahandling.CFWField;
import com.xresch.cfw.datahandling.CFWField.FormFieldType;
import com.xresch.cfw.datahandling.CFWObject;
import com.xresch.cfw.features.api.APIDefinition;
import com.xresch.cfw.features.api.APIDefinitionFetch;
import com.xresch.cfw.utils.CFWRandom;
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
		LIKES_TIRAMISU,
		CHARACTER
	}
	
	private static ArrayList<String> fullTextSearchColumns = new ArrayList<>();
	static {
		fullTextSearchColumns.add(PersonFields.PK_ID.toString());
		fullTextSearchColumns.add(PersonFields.FIRSTNAME.toString());
		fullTextSearchColumns.add(PersonFields.LASTNAME.toString());
		fullTextSearchColumns.add(PersonFields.LOCATION.toString());
		fullTextSearchColumns.add(PersonFields.EMAIL.toString());
		fullTextSearchColumns.add(PersonFields.LIKES_TIRAMISU.toString());
		fullTextSearchColumns.add(PersonFields.CHARACTER.toString());
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
	
	//------------------------------------------------------------------------------------------------
	// A tags field which will create a comma separated string
	private CFWField<String> character = 
			CFWField.newString(FormFieldType.TAGS, PersonFields.CHARACTER)
					.addAttribute("maxTags", "7")
					.setDescription("Type at least 3 characters to get suggestions.");
					
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
		this.enableFulltextSearch(fullTextSearchColumns);
		
		this.addFields(id, 
				firstname, 
				lastname, 
				email,
				location,
				likesTiramisu,
				character
				);
	}
	
	/**************************************************************************************
	 * 
	 **************************************************************************************/
	public void initDB() {
		//-------------------------------------
    	// Create Testdata
		if(PersonDBMethods.getCount() == 0) {
			
			for(int i = 0; i < 321; i++) {
				
				String firstname = CFWRandom.randomFirstnameOfGod();
				String lastname = CFWRandom.randomLastnameSweden();
				String location = CFWRandom.randomMythicalLocation();
				String email = firstname.toLowerCase() + "." + lastname.toLowerCase() + "@"+location.replace(" ", "-").toLowerCase() + ".com";
				
				int randomCount = CFW.Random.randomIntegerInRange(1, 7);
				String character = String.join(",", CFWRandom.randomArrayOfExaggaratingAdjectives(randomCount));
				
				PersonDBMethods.create(
					new Person()
						.firstname(firstname)
						.lastname(lastname)
						.email(email)
						.location(location)
						.likesTiramisu(CFWRandom.randomBoolean())
						.character(character)
				);
					
				
			}
		}

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
						PersonFields.LIKES_TIRAMISU.toString(),
						PersonFields.CHARACTER.toString(),
				};
		
		String[] outputFields = 
				new String[] {
						PersonFields.PK_ID.toString(), 
						PersonFields.EMAIL.toString(),
						PersonFields.FIRSTNAME.toString(),
						PersonFields.LASTNAME.toString(),
						PersonFields.LOCATION.toString(),
						PersonFields.LIKES_TIRAMISU.toString(),
						PersonFields.CHARACTER.toString(),
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
	
	public Person id(Integer value) {
		this.id.setValue(value);
		return this;
	}
	
	public String email() {
		return email.getValue();
	}
	
	public Person email(String value) {
		this.email.setValue(value);
		return this;
	}
	
	public String firstname() {
		return firstname.getValue();
	}
	
	public Person firstname(String value) {
		this.firstname.setValue(value);
		return this;
	}
	
	public String lastname() {
		return lastname.getValue();
	}
	
	public Person lastname(String value) {
		this.lastname.setValue(value);
		return this;
	}
	
	public String location() {
		return location.getValue();
	}
	
	public Person location(String value) {
		this.location.setValue(value);
		return this;
	}
	
	public boolean likesTiramisu() {
		return likesTiramisu.getValue();
	}
	
	public Person likesTiramisu(boolean value) {
		this.likesTiramisu.setValue(value);
		return this;
	}
	
	public String character() {
		return character.getValue();
	}
	
	public Person character(String value) {
		this.character.setValue(value);
		return this;
	}
	
}
