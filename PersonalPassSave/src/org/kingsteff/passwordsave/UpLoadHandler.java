package org.kingsteff.passwordsave;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.SucceededEvent;

public class UpLoadHandler implements Upload.FailedListener,
		Upload.SucceededListener, Upload.Receiver {

	private File file;

	@Override
	public OutputStream receiveUpload(String filename, String MIMEType) {
		String cipheredFIleName;
		cipheredFIleName = PasswordManager.baseEncryptString(filename);
		CipherOutputStream fos = null; // Output stream to write to
		try {
			File dir = new File(
					PersonalPassConstants.MAINDIR
							+ PersonalPassConstants.FILESTORE_SUBDIR
							+ PasswordManager
									.getMd5Hash(PersonalpasssaveApplication
											.getInstance().getBaseController()
											.getCurrentUser()) + "/");
			dir.mkdirs();

			file = new File(
					PersonalPassConstants.MAINDIR
							+ PersonalPassConstants.FILESTORE_SUBDIR
							+ PasswordManager
									.getMd5Hash(PersonalpasssaveApplication
											.getInstance().getBaseController()
											.getCurrentUser()) + "/"
							+ cipheredFIleName);

			// Open the file for writing.
			fos = getCipherOutputStream(file);
		} catch (final Exception e) {
			// Error while opening the file. Not reported here.
			e.printStackTrace();
			return null;
		}

		return fos; // Return the output stream to write to
	}

	@Override
	public void uploadSucceeded(SucceededEvent event) {
		System.out.println(event.getFilename() + " of type '"
				+ event.getMIMEType() + "' uploaded.");
		PersonalpasssaveApplication.getInstance().getBaseController()
				.getWindow().removeFileUploadDialog();
		FileArchiveController archiveController = PersonalpasssaveApplication
				.getInstance().getFileArchiveController();

		String path = PersonalPassConstants.MAINDIR
				+ PersonalPassConstants.FILESTORE_SUBDIR
				+ PasswordManager.getMd5Hash(PersonalpasssaveApplication
						.getInstance().getBaseController().getCurrentUser());

		FileInStore fileInStore = new FileInStore();
		fileInStore.setCipheredFileName(PasswordManager.baseEncryptString(event
				.getFilename()));
		FileArchiveDialog fileArchiveDialog = PersonalpasssaveApplication
				.getInstance().getBaseController().getWindow().getMainview()
				.getArchiveTab().getFileArchiveDialog();
		fileInStore.setDescription(fileArchiveDialog.getDescriptionInput()
				.getValue() == null ? "" : fileArchiveDialog
				.getDescriptionInput().getValue().toString());
		fileInStore.setFilePath(path);
		fileInStore
				.setFoldername(fileArchiveDialog.getFolder().getValue() == null ? ""
						: fileArchiveDialog.getFolder().getValue().toString());
		fileInStore.setParentFoldername(fileArchiveDialog.getParentFolder()
				.getValue() == null ? "" : fileArchiveDialog.getParentFolder()
				.getValue().toString());
		archiveController.saveMetaDataForFile(fileInStore);
	}

	@Override
	public void uploadFailed(FailedEvent event) {
		// TODO Auto-generated method stub

	}

	private CipherOutputStream getCipherOutputStream(File file) {
		CipherOutputStream cip = null;
		try {
			File mkdir = new File(
					PersonalPassConstants.MAINDIR
							+ PasswordManager
									.getMd5Hash(PersonalpasssaveApplication
											.getInstance().getBaseController()
											.getCurrentUser())
							+ PersonalPassConstants.FILESTORE_SUBDIR);
			UserAndPasswords andPasswords = new UserAndPasswords();
			mkdir.mkdirs();
			SecretKey key;
			Cipher cipher;
			cipher = Cipher.getInstance(PersonalPassConstants.ENCRYPTION_MODE);
			key = new SecretKeySpec(
					PersonalPassConstants.MAIN_SCHLUSSEL.getBytes(),
					PersonalPassConstants.ENCRYPTION_MODE);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			cip = new CipherOutputStream(new FileOutputStream(
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
		return cip;
	}

}
