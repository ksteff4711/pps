package org.kingsteff.passwordsave;

import java.util.Collection;
import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Table;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.Runo;

public class FilesArchiveTab extends AbsoluteLayout implements
		PpsDialogResultListener, ValueChangeListener {

	private static final Object TREE_PROPERTY_CAPTION = "TREECAPTION";

	private static final Object TREE_PROPERTY_FOLDERNAME = "FOLDERNAME";

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

	private Tree folderTree;

	private Object rootItemIdFolderTable;

	private Button addFolder;

	public FilesArchiveTab() {
		setHeight("1000px");
		setWidth("1000px");
		initTableAndButtons();
		placeGuiItems();
		self = this;
	}

	private void initTableAndButtons() {

		filesTable = new Table();
		folderTree = new Tree();
		try {
			initFolderTree();
			fillFolderTree();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		addButton = new Button();
		removeButton = new Button();
		openFileButton = new Button();
		editInfosButton = new Button();
		addFolder = new Button();

		addFolder.addListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				try {
					openAddFolderDialog();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});

		addButton.addListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				try {
					addItem();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
		filesTable.addContainerProperty("FileSize", String.class, null);
		filesTable.addContainerProperty("Object", FileInStore.class, null);
		filesTable.setSelectable(true);
		filesTable.setImmediate(true);
		filesTable.setWidth("500px");
		filesTable.setHeight("500px");
		filesTable.setEditable(false);
		filesTable.setColumnCollapsingAllowed(true);
		filesTable.setColumnCollapsed("Object", true);
		showFilesForChoosenFolder(PersonalPassConstants.FILE_ROOT_NAME);

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

	private void openAddFolderDialog() throws Exception {
		FileArchiveController archiveController = new FileArchiveController();
		FilesAddFolderDialog addFolderDialog = new FilesAddFolderDialog(
				archiveController.getAllRootFolderNames());
		final Window addFolderWindow = new Window("Add new Folder");
		addFolderWindow.addComponent(addFolderDialog);
		addFolderWindow.setWidth("500px");
		addFolderWindow.setHeight("450px");
		addFolderWindow.setModal(true);

		addFolderDialog.getCancel().addListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				PersonalpasssaveApplication.getInstance().getBaseController()
						.removeWindow(addFolderWindow);
			}

		});

		PersonalpasssaveApplication.getInstance().getBaseController()
				.addWindow(addFolderWindow);
	}

	private void initFolderTree() throws Exception {
		folderTree.addContainerProperty(TREE_PROPERTY_CAPTION, String.class,
				null);
		folderTree.addContainerProperty(TREE_PROPERTY_FOLDERNAME, String.class,
				null);
		folderTree.setItemCaptionPropertyId(TREE_PROPERTY_CAPTION);
		folderTree
				.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_PROPERTY);
		folderTree.setMultiSelect(false);
		folderTree.addListener(new Property.ValueChangeListener() {

			public void valueChange(
					com.vaadin.data.Property.ValueChangeEvent event) {
				if (event.getProperty().getValue() != null) {
					Object valueClickedID = event.getProperty().getValue();
					Collection<?> itemIds = folderTree.getItemIds();
					Item clickedNode = folderTree.getItem(valueClickedID);
					String value = (String) clickedNode.getItemProperty(
							TREE_PROPERTY_CAPTION).getValue();
					showFilesForChoosenFolder(value);
				}
			}
		});
		folderTree.setCaption("Your current Files and Folders");
	}

	private void showFilesForChoosenFolder(String value) {
		System.out.println("opening folder:" + value);
		List<FileInStore> filesForFolder = null;
		try {
			filesForFolder = PersonalpasssaveApplication.getInstance()
					.getFileArchiveController()
					.getAllFilesForSpecifiedFolder(value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		loadFilesForFolderFromFileSystem(filesForFolder);
	}

	private void fillFolderTree() throws Exception {
		List<String> allRootNodes = PersonalpasssaveApplication.getInstance()
				.getFileArchiveController().getAllRootFolderNames();
		int count = 0;

		rootItemIdFolderTable = folderTree.addItem();
		Item itemRootdNode = folderTree.getItem(rootItemIdFolderTable);
		itemRootdNode.getItemProperty(TREE_PROPERTY_CAPTION).setValue(
				"All Files");

		if (allRootNodes != null) {
			if (!allRootNodes.isEmpty()) {
				for (String currentRootNode : allRootNodes) {
					count++;
					Object itemNodeId = folderTree.addItem();
					Item treeItem = folderTree.getItem(itemNodeId);
					treeItem.getItemProperty(TREE_PROPERTY_CAPTION).setValue(
							currentRootNode);
					folderTree.setParent(itemNodeId, rootItemIdFolderTable);
				}
				folderTree.setImmediate(true);
				folderTree.expandItem(rootItemIdFolderTable);
			}
		}
	}

	private void openNewEditingDialog(FileInStore currentFile) throws Exception {
		FileArchiveController archiveController = new FileArchiveController();
		archiveEditingDialog = new FileArchiveEditingDialog(
				archiveController.getAllRootFolderNames());
		editingFilesWindow = new Window("Edit files Metadata");
		editingFilesWindow.addComponent(archiveEditingDialog);
		editingFilesWindow.setWidth("500px");
		editingFilesWindow.setHeight("450px");
		editingFilesWindow.setModal(true);
		archiveEditingDialog.getParentFolder().setValue(
				currentFile.getParentFoldername());
		archiveEditingDialog.getFolder().setValue(currentFile.getFoldername());
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
				Object choosenItemID = folderTree.getValue();
				if (choosenItemID != null) {
					Item choosenItem = folderTree.getItem(choosenItemID);
					showFilesForChoosenFolder(choosenItem
							.getItemProperty(TREE_PROPERTY_CAPTION).getValue()
							.toString());
				} else {

					showFilesForChoosenFolder(PersonalPassConstants.FILE_ROOT_NAME);

				}
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
		System.out.println("CurrentChosenID:" + currentChoosenID);
		if (currentChoosenID != null) {
			FileInStore currentFile = null;
			if (filesTable.getContainerProperty(currentChoosenID, "Object") != null) {
				currentFile = (FileInStore) filesTable.getContainerProperty(
						currentChoosenID, "Object").getValue();
			}
			System.out.println("CurrentFile:" + currentFile);
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

			try {
				openNewEditingDialog(currentFile);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private void saveChanges() {

	}

	private void openFile() {

		Object objectid = filesTable.getValue();
		if (objectid != null) {
			Item item = filesTable.getItem(objectid);
			Property itemMetaObject = item.getItemProperty("Object");
			FileArchiveController archiveController = PersonalpasssaveApplication
					.getInstance().getFileArchiveController();
			archiveController.openFileFromArchive(((FileInStore) itemMetaObject
					.getValue()));
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
		addComponent(filesTable, "top:180.0px;left:200.0px;");

		folderTree.setImmediate(true);
		addComponent(folderTree, "top:180.0px;left:5.0px;");

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

		addFolder.setCaption("Add Folder");
		addFolder.setImmediate(false);
		addFolder.setWidth("-1px");
		addFolder.setHeight("-1px");
		addFolder.setIcon(new ThemeResource("../runo/icons/16/folder-add.png"));
		addFolder.addStyleName(Runo.BUTTON_SMALL);
		addComponent(addFolder, "top:40.0px;left:300.0px;");
	}

	public void closeFileUploadDialog() {
		PersonalpasssaveApplication.getInstance().getBaseController()
				.removeWindow(newFilesWindow);

		Object choosenItemID = folderTree.getValue();
		if (choosenItemID != null) {
			Item choosenItem = folderTree.getItem(choosenItemID);
			showFilesForChoosenFolder(choosenItem
					.getItemProperty(TREE_PROPERTY_CAPTION).getValue()
					.toString());
		} else {
			showFilesForChoosenFolder(PersonalPassConstants.FILE_ROOT_NAME);
		}
	}

	private void addItem() throws Exception {
		FileArchiveController archiveController = new FileArchiveController();
		fileArchiveDialog = new FileArchiveDialog(
				archiveController.getAllRootFolderNames());
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

	private void loadFilesForFolderFromFileSystem(
			List<FileInStore> allFilesForUser) {
		filesTable.removeAllItems();
		// FileArchiveController control = PersonalpasssaveApplication
		// .getInstance().getFileArchiveController();
		// ArrayList<FileInStore> allFilesForUser =
		// control.getAllFilesForUser();
		int counter = 0;

		if (allFilesForUser != null) {
			for (FileInStore fileInStore : allFilesForUser) {
				filesTable.addItem(new Object[] { fileInStore.getDescription(),
						fileInStore.getFoldername(), fileInStore.getFileName(),
						fileInStore.getFileSize(), fileInStore, }, new Integer(
						counter));
				counter++;
			}
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
