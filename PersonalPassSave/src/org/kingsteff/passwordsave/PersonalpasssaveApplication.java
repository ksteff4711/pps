package org.kingsteff.passwordsave;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

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
	private LoginDialog dialog;
	private ObjectMarshaller marshaller;

	@Override
	public void init(VaadinRequest request) {
		PersonalPassConstants.MAINDIR = VaadinServlet.getCurrent()
		        .getServletContext().getRealPath("/")+"pps"+"/";
		marshaller = new ObjectMarshaller();
		loginManager = LoginManager.getInstance();
		baseController = new BaseController();
		passwordManager = new PasswordManager();
		fileArchiveController = new FileArchiveController();
		dialog = new LoginDialog();
		
		
		addWindow(dialog);
	}

	public ObjectMarshaller getMarshaller() {
		return marshaller;
	}

	public void setMarshaller(ObjectMarshaller marshaller) {
		this.marshaller = marshaller;
	}

	public LoginDialog getDialog() {
		return dialog;
	}

	public void setDialog(LoginDialog dialog) {
		this.dialog = dialog;
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

	public void closeMe() {
		this.close();
	}

}
