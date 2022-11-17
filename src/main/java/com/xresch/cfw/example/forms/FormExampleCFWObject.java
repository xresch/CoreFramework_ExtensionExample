package com.xresch.cfw.example.forms;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Strings;
import com.xresch.cfw._main.CFW;
import com.xresch.cfw.datahandling.CFWChartSettings;
import com.xresch.cfw.datahandling.CFWChartSettings.AxisType;
import com.xresch.cfw.datahandling.CFWChartSettings.ChartType;
import com.xresch.cfw.datahandling.CFWField;
import com.xresch.cfw.datahandling.CFWField.FormFieldType;
import com.xresch.cfw.datahandling.CFWObject;
import com.xresch.cfw.datahandling.CFWSchedule;
import com.xresch.cfw.datahandling.CFWSchedule.EndType;
import com.xresch.cfw.datahandling.CFWSchedule.IntervalType;
import com.xresch.cfw.datahandling.CFWTimeframe;
import com.xresch.cfw.features.core.AutocompleteItem;
import com.xresch.cfw.features.core.AutocompleteList;
import com.xresch.cfw.features.core.AutocompleteResult;
import com.xresch.cfw.features.core.CFWAutocompleteHandler;
import com.xresch.cfw.validation.EmailValidator;
import com.xresch.cfw.validation.LengthValidator;
import com.xresch.cfw.validation.NotNullOrEmptyValidator;
import com.xresch.cfw.validation.NumberRangeValidator;
import com.xresch.cfw.validation.ScheduleValidator;

public class FormExampleCFWObject extends CFWObject{
	
	//------------------------------------------------------------------------------------------------
	// A regular text field. The fieldname "FIRSTNAME" will be used to create the label "Firstname"
	private CFWField<String> firstname = 
				CFWField.newString(FormFieldType.TEXT, "FIRSTNAME");
	
	//------------------------------------------------------------------------------------------------
	// A text field with custom label and a length validator and a description
	private CFWField<String> lastname = 
				CFWField.newString(FormFieldType.TEXT, "LASTNAME")
						.setLabel("Lastname with custom Label")
						.setDescription("Provide a lastname with 2 to 24 characters.")
						.addValidator(new LengthValidator(2, 24));
	
	//------------------------------------------------------------------------------------------------
	// An email field with eMail validator that checks if the field is not null.
	// As well as a validator which checks if the email is between 10 and 128 characters in length.
	private CFWField<String> email = 
				CFWField.newString(FormFieldType.EMAIL, "EMAIL")
						.setLabel("eMail")
						.setDescription("Provide a valid eMail address.")
						.addValidator(new EmailValidator().setNullAllowed(false))
						.addValidator(new LengthValidator(10, 128));
	
	//------------------------------------------------------------------------------------------------
	// A text field with default value
	private CFWField<String> withDefaultValue = 
				CFWField.newString(FormFieldType.TEXT, "WITH_DEFAULT_VALUE")
						.setDescription("This field has a default value.")
						.setValue("Default Value");
	
	//------------------------------------------------------------------------------------------------
	// A regular textarea that should not be null
	private CFWField<String> description = 
				CFWField.newString(FormFieldType.TEXTAREA, "A_LONG_DESCRIPTION")
						.setDescription("Put a description in this field.")
						.addValidator(new NotNullOrEmptyValidator());
	
	//------------------------------------------------------------------------------------------------
	// A textarea with 10 rows instead of the default 5
	private CFWField<String> textarea =
				CFWField.newString(FormFieldType.TEXTAREA, "10ROW_TEXTAREA")
						.setLabel("10 Row Textarea")
						.setDescription("Now thats what I call a big text area.")
						.addAttribute("rows", "10");
	
	//------------------------------------------------------------------------------------------------
	// A regular number field with the custom Label "Enter a Number"
	private CFWField<Integer> number = 
				CFWField.newInteger(FormFieldType.NUMBER, "Number_Fieldxyz")
						.setLabel("Enter a Number");
	
	//------------------------------------------------------------------------------------------------
	// A number field with RangeValidator
	private CFWField<Integer> numberRange = 
				CFWField.newInteger(FormFieldType.NUMBER, "Number_Range")
						.setDescription("Choose a number between 8 and 8008.")
						.addValidator(new NumberRangeValidator(8, 8008))
						.setValue(42);
	
	private CFWField<Boolean> booleanField = 
			CFWField.newBoolean(FormFieldType.BOOLEAN, "boolean")
					.setLabel("Boolean");
	
	//------------------------------------------------------------------------------------------------
	// A datepicker field with an initial value
	private CFWField<Date> date = 
				CFWField.newDate(FormFieldType.DATEPICKER, "DATE")
						.setValue(new Date(1580053600000L));
	
	//------------------------------------------------------------------------------------------------
	// A date and time picker field without a default value
	private CFWField<Timestamp> timestamp = 
				CFWField.newTimestamp(FormFieldType.DATETIMEPICKER, "TIMESTAMP")
				.setValue(new Timestamp(1580053600000L));
		
	//------------------------------------------------------------------------------------------------
	// A schedule picker with default value
	private CFWField<CFWTimeframe> timeframe = 
				CFWField.newTimeframe("JSON_TIMEFRAME")
				.setLabel("Timeframe Picker")
				.setValue(
					new CFWTimeframe()
						.setEarliest(CFW.Utils.Time.getCurrentTimestampWithOffset(0, 0, 3, 3, 30))
						.setLatest(CFW.Utils.Time.getCurrentTimestampWithOffset(0, 0, 1, 1, 1))
				);
	
	//------------------------------------------------------------------------------------------------
	// A select for a timezone
	private CFWField<String> timezone =  CFWField.newString(FormFieldType.TIMEZONEPICKER, "TIME_ZONE");
	
	//------------------------------------------------------------------------------------------------
	// A schedule picker with default value
	private CFWField<CFWSchedule> schedule = 
				CFWField.newSchedule("JSON_SCHEDULE")
				.setLabel("Schedule")
				.addValidator(new ScheduleValidator())
				.setValue(
					new CFWSchedule()
						.timeframeStart(Date.from(Instant.now()))
						.endType(EndType.RUN_FOREVER)
						.intervalType(IntervalType.EVERY_X_DAYS)
						.intervalDays(1)
				);
	
	//------------------------------------------------------------------------------------------------
	// A schedule picker with default value
	private CFWField<CFWChartSettings> chartSettings = 
				CFWField.newChartSettings("JSON_CHART_SETTINGS")
				.setLabel("Chart Settings")
				.setValue(
					new CFWChartSettings()
						.chartType(ChartType.scatter)
						.showLegend(true)
						.showAxes(false)
						.stacked(true)
						.pointRadius(12)
						.xaxisType(AxisType.logarithmic)
						.yaxisType(AxisType.logarithmic)
						.yaxisMin(-42)
						.yaxisMax(10000)
				);
	
	//------------------------------------------------------------------------------------------------
	// A select with options
	private CFWField<String> select = 
				CFWField.newString(FormFieldType.SELECT, "SELECT")
						.setOptions(new String[] {"Option A","Option B","Option C","Option D"});
	
	//------------------------------------------------------------------------------------------------
	// A select with key and value options
	private static LinkedHashMap<Integer, String> options = new LinkedHashMap<Integer, String>();
	static {
		options.put(1, "Apple");
		options.put(2, "Banana");
		options.put(3, "Plumb");
		options.put(4, "Strawwberry");
	}

	private CFWField<Integer> keyValSelect = 
				CFWField.newInteger(FormFieldType.SELECT, "KEY_VAL_SELECT")
						.setOptions(options)
						.setValue(2);
	
	//------------------------------------------------------------------------------------------------
	// A list of checkboxes with key and label options
	private static LinkedHashMap<String, String> checkboxOptions = new LinkedHashMap<String, String>();
	
	// default values
	private static LinkedHashMap<String, String> checkboxDefaultValues = new LinkedHashMap<String, String>();
	static {
		checkboxOptions.put("tiramisu_key", "Tiramisu");
		checkboxOptions.put("panna_cotta_key", "Panna Cotta");
		checkboxOptions.put("ice_cream_key", "Ice Cream");
		checkboxOptions.put("chocolate_key", "Chocolate");
		
		// other values are null and therefore false
		// set all default values if you do not want to check for nulls
		checkboxDefaultValues.put("tiramisu_key", "true");
		checkboxDefaultValues.put("ice_cream_key", "true");
	}

	private CFWField<LinkedHashMap<String, String>> checkoxesNoDefaults = 
				CFWField.newCheckboxes("JSON_CHECKBOXES")
						.setLabel("Checkboxes no Defaults")
						.setDescription("Checkbox options without default selection.")
						.setOptions(checkboxOptions)
						;
	
	private CFWField<LinkedHashMap<String, String>> checkoxesWithDefaults = 
			CFWField.newCheckboxes("JSON_CHECKBOXES_WITH_DEFAULTS")
					.setLabel("Checkboxes with Defaults")
					.setDescription("Checkbox options with a default selection.")
					.setOptions(checkboxOptions)
					.setValue(checkboxDefaultValues)
					;
	
	//------------------------------------------------------------------------------------------------
	// Value Label lets the users enter value/label-pairs that can be used for example for custom select options.
	private static ArrayList<String> customListItems = new ArrayList<>();
	static {
		customListItems.add("Some entry");
		customListItems.add("Another Entry");
	}
	private CFWField<ArrayList<String> > customList = 
				CFWField.newCustomList("CUSTOM_LIST")
						.setDescription("Add items to the list.")
						.setValue(customListItems);
	
	//------------------------------------------------------------------------------------------------
	// Value Label lets the users enter value/label-pairs that can be used for example for custom select options.
	private static LinkedHashMap<String, String> valueLabels = new LinkedHashMap<String, String>();
	static {
		valueLabels.put("myValue", "My Value Label");
		valueLabels.put("test_some_entry", "Test Some Entry Labels");
	}
	private CFWField<LinkedHashMap<String, String>> valueLabel = 
				CFWField.newValueLabel("JSON_VALUE_LABEL")
						.setLabel("Value Label")
						.setDescription("Add Values and labels")
						.setValue(valueLabels);
	
	//------------------------------------------------------------------------------------------------
	// A select with options
	private CFWField<String> unmodifiableText = 
				CFWField.newString(FormFieldType.UNMODIFIABLE_TEXT, "UNMODIFIABLE_TEXT")
						.setValue("Just display the value as a unmodifiable text.");
	
	//------------------------------------------------------------------------------------------------
	// A select for ISO language
	private CFWField<String> language =  CFWField.newString(FormFieldType.LANGUAGE, "LANGUAGE");
		
	//------------------------------------------------------------------------------------------------
	// A WYSIWYG Editor with default value
	private CFWField<String> editor = 
			CFWField.newString(FormFieldType.WYSIWYG, "EDITOR")
					.setValue("<strong>Intial Value:</strong> successful!!!");
	
	//------------------------------------------------------------------------------------------------
	// A tags field which will create a comma separated string
	private CFWField<String> tags = 
			CFWField.newString(FormFieldType.TAGS, "TAGS")
					.setValue("foo,test,bar,bla")
					.addAttribute("maxTags", "20")
					.setDescription("Type at least 3 characters to get suggestions.")
					.setAutocompleteHandler(new CFWAutocompleteHandler(5) {
						
						public AutocompleteResult getAutocompleteData(HttpServletRequest request, String inputValue, int cursorPosition) {
							AutocompleteList list = new AutocompleteList();
							
							for(int i = 0; i < this.getMaxResults(); i++ ) {
								String tag = "Tag_"+inputValue+"_"+i;
								list.addItem(tag);
							}
							return new AutocompleteResult(list);
						}
					});
	
	//------------------------------------------------------------------------------------------------
	// A tags selector which will be used to select keys with an associated label.
	// new CFWAutocompleteHandler(10,1) will cause the autocomplete to display max 10 results and start autocomplete from 1 character
	private CFWField<LinkedHashMap<String,String>> tagsselector = 
			CFWField.newTagsSelector("JSON_TAGS_SELECTOR")
					.setDescription("Start typing to get suggestions.")
					.setAutocompleteHandler(new CFWAutocompleteHandler(10,1) {
						
						public AutocompleteResult getAutocompleteData(HttpServletRequest request, String inputValue, int cursorPosition) {
							AutocompleteList list = new AutocompleteList();
							for(int i = 0; i < this.getMaxResults(); i++ ) {
								String tag = inputValue+"_"+i;
								list.addItem("key_"+tag, "Label_"+tag);
							}
							return new AutocompleteResult(list);
						}
					});
	
	//------------------------------------------------------------------------------------------------
	// A text field with autocomplete
	// new CFWAutocompleteHandler(10,1) will cause the autocomplete to display max 10 results and start autocomplete from 1 character
	private CFWField<String> autocomplete = CFWField.newString(FormFieldType.TEXT, "AUTOCOMPLETE")
			.setAutocompleteHandler(new CFWAutocompleteHandler(10,1) {
				
				public AutocompleteResult getAutocompleteData(HttpServletRequest request, String inputValue, int cursorPosition) {
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
	
	//------------------------------------------------------------------------------------------------
	// A text field showing the two additional autocomplete method "replace" and "append"
	// new CFWAutocompleteHandler(5,1) will cause the autocomplete to display max 5 results and start autocomplete from 1 character
	private CFWField<String> autocompleteMethods = CFWField.newString(FormFieldType.TEXT, "AUTOCOMPLETE_METHODS")
		.setAutocompleteHandler(new CFWAutocompleteHandler(5,1) {
			
			public AutocompleteResult getAutocompleteData(HttpServletRequest request, String inputValue, int cursorPosition) {
				
				if (Strings.isNullOrEmpty(inputValue)) return null;
				
				//-----------------------------------------------
				// Create Replace Examples
				AutocompleteList replaceLastList = new AutocompleteList();
				replaceLastList.title("Replace Last");
				for(int i = 0; i < 5; i++ ) {
					String[] splitted = inputValue.split(" ");
					String lastWord = splitted[splitted.length-1];
					
					replaceLastList.addItem(
						new AutocompleteItem(
							lastWord.toUpperCase()+i, 
							"Replace Last Word with "+lastWord.toUpperCase()+i, 
							"Replace Last word with uppercase.")
								.setMethodReplaceLast(lastWord)
					);
				}
				
				//-----------------------------------------------
				// Create Replace Before Cursor Examples
				AutocompleteList listMethodReplacebeforeCursor = new AutocompleteList();
				listMethodReplacebeforeCursor.title("Replace Last Before Cursor");
				
				for(int i = 0; i < 5; i++ ) {
					String beforeCursor = inputValue.substring(0, cursorPosition);
					System.out.println("beforeCursor:"+beforeCursor);
					String[] splitted = beforeCursor.split(" ");
					String lastWord = splitted[splitted.length-1];
					
					listMethodReplacebeforeCursor.addItem(
						new AutocompleteItem(
							lastWord.toUpperCase()+i, 
							"Replace Before Cursor with "+lastWord.toUpperCase()+i, 
							"Replace last word before cursor with uppercase.")
								.setMethodReplaceBeforeCursor(lastWord)
					);
				}
				
				//-----------------------------------------------
				// Create Append Examples
				AutocompleteList listMethodAppend = new AutocompleteList();
				listMethodAppend.title("Append at End of String");
				for(int i = 0; i < 5; i++ ) {
					String[] splitted = inputValue.split(" ");
					String lastWord = splitted[splitted.length-1];
					
					listMethodAppend.addItem(
							new AutocompleteItem(
								lastWord.toUpperCase()+i, 
								"Append "+lastWord.toUpperCase()+i, 
								"Append last word as uppercase.")
									.setMethodAppend()
						);
				}
				
				return new AutocompleteResult(replaceLastList)
						.addList(listMethodReplacebeforeCursor)
						.addList(listMethodAppend)
						.setHTMLDescription("<p>Example of autocomplete methods replace and append.</ol>");
			}
		});
		
	
	//======================================================================
	// The default constructor
	public FormExampleCFWObject() {
		initialize();
	}
		
	//======================================================================
	// Initialize Fields and add the fields to the object
	public void initialize() {
				
		this.addFields(
				firstname
				, lastname
				, email
				, withDefaultValue
				, description
				, textarea
				, number
				, numberRange
				, booleanField
				, date
				, timestamp
				, timeframe
				, timezone
				, schedule
				, chartSettings
				, select
				, keyValSelect
				, checkoxesNoDefaults
				, checkoxesWithDefaults
				, customList
				, valueLabel
				, language
				, unmodifiableText
				, editor
				, tags
				, tagsselector
				, autocomplete
				, autocompleteMethods);
	}

}
