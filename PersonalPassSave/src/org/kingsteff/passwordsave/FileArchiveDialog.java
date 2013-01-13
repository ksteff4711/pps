package org.kingsteff.passwordsave;

import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Upload;

public class FileArchiveDialog extends GridLayout {

	private Upload upField;

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

		this.setColumns(2);
		this.addComponent(upField);

		this.setSpacing(true);
	}

}
