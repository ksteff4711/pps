package org.kingsteff.passwordsave;

import java.util.Vector;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Window;

public class UserListView extends CustomComponent {

	@AutoGenerated
	private AbsoluteLayout mainLayout;
	@AutoGenerated
	private Button removeButton;
	@AutoGenerated
	private Button addButton;
	@AutoGenerated
	private ListSelect userList;

	private Button closeButton;

	private Button resetPassword;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	/**
	 * The constructor should first build the main layout, set the composition
	 * root and then do any custom initialization.
	 * 
	 * The constructor will not be automatically regenerated by the visual
	 * editor.
	 */
	public UserListView() {
		buildMainLayout();
		setCompositionRoot(mainLayout);
		initUserList();
		// TODO add user code here

		removeButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				removeUser();
			}
		});

		addButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				addUser();
			}
		});

		closeButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				PersonalpasssaveApplication.getInstance().getBaseController()
						.getWindow().showMainView();
			}
		});

		resetPassword.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				resetPasswordForUser();
			}

		});

	}

	private void resetPasswordForUser() {
		Object value = userList.getValue();
		if (value != null) {
			PersonalpasssaveApplication.getInstance().getLoginManager()
					.resetUsersPassword(value.toString());

			new GeneralNotification("Password has been set to : TEST12342012",
					true, GeneralNotification.ERROR_MESSAGE).show();
		}

	}

	protected void removeUser() {
		Object value = userList.getValue();
		if (value != null) {
			if (!value.equals("admin")) {
				PersonalpasssaveApplication.getInstance().getLoginManager()
						.removeUser(value.toString());
			} else {

				new GeneralNotification("admin can not be deleted!!", true,
						GeneralNotification.ERROR_MESSAGE).show();
			}
		}
		reloadUserTable();
	}

	protected void addUser() {
		newUserAction();

	}

	private void initUserList() {
		Vector<String> allUserNames = PersonalpasssaveApplication.getInstance()
				.getLoginManager().getAllUserNames();
		for (String string : allUserNames) {
			userList.addItem(string);
		}
	}

	private void newUserAction() {
		final CreateNewUserDialog createNewUserDialog = new CreateNewUserDialog();
		final Window newUserWindow = new Window("Create User");
		newUserWindow.setContent(createNewUserDialog);
		newUserWindow.setWidth("500px");
		newUserWindow.setHeight("400px");
		newUserWindow.setModal(true);
		createNewUserDialog.getSave().addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					if (!createNewUserDialog.getTextfieldUsername().getValue()
							.toString().equalsIgnoreCase("admin")) {
						String pass1 = createNewUserDialog
								.getTextfieldPassword().getValue().toString();
						String pass2 = createNewUserDialog
								.getTextFieldverifyPassword().getValue()
								.toString();
						if (pass1.equals(pass2)) {
							PersonalpasssaveApplication
									.getInstance()
									.getLoginManager()
									.addUser(
											createNewUserDialog
													.getTextfieldUsername()
													.getValue().toString(),
											createNewUserDialog
													.getTextfieldPassword()
													.getValue().toString());
							reloadUserTable();
							PersonalpasssaveApplication.getInstance()
									.getBaseController()
									.removeWindow(newUserWindow);
						} else {

							new GeneralNotification(
									"Passwords not consistent!", true,
									GeneralNotification.ERROR_MESSAGE).show();

						}
					}
				} catch (Exception e) {

					new GeneralNotification("Creation failed", true,
							GeneralNotification.ERROR_MESSAGE).show();
					e.printStackTrace();
				}
			}
		});
		createNewUserDialog.getCancel().addListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				PersonalpasssaveApplication.getInstance().getBaseController()
						.removeWindow(newUserWindow);
			}
		});
		PersonalpasssaveApplication.getInstance().getBaseController()
				.addWindow(newUserWindow);
	}

	protected void reloadUserTable() {
		userList.removeAllItems();
		Vector<String> allUserNames = PersonalpasssaveApplication.getInstance()
				.getLoginManager().getAllUserNames();
		for (String string : allUserNames) {
			userList.addItem(string);
		}

	}

	@AutoGenerated
	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");

		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");

		// userList
		userList = new ListSelect();
		userList.setCaption("Userlist");
		userList.setImmediate(false);
		userList.setWidth("200px");
		userList.setHeight("300px");
		mainLayout.addComponent(userList, "top:137.0px;left:20.0px;");

		// addButton
		addButton = new Button();
		addButton.setCaption("add");
		addButton.setImmediate(false);
		addButton.setWidth("-1px");
		addButton.setHeight("-1px");
		mainLayout.addComponent(addButton, "top:60.0px;left:20.0px;");

		// removeButton
		removeButton = new Button();
		removeButton.setCaption("remove");
		removeButton.setImmediate(false);
		removeButton.setWidth("-1px");
		removeButton.setHeight("-1px");
		mainLayout.addComponent(removeButton, "top:60.0px;left:70.0px;");

		closeButton = new Button();
		closeButton.setCaption("close");
		closeButton.setImmediate(false);
		closeButton.setWidth("-1px");
		closeButton.setHeight("-1px");
		mainLayout.addComponent(closeButton, "top:60.0px;left:280.0px;");

		resetPassword = new Button();
		resetPassword.setCaption("reset password");
		resetPassword.setImmediate(false);
		resetPassword.setWidth("-1px");
		resetPassword.setHeight("-1px");
		mainLayout.addComponent(resetPassword, "top:60.0px;left:160.0px;");

		return mainLayout;
	}

}
