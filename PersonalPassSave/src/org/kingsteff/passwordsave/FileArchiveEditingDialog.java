package org.kingsteff.passwordsave;

import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

public class FileArchiveEditingDialog extends GridLayout {

	private TextField descriptioninput;
	private TextField folder;
	private TextField parentFolder;
	private Button save;
	private Button cancel;

	public FileArchiveEditingDialog() {
		initUI();
	}

	private void initUI() {
		descriptioninput = new TextField("Description");
		descriptioninput.setWidth("250px");
		folder = new TextField("Folder");
		folder.setWidth("250px");
		parentFolder = new TextField("Parent Folder");
		parentFolder.setWidth("250px");
		save = new Button("Save");
		cancel = new Button("Cancel");
		this.setColumns(1);
		this.addComponent(descriptioninput);
		this.addComponent(folder);
		this.addComponent(parentFolder);
		HorizontalLayout h1 = new HorizontalLayout();
		h1.addComponent(save);
		h1.addComponent(cancel);
		this.addComponent(h1);
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

	public TextField getDescriptioninput() {
		return descriptioninput;
	}

	public void setDescriptioninput(TextField descriptioninput) {
		this.descriptioninput = descriptioninput;
	}

	public Button getSave() {
		return save;
	}

	public void setSave(Button save) {
		this.save = save;
	}

	public Button getCancel() {
		return cancel;
	}

	public void setCancel(Button cancel) {
		this.cancel = cancel;
	}

}
