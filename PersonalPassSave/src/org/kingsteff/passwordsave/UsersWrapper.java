package org.kingsteff.passwordsave;

import java.util.HashMap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "wrapper")
public class UsersWrapper {

	@XmlElement
	public HashMap<String, String> userPasses = new HashMap<String, String>();

	@XmlElement
	public HashMap<String, Integer> userLoginCounts = new HashMap<String, Integer>();

	public HashMap<String, String> getUserPasses() {
		return userPasses;
	}

	public void setUserPasses(HashMap<String, String> userPasses) {
		this.userPasses = userPasses;
	}

	public HashMap<String, Integer> getUserLoginCounts() {
		return userLoginCounts;
	}

	public void setUserLoginCounts(HashMap<String, Integer> userLoginCounts) {
		this.userLoginCounts = userLoginCounts;
	}

}
