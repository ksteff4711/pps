package org.kingsteff.passwordsave;

public class FileInStore {

	private String fileName;
	private String filePath;
	private String fileSize;
	private String cipheredFileName;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getCipheredFileName() {
		return cipheredFileName;
	}

	public void setCipheredFileName(String cipheredFileName) {
		this.cipheredFileName = cipheredFileName;
	}

}
