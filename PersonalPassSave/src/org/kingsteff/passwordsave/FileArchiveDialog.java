package org.kingsteff.passwordsave;

import java.util.List;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;

public class FileArchiveDialog extends GridLayout {

	private Upload upField;
	private TextField descriptioninput;
	private ComboBox folder;
	private TextField parentFolder;
	private List<String> incomingFolderNames;

	public FileArchiveDialog(List<String> incomingFolderNames) {
		this.incomingFolderNames = incomingFolderNames;
		initUI();
	}

	private void initUI() {
		upField = new Upload();
		upField.setButtonCaption("UploadFile");
		upField.setCaption("Upload Your File To Archive");
		upField.setImmediate(true);
		UpLoadHandler upHandler = new UpLoadHandler();
		upField.addListener((Upload.SucceededListener) upHandler);
		upField.addListener((Upload.FailedListener) upHandler);
		upField.setReceiver((Upload.Receiver) upHandler);
		descriptioninput = new TextField("Description");
		descriptioninput.setWidth("250px");
		folder = new ComboBox("Folder", incomingFolderNames);
		folder.setWidth("250px");
		folder.setNullSelectionAllowed(false);
		parentFolder = new TextField("Parent Folder");
		parentFolder.setEnabled(false);
		parentFolder.setValue(PersonalPassConstants.FILE_ROOT_NAME);

		parentFolder.setWidth("250px");
		this.setColumns(1);
		this.addComponent(upField);
		this.addComponent(descriptioninput);
		this.addComponent(folder);
		this.addComponent(parentFolder);
		this.setSpacing(true);
	}

	public TextField getDescriptionInput() {
		return descriptioninput;
	}

	public void setDescriptionInput(TextField descriptioninput) {
		this.descriptioninput = descriptioninput;
	}

	public ComboBox getFolder() {
		return folder;
	}

	public void setFolder(ComboBox folder) {
		this.folder = folder;
	}

	public TextField getParentFolder() {
		return parentFolder;
	}

	public void setParentFolder(TextField parentFolder) {
		this.parentFolder = parentFolder;
	}

}
