package com.xresch.cfw.example.hierarchy;

import java.util.logging.Logger;

import com.google.common.base.Strings;
import com.xresch.cfw.datahandling.CFWObject;
import com.xresch.cfw.db.CFWDBDefaultOperations;
import com.xresch.cfw.db.CFWSQL;
import com.xresch.cfw.db.PrecheckHandler;
import com.xresch.cfw.example.hierarchy.HierarchicalPerson.HierarchicalPersonFields;
import com.xresch.cfw.logging.CFWLog;

/**************************************************************************************************************
 * @author Reto Scheiwiller
 **************************************************************************************************************/
public class HierarchicalPersonDBMethods {
	
	private static Class<HierarchicalPerson> cfwObjectClass = HierarchicalPerson.class;
	
	public static Logger logger = CFWLog.getLogger(HierarchicalPersonDBMethods.class.getName());
		
	//####################################################################################################
	// Preckeck Initialization
	//####################################################################################################
	private static PrecheckHandler prechecksCreateUpdate =  new PrecheckHandler() {
		public boolean doCheck(CFWObject object) {
			
			HierarchicalPerson HierarchicalPerson = (HierarchicalPerson)object;
			
			if(HierarchicalPerson == null || HierarchicalPerson.firstname().isEmpty()) {
				new CFWLog(logger)
					.warn("Please specify a firstname for the person.", new Throwable());
				return false;
			}

			return true;
		}
	};
	
	
	private static PrecheckHandler prechecksDelete =  new PrecheckHandler() {
		public boolean doCheck(CFWObject object) {
			HierarchicalPerson HierarchicalPerson = (HierarchicalPerson)object;
			
			if(HierarchicalPerson != null && HierarchicalPerson.likesTiramisu() == true) {
				new CFWLog(logger)
				.severe("The HierarchicalPerson '"+HierarchicalPerson.firstname()+"' cannot be deleted as people that like tiramisu will prevail for all eternity!", new Throwable());
				return false;
			}
			
			return true;
		}
	};
		
	//####################################################################################################
	// CREATE
	//####################################################################################################
	public static boolean	create(HierarchicalPerson... items) 	{ return CFWDBDefaultOperations.create(prechecksCreateUpdate, items); }
	public static boolean 	create(HierarchicalPerson item) 		{ return CFWDBDefaultOperations.create(prechecksCreateUpdate, item);}
	public static Integer 	createGetPrimaryKey(HierarchicalPerson item) { return CFWDBDefaultOperations.createGetPrimaryKey(prechecksCreateUpdate, item);}
	public static HierarchicalPerson createGetObject(HierarchicalPerson item) { 
		return HierarchicalPersonDBMethods.selectByID(
				CFWDBDefaultOperations.createGetPrimaryKey(prechecksCreateUpdate, item)
			);
	}
		
	//####################################################################################################
	// UPDATE
	//####################################################################################################
	public static boolean 	update(HierarchicalPerson... items) 	{ return CFWDBDefaultOperations.update(prechecksCreateUpdate, items); }
	public static boolean 	update(HierarchicalPerson item) 		{ return CFWDBDefaultOperations.update(prechecksCreateUpdate, item); }
	
	//####################################################################################################
	// DELETE
	//####################################################################################################
	public static boolean 	deleteByID(int id) 					{ return CFWDBDefaultOperations.deleteFirstBy(prechecksDelete, cfwObjectClass, HierarchicalPersonFields.PK_ID.toString(), id); }
	public static boolean 	deleteMultipleByID(String itemIDs) 	{ return CFWDBDefaultOperations.deleteMultipleByID(prechecksDelete, cfwObjectClass, itemIDs); }
	
	//####################################################################################################
	// DUPLICATE
	//####################################################################################################
	public static boolean duplicateByID(String id ) {
		HierarchicalPerson person = selectByID(id);
		if(person != null) {
			person.id(null);
			return create(person);
		}
		
		return false;
	}
		
	//####################################################################################################
	// SELECT
	//####################################################################################################
	public static HierarchicalPerson selectByID(String id ) {
		return CFWDBDefaultOperations.selectFirstBy(cfwObjectClass, HierarchicalPersonFields.PK_ID.toString(), id);
	}
	
	public static HierarchicalPerson selectByID(int id ) {
		return CFWDBDefaultOperations.selectFirstBy(cfwObjectClass, HierarchicalPersonFields.PK_ID.toString(), id);
	}
	
	public static HierarchicalPerson selectFirstByName(String name) { 
		return CFWDBDefaultOperations.selectFirstBy(cfwObjectClass, HierarchicalPersonFields.FIRSTNAME.toString(), name);
	}
	
	
	
	public static String getHierarchicalPersonListAsJSON() {
		
		return new CFWSQL(new HierarchicalPerson())
				.queryCache()
				.select()
				.getAsJSON();
		
	}
	
	public static String getPartialHierarchicalPersonListAsJSON(String pageSize, String pageNumber, String filterquery) {
		return getPartialHierarchicalPersonListAsJSON(Integer.parseInt(pageSize), Integer.parseInt(pageNumber), filterquery);
	}
	
	
	public static String getPartialHierarchicalPersonListAsJSON(int pageSize, int pageNumber, String filterquery) {	
		
		if(Strings.isNullOrEmpty(filterquery)) {
			//-------------------------------------
			// Unfiltered
			return new CFWSQL(new HierarchicalPerson())
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
			// on the HierarchicalPerson Object
			return new CFWSQL(new HierarchicalPerson())
					.queryCache()
					.select()
					.fulltextSearchLucene()
						.custom(filterquery)
						.build(pageSize, pageSize*(pageNumber-1))
					.getAsJSON();
		}
		
	}
	
	public static int getCount() {
		
		return new CFWSQL(new HierarchicalPerson())
				.queryCache()
				.selectCount()
				.getCount();
		
	}
	
	
	
	
	
	

	


		
}
