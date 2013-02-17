package org.kingsteff.passwordsave;

import java.util.List;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

public class FilesAddFolderDialog extends GridLayout {

	private TextField fodlerName;
	private ComboBox folder;
	private Button save;
	private Button cancel;
	private List<String> incomingFolderNames;

	public FilesAddFolderDialog(List<String> incomingFolderNames) {
		this.incomingFolderNames = incomingFolderNames;
		initUI();
	}

	private void initUI() {
		fodlerName = new TextField("New Foldername:");
		fodlerName.setWidth("250px");
		folder = new ComboBox("Parent Folder", incomingFolderNames);
		folder.setWidth("250px");
		folder.setNullSelectionAllowed(false);
		folder.setValue(PersonalPassConstants.FILE_ROOT_NAME);
		save = new Button("Save");
		cancel = new Button("Cancel");
		this.setColumns(1);
		this.addComponent(folder);
		HorizontalLayout h1 = new HorizontalLayout();
		h1.addComponent(save);
		h1.addComponent(cancel);
		this.addComponent(h1);
		this.setSpacing(true);
	}

	public TextField getFodlerName() {
		return fodlerName;
	}

	public void setFodlerName(TextField fodlerName) {
		this.fodlerName = fodlerName;
	}

	public List<String> getIncomingFolderNames() {
		return incomingFolderNames;
	}

	public void setIncomingFolderNames(List<String> incomingFolderNames) {
		this.incomingFolderNames = incomingFolderNames;
	}

	public ComboBox getFolder() {
		return folder;
	}

	public void setFolder(ComboBox folder) {
		this.folder = folder;
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
