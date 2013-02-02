package org.kingsteff.passwordsave;

import com.vaadin.ui.AbsoluteLayout;

public class BaseWindow extends AbsoluteLayout {

	private MainView mainview;
	private UserListView listView;

	public BaseWindow() {
		if (mainview == null) {
			mainview = new MainView();
		}
		removeAllComponents();
		addComponent(mainview);
		setSizeFull();
	}

	public void showUserManagement() {
		if (listView == null) {
			listView = new UserListView();
		}
		removeAllComponents();
		addComponent(listView);
		setSizeFull();
	}

	public void showMainView() {
		removeAllComponents();
		addComponent(mainview);
		setSizeFull();
	}

	public void removeFileUploadDialog() {
		mainview.getArchiveTab().closeFileUploadDialog();
	}

	public MainView getMainview() {
		return mainview;
	}

	public void setMainview(MainView mainview) {
		this.mainview = mainview;
	}

}
