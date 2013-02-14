package org.kingsteff.passwordsave;

import java.util.UUID;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

public class BaseController {

	protected static final int YESNO_DIALOG_YES = 1;
	protected static final int YESNO_DIALOG_NO = 0;
	private String currentUser;
	private BaseWindow window;

	public void loginPerformed(String username, String password) {
		if (!(username.trim().equals("") || password.trim().equals(""))) {
			System.out.println("init login");
			boolean checkUsernamePassword = PersonalpasssaveApplication
					.getInstance().getLoginManager()
					.checkUsernamePassword(username, password);
			System.out.println("checkuser:" + checkUsernamePassword);
			if (checkUsernamePassword) {
				if (!username.equals("admin")) {
					System.out.println("login as user");
					if (!PersonalpasssaveApplication.getInstance()
							.getLoginManager()
							.checkIfUserhasReachedMaxLoginTries(username)) {
						loginUser(username);
					} else {
						PersonalpasssaveApplication
								.getInstance()
								.getWindow()
								.showNotification(
										"The maximum number of login attempts has been reached!",
										Notification.TYPE_ERROR_MESSAGE);
					}
				} else {
					System.out.println("login as admin");
					loginUser(username);
				}

			} else {
				if (!username.equals("admin")) {
					if (!PersonalpasssaveApplication.getInstance()
							.getLoginManager()
							.checkIfUserhasReachedMaxLoginTries(username)) {
						PersonalpasssaveApplication.getInstance()
								.getLoginManager().increaseLoginCount(username);
						PersonalpasssaveApplication
								.getInstance()
								.getWindow()
								.showNotification("Login incorrect!",
										Notification.TYPE_ERROR_MESSAGE);
					} else {
						PersonalpasssaveApplication
								.getInstance()
								.getWindow()
								.showNotification(
										"The maximum number of login attempts has been reached!",
										Notification.TYPE_ERROR_MESSAGE);
					}
				}

			}
		} else {
			PersonalpasssaveApplication
					.getInstance()
					.getWindow()
					.showNotification("Username or password must not be empty",
							Notification.TYPE_ERROR_MESSAGE);
		}
	}

	private void loginUser(String username) {
		currentUser = username;
		window = new BaseWindow();
		PersonalpasssaveApplication.getInstance().getWindow()
				.setContent(window);
		PersonalpasssaveApplication.getInstance().getLoginManager()
				.removeUserAttempts(username);
	}

	public String getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

	public BaseWindow getWindow() {
		return window;
	}

	public void setWindow(BaseWindow window) {
		this.window = window;
	}

	public void addWindow(Window newUserWindow) {
		PersonalpasssaveApplication.getInstance().getWindow()
				.addWindow(newUserWindow);

	}

	public void removeWindow(Window newUserWindow) {
		PersonalpasssaveApplication.getInstance().getWindow()
				.removeWindow(newUserWindow);

	}

	public static String generaterandomId() {
		String uuid = UUID.randomUUID().toString();
		return uuid;
	}

	public void openYesNoDialog(String message, String header,
			final PpsDialogResultListener listener) {

		final Window dialog = new Window(header);
		dialog.setModal(true);
		window.getWindow().addWindow(dialog);
		Label text = new Label(message);
		text.setContentMode(Label.CONTENT_XHTML);
		dialog.addComponent(text);
		HorizontalLayout tlayout = new HorizontalLayout();
		tlayout.setSpacing(true);
		tlayout.addComponent(new Button("Yes", new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				listener.yesNoResultReturned(YESNO_DIALOG_YES);
				window.getWindow().removeWindow(dialog);

			}
		}));
		tlayout.addComponent(new Button("No", new Button.ClickListener() {

			public void buttonClick(ClickEvent event) {
				listener.yesNoResultReturned(YESNO_DIALOG_NO);
				window.getWindow().removeWindow(dialog);

			}
		}));
		dialog.addComponent(tlayout);
		dialog.setWidth("450px");
		dialog.setHeight("220px");
	}

}
