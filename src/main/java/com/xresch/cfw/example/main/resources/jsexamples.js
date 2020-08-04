
/**************************************************************************************************************
 * 
 * @author Reto Scheiwiller, (c) Copyright 2019 
 * @license Creative Commons: Attribution-NonCommercial-NoDerivatives 4.0 International
 **************************************************************************************************************/

var jsexamples_URL = "./list";
var jsexamples_LAST_OPTIONS = null;

/******************************************************************
 * Reset the view.
 ******************************************************************/
function jsexamples_createTabs(){
	var pillsTab = $("#pills-tab");
	
	if(pillsTab.length == 0){
		
		var list = $('<ul class="nav nav-pills mb-3" id="pills-tab" role="tablist">');
		
		if(CFW.hasPermission('Dashboard Creator') 
		|| CFW.hasPermission('Dashboard Admin')){
			list.append(
				'<li class="nav-item"><a class="nav-link" id="tab-mydashboards" data-toggle="pill" href="#" role="tab" onclick="jsexamples_draw({tab: \'mydashboards\'})"><i class="fas fa-user-circle mr-2"></i>My Dashboards</a></li>'
				+'<li class="nav-item"><a class="nav-link" id="tab-shareddashboards" data-toggle="pill" href="#" role="tab" onclick="jsexamples_draw({tab: \'shareddashboards\'})"><i class="fas fa-share-alt mr-2"></i>Shared Dashboards</a></li>'
			);
		}else if(CFW.hasPermission('Dashboard Viewer')){
			list.append('<li class="nav-item"><a class="nav-link" id="tab-shareddashboards" data-toggle="pill" href="#" role="tab" onclick="jsexamples_draw({tab: \'shareddashboards\'})"><i class="fas fa-share-alt mr-2"></i>Shared Dashboards</a></li>');
		}
		
		if(CFW.hasPermission('Dashboard Admin')){
					list.append(
						'<li class="nav-item"><a class="nav-link" id="tab-admindashboards" data-toggle="pill" href="#" role="tab" onclick="jsexamples_draw({tab: \'admindashboards\'})"><i class="fas fa-tools mr-2"></i>Manage Dashboards</a></li>');
		}
		
		var parent = $("#cfw-container");
		parent.append(list);
		parent.append('<div id="tab-content"></div>');
	}

}

/******************************************************************
 * Create Role
 ******************************************************************/
function jsexamples_createDashboard(){
	
	var html = $('<div id="cfw-usermgmt-createDashboard">');	

	CFW.http.getForm('cfwCreateDashboardForm', html);
	
	CFW.ui.showModal(CFWL('jsexamples_createDashboard', 
			CFWL("jsexamples_createDashboard", "Create Dashboard")), 
			html, "CFW.cache.clearCache(); jsexamples_draw(jsexamples_LAST_OPTIONS)");
	
}
/******************************************************************
 * Edit Role
 ******************************************************************/
function jsexamples_editDashboard(id){
	
	var allDiv = $('<div id="cfw-usermgmt">');	

	//-----------------------------------
	// Role Details
	//-----------------------------------
	var detailsDiv = $('<div id="cfw-usermgmt-details">');
	detailsDiv.append('<h2>'+CFWL('jsexamples_dashboard', "Dashboard")+' Details</h2>');
	allDiv.append(detailsDiv);
	

	CFW.ui.showModal(
			CFWL("jsexamples_editDashboard","Edit Dashboard"), 
			allDiv, 
			"CFW.cache.clearCache(); jsexamples_draw(jsexamples_LAST_OPTIONS)"
	);
	
	//-----------------------------------
	// Load Form
	//-----------------------------------
	CFW.http.createForm(jsexamples_URL, {action: "getform", item: "editdashboard", id: id}, detailsDiv);
	
}

/******************************************************************
 * Delete
 ******************************************************************/
function jsexamples_delete(ids){
	
	params = {action: "delete", item: "dashboards", ids: ids};
	CFW.http.getJSON(jsexamples_URL, params, 
		function(data) {
			if(data.success){
				//CFW.ui.showSmallModal('Success!', '<span>The selected '+item+' were deleted.</span>');
				//clear cache and reload data
				CFW.cache.clearCache();
				jsexamples_draw(jsexamples_LAST_OPTIONS);
			}else{
				CFW.ui.showSmallModal("Error!", '<span>The selected '+item+' could <b style="color: red">NOT</b> be deleted.</span>');
			}
	});
}

/******************************************************************
 * Delete
 ******************************************************************/
function jsexamples_duplicate(id){
	
	params = {action: "duplicate", item: "dashboard", id: id};
	CFW.http.getJSON(jsexamples_URL, params, 
		function(data) {
			if(data.success){
				CFW.cache.clearCache();
				jsexamples_draw(jsexamples_LAST_OPTIONS);
			}
	});
}

/******************************************************************
 * 
 ******************************************************************/
function jsexamples_printMyDashboards(data){
	jsexamples_printDashboards(data, 'mydashboards');
}

/******************************************************************
 * 
 ******************************************************************/
function jsexamples_printSharedDashboards(data){
	jsexamples_printDashboards(data, 'shareddashboards');
}

/******************************************************************
 * 
 ******************************************************************/
function jsexamples_printAdminDashboards(data){
	jsexamples_printDashboards(data, 'admindashboards');
}


/******************************************************************
 * Print the list of roles;
 * 
 * @param data as returned by CFW.http.getJSON()
 * @return 
 ******************************************************************/
function jsexamples_printDashboards(data, type){
	
	var parent = $("#tab-content");

	//--------------------------------
	// Button
	if(type == 'mydashboards'){
		var createButton = $('<button class="btn btn-sm btn-success mb-2" onclick="jsexamples_createDashboard()">'
							+ '<i class="fas fa-plus-circle"></i> '+ CFWL('jsexamples_createDashboard')
					   + '</button>');
	
		parent.append(createButton);
	}
	
	//--------------------------------
	// Table
	
	if(data.payload != undefined){
		
		var resultCount = data.payload.length;
		if(resultCount == 0){
			CFW.ui.addToastInfo("Hmm... seems there aren't any dashboards in the list.");
			return;
		}
		
		//-----------------------------------
		// Prepare Columns
		var showFields = [];
		if(type == 'mydashboards'){
			showFields = ['NAME', 'DESCRIPTION', 'IS_SHARED'];
		}else if (type == 'shareddashboards'){
			showFields = ['OWNER', 'NAME', 'DESCRIPTION'];
		}else if (type == 'admindashboards'){
			showFields = ['PK_ID', 'OWNER', 'NAME', 'DESCRIPTION', 'IS_SHARED'];
		}
		
		
		//======================================
		// Prepare actions
		
		//-------------------------
		// View Button
		var actionButtons = [ 
			function (record, id){ 
				return '<a class="btn btn-success btn-sm" role="button" href="/app/dashboard/view?id='+id+'&title='+encodeURIComponent(record.NAME)+'" alt="View" title="View" >'
				+ '<i class="fa fa-eye"></i>'
				+ '</a>';
			}];

		//-------------------------
		// Edit Button
		if(type == 'mydashboards'
		|| type == 'admindashboards'){

			actionButtons.push(
				function (record, id){ 
					var htmlString = '';
					if(JSDATA.userid == record.FK_ID_USER || type == 'admindashboards'){
						htmlString += '<button class="btn btn-primary btn-sm" alt="Edit" title="Edit" '
							+'onclick="jsexamples_editDashboard('+id+');">'
							+ '<i class="fa fa-pen"></i>'
							+ '</button>';
					}else{
						htmlString += '&nbsp;';
					}
					return htmlString;
				});
		}
		
		if(CFW.hasPermission('Dashboard Creator') 
		|| CFW.hasPermission('Dashboard Admin')){
			//-------------------------
			// Duplicate Button
			actionButtons.push(
				function (record, id){
					var htmlString = '<button class="btn btn-warning btn-sm" alt="Duplicate" title="Duplicate" '
							+'onclick="CFW.ui.confirmExecute(\'This will create a duplicate of <b>\\\''+record.NAME.replace(/\"/g,'&quot;')+'\\\'</b> and add it to your dashboards.\', \'Do it!\', \'jsexamples_duplicate('+id+');\')">'
							+ '<i class="fas fa-clone"></i>'
							+ '</button>';
					
					return htmlString;
				});
		}
		
		//-------------------------
		// Delete Button
		if(type == 'mydashboards'
		|| type == 'admindashboards'){
			actionButtons.push(
				function (record, id){
					var htmlString = '';
					if(JSDATA.userid == record.FK_ID_USER || type == 'admindashboards'){
						htmlString += '<button class="btn btn-danger btn-sm" alt="Delete" title="Delete" '
							+'onclick="CFW.ui.confirmExecute(\'Do you want to delete the dashboard <b>\\\''+record.NAME.replace(/\"/g,'&quot;')+'\\\'</b>?\', \'Delete\', \'jsexamples_delete('+id+');\')">'
							+ '<i class="fa fa-trash"></i>'
							+ '</button>';
					}else{
						htmlString += '&nbsp;';
					}
					return htmlString;
				});
		}

		

		//-----------------------------------
		// Render Data
		var rendererSettings = {
			 	idfield: 'PK_ID',
			 	bgstylefield: null,
			 	textstylefield: null,
			 	titlefields: ['NAME'],
			 	titledelimiter: ' ',
			 	visiblefields: showFields,
			 	labels: {
			 		PK_ID: "ID",
			 		USERNAME: 'Owner',
			 		IS_SHARED: 'Shared'
			 	},
			 	customizers: {
			 		IS_SHARED: function(record, value) { 
			 			var isShared = value;
			 			if(isShared){
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
	
	var tabToDisplay = CFW.cache.retrieveValue("dashboardlist-lasttab", "mydashboards");
	
	if(CFW.hasPermission('Dashboard Viewer') 
	&& !CFW.hasPermission('Dashboard Creator') 
	&& !CFW.hasPermission('Dashboard Admin')){
		tabToDisplay = "shareddashboards";
	}
	
	$('#tab-'+tabToDisplay).addClass('active');
	
	jsexamples_draw({tab: tabToDisplay});
}

function jsexamples_draw(options){
	jsexamples_LAST_OPTIONS = options;
	
	CFW.cache.storeValue("dashboardlist-lasttab", options.tab);
	$("#tab-content").html("");
	
	CFW.ui.toogleLoader(true);
	
	window.setTimeout( 
	function(){
		
		switch(options.tab){
			case "mydashboards":		CFW.http.fetchAndCacheData(jsexamples_URL, {action: "fetch", item: "mydashboards"}, "mydashboards", jsexamples_printMyDashboards);
										break;	
			case "shareddashboards":	CFW.http.fetchAndCacheData(jsexamples_URL, {action: "fetch", item: "shareddashboards"}, "shareddashboards", jsexamples_printSharedDashboards);
										break;
			case "admindashboards":		CFW.http.fetchAndCacheData(jsexamples_URL, {action: "fetch", item: "admindashboards"}, "admindashboards", jsexamples_printAdminDashboards);
										break;						
			default:				CFW.ui.addToastDanger('This tab is unknown: '+options.tab);
		}
		
		CFW.ui.toogleLoader(false);
	}, 50);
}