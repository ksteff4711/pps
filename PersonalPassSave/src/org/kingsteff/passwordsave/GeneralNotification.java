package org.kingsteff.passwordsave;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class GeneralNotification extends Window {

	/**
	 * 
	 */
	public static int INFO_MESSAGE = 0;
	public static int ERROR_MESSAGE = 1;
	private static final long serialVersionUID = 12312313L;
	VerticalLayout vertical = new VerticalLayout();
	Button close = new Button();
	Button dummy = new Button("dummy");
	boolean isHtml = false;
	String message = "";
	private final Label messageLabel;
	private final GeneralNotification mylink = this;

	public GeneralNotification(String message, boolean ishtml, int messagetype,
			final BaseController base) {
		System.out.println("GN::" + ishtml);
		base.beforShowMessage();
		messageLabel = new Label();
		messageLabel.setContentMode(ContentMode.HTML);
		messageLabel.setValue(getMessageTextAsHtml(message));

		initDialogUI();
		close.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				mylink.close();
				base.notificationReturned();
			}
		});
	}

	private void initDialogUI() {
		close.setCaption("Close");
		HorizontalLayout h1 = new HorizontalLayout();
		h1.setWidth("100%");
		h1.addComponent(new Label("SystemMessage:"));
		HorizontalLayout h2 = new HorizontalLayout();
		h2.setWidth("100%");
		h2.addComponent(messageLabel);
		HorizontalLayout h3 = new HorizontalLayout();
		h3.setWidth("100%");
		h3.addComponent(close);
		h3.setComponentAlignment(close, Alignment.BOTTOM_RIGHT);
		h2.setComponentAlignment(messageLabel, Alignment.MIDDLE_LEFT);
		vertical.addComponent(h1);
		vertical.addComponent(h2);
		vertical.addComponent(h3);
		setContent(vertical);
	}

	public void show() {
		setModal(false);
		setClosable(false);
		PersonalpasssaveApplication.getInstance().addWindow(this);
		this.center();
	}

	private String getMessageTextAsHtml(String message2) {

		return "<html><p align=left width='200px'>" + message2 + "<p></html>";

	}

}
