package org.kingsteff.passwordsave;

public class FileInStore {

	private String fileName;
	private String filePath;
	private String fileSize;
	private String cipheredFileName;
	private String description;
	private String foldername;
	private String parentFoldername;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFoldername() {
		return foldername;
	}

	public void setFoldername(String foldername) {
		this.foldername = foldername;
	}

	public String getParentFoldername() {
		return parentFoldername;
	}

	public void setParentFoldername(String parentFoldername) {
		this.parentFoldername = parentFoldername;
	}

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
