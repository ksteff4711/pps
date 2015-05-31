package org.kingsteff.passwordsave;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;

import com.google.gwt.thirdparty.javascript.jscomp.parsing.parser.trees.GetAccessorTree;

public class PasswordManager {

	public void removeUsersPasswordsFolder(String username) {
		String fileName = PersonalPassConstants.MAINDIR + getMd5Hash(username)
				+ "/" + PersonalPassConstants.PASSWORDS_FILENAME_PREFIX
				+ getMd5Hash(username)
				+ PersonalPassConstants.PASSWORDS_FILENAME_SUFFIX;
		File file = new File(fileName);
		file.delete();
		File folder = new File(PersonalPassConstants.MAINDIR
				+ getMd5Hash(username));
		folder.delete();
	}

	@SuppressWarnings("resource")
	public UserAndPasswords getAllPasswordsForUser(String username)
			throws Exception {
		String fileName = PersonalPassConstants.MAINDIR + getMd5Hash(username)
				+ "/" + PersonalPassConstants.PASSWORDS_FILENAME_PREFIX
				+ getMd5Hash(username)
				+ PersonalPassConstants.PASSWORDS_FILENAME_SUFFIX;

		try {
			CipherInputStream in;
			Cipher cipher;
			SecretKey key;
			cipher = Cipher.getInstance(PersonalPassConstants.ENCRYPTION_MODE);
			key = new SecretKeySpec(
					PersonalPassConstants.MAIN_SCHLUSSEL.getBytes(),
					PersonalPassConstants.ENCRYPTION_MODE);
			cipher.init(Cipher.DECRYPT_MODE, key);
			in = new CipherInputStream(new FileInputStream(fileName), cipher);

			// int read = 0;
			StringBuffer buff = new StringBuffer();
			// while ((read = in.read()) != -1) {
			// buff.append((char) read);
			// }
			String line;
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			while ((line = reader.readLine()) != null) {
				buff.append(line);
			}

			ObjectMarshaller marshaller = new ObjectMarshaller();
			Object fromXml = marshaller.fromXmlWithXStream(buff.toString());
			UserAndPasswords wrapper = (UserAndPasswords) fromXml;
			return wrapper;
		} catch (Exception e) {

			throw e;
		}

	}

	public void addPassword(PasswordInfos incoming, String login) {
		File mkdir = new File(PersonalPassConstants.MAINDIR + getMd5Hash(login));

		if (incoming.getId() != null) {
			try {
				if (!mkdir.exists()) {
					UserAndPasswords andPasswords = new UserAndPasswords();
					andPasswords.login = login;
					andPasswords.passwords.put(incoming.getId(), incoming);
					mkdir.mkdirs();
					SecretKey key;
					Cipher cipher;
					cipher = Cipher
							.getInstance(PersonalPassConstants.ENCRYPTION_MODE);
					key = new SecretKeySpec(
							PersonalPassConstants.MAIN_SCHLUSSEL.getBytes(),
							PersonalPassConstants.ENCRYPTION_MODE);
					cipher.init(Cipher.ENCRYPT_MODE, key);
					String fileName = PersonalPassConstants.MAINDIR
							+ getMd5Hash(login) + "/"
							+ PersonalPassConstants.PASSWORDS_FILENAME_PREFIX
							+ getMd5Hash(login)
							+ PersonalPassConstants.PASSWORDS_FILENAME_SUFFIX;
					CipherOutputStream cip = new CipherOutputStream(
							new FileOutputStream(fileName), cipher);
					ObjectMarshaller marshaller = new ObjectMarshaller();
					cip.write(marshaller.toXmlWithXStream(andPasswords)
							.getBytes("UTF8"));
					cip.close();
				} else {
					UserAndPasswords allPasswordsForUser = getAllPasswordsForUser(login);
					allPasswordsForUser.passwords.put(incoming.getId(),
							incoming);
					SecretKey key;
					Cipher cipher;
					cipher = Cipher
							.getInstance(PersonalPassConstants.ENCRYPTION_MODE);
					key = new SecretKeySpec(
							PersonalPassConstants.MAIN_SCHLUSSEL.getBytes(),
							PersonalPassConstants.ENCRYPTION_MODE);
					cipher.init(Cipher.ENCRYPT_MODE, key);
					String fileName = PersonalPassConstants.MAINDIR
							+ getMd5Hash(login) + "/"
							+ PersonalPassConstants.PASSWORDS_FILENAME_PREFIX
							+ getMd5Hash(login)
							+ PersonalPassConstants.PASSWORDS_FILENAME_SUFFIX;
					CipherOutputStream cip = new CipherOutputStream(
							new FileOutputStream(fileName), cipher);
					PrintWriter prt = new PrintWriter(new OutputStreamWriter(
							cip, "UTF-8"));
					ObjectMarshaller marshaller = new ObjectMarshaller();
					prt.write(marshaller.toXmlWithXStream(allPasswordsForUser));
					prt.close();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("CANNOT ADD PASSWORD WITH NULL ID" + incoming);
		}

	}

	public static String getMd5Hash(String md5) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
						.substring(1, 3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
		}
		return null;
	}

	public void addAllPasswords(String login,
			UserAndPasswords allPasswordsForUser) {

		try {
			SecretKey key;
			Cipher cipher;
			cipher = Cipher.getInstance(PersonalPassConstants.ENCRYPTION_MODE);
			key = new SecretKeySpec(
					PersonalPassConstants.MAIN_SCHLUSSEL.getBytes(),
					PersonalPassConstants.ENCRYPTION_MODE);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			String fileName = PersonalPassConstants.MAINDIR + getMd5Hash(login)
					+ "/" + PersonalPassConstants.PASSWORDS_FILENAME_PREFIX
					+ getMd5Hash(login)
					+ PersonalPassConstants.PASSWORDS_FILENAME_SUFFIX;
			CipherOutputStream cip = new CipherOutputStream(
					new FileOutputStream(fileName), cipher);
			ObjectMarshaller marshaller = new ObjectMarshaller();
			String xmlMarshall = marshaller
					.toXmlWithXStream(allPasswordsForUser);
			cip.write(xmlMarshall.getBytes());
			cip.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void removePassword(String id, String login) {
		try {
			UserAndPasswords allPasswordsForUser = getAllPasswordsForUser(login);
			allPasswordsForUser.passwords.remove(id);
			SecretKey key;
			Cipher cipher;
			cipher = Cipher.getInstance(PersonalPassConstants.ENCRYPTION_MODE);
			key = new SecretKeySpec(
					PersonalPassConstants.MAIN_SCHLUSSEL.getBytes(),
					PersonalPassConstants.ENCRYPTION_MODE);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			String fileName = PersonalPassConstants.MAINDIR + getMd5Hash(login)
					+ "/" + PersonalPassConstants.PASSWORDS_FILENAME_PREFIX
					+ getMd5Hash(login)
					+ PersonalPassConstants.PASSWORDS_FILENAME_SUFFIX;
			CipherOutputStream cip = new CipherOutputStream(
					new FileOutputStream(fileName), cipher);
			ObjectMarshaller marshaller = new ObjectMarshaller();
			cip.write(marshaller.toXmlWithXStream(allPasswordsForUser)
					.getBytes());
			cip.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String generateRandomPassword(int length, boolean withSpecialChars) {
		String chars;
		if (withSpecialChars) {
			chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234657890abcdefghijklmnopqrstuvwxyz;:_-.,!§$%&()=?*+";
		} else {
			chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234657890abcdefghijklmnopqrstuvwxyz";
		}
		return RandomStringUtils.random(length, chars);
	}

	public static String encryptString(String toEncrypt) throws Exception {
		Cipher cipher;
		SecretKey key;
		cipher = Cipher.getInstance(PersonalPassConstants.ENCRYPTION_MODE);
		key = new SecretKeySpec(
				PersonalPassConstants.STRING_SCHLUSSEL.getBytes(),
				PersonalPassConstants.ENCRYPTION_MODE);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] cipherText = cipher.doFinal(toEncrypt.getBytes());
		return new String(cipherText);
	}

	public static String deCryptString(String toDecrypt) throws Exception {
		Cipher cipher;
		SecretKey key;
		cipher = Cipher.getInstance(PersonalPassConstants.ENCRYPTION_MODE);
		key = new SecretKeySpec(
				PersonalPassConstants.STRING_SCHLUSSEL.getBytes(),
				PersonalPassConstants.ENCRYPTION_MODE);
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] decrypted = cipher.doFinal(toDecrypt.getBytes());
		return new String(decrypted);
	}

	public static String baseEncryptString(String toEncrypt) {
		Base64 b64 = new Base64();
		String result = new String(b64.encode(toEncrypt.getBytes()));
		return result;
	}

	public static String baseDeCryptString(String toDecrypt) {
		Base64 b64 = new Base64();
		String result = new String(b64.decode(toDecrypt.getBytes()));
		return result;
	}

	public File generateCsvFile(String sFileName) {
		try {
			FileWriter writer = new FileWriter(sFileName);

			writer.append("login");
			writer.append(',');
			writer.append("password");
			writer.append(',');
			writer.append("location");
			writer.append(',');
			writer.append("comment");
			writer.append(',');
			writer.append("label");
			writer.append(',');
			writer.append("creationdate");
			writer.append(',');
			writer.append("userid");
			writer.append(',');
			writer.append("id");
			writer.append(',');
			writer.append("website");
			writer.append('\n');
			String currentUser = PersonalpasssaveApplication.getInstance()
					.getBaseController().getCurrentUser();
			UserAndPasswords allPasswordsForUser = getAllPasswordsForUser(currentUser);
			for (String t : allPasswordsForUser.passwords.keySet()) {
				PasswordInfos passwordInfos = allPasswordsForUser.passwords
						.get(t);
				writer.append(passwordInfos.getLogin());
				writer.append(',');
				writer.append(passwordInfos.getPassword());
				writer.append(',');
				writer.append(passwordInfos.getLocation());
				writer.append(',');
				writer.append(passwordInfos.getComment());
				writer.append(',');
				writer.append(passwordInfos.getLabel());
				writer.append(',');
				writer.append(passwordInfos.getCreationdate() + "");
				writer.append(',');
				writer.append(passwordInfos.getUserID() + "");
				writer.append(',');
				writer.append(passwordInfos.getId());
				writer.append(',');
				writer.append(passwordInfos.getWebsite());
				writer.append('\n');
			}
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new File(sFileName);
	}
}
