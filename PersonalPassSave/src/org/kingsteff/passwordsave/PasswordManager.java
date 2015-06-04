package org.kingsteff.passwordsave;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;

import com.google.gwt.thirdparty.javascript.jscomp.parsing.parser.trees.GetAccessorTree;

public class PasswordManager {
	
	

	private UserAndPasswords allPasswordsForUser;

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

			 int read = 0;
			StringBuffer buff = new StringBuffer();

			
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			String line;
			while ((line = reader.readLine()) != null) {
				buff.append(line);
				System.out.println("append");
			}

			

			return (UserAndPasswords) PersonalpasssaveApplication.getInstance().getMarshaller().fromXml(buff.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public void addPassword(PasswordInfos incoming, String login) {
		File mkdir = new File(PersonalPassConstants.MAINDIR + getMd5Hash(login));

		if (incoming.getId() != null) {
			try {
				if (!mkdir.exists()) {
					UserAndPasswords andPasswords = new UserAndPasswords();
					andPasswords.setLogin(login);
					andPasswords.getPasswords().put(incoming.getId(), incoming);
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

					cip.write(PersonalpasssaveApplication.getInstance().getMarshaller().toXmlWithXStream(andPasswords)
							.getBytes("UTF8"));
					cip.close();
				} else {
					
					if(allPasswordsForUser==null){
						allPasswordsForUser = getAllPasswordsForUser(login);
						if (allPasswordsForUser==null) {
							allPasswordsForUser = new UserAndPasswords();
						}
					}
					allPasswordsForUser.getPasswords().put(incoming.getId(),
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
					prt.write(PersonalpasssaveApplication.getInstance().getMarshaller().toXmlWithXStream(allPasswordsForUser));
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
			String xmlMarshall = PersonalpasssaveApplication.getInstance().getMarshaller()
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
			allPasswordsForUser.getPasswords().remove(id);
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
			
			cip.write(PersonalpasssaveApplication.getInstance().getMarshaller().toXmlWithXStream(allPasswordsForUser)
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
//			FileWriter writer = new FileWriter(sFileName);
			PrintWriter writer = new PrintWriter( sFileName, "UTF-8" );
			writer.append("login");
			writer.append("§");
			writer.append("password");
			writer.append("§");
			writer.append("location");
			writer.append("§");
			writer.append("comment");
			writer.append("§");
			writer.append("label");
			writer.append("§");
			writer.append("creationdate");
			writer.append("§");
			writer.append("userid");
			writer.append("§");
			writer.append("id");
			writer.append("§");
			writer.append("website");
			writer.append('\n');
			String currentUser = PersonalpasssaveApplication.getInstance()
					.getBaseController().getCurrentUser();
			UserAndPasswords allPasswordsForUser = getAllPasswordsForUser(currentUser);
			for (String t : allPasswordsForUser.getPasswords().keySet()) {
				PasswordInfos passwordInfos = allPasswordsForUser.getPasswords()
						.get(t);
				writer.append(((passwordInfos.getLogin()!=null)&&(!passwordInfos.getLogin().trim().equals("")))?passwordInfos.getLogin():"---");
				writer.append("§");
				writer.append(((passwordInfos.getPassword()!=null)&&(!passwordInfos.getPassword().trim().equals("")))?passwordInfos.getPassword():"---");
				writer.append("§");
				writer.append(((passwordInfos.getLocation()!=null)&&(!passwordInfos.getLocation().trim().equals("")))?passwordInfos.getLocation():"---");
				writer.append("§");
				writer.append(((passwordInfos.getComment()!=null)&&(!passwordInfos.getComment().trim().equals("")))?passwordInfos.getComment():"---");
				writer.append("§");
				writer.append(((passwordInfos.getLabel()!=null)&&(!passwordInfos.getLabel().trim().equals("")))?passwordInfos.getLabel():"---");
				writer.append("§");
				writer.append(getFormatedDateString(passwordInfos.getCreationdate()));
				writer.append("§");
				writer.append(passwordInfos.getUserID()+"");
				writer.append("§");
				writer.append(((passwordInfos.getId()!=null)&&(!passwordInfos.getId().trim().equals("")))?passwordInfos.getId():"---");
				writer.append("§");
				writer.append(((passwordInfos.getWebsite()!=null)&&(!passwordInfos.getWebsite().trim().equals("")))?passwordInfos.getWebsite():"---");
				writer.append('\n');
			}
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new File(sFileName);
	}

	public void importData(File inputFile) {
		BufferedReader br = null;
		String line = "";
		
		try {

			String currentUser = PersonalpasssaveApplication.getInstance()
					.getBaseController().getCurrentUser();
			UserAndPasswords allPasswordsForUser = getAllPasswordsForUser(currentUser);
			if ((allPasswordsForUser!=null)&&(allPasswordsForUser.getPasswords()!=null)&&(allPasswordsForUser.getPasswords().keySet()!=null)) {
				for (String t : allPasswordsForUser.getPasswords().keySet()) {
					PasswordInfos passwordInfos = allPasswordsForUser
							.getPasswords().get(t);
					removePassword(passwordInfos.getId(), currentUser);
				}
			}
			br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(inputFile), "UTF8"));
			int linecounter = 0;
			while ((line = br.readLine()) != null) {

				if (linecounter>0) {
					String[] splitUsingTokenizer = splitUsingTokenizer(line,
							"§");
					int count = 0;
					PasswordInfos infos = new PasswordInfos();
					infos.setId(BaseController.generaterandomId());
					for (String string : splitUsingTokenizer) {
						System.out.println("-->" + string);
						switch (count) {
						case 0: {
							infos.setLogin(string);
							break;
						}
						case 1: {
							infos.setPassword(string);
							break;
						}
						case 2: {
							infos.setLocation(string);
							break;
						}
						case 3: {
							infos.setComment(string);
							break;
						}
						case 4: {
							infos.setLabel(string);
							break;
						}
						case 5: {
							infos.setCreationdate(getDateFromString(string));
							break;
						}
						case 6: {
							infos.setUserID(new Integer(string));
							break;
						}
						case 7: {
							infos.setId(string);
							break;
						}
						case 8: {
							infos.setWebsite(string);
							break;
						}
						}
						count++;
					}
					addPassword(infos, currentUser);
				}
				linecounter++;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String[] splitUsingTokenizer(String subject, String delimiters) {
		StringTokenizer strTkn = new StringTokenizer(subject, delimiters);
		ArrayList<String> arrLis = new ArrayList<String>(subject.length());

		while (strTkn.hasMoreTokens()){
			arrLis.add(strTkn.nextToken());
		}

		return arrLis.toArray(new String[0]);
	}
	
	private String getFormatedDateString(Date incomingDate) {
		SimpleDateFormat dateformat = new SimpleDateFormat(
				"HH:mm.ss MM.dd.yyyy");

		StringBuilder formatedDate = new StringBuilder(
				dateformat.format(incomingDate));
		return formatedDate.toString();
	}
	
	private Date getDateFromString(String input){
		
		try {
			DateFormat format = new SimpleDateFormat("HH:mm.ss MM.dd.yyyy", Locale.GERMAN);
			return  format.parse(input);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Date();
		}
		
	}
}
