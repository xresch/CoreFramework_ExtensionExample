
/**************************************************************************************************************
 * This file contains various javascript examples.
 * For your convenience, this code is highly redundant for eassier copy&paste.
 * @author Reto Scheiwiller, (c) Copyright 2019 
 **************************************************************************************************************/

var URL_HIERARCHY = "/app/hierarchy";
var JSEXAMPLES_LAST_OPTIONS = null;
var URL_PARAMS=CFW.http.getURLParams();

var EXAMPLE_CONFIG_ID = "hierarchicalperson";

/******************************************************************
 * Delete
 ******************************************************************/
function hierarchyexamples_duplicate(id){
	
	params = {action: "duplicate", item: "person", id: id};
	CFW.http.getJSON(URL_HIERARCHY, params, 
		function(data) {
			if(data.success){
				CFW.cache.clearCache();
				jsexamples_draw(JSEXAMPLES_LAST_OPTIONS);
			}
	});
}

/******************************************************************
 *
 * @param data as returned by CFW.http.getJSON()
 * @return 
 ******************************************************************/
function hierarchyexamples_printSortableHierarchy(data){
	
	var parent = $("#cfw-container");
	parent.html("");
	parent.append("<h1>Sortable Hierarchy<h1>");
	
	//--------------------------------
	// Button	
	
	//--------------------------------
	// Table
	
	if(data.payload != undefined){
		//-----------------------------------
		// Render Data
		var rendererSettings = {
				data: data.payload,
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
				rendererSettings: {
					hierarchy_sorter: {configid: EXAMPLE_CONFIG_ID}
				},
			};
				
		var renderResult = CFW.render.getRenderer('hierarchy_sorter').render(rendererSettings);	
		
		parent.append(renderResult);
		
	}else{
		CFW.ui.addAlert('error', 'Something went wrong and no items can be displayed.');
	}
}

/******************************************************************
 * Main function
 ******************************************************************/
function hierarchyexamples_draw(){
	
	CFW.ui.toogleLoader(true);
	
	window.setTimeout( 
	function(){
		
		//CFW.http.fetchAndCacheData(URL_HIERARCHY, {configid: "hierarchicalperson", action: "fetch", item: "hierarchy", rootid: 1}, "hierarchy", hierarchyexamples_printSortableHierarchy);
		CFW.http.fetchAndCacheData(URL_HIERARCHY, {configid: EXAMPLE_CONFIG_ID, action: "fetch", item: "hierarchy"}, "hierarchy", hierarchyexamples_printSortableHierarchy);
		
		CFW.ui.toogleLoader(false);
	}, 50);
}