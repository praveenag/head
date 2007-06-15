<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://struts.apache.org/tags-html-el" prefix="html-el"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="/tags/mifos-html" prefix="mifos"%>
<%@taglib uri="/tags/date" prefix="date"%>
<%@ taglib uri="/mifos/custom-tags" prefix="customtags"%>
<%@ taglib uri="/sessionaccess" prefix="session"%>
<style type="text/css">
h1 {

	text-decoration:none; 
	color:#000000;
	font-family: Arial, Verdana, Helvetica, sans-serif;
	font-size: 11pt;
	font-weight: bold;
	margin-top: 25px;
	margin-bottom: 25px;
}
h2{
	text-decoration:none; 
	color:#000000;
	font-family: Arial, Verdana, Helvetica, sans-serif;
	font-size: 8pt;
	font-weight: bold;
}
orange{
	color:#CC6601;
}
red {
	color:#FF0000;
}
hr {
	color: #D7DEEE;
	background-color: #D7DEEE;
	text-align: left;
	margin: 15px auto 5px 0;
	height: 1px;
	width: 95%;
	border: 0px;
}
.entry {
	height: 30px;
	padding-top: 4px;
	padding-right: 5px;
	padding-bottom: 0px;
	padding-left: 5px;
	border-top-width: 1px;
	border-top-style: solid;
	border-top-color: #D7DEEE;
}
</style>
<tiles:insert definition=".clientsacclayoutsearchmenu">
<tiles:put name="body" type="string">
<html-el:form action="/surveyInstanceAction.do?method=preview">
<h1><c:out value="${session:getFromSession(sessionScope.flowManager,requestScope.currentFlowKey,'client').displayName}"/> - 
<orange><mifos:mifoslabel name="Surveys.instance.entersurveydata" bundle="SurveysUIResources"/></orange></h1>
<span class="fontnormal"><mifos:mifoslabel name="Surveys.instance.instructions" bundle="SurveysUIResources"/></span>
<hr>
<h1><c:out value="${sessionScope.retrievedSurvey.name}"/></h1>
<hr>
<font class="fontnormalRedBold"><html-el:errors bundle="SurveysUIResources" /></font>
<table width="100%" border="0" cellpadding="3" cellspacing="0">
	<tr>
		<td width="25%" height="30" align="right">
		<red>*</red><span class="fontnormal8ptbold"><mifos:mifoslabel name="Surveys.instance.dateofsurvey" bundle="SurveysUIResources"/>:</span>
		</td>
		<td width="70%">
		<date:datetag property="dateSurveyed" renderstyle="simple"/>
		</td>
	</tr>
	<tr>
		<td height="30" align="right">
		<span class="fontnormal8ptbold"><mifos:mifoslabel name="Surveys.instance.surveyedby" bundle="SurveysUIResources"/>:</span></td>
		<td height="30" class="drawtablerow">
		<html-el:text property="officerName" />
		</td>
	</tr>
</table>
<table width="95%">
	<c:set var="count" value="1"/>
	<c:forEach var="question" items="${sessionScope.retrievedSurvey.questions}">
		<tr>
			<td class="entry">
			<h2><c:out value="${count}"/>. <c:out value="${question.question.questionText}"/></h2>
			</td>
		</tr>
		<tr>
			<td class="entry">
			<c:choose>
			<c:when test="${question.question.answerType == 4}">
			<c:forEach var="choice" items="${question.question.choices}">
			<html-el:radio property="response(${question.question.questionId})" value="${choice.choiceId}">
			<c:out value="${choice.choiceText}"/></html-el:radio><br>
			</c:forEach>
			<html-el:radio property="response(${question.question.questionId})" value="" style="visibility:hidden;checked:true"/>
			</c:when>
			<c:when test="${question.question.answerType == 2}">
			<html-el:textarea property="response(${question.question.questionId})" cols="70" rows="10" />
			</c:when>
			<c:otherwise><html-el:text property="response(${question.question.questionId})"/></c:otherwise>
			</c:choose>
			</td>
		</tr>
		<tr>
			<td class="entry">&nbsp;</td>
		</tr>
	<c:set var="count" value="${count+1}"/>
	</c:forEach>
		<tr>
		<td>
		<html-el:reset style="width:65px;" styleClass="cancelbuttn">
		<mifos:mifoslabel name="Surveys.button.clearall" bundle="SurveysUIResources" />
		</html-el:reset>
		</td>
		</tr>
</table>
<br><hr>
<table width="93%" border="0" cellpadding="0" cellspacing="0">
<tr>
		<td align="center">
		<html-el:submit style="width:65px;" property="button" styleClass="buttn">
		<mifos:mifoslabel name="Surveys.button.preview" bundle="SurveysUIResources" />
		</html-el:submit>&nbsp; 
		<html-el:button property="calcelButton" style="width:65px;" styleClass="cancelbuttn" onclick="window.location='adminAction.do?method=load">
		<mifos:mifoslabel name="Surveys.button.cancel" bundle="SurveysUIResources" />
		</html-el:button>
		</td>
	</tr>
</table>
</html-el:form>
</tiles:put> 
</tiles:insert>