
/**************************************************************************************************************
 * This file contains various javascript examples.
 * For your convenience, this code is highly redundant for eassier copy&paste.
 * @author Reto Scheiwiller, (c) Copyright 2019 
 **************************************************************************************************************/

var JSEXAMPLES_URL = "./jsexamples";
var JSEXAMPLES_LAST_OPTIONS = null;

/******************************************************************
 * Reset the view.
 ******************************************************************/
function hierarchyexamples_createTabs(){
	var pillsTab = $("#pills-tab");
	
	if(pillsTab.length == 0){
		
		var list = $('<ul class="nav nav-pills mb-3" id="pills-tab" role="tablist">');
		
		list.append('<li class="nav-item"><a class="nav-link" id="tab-datahandling" data-toggle="pill" href="#" role="tab" onclick="hierarchyexamples_draw({tab: \'datahandling\'})"><i class="fas fa-share-alt mr-2"></i>Data Handling</a></li>');
		list.append('<li class="nav-item"><a class="nav-link" id="tab-pagination-static" data-toggle="pill" href="#" role="tab" onclick="hierarchyexamples_draw({tab: \'pagination-static\'})"><i class="fas fa-copy mr-2"></i>Pagination(Static)</a></li>');
		list.append('<li class="nav-item"><a class="nav-link" id="tab-pagination-dynamic" data-toggle="pill" href="#" role="tab" onclick="hierarchyexamples_draw({tab: \'pagination-dynamic\'})"><i class="fas fa-dna mr-2"></i>Pagination(Dynamic)</a></li>');
		list.append('<li class="nav-item"><a class="nav-link" id="tab-full-dataviewer" data-toggle="pill" href="#" role="tab" onclick="hierarchyexamples_draw({tab: \'full-dataviewer\'})"><i class="fas fa-eye mr-2"></i>Full Dataviewer</a></li>');
		
		var parent = $("#cfw-container");
		parent.append(list);
		parent.append('<div id="tab-content"></div>');
	}

}

/******************************************************************
 * Create Role
 ******************************************************************/
function hierarchyexamples_addPerson(){
	
	var html = $('<div>');	

	CFW.http.getForm('cfwCreatePersonForm', html);
	
	CFW.ui.showModal(
			"Create Person", 
			html, 
			"CFW.cache.clearCache(); hierarchyexamples_draw(JSEXAMPLES_LAST_OPTIONS)"
		);
	
}
/******************************************************************
 * Edit Role
 ******************************************************************/
function hierarchyexamples_edit(id){
	
	//-----------------------------------
	// Details
	//-----------------------------------
	var detailsDiv = $('<div id="jsexamples-details">');
	
	CFW.ui.showModal(
		"Edit Person", 
		detailsDiv, 
		"CFW.cache.clearCache(); hierarchyexamples_draw(JSEXAMPLES_LAST_OPTIONS)"
	);
	
	//-----------------------------------
	// Load Form
	//-----------------------------------
	CFW.http.createForm(JSEXAMPLES_URL, {action: "getform", item: "editperson", id: id}, detailsDiv);
	
}

/******************************************************************
 * Delete
 ******************************************************************/
function hierarchyexamples_delete(id){
	
	params = {action: "delete", item: "person", id: id};
	CFW.http.getJSON(JSEXAMPLES_URL, params, 
		function(data) {
			if(data.success){
				//CFW.ui.showSmallModal('Success!', '<span>The selected '+item+' were deleted.</span>');
				//clear cache and reload data
				CFW.cache.clearCache();
				hierarchyexamples_draw(JSEXAMPLES_LAST_OPTIONS);
			}else{
				CFW.ui.showSmallModal("Error!", '<span>The selected person could <b style="color: red">NOT</strong> be deleted.</span>');
			}
	});
}

/******************************************************************
 * Delete
 ******************************************************************/
function hierarchyexamples_duplicate(id){
	
	params = {action: "duplicate", item: "person", id: id};
	CFW.http.getJSON(JSEXAMPLES_URL, params, 
		function(data) {
			if(data.success){
				CFW.cache.clearCache();
				hierarchyexamples_draw(JSEXAMPLES_LAST_OPTIONS);
			}
	});
}

/******************************************************************
 * Example of basic datahandling;
 * 
 * @param data as returned by CFW.http.getJSON()
 * @return 
 ******************************************************************/
function hierarchyexamples_printDataHandling(data){
	
	var parent = $("#tab-content");

	//--------------------------------
	// Button
	var addPersonButton = $('<button class="btn btn-sm btn-success mb-2" onclick="hierarchyexamples_addPerson()">'
						+ '<i class="mr-1 fas fa-plus-circle"></i>Add Person</button>');

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
						+'onclick="hierarchyexamples_edit('+id+');">'
						+ '<i class="fa fa-pen"></i>'
						+ '</button>';

			});

		
		//-------------------------
		// Duplicate Button
		actionButtons.push(
			function (record, id){
				return '<button class="btn btn-warning btn-sm" alt="Duplicate" title="Duplicate" '
						+'onclick="CFW.ui.confirmExecute(\'This will create a duplicate of <strong>\\\''+record.FIRSTNAME.replace(/\"/g,'&quot;')+'\\\'</strong>.\', \'Do it!\', \'hierarchyexamples_duplicate('+id+');\')">'
						+ '<i class="fas fa-clone"></i>'
						+ '</button>';
		});
		
		//-------------------------
		// Delete Button
		actionButtons.push(
			function (record, id){
				return '<button class="btn btn-danger btn-sm" alt="Delete" title="Delete" '
						+'onclick="CFW.ui.confirmExecute(\'Do you want to delete <strong>\\\''+record.FIRSTNAME.replace(/\"/g,'&quot;')+'\\\'</strong>?\', \'Delete\', \'hierarchyexamples_delete('+id+');\')">'
						+ '<i class="fa fa-trash"></i>'
						+ '</button>';

			});
		

		//-----------------------------------
		// Render Data
		var rendererSettings = {
				data: data.payload,
			 	idfield: 'PK_ID',
			 	bgstylefield: null,
			 	textstylefield: null,
			 	titlefields: ['NAME'],
			 	titleformat: '{0}',
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
 * Example of pagination of static data using the dataviewer render.
 * 
 * @param data as returned by CFW.http.getJSON()
 ******************************************************************/
function hierarchyexamples_printPaginationStatic(data){
	
	var parent = $("#tab-content");

	//--------------------------------
	// Button
	var addPersonButton = $('<button class="btn btn-sm btn-success mb-2" onclick="hierarchyexamples_addPerson()">'
						+ '<i class="mr-1 fas fa-plus-circle"></i>Add Person</button>');

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
						+'onclick="hierarchyexamples_edit('+id+');">'
						+ '<i class="fa fa-pen"></i>'
						+ '</button>';

			});

		
		//-------------------------
		// Duplicate Button
		actionButtons.push(
			function (record, id){
				return '<button class="btn btn-warning btn-sm" alt="Duplicate" title="Duplicate" '
						+'onclick="CFW.ui.confirmExecute(\'This will create a duplicate of <strong>\\\''+record.FIRSTNAME.replace(/\"/g,'&quot;')+'\\\'</strong>.\', \'Do it!\', \'hierarchyexamples_duplicate('+id+');\')">'
						+ '<i class="fas fa-clone"></i>'
						+ '</button>';
		});
		
		//-------------------------
		// Delete Button
		actionButtons.push(
			function (record, id){
				return '<button class="btn btn-danger btn-sm" alt="Delete" title="Delete" '
						+'onclick="CFW.ui.confirmExecute(\'Do you want to delete <strong>\\\''+record.FIRSTNAME.replace(/\"/g,'&quot;')+'\\\'</strong>?\', \'Delete\', \'hierarchyexamples_delete('+id+');\')">'
						+ '<i class="fa fa-trash"></i>'
						+ '</button>';

			});
		

		//-----------------------------------
		// Render Data
		var rendererSettings = {
				data: data.payload,
			 	idfield: 'PK_ID',
			 	bgstylefield: null,
			 	textstylefield: null,
			 	titlefields: ['NAME'],
			 	titleformat: '{0}',
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
				
				rendererSettings: {
					dataviewer: {
						storeid: 'staticpaginationexample',
					},
				},
			};
		
		var renderResult = CFW.render.getRenderer('dataviewer').render(rendererSettings);	
		
		parent.append(renderResult);
		
	}else{
		CFW.ui.addAlert('error', 'Something went wrong and no users can be displayed.');
	}
}


/******************************************************************
 * Example of pagination of static data using the dataviewer render.
 * 
 * @param data as returned by CFW.http.getJSON()
 ******************************************************************/
function hierarchyexamples_printPaginationDynamic(){
	
	var parent = $("#tab-content");

	//--------------------------------
	// Button
	var addPersonButton = $('<button class="btn btn-sm btn-success mb-2" onclick="hierarchyexamples_addPerson()">'
						+ '<i class="mr-1 fas fa-plus-circle"></i>Add Person</button>');

	parent.append(addPersonButton);
	
	
	//======================================
	// Prepare actions
	var actionButtons = [];
	//-------------------------
	// Edit Button
	actionButtons.push(
		function (record, id){ 
			return '<button class="btn btn-primary btn-sm" alt="Edit" title="Edit" '
					+'onclick="hierarchyexamples_edit('+id+');">'
					+ '<i class="fa fa-pen"></i>'
					+ '</button>';

		});

	
	//-------------------------
	// Duplicate Button
	actionButtons.push(
		function (record, id){
			return '<button class="btn btn-warning btn-sm" alt="Duplicate" title="Duplicate" '
					+'onclick="CFW.ui.confirmExecute(\'This will create a duplicate of <strong>\\\''+record.FIRSTNAME.replace(/\"/g,'&quot;')+'\\\'</strong>.\', \'Do it!\', \'hierarchyexamples_duplicate('+id+');\')">'
					+ '<i class="fas fa-clone"></i>'
					+ '</button>';
	});
	
	//-------------------------
	// Delete Button
	actionButtons.push(
		function (record, id){
			return '<button class="btn btn-danger btn-sm" alt="Delete" title="Delete" '
					+'onclick="CFW.ui.confirmExecute(\'Do you want to delete <strong>\\\''+record.FIRSTNAME.replace(/\"/g,'&quot;')+'\\\'</strong>?\', \'Delete\', \'hierarchyexamples_delete('+id+');\')">'
					+ '<i class="fa fa-trash"></i>'
					+ '</button>';

		});
	

	//-----------------------------------
	// Render Data
	var rendererSettings = {
			data: null,
		 	idfield: 'PK_ID',
		 	bgstylefield: null,
		 	textstylefield: null,
		 	titlefields: ['NAME'],
		 	titleformat: '{0}',
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
			
			rendererSettings: {
				dataviewer: {
					storeid: 'dynamicpaginationexample',
					datainterface: {
						url: JSEXAMPLES_URL,
						item: 'personlist'
					}
				},
			},
		};
	
	var renderResult = CFW.render.getRenderer('dataviewer').render(rendererSettings);	
	
	parent.append(renderResult);
	
}

/******************************************************************
 * Full example using the dataviewer renderer.
 * 
 * @param data as returned by CFW.http.getJSON()
 ******************************************************************/
function hierarchyexamples_printFullDataviewer(){
	
	var parent = $("#tab-content");

	//--------------------------------
	// Button
	var addPersonButton = $('<button class="btn btn-sm btn-success mb-2" onclick="hierarchyexamples_addPerson()">'
						+ '<i class="mr-1 fas fa-plus-circle"></i>Add Person</button>');

	parent.append(addPersonButton);
	
	
	//======================================
	// Prepare actions
	var actionButtons = [];
	//-------------------------
	// Edit Button
	actionButtons.push(
		function (record, id){ 
			return '<button class="btn btn-primary btn-sm" alt="Edit" title="Edit" '
					+'onclick="hierarchyexamples_edit('+id+');">'
					+ '<i class="fa fa-pen"></i>'
					+ '</button>';

		});

	
	//-------------------------
	// Duplicate Button
	actionButtons.push(
		function (record, id){
			return '<button class="btn btn-warning btn-sm" alt="Duplicate" title="Duplicate" '
					+'onclick="CFW.ui.confirmExecute(\'This will create a duplicate of <strong>\\\''+record.FIRSTNAME.replace(/\"/g,'&quot;')+'\\\'</strong>.\', \'Do it!\', \'hierarchyexamples_duplicate('+id+');\')">'
					+ '<i class="fas fa-clone"></i>'
					+ '</button>';
	});
	
	//-------------------------
	// Delete Button
	actionButtons.push(
		function (record, id){
			return '<button class="btn btn-danger btn-sm" alt="Delete" title="Delete" '
					+'onclick="CFW.ui.confirmExecute(\'Do you want to delete <strong>\\\''+record.FIRSTNAME.replace(/\"/g,'&quot;')+'\\\'</strong>?\', \'Delete\', \'hierarchyexamples_delete('+id+');\')">'
					+ '<i class="fa fa-trash"></i>'
					+ '</button>';

		});
	

	//-----------------------------------
	// Render Data
	var rendererSettings = {
			data: null,
		 	idfield: 'PK_ID',
		 	bgstylefield: null,
		 	textstylefield: null,
		 	titlefields: ['FIRSTNAME', 'LASTNAME'],
		 	titleformat: '{0} {1}',
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
			
			rendererSettings: {
				dataviewer: {
					storeid: 'fulldataviewerexample',
					datainterface: {
						url: JSEXAMPLES_URL,
						item: 'personlist'
					},
					renderers: [
						{	label: 'Table',
							name: 'table',
							renderdef: {
								rendererSettings: {
									table: {filterable: false},
								},
							}
						},
						{	label: 'Smaller Table',
							name: 'table',
							renderdef: {
								visiblefields: ['FIRSTNAME', 'LASTNAME', 'EMAIL', 'LIKES_TIRAMISU'],
								actions: [],
								rendererSettings: {
									table: {filterable: false, narrow: true},
								},
							}
						},
						{	label: 'Panels',
							name: 'panels',
							renderdef: {}
						},
						{	label: 'Cards',
							name: 'cards',
							renderdef: {}
						},
						{	label: 'Tiles',
							name: 'tiles',
							renderdef: {
								visiblefields: ['PK_ID', 'LOCATION', "EMAIL", "LIKES_TIRAMISU"],
								rendererSettings: {
									tiles: {
										popover: false,
										border: '2px solid black'
									},
								},
								
							}
						},
						{	label: 'CSV',
							name: 'csv',
							renderdef: {}
						},
						{	label: 'JSON',
							name: 'json',
							renderdef: {}
						}
					],
				},
			},
		};
	
	var renderResult = CFW.render.getRenderer('dataviewer').render(rendererSettings);	
	
	parent.append(renderResult);
	
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

function hierarchyexamples_initialDraw(){
	
	hierarchyexamples_createTabs();
	
	//-----------------------------------
	// Restore last tab
	var tabToDisplay = CFW.cache.retrieveValueForPage("jsexamples-lasttab", "datahandling");
	
	$('#tab-'+tabToDisplay).addClass('active');
	
	hierarchyexamples_draw({tab: tabToDisplay});
}

function hierarchyexamples_draw(options){
	JSEXAMPLES_LAST_OPTIONS = options;
	
	CFW.cache.storeValueForPage("jsexamples-lasttab", options.tab);
	$("#tab-content").html("");
	
	CFW.ui.toogleLoader(true);
	
	window.setTimeout( 
	function(){
		
		switch(options.tab){
			case "datahandling":		CFW.http.fetchAndCacheData(JSEXAMPLES_URL, {action: "fetch", item: "personlist"}, "personlist", hierarchyexamples_printDataHandling);
										break;	
			case "pagination-static":	CFW.http.fetchAndCacheData(JSEXAMPLES_URL, {action: "fetch", item: "personlist"}, "personlist", hierarchyexamples_printPaginationStatic);
										break;	
			case "pagination-dynamic":	hierarchyexamples_printPaginationDynamic();
										break;	
			case "full-dataviewer":		hierarchyexamples_printFullDataviewer();
			break;	
			default:				CFW.ui.addToastDanger('This tab is unknown: '+options.tab);
		}
		
		CFW.ui.toogleLoader(false);
	}, 50);
}