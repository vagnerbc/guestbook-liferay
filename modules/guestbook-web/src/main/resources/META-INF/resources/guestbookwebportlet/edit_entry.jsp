<%@include file="../init.jsp" %>

<% 
boolean editable = ParamUtil.getBoolean(renderRequest, "editable");

long entryId = ParamUtil.getLong(renderRequest, "entryId");

Entry entry = null;
if (entryId > 0) {
  entry = EntryLocalServiceUtil.getEntry(entryId);
}

long guestbookId = ParamUtil.getLong(renderRequest, "guestbookId");

%>

<portlet:renderURL var="viewURL">
	<portlet:param name="mvcPath" value="/guestbookwebportlet/view.jsp"></portlet:param>
</portlet:renderURL>

<portlet:actionURL name="addEntry" var="addEntryURL"></portlet:actionURL>

<aui:form action="<%= addEntryURL %>" name="<portlet:namespace />fm">
	
	<aui:model-context bean="<%= entry %>" model="<%= Entry.class %>" />

    <aui:fieldset>
        <aui:input name="name" disabled="<%=!editable%>">
        	<aui:validator name="required" errorMessage="The Name is required." />
        </aui:input>
        <aui:input name="email" disabled="<%=!editable%>">
        	<aui:validator name="required" errorMessage="The Email is required."/>
        	<aui:validator name="email" errorMessage="The Email is incorrect."/>
        </aui:input>
        <aui:input name="message" disabled="<%=!editable%>" />
        <aui:input name="entryId" type="hidden" />
        <aui:input name="guestbookId" type="hidden" value='<%= entry == null ? guestbookId : entry.getGuestbookId() %>'/>
    </aui:fieldset>

	<c:if test="<%=editable%>">
	    <aui:button-row>
	        <aui:button type="submit"></aui:button>
	        <aui:button type="cancel" onClick="<%= viewURL.toString() %>"></aui:button>
	    </aui:button-row>
    </c:if>
</aui:form>