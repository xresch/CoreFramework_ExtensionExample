package com.xresch.cfw.example.main;

import java.util.logging.Logger;

import com.xresch.cfw.datahandling.CFWObject;
import com.xresch.cfw.db.CFWDBDefaultOperations;
import com.xresch.cfw.db.PrecheckHandler;
import com.xresch.cfw.example.main.Person.PersonFields;
import com.xresch.cfw.logging.CFWLog;

/**************************************************************************************************************
 * @author Reto Scheiwiller
 **************************************************************************************************************/
public class PersonDBMethods {
	
	private static Class<Person> cfwObjectClass = Person.class;
	
	public static Logger logger = CFWLog.getLogger(PersonDBMethods.class.getName());
		
	//####################################################################################################
	// Preckeck Initialization
	//####################################################################################################
	private static PrecheckHandler prechecksCreateUpdate =  new PrecheckHandler() {
		public boolean doCheck(CFWObject object) {
			
			Person Person = (Person)object;
			
			if(Person == null || Person.firstname().isEmpty()) {
				new CFWLog(logger)
					.method("doCheck")
					.warn("Please specify a firstname for the person.", new Throwable());
				return false;
			}

			return true;
		}
	};
	
	
	private static PrecheckHandler prechecksDelete =  new PrecheckHandler() {
		public boolean doCheck(CFWObject object) {
			Person Person = (Person)object;
			
			if(Person != null && Person.likesTiramisu() == true) {
				new CFWLog(logger)
				.method("doCheck")
				.severe("The Person '"+Person.firstname()+"' cannot be deleted as people that like tiramisu will prevail for all eternity!", new Throwable());
				return false;
			}
			
			return true;
		}
	};
		
	//####################################################################################################
	// CREATE
	//####################################################################################################
	public static boolean	create(Person... items) 	{ return CFWDBDefaultOperations.create(prechecksCreateUpdate, items); }
	public static boolean 	create(Person item) 		{ return CFWDBDefaultOperations.create(prechecksCreateUpdate, item);}
	public static Integer 	createGetPrimaryKey(Person item) { return CFWDBDefaultOperations.createGetPrimaryKey(prechecksCreateUpdate, item);}
	
	
	//####################################################################################################
	// UPDATE
	//####################################################################################################
	public static boolean 	update(Person... items) 	{ return CFWDBDefaultOperations.update(prechecksCreateUpdate, items); }
	public static boolean 	update(Person item) 		{ return CFWDBDefaultOperations.update(prechecksCreateUpdate, item); }
	
	//####################################################################################################
	// DELETE
	//####################################################################################################
	public static boolean 	deleteByID(int id) 					{ return CFWDBDefaultOperations.deleteFirstBy(prechecksDelete, cfwObjectClass, PersonFields.PK_ID.toString(), id); }
	public static boolean 	deleteMultipleByID(String itemIDs) 	{ return CFWDBDefaultOperations.deleteMultipleByID(cfwObjectClass, itemIDs); }
	
	//####################################################################################################
	// DUPLICATE
	//####################################################################################################
	public static boolean duplicateByID(String id ) {
		Person person = selectByID(id);
		if(person != null) {
			person.id(null);
			return create(person);
		}
		
		return false;
	}
		
	//####################################################################################################
	// SELECT
	//####################################################################################################
	public static Person selectByID(String id ) {
		return CFWDBDefaultOperations.selectFirstBy(cfwObjectClass, PersonFields.PK_ID.toString(), id);
	}
	
	public static Person selectByID(int id ) {
		return CFWDBDefaultOperations.selectFirstBy(cfwObjectClass, PersonFields.PK_ID.toString(), id);
	}
	
	public static Person selectFirstByName(String name) { 
		return CFWDBDefaultOperations.selectFirstBy(cfwObjectClass, PersonFields.FIRSTNAME.toString(), name);
	}
	
	
	
	public static String getPersonListAsJSON() {
		
		return new Person()
				.queryCache(PersonDBMethods.class, "getPersonListAsJSON")
				.select()
				.getAsJSON();
		
	}
	
	public static int getCount() {
		
		return new Person()
				.queryCache(PersonDBMethods.class, "getCount")
				.selectCount()
				.getCount();
		
	}
	
	
	
	
	

	


		
}
