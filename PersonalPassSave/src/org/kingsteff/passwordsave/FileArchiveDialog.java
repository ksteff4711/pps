package org.kingsteff.passwordsave;

import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;

public class FileArchiveDialog extends GridLayout {

	private Upload upField;
	private TextField descriptioninput;
	private TextField folder;
	private TextField parentFolder;

	public FileArchiveDialog() {
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
		folder = new TextField("Folder");
		folder.setWidth("250px");
		parentFolder = new TextField("Parent Folder");
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

	public TextField getFolder() {
		return folder;
	}

	public void setFolder(TextField folder) {
		this.folder = folder;
	}

	public TextField getParentFolder() {
		return parentFolder;
	}

	public void setParentFolder(TextField parentFolder) {
		this.parentFolder = parentFolder;
	}

}
