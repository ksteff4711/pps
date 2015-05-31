package org.kingsteff.passwordsave;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.SucceededEvent;

public class ImportUploadHandler implements Upload.FailedListener,
Upload.SucceededListener, Upload.Receiver {

	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
		try{
         String path =  PersonalPassConstants.MAINDIR+"/temp/" ;
         File dir = new File(path);
         dir.mkdirs();
         File[] listFiles = dir.listFiles();
         if((listFiles!=null)&&(listFiles.length>0)){
        	 for(File cur:listFiles){
        		 cur.delete();
        	 }
         }
         
         File file  = null;
         file = new File(path+filename);
         file.createNewFile();
         return new FileOutputStream(file);
         }catch(Exception e){
             e.getMessage();
         }
         return null;
	}

	@Override
	public void uploadSucceeded(SucceededEvent event) {
		String path =  PersonalPassConstants.MAINDIR+"/temp/" ;
		File fileJMiner = new File(path+event.getFilename());
		PasswordManager manager = PersonalpasssaveApplication.getInstance().getPasswordManager();
		manager.importData(fileJMiner);
	}

	@Override
	public void uploadFailed(FailedEvent event) {
		// TODO Auto-generated method stub
		
	}

}
