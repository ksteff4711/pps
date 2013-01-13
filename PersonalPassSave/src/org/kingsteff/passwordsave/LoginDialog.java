package org.kingsteff.passwordsave;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

public class LoginDialog extends Window {

	private Button btnLogin = null;
	private TextField login = null;
	private PasswordField password = null;

	private AbsoluteLayout mainLayout = new AbsoluteLayout();

	private AbsoluteLayout logincenterLayout = new AbsoluteLayout();

	public LoginDialog() {
		setName("login");
		initUI();
	}

	private void initUI() {

		btnLogin = new Button("Login");
		login = new TextField();
		// login.setValue("admin");
		password = new PasswordField();
		// password.setValue("admin");
		btnLogin.addListener(new Button.ClickListener() {
			public void buttonClick(Button.ClickEvent event) {
				PersonalpasssaveApplication
						.getInstance()
						.getBaseController()
						.loginPerformed((String) login.getValue(),
								(String) password.getValue());
			}

		});

		btnLogin.setClickShortcut(KeyCode.ENTER);

		Label loginMessage = new Label("Personal Passwordsave");
		Label loginLogin = new Label("");
		loginMessage.setStyleName("big_header");

		Label labelLoginName = new Label("Username");
		labelLoginName.setStyleName("label_general");
		labelLoginName.setWidth("100px");
		labelLoginName.setHeight("20px");
		login.setWidth("150px");

		Label labelpassword = new Label("Password");

		labelpassword.setStyleName("label_general");
		labelpassword.setWidth("100px");
		labelpassword.setHeight("20px");
		password.setWidth("150px");

		Label labeldomainChooser = new Label("Domain wählen");
		labeldomainChooser.setStyleName("label_general");
		labeldomainChooser.setWidth("100px");
		labeldomainChooser.setHeight("20px");

		logincenterLayout.setWidth("500px");
		logincenterLayout.setHeight("300px");
		logincenterLayout.addComponent(login, "top:50px;left:155px");
		logincenterLayout.addComponent(password, "top:80px;left:155px");

		logincenterLayout.addComponent(loginMessage, "top:10px;left:10px");
		logincenterLayout.addComponent(labelLoginName, "top:50px;left:10px");
		logincenterLayout.addComponent(labelpassword, "top:80px;left:10px");
		logincenterLayout.addComponent(btnLogin, "top:120px;left:10px");
		mainLayout.addComponent(logincenterLayout, "left: 35%; right: 0%;"
				+ "top: 30%; bottom: 0%;");
		addComponent(mainLayout);
	}

	public Layout getMainLayout() {
		return mainLayout;
	}

	public void setMainLayout(AbsoluteLayout mainLayout) {
		this.mainLayout = mainLayout;
	}

}
