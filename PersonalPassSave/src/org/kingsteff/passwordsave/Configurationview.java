package org.kingsteff.passwordsave;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

public class Configurationview {
	
	
	
	Label pathToFilesLab = new Label();
	TextField pathFilesTf= new TextField();
	
	Button ok = new Button("ok");
	Button close = new Button("close");
	
	Button importxml = new Button("import systemdata from file");
	
	Button fileMigrateFromPreviousVersion = new Button("migrate System Data");
	
	Label mysqlServerURLLabel = new Label();
	TextField msqlServerUrlTF = new TextField();
	
	Label mysqlServerUsernamelabel = new Label();
	TextField mysqlSeverUsernameTextField = new TextField();
	
	Label msqlServerPassLabel = new Label ();
	TextField mysqlServerPassTextField = new TextField();
	
	Label mainKeyForCipher = new Label();
	TextField mainKeyForCipherTextField = new TextField();
	
	
	
}
