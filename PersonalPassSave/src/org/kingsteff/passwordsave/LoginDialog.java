package org.kingsteff.passwordsave;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

public class LoginDialog extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Button btnLogin = null;
	private TextField loginTextField = null;
	private PasswordField passwordTextfield = null;

	private final CssLayout logincenterLayout = new CssLayout();
	private Label messageStringLabel;

	public LoginDialog() {
		initUI();
	}

	private void initUI() {

		btnLogin = new Button("Login");
		loginTextField = new TextField();
		passwordTextfield = new PasswordField();
		btnLogin.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(Button.ClickEvent event) {
				PersonalpasssaveApplication
						.getInstance()
						.getBaseController()
						.loginPerformed(loginTextField.getValue(),
								passwordTextfield.getValue());
			}

		});

		HorizontalLayout loginFieldLayout = new HorizontalLayout();
		HorizontalLayout passwordFieldLayout = new HorizontalLayout();
		HorizontalLayout buttonLayout = new HorizontalLayout();
		HorizontalLayout messageLayout = new HorizontalLayout();

		btnLogin.setClickShortcut(KeyCode.ENTER);

		Label loginMessage = new Label("Personal Passwordsave");
		loginMessage.setStyleName("big_header");

		Label labelLoginName = new Label("Username");
		labelLoginName.setStyleName("label_general");
		labelLoginName.setWidth("100px");
		loginTextField.setWidth("150px");

		Label labelpassword = new Label("Password");

		labelpassword.setStyleName("label_general");
		labelpassword.setWidth("100px");
		passwordTextfield.setWidth("150px");

		loginFieldLayout.setWidth("100%");
		passwordFieldLayout.setWidth("100%");
		buttonLayout.setWidth("100%");
		messageLayout.setWidth("100%");

		loginFieldLayout.addComponent(labelLoginName);
		loginFieldLayout.addComponent(loginTextField);
		loginFieldLayout.setStyleName("sublayout");

		passwordFieldLayout.addComponent(labelpassword);
		passwordFieldLayout.addComponent(passwordTextfield);
		passwordFieldLayout.setStyleName("sublayout");

		messageStringLabel = new Label();

		messageLayout.addComponent(messageStringLabel);
		messageLayout.setStyleName("sublayout");
		// message

		buttonLayout.addComponent(btnLogin);
		buttonLayout.setStyleName("sublayout");

		logincenterLayout.addComponent(loginFieldLayout);
		logincenterLayout.addComponent(passwordFieldLayout);
		logincenterLayout.addComponent(messageLayout);
		logincenterLayout.addComponent(buttonLayout);
		logincenterLayout.setHeight("200px");
		logincenterLayout.setWidth("350px");

		setContent(logincenterLayout);
		this.setStyleName("centerLayout");
		this.center();
		this.setModal(false);
		this.setClosable(true);
	}

	public void changeMessageInLoginDialog(String message) {
		messageStringLabel.setValue(message);
		messageStringLabel.addStyleName("messageLabelRed");
	}

}
