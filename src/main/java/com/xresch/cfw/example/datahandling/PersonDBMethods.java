package com.xresch.cfw.example.datahandling;

import java.util.logging.Logger;

import com.google.common.base.Strings;
import com.xresch.cfw.datahandling.CFWObject;
import com.xresch.cfw.db.CFWDBDefaultOperations;
import com.xresch.cfw.db.CFWSQL;
import com.xresch.cfw.db.PrecheckHandler;
import com.xresch.cfw.example.datahandling.Person.PersonFields;
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
		
		return new CFWSQL(new Person())
				.queryCache()
				.select()
				.getAsJSON();
		
	}
	
	public static String getPartialPersonListAsJSON(String pageSize, String pageNumber, String filterquery) {
		return getPartialPersonListAsJSON(Integer.parseInt(pageSize), Integer.parseInt(pageNumber), filterquery);
	}
	
	
	public static String getPartialPersonListAsJSON(int pageSize, int pageNumber, String filterquery) {	
		
		if(Strings.isNullOrEmpty(filterquery)) {
			//-------------------------------------
			// Unfiltered
			return new CFWSQL(new Person())
				.queryCache()
				.columnSubquery("TOTAL_RECORDS", "COUNT(*) OVER()")
				.select()
				.limit(pageSize)
				.offset(pageSize*(pageNumber-1))
				.getAsJSON();
		}else {
			//-------------------------------------
			// Filter with fulltext search
			// Enabled by CFWObject.enableFulltextSearch()
			// on the Person Object
			return new CFWSQL(new Person())
					.queryCache()
					.select()
					.fulltextSearch()
						.custom(filterquery)
						.build(pageSize, pageSize*(pageNumber-1))
					.getAsJSON();
		}
		
	}
	
	public static int getCount() {
		
		return new Person()
				.queryCache(PersonDBMethods.class, "getCount")
				.selectCount()
				.getCount();
		
	}
	
	
	
	
	

	


		
}
