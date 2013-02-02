package org.kingsteff.passwordsave;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.HttpServletRequestListener;
import com.vaadin.ui.Window;

public class PersonalpasssaveApplication extends Application implements
		HttpServletRequestListener {
	private static ThreadLocal<PersonalpasssaveApplication> threadLocal = new ThreadLocal<PersonalpasssaveApplication>();
	private BaseController baseController;
	private LoginManager loginManager;
	private PasswordManager passwordManager;
	private FileArchiveController fileArchiveController;
	private Window mainWindow;

	@Override
	public void init() {
		setTheme("pps");
		mainWindow = new Window("Personalpasssave Application");

		loginManager = LoginManager.getInstance();
		baseController = new BaseController();
		passwordManager = new PasswordManager();
		fileArchiveController = new FileArchiveController();
		LoginDialog dialog = new LoginDialog();
		dialog.getMainLayout().setParent(null);
		mainWindow.setContent(dialog.getMainLayout());
		setMainWindow(mainWindow);
	}

	public LoginManager getLoginManager() {
		return loginManager;
	}

	public FileArchiveController getFileArchiveController() {
		return fileArchiveController;
	}

	public void setFileArchiveController(
			FileArchiveController fileArchiveController) {
		this.fileArchiveController = fileArchiveController;
	}

	public void setLoginManager(LoginManager loginManager) {
		this.loginManager = loginManager;
	}

	public PasswordManager getPasswordManager() {
		return passwordManager;
	}

	public void setPasswordManager(PasswordManager passwordManager) {
		this.passwordManager = passwordManager;
	}

	public void setBaseController(BaseController baseController) {
		this.baseController = baseController;
	}

	/**
	 * @return the current application instance
	 */
	public static PersonalpasssaveApplication getCurrent() {
		return threadLocal.get();
	}

	// Set the current application instance
	public static void setInstance(PersonalpasssaveApplication application) {
		if (getInstance() == null) {
			threadLocal.set(application);
		}
	}

	public static PersonalpasssaveApplication getInstance() {
		return threadLocal.get();
	}

	public BaseController getBaseController() {
		// TODO Auto-generated method stub
		return baseController;
	}

	public void onRequestStart(HttpServletRequest request,
			HttpServletResponse response) {
		PersonalpasssaveApplication.setInstance(this);
	}

	public void onRequestEnd(HttpServletRequest request,
			HttpServletResponse response) {
		threadLocal.remove();
	}

	public Window getWindow() {
		// TODO Auto-generated method stub
		return mainWindow;
	}

}
