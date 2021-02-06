
/**************************************************************************************************************
 * This file contains various javascript examples.
 * For your convenience, this code is highly redundant for eassier copy&paste.
 * @author Reto Scheiwiller, (c) Copyright 2019 
 **************************************************************************************************************/

var JSEXAMPLES_URL = "./multiformexamples";
var JSEXAMPLES_LAST_OPTIONS = null;

/******************************************************************
 * Reset the view.
 ******************************************************************/
function multiformexamples_createTabs(){
	var pillsTab = $("#pills-tab");
	
	if(pillsTab.length == 0){
		
		var list = $('<ul class="nav nav-pills mb-3" id="pills-tab" role="tablist">');
		
		list.append('<li class="nav-item"><a class="nav-link" id="tab-datahandling" data-toggle="pill" href="#" role="tab" onclick="multiformexamples_draw({tab: \'basicmultiform\'})"><i class="fas fa-share-alt mr-2"></i>Basic Multi Form</a></li>');
		
		var parent = $("#cfw-container");
		parent.append(list);
		parent.append('<div id="tab-content"></div>');
	}

}

/******************************************************************
 * Edit Role
 ******************************************************************/
function multiformexamples_printBasicForm(){
	
	//-----------------------------------
	// Load Form
	//-----------------------------------
	let targetDiv = $("#tab-content");
	CFW.http.createForm(JSEXAMPLES_URL, {action: "getform", item: "basicmultiform"}, targetDiv);
	
}

/******************************************************************
 * Delete
 ******************************************************************/
function multiformexamples_delete(id){
	
	params = {action: "delete", item: "person", id: id};
	CFW.http.getJSON(JSEXAMPLES_URL, params, 
		function(data) {
			if(data.success){
				//CFW.ui.showSmallModal('Success!', '<span>The selected '+item+' were deleted.</span>');
				//clear cache and reload data
				CFW.cache.clearCache();
				multiformexamples_draw(JSEXAMPLES_LAST_OPTIONS);
			}else{
				CFW.ui.showSmallModal("Error!", '<span>The selected person could <b style="color: red">NOT</strong> be deleted.</span>');
			}
	});
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
function multiformexamples_initialDraw(){
	
	multiformexamples_createTabs();
	
	//-----------------------------------
	// Restore last tab
	var tabToDisplay = CFW.cache.retrieveValueForPage("jsexamples-lasttab", "basicmultiform");
	
	$('#tab-'+tabToDisplay).addClass('active');
	
	multiformexamples_draw({tab: tabToDisplay});
}

function multiformexamples_draw(options){
	JSEXAMPLES_LAST_OPTIONS = options;
	
	CFW.cache.storeValueForPage("jsexamples-lasttab", options.tab);
	$("#tab-content").html("");
	
	CFW.ui.toogleLoader(true);
	
	window.setTimeout( 
	function(){
		
		switch(options.tab){
			case "basicmultiform":		multiformexamples_printBasicForm();
										break;	
			break;	
			default:				CFW.ui.addToastDanger('This tab is unknown: '+options.tab);
		}
		
		CFW.ui.toogleLoader(false);
	}, 50);
}