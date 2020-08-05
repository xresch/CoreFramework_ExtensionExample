
/**************************************************************************************************************
 * 
 * @author Reto Scheiwiller, (c) Copyright 2019 
 **************************************************************************************************************/

var jsexamples_URL = "./jsexamples";
var JSEXAMPLES_LAST_OPTIONS = null;

/******************************************************************
 * Reset the view.
 ******************************************************************/
function jsexamples_createTabs(){
	var pillsTab = $("#pills-tab");
	
	if(pillsTab.length == 0){
		
		var list = $('<ul class="nav nav-pills mb-3" id="pills-tab" role="tablist">');
		
		list.append('<li class="nav-item"><a class="nav-link" id="tab-datahandling" data-toggle="pill" href="#" role="tab" onclick="jsexamples_draw({tab: \'datahandling\'})"><i class="fas fa-share-alt mr-2"></i>Data Handling</a></li>');

		var parent = $("#cfw-container");
		parent.append(list);
		parent.append('<div id="tab-content"></div>');
	}

}

/******************************************************************
 * Create Role
 ******************************************************************/
function jsexamples_addPerson(){
	
	var html = $('<div>');	

	CFW.http.getForm('cfwCreatePersonForm', html);
	
	CFW.ui.showModal(
			"Create Person", 
			html, 
			"CFW.cache.clearCache(); jsexamples_draw(JSEXAMPLES_LAST_OPTIONS)"
		);
	
}
/******************************************************************
 * Edit Role
 ******************************************************************/
function jsexamples_edit(id){
	
	//-----------------------------------
	// Details
	//-----------------------------------
	var detailsDiv = $('<div id="jsexamples-details">');
	
	CFW.ui.showModal(
		"Edit Person", 
		detailsDiv, 
		"CFW.cache.clearCache(); jsexamples_draw(JSEXAMPLES_LAST_OPTIONS)"
	);
	
	//-----------------------------------
	// Load Form
	//-----------------------------------
	CFW.http.createForm(jsexamples_URL, {action: "getform", item: "editperson", id: id}, detailsDiv);
	
}

/******************************************************************
 * Delete
 ******************************************************************/
function jsexamples_delete(id){
	
	params = {action: "delete", item: "person", id: id};
	CFW.http.getJSON(jsexamples_URL, params, 
		function(data) {
			if(data.success){
				//CFW.ui.showSmallModal('Success!', '<span>The selected '+item+' were deleted.</span>');
				//clear cache and reload data
				CFW.cache.clearCache();
				jsexamples_draw(JSEXAMPLES_LAST_OPTIONS);
			}else{
				CFW.ui.showSmallModal("Error!", '<span>The selected person could <b style="color: red">NOT</b> be deleted.</span>');
			}
	});
}

/******************************************************************
 * Delete
 ******************************************************************/
function jsexamples_duplicate(id){
	
	params = {action: "duplicate", item: "person", id: id};
	CFW.http.getJSON(jsexamples_URL, params, 
		function(data) {
			if(data.success){
				CFW.cache.clearCache();
				jsexamples_draw(JSEXAMPLES_LAST_OPTIONS);
			}
	});
}


/******************************************************************
 * Print the list of roles;
 * 
 * @param data as returned by CFW.http.getJSON()
 * @return 
 ******************************************************************/
function jsexamples_printList(data, type){
	
	var parent = $("#tab-content");

	//--------------------------------
	// Button
	var addPersonButton = $('<button class="btn btn-sm btn-success mb-2" onclick="jsexamples_addPerson()">'
						+ '<i class="fas fa-plus-circle"></i>Add Person</button>');

	parent.append(addPersonButton);
	
	
	//--------------------------------
	// Table
	
	if(data.payload != undefined){
		
		var resultCount = data.payload.length;
		if(resultCount == 0){
			CFW.ui.addToastInfo("Hmm... seems there aren't any people in the list.");
			return;
		}
				
		
		//======================================
		// Prepare actions
		var actionButtons = [];
		//-------------------------
		// Edit Button
		actionButtons.push(
			function (record, id){ 
				return '<button class="btn btn-primary btn-sm" alt="Edit" title="Edit" '
						+'onclick="jsexamples_edit('+id+');">'
						+ '<i class="fa fa-pen"></i>'
						+ '</button>';

			});

		
		//-------------------------
		// Duplicate Button
		actionButtons.push(
			function (record, id){
				return '<button class="btn btn-warning btn-sm" alt="Duplicate" title="Duplicate" '
						+'onclick="CFW.ui.confirmExecute(\'This will create a duplicate of <b>\\\''+record.FIRSTNAME.replace(/\"/g,'&quot;')+'\\\'</b>.\', \'Do it!\', \'jsexamples_duplicate('+id+');\')">'
						+ '<i class="fas fa-clone"></i>'
						+ '</button>';
		});
		
		//-------------------------
		// Delete Button
		actionButtons.push(
			function (record, id){
				return '<button class="btn btn-danger btn-sm" alt="Delete" title="Delete" '
						+'onclick="CFW.ui.confirmExecute(\'Do you want to delete <b>\\\''+record.FIRSTNAME.replace(/\"/g,'&quot;')+'\\\'</b>?\', \'Delete\', \'jsexamples_delete('+id+');\')">'
						+ '<i class="fa fa-trash"></i>'
						+ '</button>';

			});
		

		//-----------------------------------
		// Render Data
		var rendererSettings = {
			 	idfield: 'PK_ID',
			 	bgstylefield: null,
			 	textstylefield: null,
			 	titlefields: ['NAME'],
			 	titledelimiter: ' ',
			 	visiblefields: ['PK_ID', 'FIRSTNAME', 'LASTNAME', 'LOCATION', "EMAIL", "LIKES_TIRAMISU"],
			 	labels: {
			 		PK_ID: "ID",
			 	},
			 	customizers: {
			 		LIKES_TIRAMISU: function(record, value) { 
			 			var likesTiramisu = value;
			 			if(likesTiramisu){
								return '<span class="badge badge-success m-1">true</span>';
						}else{
							return '<span class="badge badge-danger m-1">false</span>';
						}
			 			 
			 		}
			 	},
				actions: actionButtons,
//				bulkActions: {
//					"Edit": function (elements, records, values){ alert('Edit records '+values.join(',')+'!'); },
//					"Delete": function (elements, records, values){ $(elements).remove(); },
//				},
//				bulkActionsPos: "both",
				data: data.payload,
				rendererSettings: {
					table: {narrow: false, filterable: true}
				},
			};
				
		var renderResult = CFW.render.getRenderer('table').render(rendererSettings);	
		
		parent.append(renderResult);
		
	}else{
		CFW.ui.addAlert('error', 'Something went wrong and no users can be displayed.');
	}
}

/******************************************************************
 * Main method for building the different views.
 * 
 * @param options Array with arguments:
 * 	{
 * 		tab: 'mydashboards|shareddashboards|admindashboards', 
 *  }
 * @return 
 ******************************************************************/

function jsexamples_initialDraw(){
	
	jsexamples_createTabs();
	
	var tabToDisplay = CFW.cache.retrieveValue("jsexamples-lasttab", "datahandling");
	
	$('#tab-'+tabToDisplay).addClass('active');
	
	jsexamples_draw({tab: tabToDisplay});
}

function jsexamples_draw(options){
	JSEXAMPLES_LAST_OPTIONS = options;
	
	CFW.cache.storeValue("jsexamples-lasttab", options.tab);
	$("#tab-content").html("");
	
	CFW.ui.toogleLoader(true);
	
	window.setTimeout( 
	function(){
		
		switch(options.tab){
			case "datahandling":	CFW.http.fetchAndCacheData(jsexamples_URL, {action: "fetch", item: "personlist"}, "personlist", jsexamples_printList);
										break;						
			default:				CFW.ui.addToastDanger('This tab is unknown: '+options.tab);
		}
		
		CFW.ui.toogleLoader(false);
	}, 50);
}