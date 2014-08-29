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
	private final AbsoluteLayout layout = new AbsoluteLayout();
	
	private Button saveButton;

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
		saveButton = new Button();
		saveButton.setCaption("save");
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

		layout.addComponent(labelField, "top:15.0px;left:10.0px;");
		layout.addComponent(loginfield, "top:60.0px;left:10.0px;");
		layout.addComponent(websiteField, "top:120.0px;left:10.0px;");
		layout.addComponent(openWebsite, "top:120.0px;left:280.0px;");
		layout.addComponent(commentArea, "top:190.0px;left:10.0px;");
		layout.addComponent(passwordArea, "top:290.0px;left:10.0px;");
		if(isNewItemLayout){
			layout.addComponent(saveButton, "top:410.0px;left:10.0px;");
		}
		layout.addComponent(close, "top:410.0px;left:80.0px;");

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
		infos.setLogin(login);
		infos.setPassword(password);
		infos.setWebsite(websiteField.getValue());
		return infos;
	}

}
