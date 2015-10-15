package com.example.filechooselistadapter;


enum FileType {
	FILE , DIRECTORY;
}

// =========================
// Model
// =========================
class FileInfo {
	private FileType fileType;
	private String fileName;
	private String filePath;

	public FileInfo(String filePath, String fileName, boolean isDirectory) {
		this.filePath = filePath;
		this.fileName = fileName;
		fileType = isDirectory ? FileType.DIRECTORY : FileType.FILE;
	}

	public boolean isDirectory(){
		if(fileType == FileType.DIRECTORY)
			return true ;
		else
			return false ;
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

	@Override
	public String toString() {
		return "FileInfo [fileType=" + fileType + ", fileName=" + fileName
				+ ", filePath=" + filePath + "]";
	}
}