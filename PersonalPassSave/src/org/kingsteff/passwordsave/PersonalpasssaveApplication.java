package org.kingsteff.passwordsave;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

@Theme("pps")
public class PersonalpasssaveApplication extends UI {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BaseController baseController;
	private LoginManager loginManager;
	private PasswordManager passwordManager;
	private FileArchiveController fileArchiveController;

	@Override
	public void init(VaadinRequest request) {
		loginManager = LoginManager.getInstance();
		baseController = new BaseController();
		passwordManager = new PasswordManager();
		fileArchiveController = new FileArchiveController();
		LoginDialog dialog = new LoginDialog();
		dialog.getMainLayout().setParent(null);
		setContent(dialog.getMainLayout());
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

	public static PersonalpasssaveApplication getInstance() {
		return (PersonalpasssaveApplication) UI.getCurrent();
	}

	public BaseController getBaseController() {
		// TODO Auto-generated method stub
		return baseController;
	}

}
