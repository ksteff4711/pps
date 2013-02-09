package org.kingsteff.passwordsave;

import java.util.ArrayList;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.Runo;

public class FilesArchiveTab extends AbsoluteLayout implements
		PpsDialogResultListener, ValueChangeListener {

	private Table filesTable;

	private Button addButton;

	private Button removeButton;

	private Button openFileButton;

	private Button editInfosButton;

	private PpsDialogResultListener self;

	private Window newFilesWindow;
	private Window editingFilesWindow;

	private Object currentChoosenID;

	private FileArchiveDialog fileArchiveDialog;

	private FileArchiveEditingDialog archiveEditingDialog;

	public FilesArchiveTab() {
		setHeight("1000px");
		setWidth("1000px");
		initTableAndButtons();
		placeGuiItems();
		self = this;
	}

	private void initTableAndButtons() {

		filesTable = new Table();

		addButton = new Button();
		removeButton = new Button();
		openFileButton = new Button();
		editInfosButton = new Button();

		addButton.addListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				addItem();
			}

		});

		editInfosButton.addListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				filesTable.setEditable(!filesTable.isEditable());
				if (editInfosButton.getCaption().equals("Save")) {
					saveChanges();
				}
				editInfosButton.setCaption((filesTable.isEditable() ? "Save"
						: "Edit"));
				if (filesTable.isEditable()) {
					editInfosButton.setIcon(new ThemeResource(
							"../pps/images/save.png"));
				} else {
					editInfosButton.setIcon(new ThemeResource(
							"../pps/images/edit.png"));
				}
			}
		});

		removeButton.addListener(new Button.ClickListener() {

			public void buttonClick(ClickEvent event) {
				if (currentChoosenID != null) {
					Item item = filesTable.getItem(currentChoosenID);
					Property itemName = item.getItemProperty("FileName");
					Property itemPath = item.getItemProperty("Filepath");
					PersonalpasssaveApplication
							.getInstance()
							.getBaseController()
							.openYesNoDialog(
									"Really delete this file:"
											+ itemName.getValue()
											+ " ??? (CAN NOT BE UNDONE!!!)",
									"Warning", self);
				}
			}
		});

		openFileButton.addListener(new Button.ClickListener() {
			private Object currentChoosenID;

			public void buttonClick(ClickEvent event) {
				openFile();
			}
		});

		filesTable.addContainerProperty("Description", String.class, null);
		filesTable.addContainerProperty("Foldername", String.class, null);
		filesTable.addContainerProperty("FileName", String.class, null);
		filesTable.addContainerProperty("Filepath", String.class, null);
		filesTable.addContainerProperty("FileSize", String.class, null);
		filesTable.addContainerProperty("Object", FileInStore.class, null);
		filesTable.setSelectable(true);
		filesTable.setImmediate(true);
		filesTable.setWidth("-1px");
		filesTable.setHeight("600px");
		filesTable.setEditable(false);
		filesTable.setColumnCollapsingAllowed(true);
		filesTable.setColumnCollapsed("Object", true);
		loadDataForCurrentUser();

		filesTable.setColumnCollapsingAllowed(true);

		ItemClickEvent.ItemClickListener doubleClickListener = (new ItemClickEvent.ItemClickListener() {
			private static final long serialVersionUID = 2068314108919135281L;

			public void itemClick(ItemClickEvent event) {
				if (event.isDoubleClick()) {
					editingAction();

				}
			}

		});

		filesTable.addListener(doubleClickListener);
		filesTable.addListener(this);

	}

	private void openNewEditingDialog(FileInStore currentFile) {
		archiveEditingDialog = new FileArchiveEditingDialog();
		editingFilesWindow = new Window("Edit files Metadata");
		editingFilesWindow.addComponent(archiveEditingDialog);
		editingFilesWindow.setWidth("500px");
		editingFilesWindow.setHeight("450px");
		editingFilesWindow.setModal(true);
		archiveEditingDialog.getParentFolder().setValue(
				currentFile.getFoldername());
		archiveEditingDialog.getFolder().setValue(
				currentFile.getParentFoldername());
		archiveEditingDialog.getDescriptionInput().setValue(
				currentFile.getDescription());
		archiveEditingDialog.getSave().addListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				saveEditedItem(
						archiveEditingDialog.getFolder().getValue() + "",
						archiveEditingDialog.getParentFolder().getValue() + "",
						archiveEditingDialog.getDescriptionInput().getValue()
								+ "");
				PersonalpasssaveApplication.getInstance().getBaseController()
						.removeWindow(editingFilesWindow);
				loadDataForCurrentUser();
			}

		});
		archiveEditingDialog.getCancel().addListener(
				new Button.ClickListener() {
					public void buttonClick(ClickEvent event) {
						PersonalpasssaveApplication.getInstance()
								.getBaseController()
								.removeWindow(editingFilesWindow);
					}

				});
		PersonalpasssaveApplication.getInstance().getBaseController()
				.addWindow(editingFilesWindow);

	}

	private void saveEditedItem(String foldername, String parentFoldername,
			String description) {
		FileArchiveController archiveController = PersonalpasssaveApplication
				.getInstance().getFileArchiveController();

		if (currentChoosenID != null) {
			FileInStore currentFile = null;
			if (filesTable.getContainerProperty(currentChoosenID, "Object") != null) {
				currentFile = (FileInStore) filesTable.getContainerProperty(
						currentChoosenID, "Object").getValue();
			}
			if (currentFile != null) {
				currentFile.setParentFoldername(parentFoldername);
				currentFile.setFoldername(foldername);
				currentFile.setDescription(description);
				archiveController.saveMetaDataForFile(currentFile);
			} else {
				System.out.println("current file is null");
			}
		}
	}

	private void editingAction() {
		FileInStore currentFile = null;
		if (currentChoosenID != null) {

			if (filesTable.getContainerProperty(currentChoosenID, "Object") != null) {
				currentFile = (FileInStore) filesTable.getContainerProperty(
						currentChoosenID, "Object").getValue();
			}

			openNewEditingDialog(currentFile);

		}
	}

	private void saveChanges() {

	}

	protected void openFile() {

		Object objectid = filesTable.getValue();
		if (objectid != null) {
			Item item = filesTable.getItem(objectid);
			Property itemName = item.getItemProperty("FileName");
			Property itemPath = item.getItemProperty("Filepath");
			FileArchiveController archiveController = PersonalpasssaveApplication
					.getInstance().getFileArchiveController();
			archiveController.openFileFromArchive(itemPath.toString(),
					itemName.toString());
		} else {
			PersonalpasssaveApplication
					.getInstance()
					.getWindow()
					.showNotification("No FIle selected",
							Notification.TYPE_ERROR_MESSAGE);
		}

	}

	private void placeGuiItems() {
		// passtable

		filesTable.setCaption("Your current files in archive");
		filesTable.setImmediate(true);
		addComponent(filesTable, "top:180.0px;left:20.0px;");

		// addButton

		addButton.setCaption("Add");
		addButton.setImmediate(false);
		addButton.setWidth("-1px");
		addButton.setHeight("-1px");
		addButton
				.setIcon(new ThemeResource("../runo/icons/16/document-add.png"));
		addButton.addStyleName(Runo.BUTTON_SMALL);
		addComponent(addButton, "top:40.0px;left:20.0px;");

		// removeButton

		removeButton.setCaption("Remove");
		removeButton.setImmediate(false);
		removeButton.setWidth("-1px");
		removeButton.setHeight("-1px");
		removeButton.setIcon(new ThemeResource(
				"../runo/icons/16/document-delete.png"));
		removeButton.addStyleName(Runo.BUTTON_SMALL);
		addComponent(removeButton, "top:40.0px;left:90.0px;");

		openFileButton.setCaption("Open File");
		openFileButton.setImmediate(false);
		openFileButton.setWidth("-1px");
		openFileButton.setHeight("-1px");
		openFileButton.setIcon(new ThemeResource("../pps/images/open.png"));
		openFileButton.addStyleName(Runo.BUTTON_SMALL);
		addComponent(openFileButton, "top:40.0px;left:190.0px;");
		// editButton

	}

	public void closeFileUploadDialog() {
		PersonalpasssaveApplication.getInstance().getBaseController()
				.removeWindow(newFilesWindow);
		loadDataForCurrentUser();
	}

	protected void addItem() {
		fileArchiveDialog = new FileArchiveDialog();
		newFilesWindow = new Window("Put file to archive");
		newFilesWindow.addComponent(fileArchiveDialog);
		newFilesWindow.setWidth("500px");
		newFilesWindow.setHeight("450px");
		newFilesWindow.setModal(true);

		PersonalpasssaveApplication.getInstance().getBaseController()
				.addWindow(newFilesWindow);
	}

	protected void showDownloadOption() {
		// TODO Auto-generated method stub

	}

	public FileArchiveDialog getFileArchiveDialog() {
		return fileArchiveDialog;
	}

	private void loadDataForCurrentUser() {
		filesTable.removeAllItems();
		FileArchiveController control = PersonalpasssaveApplication
				.getInstance().getFileArchiveController();
		ArrayList<FileInStore> allFilesForUser = control.getAllFilesForUser();
		int counter = 0;

		for (FileInStore fileInStore : allFilesForUser) {
			filesTable.addItem(new Object[] { fileInStore.getDescription(),
					fileInStore.getFoldername(), fileInStore.getFileName(),
					fileInStore.getFilePath(), fileInStore.getFileSize(),
					fileInStore, }, new Integer(counter));
			counter++;
		}

	}

	@Override
	public void valueChange(ValueChangeEvent event) {
		System.out.println("-->>" + filesTable.getValue());
		Object objectid = filesTable.getValue();
		if (objectid != null) {
			currentChoosenID = objectid;
		}

	}

	@Override
	public void yesNoResultReturned(int status) {
		if (status == BaseController.YESNO_DIALOG_NO) {

		} else {
			FileArchiveController control = PersonalpasssaveApplication
					.getInstance().getFileArchiveController();
			Property itemName = null;
			Property itemPath = null;
			Object objectid = filesTable.getValue();
			if (objectid != null) {
				Item item = filesTable.getItem(objectid);
				itemName = item.getItemProperty("FileName");
				itemPath = item.getItemProperty("Filepath");
				FileArchiveController archiveController = PersonalpasssaveApplication
						.getInstance().getFileArchiveController();
				archiveController.removeFileFromArchive(itemPath.toString(),
						itemName.toString());

			} else {
				PersonalpasssaveApplication
						.getInstance()
						.getWindow()
						.showNotification("No FIle selected",
								Notification.TYPE_ERROR_MESSAGE);

			}
			try {
				control.removeFileFromArchive(itemPath.getValue().toString(),
						itemName.getValue().toString());
			} catch (Exception e) {
				PersonalpasssaveApplication
						.getInstance()
						.getWindow()
						.showNotification("Path or Filename is invalid",
								Notification.TYPE_ERROR_MESSAGE);

			}
		}
	}

}
