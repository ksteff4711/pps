package org.kingsteff.passwordsave;

import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TabSheet;

public class MainView extends GridLayout {
	private TabSheet tabsheet = null;
	private PasswordTab passTab = null;
	private FilesArchiveTab archiveTab = null;

	public MainView() {
		tabsheet = new TabSheet();
		passTab = new PasswordTab();
		archiveTab = new FilesArchiveTab();
		tabsheet.addTab(passTab, "Your PasswordList");
		tabsheet.addTab(archiveTab, "Your archived files");
		addComponent(tabsheet);
		setHeight("100%");
		setWidth("99%");
	}

	public TabSheet getTabsheet() {
		return tabsheet;
	}

	public PasswordTab getPassTab() {
		return passTab;
	}

	public FilesArchiveTab getArchiveTab() {
		return archiveTab;
	}

}