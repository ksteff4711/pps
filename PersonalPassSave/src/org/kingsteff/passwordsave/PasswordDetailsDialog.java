package org.kingsteff.passwordsave;

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

	private final Button close;

	private final Button openWebsite;

	private final TextArea passwordArea;
	private final TextField websiteField;
	private final TextArea commentArea;
	private final TextField loginfield;
	private final AbsoluteLayout layout = new AbsoluteLayout();

	public PasswordDetailsDialog(String password, String login,
			final String website, String comment) {
		super();
		this.password = password;
		this.login = login;
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
							GeneralNotification.ERROR_MESSAGE).show();
				}
			}

		});
		openWebsite.setIcon(new ThemeResource("../runo/icons/16/globe.png"));
		openWebsite.addStyleName(Runo.BUTTON_SMALL);
		close = new Button();
		close.setCaption("close");
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

		layout.addComponent(loginfield, "top:15.0px;left:10.0px;");
		layout.addComponent(websiteField, "top:60.0px;left:10.0px;");
		layout.addComponent(openWebsite, "top:60.0px;left:280.0px;");
		layout.addComponent(commentArea, "top:120.0px;left:10.0px;");
		layout.addComponent(passwordArea, "top:190.0px;left:10.0px;");
		layout.addComponent(close, "top:310.0px;left:10.0px;");

		setContent(layout);
		passwordArea.setValue(password);
		loginfield.setValue(login);
		commentArea.setValue(comment);
		websiteField.setValue(website);
		setWidth("450px");
		setHeight("400px");

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

}
