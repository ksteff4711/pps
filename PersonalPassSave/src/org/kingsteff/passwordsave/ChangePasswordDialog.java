package org.kingsteff.passwordsave;

import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;

public class ChangePasswordDialog extends GridLayout {

	private Button cancel;
	private Button save;
	private PasswordField textfieldPassword;
	private PasswordField textFieldverifyPassword;

	public ChangePasswordDialog() {
		initUI();
	}

	private void initUI() {
		textfieldPassword = new PasswordField();
		textFieldverifyPassword = new PasswordField();
		Label labelPassword = new Label("Password:");
		Label headingCreateUser = new Label("Change your password");
		Label labelVerfiyPassword = new Label("Verify Password:");
		save = new Button("Save");
		cancel = new Button("Cancel");
		this.setColumns(2);
		this.setRows(10);
		this.addComponent(headingCreateUser, 0, 0, 1, 0);
		this.addComponent(labelPassword);
		this.addComponent(textfieldPassword);
		this.addComponent(labelVerfiyPassword);
		this.addComponent(textFieldverifyPassword);
		this.addComponent(save);
		this.addComponent(cancel);
		this.setSpacing(true);
	}

	public Button getCancel() {
		return cancel;
	}

	public void setCancel(Button cancel) {
		this.cancel = cancel;
	}

	public Button getSave() {
		return save;
	}

	public void setSave(Button save) {
		this.save = save;
	}

	public PasswordField getTextfieldPassword() {
		return textfieldPassword;
	}

	public void setTextfieldPassword(PasswordField textfieldPassword) {
		this.textfieldPassword = textfieldPassword;
	}

	public PasswordField getTextFieldverifyPassword() {
		return textFieldverifyPassword;
	}

	public void setTextFieldverifyPassword(PasswordField textFieldverifyPassword) {
		this.textFieldverifyPassword = textFieldverifyPassword;
	}

}
