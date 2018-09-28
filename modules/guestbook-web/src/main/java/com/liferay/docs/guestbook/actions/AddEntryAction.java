package com.liferay.docs.guestbook.actions;

import com.liferay.docs.guestbook.constants.GuestbookPortletKeys;
import com.liferay.docs.guestbook.model.Entry;
import com.liferay.docs.guestbook.service.EntryLocalService;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
    immediate = true,
    property = {
        "javax.portlet.name=" + GuestbookPortletKeys.GUESTBOOK,
        "mvc.command.name=addEntry"
    },
    service = MVCActionCommand.class
)
public class AddEntryAction extends BaseMVCActionCommand {

  @Override
  protected void doProcessAction(ActionRequest request, ActionResponse response) throws Exception {
    ServiceContext serviceContext = ServiceContextFactory.getInstance(
        Entry.class.getName(), request);

    String userName = ParamUtil.getString(request, "name");
    String email = ParamUtil.getString(request, "email");
    String message = ParamUtil.getString(request, "message");
    long guestbookId = ParamUtil.getLong(request, "guestbookId");
    long entryId = ParamUtil.getLong(request, "entryId");

    if (entryId > 0) {

      try {

        _entryLocalService.updateEntry(guestbookId, entryId, userName,
            email, message, serviceContext);

        SessionMessages.add(request, "entryAdded");

        response.setRenderParameter(
            "guestbookId", Long.toString(guestbookId));

      } catch (Exception e) {
        System.out.println(e);

        SessionErrors.add(request, e.getClass().getName());

        PortalUtil.copyRequestParameters(request, response);

        response.setRenderParameter(
            "mvcPath", "/guestbookwebportlet/edit_entry.jsp");
      }

    } else {

      try {
        _entryLocalService.addEntry(guestbookId, userName, email,
            message, serviceContext);

        SessionMessages.add(request, "entryAdded");

        response.setRenderParameter(
            "guestbookId", Long.toString(guestbookId));

      } catch (Exception e) {
        SessionErrors.add(request, e.getClass().getName());

        PortalUtil.copyRequestParameters(request, response);

        response.setRenderParameter(
            "mvcPath", "/guestbookwebportlet/edit_entry.jsp");
      }
    }

  }

  @Reference(unbind = "-")
  protected void setEntryService(EntryLocalService entryLocalService) {
    _entryLocalService = entryLocalService;
  }

  private EntryLocalService _entryLocalService;

}
