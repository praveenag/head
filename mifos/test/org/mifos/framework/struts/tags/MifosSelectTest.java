package org.mifos.framework.struts.tags;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.Map;

import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

public class MifosSelectTest {

	private static final String INTRODUCTORY_STYLES_AND_SCRIPT = " <STYLE> " +
				".ttip {border:1px solid black;" +
				"font-size:12px;" +
				"layer-background-color:lightyellow;" +
				"background-color:lightyellow}  " +
				"</STYLE> " +
				"<script language=\"javascript\" " +
				"SRC=\"pages/framework/js/Logic.js\" >" +
				"</script> " +
				"<link rel=\"stylesheet\" type=\"text/css\" " +
				"href=\"pages/framework/css/tooltip.css\" " +
				"title=\"MyCSS\"/>";
	
	@Test public void gettersAndSetters() {
		MifosSelect mifosSelect = new MifosSelect();
		mifosSelect.setId("id");
		mifosSelect.setInput("input");
		mifosSelect.setLabel("label");
		mifosSelect.setMultiple("multiple");
		mifosSelect.setName("name");
		mifosSelect.setOutput("output");
		mifosSelect.setProperty("property");
		mifosSelect.setProperty1("property1");
		mifosSelect.setProperty2("property2");
		mifosSelect.setSelectStyle("selectStyle");
		mifosSelect.setSize("1");
		mifosSelect.setValue("key","value");
		assertEquals("id",mifosSelect.getId());
		assertEquals("input",mifosSelect.getInput());
		assertEquals("label",mifosSelect.getLabel());
		assertEquals("multiple",mifosSelect.getMultiple());
		assertEquals("name",mifosSelect.getName());
		assertEquals("output",mifosSelect.getOutput());
		assertEquals("property",mifosSelect.getProperty());
		assertEquals("property1",mifosSelect.getProperty1());
		assertEquals("property2",mifosSelect.getProperty2());
		assertEquals("selectStyle",mifosSelect.getSelectStyle());
		assertEquals("1",mifosSelect.getSize());
		mifosSelect = new MifosSelect("newlabel");
		assertEquals("newlabel",mifosSelect.getLabel());
	}
	
	@Test public void renderEmpty() {
		assertEquals(INTRODUCTORY_STYLES_AND_SCRIPT +
			"<table >" +
			"<tr> " +
			"<td>" +
			selectTheItem(true) +
			"</td>" +
			"<td>" +
			"<table width=\"50%\" border=\"0\" " +
			"cellspacing=\"0\" cellpadding=\"3\"> " +
			"<tr>" +
			"<td align=\"center\">" +
			"<INPUT  name=\"MoveRight\" type=\"button\" value=\"Add >>\" " +
			"style=\"width:65px\" class=\"insidebuttn\" " +
			"onclick=\"moveOptions(this.form.LeftSelect,this.form.null)\"" +
			"onMouseover=\"showtip(this,event,'Click To move the selected item')\" " +
			"onMouseout=\"hidetip()\" >" +
			"</INPUT>" +
			"</td>" +
			"</tr>" +
			"<tr>" +
			"<td height=\"26\" align=\"center\">" +
			"<INPUT  type=\"button\" value=\"<< Remove\" " +
			"style=\"width:65px\" class=\"insidebuttn\" " +
			"onclick=\"moveOptions(this.form.null,this.form.LeftSelect)\"" +
			"onMouseover=\"showtip(this,event,'Click To move the selected item')\" " +
			"onMouseout=\"hidetip()\" >" +
			"</INPUT>" +
			"</td>" +
			"</tr>" +
			"</table>" +
			"</td>" +
			"<td>" +
			selectTheItem(false) +
			"</td>" +
			"</tr>" +
			"</table>" +
			"<div id=\"tooltip\" " +
			"style=\"position:absolute;" +
			"visibility:hidden;" +
			"border:1px solid black;" +
			"font-size:12px;" +
			"layer-background-color:lightyellow;" +
			"background-color:lightyellow;" +
			"z-index:1;" +
			"padding:1px\">" +
			"</div>",
			new MifosSelect().render(
				Collections.EMPTY_LIST,
				Collections.EMPTY_LIST));
	}
	
	private String selectTheItem(boolean leftSelect) {
		return 
			"<SELECT onMouseover=\"showtip(this,event,'Select the item(s)')\" " +
			"onMouseOut=\"hidetip()\" " +
			"onchange =\"showtip(this,event,'Select the item(s)')\" " +
			"style=\"WIDTH: 136px\" " +
			(leftSelect ? "name=\"LeftSelect\" " : "") +
			"size=\"5\">" +
			"</SELECT> ";
	}

	@Test public void helperEmpty() throws Exception {
		Map map = new MifosSelect().helper(Collections.EMPTY_LIST);
		assertEquals(null, map);
	}
	
	@Test public void helperNonEmpty() throws Exception {
		MifosSelect select = new MifosSelect();
		select.setProperty1("propertyOne");
		select.setProperty2("propertyTwo");
		Map map = select.helper(Collections.singletonList(new Foo()));
		assertEquals(1, map.size());
		assertEquals(new Integer(5), map.keySet().iterator().next());
		assertEquals("Elm", map.get(5));
	}
	
	@Test(expected=IllegalAccessException.class)
	public void helperPrivate() throws Exception {
		MifosSelect select = new MifosSelect();
		select.setProperty1("propertyOne");
		select.setProperty2("privateProperty");
		select.helper(Collections.singletonList(new Foo()));
	}
	
	class Foo {
		public int getPropertyOne() {
			return 5;
		}
		
		public String getPropertyTwo() {
			return "Elm";
		}
		
		@SuppressWarnings("unused")
		private String getPrivateProperty() {
			return "mine";
		}
	}

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(MifosSelectTest.class);
	}

}
