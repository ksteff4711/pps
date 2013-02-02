package org.kingsteff.passwordsave;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Runo;

public class PasswordTab extends AbsoluteLayout implements
		PpsDialogResultListener, ValueChangeListener {

	@AutoGenerated
	private Button editButton;
	@AutoGenerated
	private Button removeButton;
	@AutoGenerated
	private Button addButton;
	@AutoGenerated
	private Table passtable;
	private UserAndPasswords allPasswordsForUser;
	private PasswordManager manager;

	private Button logout;

	private Button userManagement;

	private PasswordTab self = null;
	private Object currentChoosenID;

	private Button generateRandomPassword = null;
	private TextField randomPasswordTextfield = null;
	private TextField randomPasswordLenghtTextfield = null;

	private HorizontalLayout activateGenerator = null;
	private Label activateGeneratorLabel;
	private Button changePassword;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	/**
	 * The constructor should first build the main layout, set the composition
	 * root and then do any custom initialization.
	 * 
	 * The constructor will not be automatically regenerated by the visual
	 * editor.
	 */
	public PasswordTab() {
		buildMainLayout();

		initTableData();
		self = this;
		// TODO add user code here
	}

	private void initTableData() {
		editButton.addListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				passtable.setEditable(!passtable.isEditable());
				if (editButton.getCaption().equals("Save")) {
					saveChanges();
				}
				editButton.setCaption((passtable.isEditable() ? "Save" : "Edit"));
				if (passtable.isEditable()) {
					editButton.setIcon(new ThemeResource(
							"../pps/images/save.png"));
				} else {
					editButton.setIcon(new ThemeResource(
							"../pps/images/edit.png"));
				}
			}
		});

		addButton.addListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				addItem();
			}

		});

		userManagement.addListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				showUserManagent();
			}
		});

		logout.addListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				PersonalpasssaveApplication.getInstance().close();
			}
		});

		removeButton.addListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				if (currentChoosenID != null) {
					Item item = passtable.getItem(currentChoosenID);
					Property itemName = item.getItemProperty("Label");
					PersonalpasssaveApplication
							.getInstance()
							.getBaseController()
							.openYesNoDialog(
									"Really delete password:"
											+ itemName.getValue() + "???",
									"Warning", self);
				}
			}
		});
		generateRandomPassword.addListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {

				try {
					String string = randomPasswordLenghtTextfield.getValue()
							.toString();
					randomPasswordTextfield.setValue(manager
							.generateRandomPassword(Integer.parseInt(string),
									false));
				} catch (Exception e) {
					PersonalpasssaveApplication
							.getInstance()
							.getWindow()
							.showNotification(
									"Invalid character in length field");
				}
			}
		});

		activateGenerator.addListener(new LayoutClickListener() {

			public void layoutClick(LayoutClickEvent event) {

				if (!generateRandomPassword.isVisible()) {
					removeComponent(activateGenerator);
					activateGeneratorLabel.setIcon(new ThemeResource(
							"../runo/icons/16/cancel.png"));
					activateGeneratorLabel.setValue("close");

					generateRandomPassword.setVisible(true);
					randomPasswordTextfield.setVisible(true);
					randomPasswordLenghtTextfield.setVisible(true);
					addComponent(activateGenerator, "top:80.0px;left:20.0px;");
				} else {
					removeComponent(activateGenerator);
					activateGeneratorLabel.setIcon(new ThemeResource(
							"../pps/images/generator.png"));
					activateGeneratorLabel.setValue("Generator");
					generateRandomPassword.setVisible(false);
					randomPasswordTextfield.setVisible(false);
					randomPasswordLenghtTextfield.setVisible(false);
					addComponent(activateGenerator, "top:80.0px;left:20.0px;");
				}

			}
		});

		changePassword.addListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				changePasswordAction();
			}
		});

		passtable.addContainerProperty("ID", String.class, "");
		passtable.addContainerProperty("Label", String.class, "");
		passtable.addContainerProperty("Login", String.class, "");
		passtable.addContainerProperty("Password", String.class, "");
		passtable.addContainerProperty("Creation Date", Date.class, "");
		passtable.addContainerProperty("Comment", String.class, "");
		passtable.addContainerProperty("Website", String.class, "");
		passtable.setSelectable(true);
		passtable.setImmediate(true);
		passtable.setEditable(false);

		loadDataForCurrentUser();

		passtable.setColumnCollapsingAllowed(true);
		passtable.setColumnCollapsed("ID", true);

		passtable.setColumnWidth("Comment", 200);
		passtable.setColumnWidth("Label", 200);
		passtable.setColumnWidth("Password", 200);
		passtable.setColumnWidth("Website", 200);

		ItemClickEvent.ItemClickListener doubleClickListener = (new ItemClickEvent.ItemClickListener() {
			private static final long serialVersionUID = 2068314108919135281L;

			public void itemClick(ItemClickEvent event) {
				if (event.isDoubleClick()) {
					showDetails();
				}
			}
		});

		passtable.addListener(doubleClickListener);
		passtable.addListener(this);

	}

	private void removeEntry() {
		Object objectid = passtable.getValue();

		Item item = passtable.getItem(objectid);
		Property itemPropertyID = item.getItemProperty("ID");
		manager.removePassword(itemPropertyID.getValue().toString(),
				PersonalpasssaveApplication.getInstance().getBaseController()
						.getCurrentUser());
		loadDataForCurrentUser();
	}

	private void showUserManagent() {
		PersonalpasssaveApplication.getInstance().getBaseController()
				.getWindow().showUserManagement();
	}

	private void saveChanges() {
		Collection itemIds = passtable.getItemIds();
		ArrayList beans = new ArrayList();
		for (Object currentItem : itemIds) {
			Item item = passtable.getItem(currentItem);
			Property itemPropertyID = item.getItemProperty("ID");
			Property itemPropertyLabel = item.getItemProperty("Label");
			Property itemPropertyLogin = item.getItemProperty("Login");
			Property itemPropertyPassword = item.getItemProperty("Password");
			Property itemPropertyCreationDate = item
					.getItemProperty("Creation Date");
			Property itemPropertyComment = item.getItemProperty("Comment");
			Property itemPropertyWebsite = item.getItemProperty("Website");
			PasswordInfos newInfos = new PasswordInfos();
			if (itemPropertyWebsite.getValue() == null) {
				newInfos.setWebsite("");
			} else {
				newInfos.setWebsite(itemPropertyWebsite.getValue().toString());
			}

			newInfos.setComment(itemPropertyComment.getValue().toString());
			newInfos.setLabel(itemPropertyLabel.getValue().toString());
			newInfos.setPassword(itemPropertyPassword.getValue().toString());
			newInfos.setLogin(itemPropertyLogin.getValue().toString());
			newInfos.setCreationdate((Date) itemPropertyCreationDate.getValue());
			newInfos.setId(itemPropertyID.getValue().toString());

			allPasswordsForUser.passwords.put(itemPropertyID.toString(),
					newInfos);
			System.out.println("for -->" + itemPropertyID.toString());

		}

		System.out.println(allPasswordsForUser);
		manager.addAllPasswords(PersonalpasssaveApplication.getInstance()
				.getBaseController().getCurrentUser(), allPasswordsForUser);
		loadDataForCurrentUser();

	}

	private void loadDataForCurrentUser() {
		passtable.removeAllItems();
		manager = new PasswordManager();
		try {
			allPasswordsForUser = manager
					.getAllPasswordsForUser(PersonalpasssaveApplication
							.getInstance().getBaseController().getCurrentUser());
			System.out.println("loading");
			System.out.println(allPasswordsForUser);
			int counter = 0;

			ArrayList<PasswordInfos> valueSet = new ArrayList<PasswordInfos>(
					allPasswordsForUser.passwords.values());
			Collections.sort(valueSet);
			for (PasswordInfos current : valueSet) {
				passtable.addItem(new Object[] {

				current.getId(), current.getLabel(), current.getLogin(),
						current.getPassword(), current.getCreationdate(),
						current.getComment(), current.getWebsite(), },
						new Integer(counter));
				counter++;
			}
			passtable.setCaption("Your current passwordlist has:"
					+ passtable.getItemIds().size() + " items");
		} catch (Exception e) {

			PasswordInfos incoming = new PasswordInfos();
			incoming.setLabel("DELETABLE DUMMY");
			incoming.setComment("DELETABLE DUMMY");
			incoming.setPassword("DELETABLE DUMMY");
			incoming.setId(BaseController.generaterandomId());
			manager.addPassword(incoming, PersonalpasssaveApplication
					.getInstance().getBaseController().getCurrentUser());

			try {
				allPasswordsForUser = manager
						.getAllPasswordsForUser(PersonalpasssaveApplication
								.getInstance().getBaseController()
								.getCurrentUser());

				int counter = 0;

				for (Object current : allPasswordsForUser.passwords.keySet()) {
					passtable
							.addItem(
									new Object[] {
											// current.getId(),
											((PasswordInfos) allPasswordsForUser.passwords
													.get(current)).getId(),
											((PasswordInfos) allPasswordsForUser.passwords
													.get(current)).getLabel(),
											((PasswordInfos) allPasswordsForUser.passwords
													.get(current)).getLogin(),
											((PasswordInfos) allPasswordsForUser.passwords
													.get(current))
													.getPassword(),
											((PasswordInfos) allPasswordsForUser.passwords
													.get(current)).getComment(),
											((PasswordInfos) allPasswordsForUser.passwords
													.get(current)).getWebsite(), },
									new Integer(counter));
					counter++;

				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	@AutoGenerated
	private void buildMainLayout() {
		// common part: create layout

		setImmediate(true);
		setWidth("100%");
		setHeight("1500px");
		setMargin(false);

		// passtable
		passtable = new Table();
		passtable.setCaption("Your current passwordlist");
		passtable.setImmediate(true);
		passtable.setHeight("-1px");
		passtable.setWidth("-1px");
		addComponent(passtable, "top:210.0px;left:20.0px; bottom:80px;");

		// addButton
		addButton = new Button();
		addButton.setCaption("Add");
		addButton.setImmediate(false);
		addButton.setWidth("-1px");
		addButton.setHeight("-1px");
		addButton
				.setIcon(new ThemeResource("../runo/icons/16/document-add.png"));
		addButton.addStyleName(Runo.BUTTON_SMALL);
		addComponent(addButton, "top:40.0px;left:20.0px;");

		// removeButton
		removeButton = new Button();
		removeButton.setCaption("Remove");
		removeButton.setImmediate(false);
		removeButton.setWidth("-1px");
		removeButton.setHeight("-1px");
		removeButton.setIcon(new ThemeResource(
				"../runo/icons/16/document-delete.png"));
		removeButton.addStyleName(Runo.BUTTON_SMALL);
		addComponent(removeButton, "top:40.0px;left:90.0px;");

		// editButton
		editButton = new Button();
		editButton.setCaption("Edit");
		editButton.setImmediate(false);
		editButton.setWidth("-1px");
		editButton.setHeight("-1px");
		editButton.setIcon(new ThemeResource("../pps/images/edit.png"));
		editButton.addStyleName(Runo.BUTTON_SMALL);
		addComponent(editButton, "top:40.0px;left:190.0px;");

		// editButton
		userManagement = new Button();
		userManagement.setCaption("UserManagement");
		userManagement.setImmediate(false);
		userManagement.setWidth("-1px");
		userManagement.setHeight("-1px");
		userManagement.setIcon(new ThemeResource("../runo/icons/16/users.png"));
		if (PersonalpasssaveApplication.getInstance().getBaseController()
				.getCurrentUser().equals("admin")) {
			addComponent(userManagement, "top:40.0px;left:380.0px;");
		}
		userManagement.addStyleName(Runo.BUTTON_SMALL);

		logout = new Button();
		logout.setCaption("logout");
		logout.setImmediate(false);
		logout.setWidth("-1px");
		logout.setHeight("-1px");
		logout.setIcon(new ThemeResource("../runo/icons/16/cancel.png"));
		addComponent(logout, "top:40.0px;left:900.0px;");
		logout.addStyleName(Runo.BUTTON_SMALL);

		generateRandomPassword = new Button();
		generateRandomPassword.setCaption("generate");
		generateRandomPassword.setImmediate(true);
		generateRandomPassword.setWidth("-1px");
		generateRandomPassword.setHeight("-1px");
		addComponent(generateRandomPassword, "top:130.0px;left:220.0px;");
		generateRandomPassword.setVisible(false);
		generateRandomPassword.addStyleName(Runo.BUTTON_SMALL);

		randomPasswordTextfield = new TextField();
		randomPasswordTextfield.setCaption("Random Password Generator");
		randomPasswordTextfield.setWidth("200px");
		randomPasswordTextfield.setImmediate(true);
		addComponent(randomPasswordTextfield, "top:130.0px;left:20.0px;");
		randomPasswordTextfield.setVisible(false);

		randomPasswordLenghtTextfield = new TextField();
		randomPasswordLenghtTextfield.setCaption("Password Length");
		randomPasswordLenghtTextfield.setValue("12");
		randomPasswordLenghtTextfield.setWidth("35px");
		randomPasswordLenghtTextfield.setImmediate(true);
		addComponent(randomPasswordLenghtTextfield, "top:130.0px;left:320.0px;");
		randomPasswordLenghtTextfield.setVisible(false);

		activateGenerator = new HorizontalLayout();
		activateGeneratorLabel = new Label();
		activateGeneratorLabel.setIcon(new ThemeResource(
				"../pps/images/generator.png"));
		activateGeneratorLabel.setValue("Generator");
		activateGenerator.addComponent(activateGeneratorLabel);
		addComponent(activateGenerator, "top:80.0px;left:20.0px;");

		changePassword = new Button();
		changePassword.setCaption("my password");
		changePassword.setImmediate(true);
		changePassword.setWidth("-1px");
		changePassword.setHeight("-1px");
		changePassword.addStyleName(Runo.BUTTON_SMALL);
		changePassword.setIcon(new ThemeResource("../runo/icons/16/lock.png"));
		addComponent(changePassword, "top:40.0px;left:260.0px;");
		changePassword.setVisible(true);

	}

	private void addItem() {
		Object addItem = passtable.addItem();

		editButton.setCaption("Save");
		editButton.setIcon(new ThemeResource("../pps/images/save.png"));
		Item item = passtable.getItem(addItem);
		Property itemPropertyCreationDate = item
				.getItemProperty("Creation Date");
		itemPropertyCreationDate.setValue(new Date());
		Property itemPropertyLabel = item.getItemProperty("Label");
		itemPropertyLabel.setValue("");
		Property itemPropertyLogin = item.getItemProperty("Login");
		itemPropertyLogin.setValue("");
		Property itemPropertyPassword = item.getItemProperty("Password");
		itemPropertyPassword.setValue("");
		Property itemPropertyComment = item.getItemProperty("Comment");
		itemPropertyComment.setValue("");
		Property itemPropertyID = item.getItemProperty("ID");
		itemPropertyID.setValue(BaseController.generaterandomId());
		Property itemPropertyWebsite = item.getItemProperty("Website");
		itemPropertyWebsite.setValue("");
		editTable(addItem);
		passtable.setCurrentPageFirstItemId(addItem);
	}

	private void editTable(final Object newItemId) {
		passtable.setEditable(true);
	}

	@Override
	public void yesNoResultReturned(int status) {
		if (status == BaseController.YESNO_DIALOG_NO) {

		} else {
			removeEntry();
		}

	}

	private void showDetails() {

		Item item = passtable.getItem(currentChoosenID);
		if (item != null) {
			Property itemPropertypassword = item.getItemProperty("Password");
			Property itemPropertylogin = item.getItemProperty("Login");
			Property itemPropertywebsite = item.getItemProperty("Website");
			Property itemPropertycomment = item.getItemProperty("Comment");
			final DetailsDialog detailsDialog = new DetailsDialog(
					itemPropertypassword.getValue().toString(),
					itemPropertylogin.getValue().toString(),
					itemPropertywebsite.getValue() != null ? itemPropertywebsite
							.getValue().toString() : "",
					itemPropertycomment != null ? itemPropertycomment
							.getValue().toString() : "");
			detailsDialog.setModal(true);
			detailsDialog.setCaption("Details");
			PersonalpasssaveApplication.getInstance().getBaseController()
					.addWindow(detailsDialog);
			detailsDialog.getClose().addListener(new Button.ClickListener() {
				public void buttonClick(ClickEvent event) {
					PersonalpasssaveApplication.getInstance()
							.getBaseController().removeWindow(detailsDialog);
				}
			});
		}

	}

	public void valueChange(ValueChangeEvent event) {
		Object objectid = passtable.getValue();
		if (objectid != null) {
			currentChoosenID = objectid;
		}

	}

	private void changePasswordAction() {
		final ChangePasswordDialog changePasswordDialog = new ChangePasswordDialog();
		final Window changePAsswordWindow = new Window("Passwordchange");
		changePAsswordWindow.addComponent(changePasswordDialog);
		changePAsswordWindow.setWidth("300px");
		changePAsswordWindow.setHeight("250px");
		changePAsswordWindow.setModal(true);
		changePasswordDialog.getSave().addListener(new ClickListener() {

			public void buttonClick(ClickEvent event) {
				try {

					String pass1 = changePasswordDialog.getTextfieldPassword()
							.getValue().toString();
					String pass2 = changePasswordDialog
							.getTextFieldverifyPassword().getValue().toString();
					if (pass1.equals(pass2)) {
						PersonalpasssaveApplication
								.getInstance()
								.getLoginManager()
								.setUsersPassword(
										PersonalpasssaveApplication
												.getInstance()
												.getBaseController()
												.getCurrentUser(),
										changePasswordDialog
												.getTextfieldPassword()
												.getValue().toString());

						PersonalpasssaveApplication.getInstance()
								.getBaseController()
								.removeWindow(changePAsswordWindow);
					} else {
						PersonalpasssaveApplication.getInstance().getWindow()
								.showNotification("Passwords not consistent!");
					}

				} catch (Exception e) {
					PersonalpasssaveApplication.getInstance().getWindow()
							.showNotification("Creation failed");
					e.printStackTrace();
				}
			}
		});
		changePasswordDialog.getCancel().addListener(new ClickListener() {

			public void buttonClick(ClickEvent event) {
				PersonalpasssaveApplication.getInstance().getBaseController()
						.removeWindow(changePAsswordWindow);
			}
		});
		PersonalpasssaveApplication.getInstance().getBaseController()
				.addWindow(changePAsswordWindow);
	}

}
