package org.kingsteff.passwordsave;

import java.util.Date;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Runo;

public class PasswordDetailsDialog extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String password;
	private String login;

	private Button close;

	private Button openWebsite;

	private TextArea passwordArea;
	private TextField websiteField;
	private TextField labelField;
	private TextArea commentArea;
	private TextField loginfield;
	private TextField generatorlength;
	
	private final AbsoluteLayout layout = new AbsoluteLayout();
	
	private Button saveButton;
	
	private Button generatorButton;
	private TextField randomPasswordLenghtTextfield;

	public PasswordDetailsDialog(String password, String login,
			final String website, String comment) {
		super();
		this.password = password;
		this.login = login;
		initWindow(password, login, website, comment, false);

	}

	private void initWindow(String password, String login,
			final String website, String comment, boolean isNewItemLayout) {
		websiteField = new TextField();
		websiteField.setCaption("Website");
		openWebsite = new Button();
		openWebsite.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if ((website != null) && (!(website.trim().equals("")))) {
					PersonalpasssaveApplication.getInstance().getPage()
							.open(website, "_blank");

				} else {

					new GeneralNotification("No Websiteentry URL found.", true,
							GeneralNotification.ERROR_MESSAGE,
							PersonalpasssaveApplication.getInstance()
									.getBaseController()).show();
				}
			}

		});
		openWebsite.setIcon(new ThemeResource("../runo/icons/16/globe.png"));
		openWebsite.addStyleName(Runo.BUTTON_SMALL);
		close = new Button();
		close.setCaption("close");
		close.setIcon(new ThemeResource(
				"../runo/icons/16/cancel.png"));
		saveButton = new Button();
		saveButton.setCaption("save");
		saveButton.setIcon(new ThemeResource(
				"../runo/icons/16/ok.png"));
		generatorButton = new Button();
		generatorButton.setIcon(new ThemeResource(
				"../runo/icons/16/reload.png"));
		
		generatorButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {

				try {
					String string = randomPasswordLenghtTextfield.getValue()
							.toString();
					passwordArea.setValue(PersonalpasssaveApplication.getInstance().getPasswordManager()
							.generateRandomPassword(Integer.parseInt(string),
									false));
				} catch (Exception e) {

					new GeneralNotification(
							"Invalid character in length field", true,
							GeneralNotification.ERROR_MESSAGE,
							PersonalpasssaveApplication.getInstance()
									.getBaseController()).show();
				}
			}
		});
		
		
		
		loginfield = new TextField();
		loginfield.setCaption("Login:");
		loginfield.setWidth("350px");
		websiteField.setWidth("250px");

		commentArea = new TextArea();
		commentArea.setCaption("comment:");
		commentArea.setWidth("350px");
		commentArea.setHeight("50px");

		passwordArea = new TextArea();
		passwordArea.setCaption("current Password:");
		passwordArea.setWidth("350px");
		passwordArea.setHeight("100px");
		
		labelField = new TextField();
		labelField.setWidth("350px");
		labelField.setCaption("Label:");
		
		
		randomPasswordLenghtTextfield = new TextField();
		randomPasswordLenghtTextfield.setCaption("Password Length");
		randomPasswordLenghtTextfield.setValue("12");
		randomPasswordLenghtTextfield.setWidth("45px");
		randomPasswordLenghtTextfield.setImmediate(true);
		
		

		layout.addComponent(labelField, "top:25.0px;left:10.0px;");
		layout.addComponent(loginfield, "top:80.0px;left:10.0px;");
		layout.addComponent(websiteField, "top:160.0px;left:10.0px;");
		layout.addComponent(openWebsite, "top:160.0px;left:280.0px;");
		layout.addComponent(commentArea, "top:240.0px;left:10.0px;");
		layout.addComponent(passwordArea, "top:320.0px;left:10.0px;");
		if(isNewItemLayout){
			layout.addComponent(saveButton, "top:450.0px;left:120.0px;");
			layout.addComponent(generatorButton, "top:450.0px;left:220.0px;");
			layout.addComponent(randomPasswordLenghtTextfield, "top:450.0px;left:290.0px;");
			openWebsite.setEnabled(false);
		}
		layout.addComponent(close, "top:450.0px;left:10.0px;");

		setContent(layout);
		passwordArea.setValue(password);
		loginfield.setValue(login);
		commentArea.setValue(comment);
		websiteField.setValue(website);
		setWidth("450px");
		setHeight("600px");
	}

	public PasswordDetailsDialog() {
		initWindow("", "", "", "", true);
	}

	public Button getClose() {
		return close;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public TextArea getPasswordArea() {
		return passwordArea;
	}

	public TextField getLoginfield() {
		return loginfield;
	}

	public Button getSaveButton() {
		return saveButton;
		
	}

	public PasswordInfos getPasswordInfos() {
		PasswordInfos infos = new PasswordInfos();
		infos.setComment(commentArea.getValue());
		infos.setCreationdate(new Date());
		infos.setId(BaseController.generaterandomId());
		infos.setLabel(labelField.getValue());
		infos.setLogin(loginfield.getValue());
		infos.setPassword(passwordArea.getValue());
		infos.setWebsite(websiteField.getValue());
		return infos;
	}

}
