package org.kingsteff.passwordsave;

import java.util.UUID;

import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class BaseController {

	protected static final int YESNO_DIALOG_YES = 1;
	protected static final int YESNO_DIALOG_NO = 0;
	private String currentUser;
	private BaseWindow window;

	public BaseController() {
		PersonalpasssaveApplication.getInstance().setContent(
				new VerticalLayout());
	}

	public void loginPerformed(String username, String password) {
		if (!(username.trim().equals("") || password.trim().equals(""))) {
			System.out.println("init login");
			boolean checkUsernamePassword = PersonalpasssaveApplication
					.getInstance().getLoginManager()
					.checkUsernamePassword(username, password);
			System.out.println("checkuser:" + checkUsernamePassword);
			if (checkUsernamePassword) {
				if (!username.equals("admin")) {
					System.out.println("login as user" + username);
					if (!PersonalpasssaveApplication.getInstance()
							.getLoginManager()
							.checkIfUserhasReachedMaxLoginTries(username)) {
						loginUser(username);
					} else {
						com.vaadin.ui.Notification
								.show("The maximum number of login attempts has been reached!");
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

						new GeneralNotification("Login incorrect", true,
								GeneralNotification.ERROR_MESSAGE, this).show();
					} else {
						new GeneralNotification(
								"The maximum number of login attempts has been reached!",
								true, GeneralNotification.ERROR_MESSAGE, this)
								.show();
					}
				}

			}
		} else {
			new GeneralNotification("Username and Password must not be empty!",
					true, GeneralNotification.ERROR_MESSAGE, this).show();
		}
	}

	private void loginUser(String username) {
		currentUser = username;
		window = new BaseWindow();
		PersonalpasssaveApplication.getInstance().setContent(window);
		PersonalpasssaveApplication.getInstance().getLoginManager()
				.removeUserAttempts(username);
	}

	public void notificationReturned() {
		PersonalpasssaveApplication.getInstance().addWindow(
				PersonalpasssaveApplication.getInstance().getDialog());
	}

	public void beforShowMessage() {
		PersonalpasssaveApplication.getInstance().removeWindow(
				PersonalpasssaveApplication.getInstance().getDialog());
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
		PersonalpasssaveApplication.getInstance().addWindow(newUserWindow);

	}

	public void removeWindow(Window newUserWindow) {
		PersonalpasssaveApplication.getInstance().removeWindow(newUserWindow);

	}

	public static String generaterandomId() {
		String uuid = UUID.randomUUID().toString();
		return uuid;
	}

}
