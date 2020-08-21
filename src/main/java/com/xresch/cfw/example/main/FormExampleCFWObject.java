package com.xresch.cfw.example.main;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import com.xresch.cfw.datahandling.CFWField;
import com.xresch.cfw.datahandling.CFWField.FormFieldType;
import com.xresch.cfw.datahandling.CFWObject;
import com.xresch.cfw.features.core.AutocompleteList;
import com.xresch.cfw.features.core.AutocompleteResult;
import com.xresch.cfw.features.core.CFWAutocompleteHandler;
import com.xresch.cfw.validation.LengthValidator;

public class FormExampleCFWObject extends CFWObject{
	
	private CFWField<String> firstname 		= CFWField.newString(FormFieldType.TEXT, "FIRSTNAME");
	private CFWField<String> lastname 		= CFWField.newString(FormFieldType.TEXT, "LASTNAME").setLabel("Lastname with custom Label");
	private CFWField<String> withValue 		= CFWField.newString(FormFieldType.TEXT, "WITH_VALUE").setLabel("With Value");
	private CFWField<String> description 	= CFWField.newString(FormFieldType.TEXTAREA, "A_LONG_DESCRIPTION")
			.addValidator(new LengthValidator(5, 10));
	
	private CFWField<String> textarea = CFWField.newString(FormFieldType.TEXTAREA, "10ROW_TEXTAREA").setLabel("10 Row Textarea")
			.addAttribute("rows", "10");
	
	private CFWField<Integer> number = CFWField.newInteger(FormFieldType.NUMBER, "Number_Fieldxyz").setLabel("Enter a Number");
	
	private CFWField<Date> date = CFWField.newDate(FormFieldType.DATEPICKER, "DATE")
			.setValue(new Date(1580053600000L));
	
	private CFWField<Timestamp> timestamp = CFWField.newTimestamp(FormFieldType.DATETIMEPICKER, "TIMESTAMP");
	
	private CFWField<String> select = CFWField.newString(FormFieldType.SELECT, "SELECT")
			.setOptions(new String[] {"Option A","Option B","Option C","Option D"});
	
	private CFWField<Integer> keyValSelect = CFWField.newInteger(FormFieldType.SELECT, "KEY_VAL_SELECT")
											.setValue(2);
	
	private CFWField<String> editor = CFWField.newString(FormFieldType.WYSIWYG, "EDITOR")
			.setValue("<strong>Intial Value:</strong> successfull!!!");
	
	private CFWField<String> tags = CFWField.newString(FormFieldType.TAGS, "TAGS")
			.setValue("foo,test,bar,bla")
			.addAttribute("maxTags", "20")
			.setAutocompleteHandler(new CFWAutocompleteHandler(5) {
				
				public AutocompleteResult getAutocompleteData(HttpServletRequest request, String inputValue) {
					AutocompleteList list = new AutocompleteList();
					
					for(int i = 0; i < this.getMaxResults(); i++ ) {
						String tag = "Tag_"+inputValue+"_"+i;
						list.addItem(tag);
					}
					return new AutocompleteResult(list);
				}
			});
	
	private CFWField<LinkedHashMap<String,String>> tagsselector = CFWField.newTagsSelector("JSON_TAGS_SELECTOR")
			.setAutocompleteHandler(new CFWAutocompleteHandler() {
				
				public AutocompleteResult getAutocompleteData(HttpServletRequest request, String inputValue) {
					AutocompleteList list = new AutocompleteList();
					for(int i = 0; i < 10; i++ ) {
						String tag = inputValue+"_"+i;
						list.addItem("key_"+tag, "Label_"+tag);
					}
					return new AutocompleteResult(list);
				}
			});
	
	private CFWField<String> autocomplete = CFWField.newString(FormFieldType.TEXT, "AUTOCOMPLETE")
			.setAutocompleteHandler(new CFWAutocompleteHandler(5) {
				
				public AutocompleteResult getAutocompleteData(HttpServletRequest request, String inputValue) {
					AutocompleteList list = new AutocompleteList();
					for(int i = 0; i < 7; i++ ) {
						String tag = "Test_"+inputValue+"_"+i;
						list.addItem(tag);
					}
					
					AutocompleteList list2 = new AutocompleteList();
					for(int i = 0; i < 10; i++ ) {
						String tag = "Foobar_"+inputValue+"_"+i;
						list2.addItem(tag, tag, "Some example description that should be added.");
					}
					
					return new AutocompleteResult(list)
							.addList(list2)
							.setHTMLDescription("<p>This is your HTML Description. Feel free to add some stuff like a list:<p <ol><li>Do this</li><li>Do that</li><li>Do even more...</li></ol>");
				}
			});
	
	public FormExampleCFWObject() {
		
		initialize();
	}
		
	public void initialize() {
		withValue.setValueValidated("This is the Value");
		LinkedHashMap<Integer, String> options = new LinkedHashMap<Integer, String>();
		options.put(1, "Apple");
		options.put(2, "Banana");
		options.put(3, "Plumb");
		options.put(4, "Strawwberry");
		keyValSelect.setOptions(options);
		
		this.addFields(firstname, lastname, withValue, description, textarea, number, date, timestamp, select, keyValSelect, editor, tags, tagsselector, autocomplete);
	}

}
