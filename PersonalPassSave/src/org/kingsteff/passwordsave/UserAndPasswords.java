package org.kingsteff.passwordsave;

import java.util.HashMap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "userandpasswords")
public class UserAndPasswords {

	@XmlElement
	public String login;
	@XmlElement
	public HashMap<String, PasswordInfos> passwords = new HashMap<String, PasswordInfos>();

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public HashMap<String, PasswordInfos> getPasswords() {
		return passwords;
	}

	public void setPasswords(HashMap<String, PasswordInfos> passwords) {
		this.passwords = passwords;
	}

	public String toString() {
		return ("User:" + login + " " + passwords.toString());
	}
}
