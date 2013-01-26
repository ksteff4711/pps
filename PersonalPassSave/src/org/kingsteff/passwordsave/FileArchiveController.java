package org.kingsteff.passwordsave;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.vaadin.terminal.StreamResource;

public class FileArchiveController {

	public void addFileToList() {
//ist noch im upLoadHandler
	}

	
	public void removeFileFromArchive(String filePath, String realFIleName){
		File file = new File(filePath+"/"+realFIleName);
		file.delete();
	}
	
	public ArrayList<FileInStore> getAllFilesForUser() {
		ArrayList<FileInStore> userFiles = new ArrayList<FileInStore>();
		File dir = new File(PersonalPassConstants.MAINDIR
				+ PersonalPassConstants.FILESTORE_SUBDIR
				+ PasswordManager.getMd5Hash(PersonalpasssaveApplication
						.getInstance().getBaseController().getCurrentUser())
				+ "/");
		File[] listFiles = dir.listFiles();
		if (listFiles != null) {
			for (File file : listFiles) {
				FileInStore newFileInStore = new FileInStore();

				newFileInStore.setFileName(PasswordManager
						.baseDeCryptString(file.getName()));
				newFileInStore.setFilePath(file.getAbsolutePath());
				newFileInStore.setFileSize(file.length() / 1000 + " kilobytes");
				newFileInStore.setCipheredFileName(file.getName());
				userFiles.add(newFileInStore);
			}
		}
		return userFiles;
	}

	public void openFileFromArchive(String filePath, String realFIleName) {

		// Create an instance of our stream source.
		StreamResource.StreamSource cipherResource = new MyCipherSource(
				filePath);

		// Create a resource that uses the stream source and give it a name.
		// The constructor will automatically register the resource in
		// the application.
		StreamResource cipherresource = new StreamResource(cipherResource,
				realFIleName, PersonalpasssaveApplication.getInstance());

		PersonalpasssaveApplication.getInstance().getMainWindow()
				.open(cipherresource, "_blank");
	}

	private CipherInputStream getCipherInputputStream(File file) {
		CipherInputStream in = null;
		try {
			Cipher cipher;
			SecretKey key;
			cipher = Cipher.getInstance(PersonalPassConstants.ENCRYPTION_MODE);
			key = new SecretKeySpec(
					PersonalPassConstants.MAIN_SCHLUSSEL.getBytes(),
					PersonalPassConstants.ENCRYPTION_MODE);
			cipher.init(Cipher.DECRYPT_MODE, key);
			in = new CipherInputStream(new FileInputStream(
					file.getAbsolutePath()), cipher);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return in;

	}

	class MyCipherSource implements StreamResource.StreamSource {

		private String filePath;

		public MyCipherSource(String filePath) {
			this.filePath = filePath;
		}

		@Override
		public InputStream getStream() {
			return getCipherInputputStream(new File(filePath));
		}

	}

}
