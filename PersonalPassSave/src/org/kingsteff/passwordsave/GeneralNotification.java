package org.kingsteff.passwordsave;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
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
		vertical.addComponent(messageLabel);
		vertical.addComponent(close);
		vertical.addComponent(dummy);
		vertical.setComponentAlignment(close, Alignment.BOTTOM_RIGHT);
		vertical.setComponentAlignment(messageLabel, Alignment.MIDDLE_LEFT);
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
