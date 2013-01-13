package org.kingsteff.passwordsave;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Vector;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class LoginManager {

	public static LoginManager self = null;

	private UsersWrapper wrapper = new UsersWrapper();
	final static Charset ENCODING = StandardCharsets.UTF_8;

	private LoginManager() {
		try {
			readUsersFromEncryptedFile();
		} catch (Exception e) {
			if (e instanceof FileNotFoundException) {
				addUser("admin", "admin");
			}
		}
	}

	public static LoginManager getInstance() {
		if (self == null) {
			self = new LoginManager();
		}
		return self;
	}

	public boolean checkUsernamePassword(String username, String password) {
		String string = wrapper.userPasses.get(username);
		if (string == null) {
			return false;
		} else {

			return string.equals(password);
		}
	}

	public void removeUserAttempts(String username) {

		wrapper.userLoginCounts.remove(username);

	}

	public void increaseLoginCount(String username) {
		Integer integer = wrapper.userLoginCounts.get(username);
		if (integer == null) {
			wrapper.userLoginCounts.put(username, new Integer(1));
		} else {
			int newValue = 0;
			try {
				newValue = (integer.intValue() + 1);
				wrapper.userLoginCounts.put(username, new Integer(newValue));
			} catch (Exception e) {
				wrapper.userLoginCounts.put(username, new Integer(
						Integer.MAX_VALUE));
			}

		}
		saveAllUsersToHdd();
	}

	public boolean checkIfUserhasReachedMaxLoginTries(String username) {
		Integer integer;
		if (wrapper.userLoginCounts != null) {
			integer = wrapper.userLoginCounts.get(username);
		} else {
			wrapper.userLoginCounts = new HashMap<String, Integer>();
			integer = wrapper.userLoginCounts.get(username);
			saveAllUsersToHdd();
		}
		if (integer == null) {
			return false;
		} else {
			return (integer.intValue() > 5);
		}
	}

	public void addUser(String login, String password) {
		wrapper.userPasses.put(login, password);
		saveAllUsersToHdd();
	}

	public void removeUser(String login) {
		wrapper.userPasses.remove(login);
		saveAllUsersToHdd();
	}

	public void resetUsersPassword(String username) {
		wrapper.userPasses.put(username, "TEST12342012");
		wrapper.userLoginCounts.remove(username);
		saveAllUsersToHdd();
	}

	public void setUsersPassword(String login, String newPasssword) {
		wrapper.userPasses.put(login, newPasssword);
		saveAllUsersToHdd();
	}

	public Vector<String> getAllUserNames() {
		Vector<String> newUsers = new Vector<String>();
		for (Object current : wrapper.userPasses.keySet()) {
			newUsers.add(current.toString());
		}
		return newUsers;
	}

	private void saveAllUsersToHdd() {

		ObjectMarshaller objectMarshaller = new ObjectMarshaller();
		String xml = objectMarshaller.toXmlWithXStream(wrapper);
		writeFileToEncrytedFile(xml);
	}

	private void writeFileToEncrytedFile(String incoming) {
		File dir = new File(PersonalPassConstants.MAINDIR
				+ PersonalPassConstants.USERS_SUBDIR);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		try {
			SecretKey key;
			Cipher cipher;
			cipher = Cipher.getInstance("DESede");
			key = new SecretKeySpec("kgl51um6ad6598pbjnbz6xln".getBytes(),
					"DESede");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			CipherOutputStream cip = new CipherOutputStream(
					new FileOutputStream(PersonalPassConstants.MAINDIR
							+ PersonalPassConstants.USERS_SUBDIR
							+ PersonalPassConstants.USERS_FILENAME), cipher);
			cip.write(incoming.getBytes());
			cip.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void readUsersFromEncryptedFile() throws Exception {
		try {
			CipherInputStream in;
			OutputStream out;
			Cipher cipher;
			SecretKey key;
			byte[] byteBuffer;
			cipher = Cipher.getInstance("DESede");
			key = new SecretKeySpec("kgl51um6ad6598pbjnbz6xln".getBytes(),
					"DESede");
			cipher.init(Cipher.DECRYPT_MODE, key);
			in = new CipherInputStream(new FileInputStream(
					PersonalPassConstants.MAINDIR
							+ PersonalPassConstants.USERS_SUBDIR
							+ PersonalPassConstants.USERS_FILENAME), cipher);
			int read = 0;
			StringBuffer buff = new StringBuffer();
			while ((read = in.read()) != -1) {
				buff.append((char) read);
			}
			ObjectMarshaller marshaller = new ObjectMarshaller();
			Object fromXml = marshaller.fromXmlWithXStream(buff.toString());
			wrapper = (UsersWrapper) fromXml;
			if (wrapper.userPasses.isEmpty()) {
				wrapper.userPasses.put("admin", "admin");
				saveAllUsersToHdd();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;

		}
	}

}
