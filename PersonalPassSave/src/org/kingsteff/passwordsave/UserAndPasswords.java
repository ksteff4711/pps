package org.kingsteff.passwordsave;

import java.util.HashMap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "userandpasswords")
public class UserAndPasswords {

	@XmlElement
	private String loginUP;
	@XmlElement
	private HashMap<String, PasswordInfos> passwordsList = new HashMap<String, PasswordInfos>();

	public String getLogin() {
		return loginUP;
	}

	public void setLogin(String login) {
		this.loginUP = login;
	}

	public HashMap<String, PasswordInfos> getPasswords() {
		return passwordsList;
	}

	public void setPasswords(HashMap<String, PasswordInfos> passwords) {
		this.passwordsList = passwords;
	}

	public String toString() {
		return ("User:" + loginUP + " " + passwordsList.toString());
	}
}
