package com.nectar.ambrosia.reporting;

public class HTMLTreeView {
	
	public static String getLIItemWithExpendCollapse(String id) {
		return String.format("<input type='checkbox' checked='checked' id='%s' />", id);
	}
	public static String getLILabel(String forID,String label) {
		return  String.format("<label class='tree_label' for='%s'>%s</label>",forID,label);
	}
	public static String getLIItem(String customProp, String label) {
		return String.format("<li><span class='tree_label' %s >%s</span></li>", customProp,label);
	}
	public static String openUL() {
		return "<ul>";
	}
	public static String closeUL() {
		return "</ul>";
	}
	public static String openLI() {
		return "<li>";
	}
	public static String closeLI() {
		return "</li>";
	}
}
