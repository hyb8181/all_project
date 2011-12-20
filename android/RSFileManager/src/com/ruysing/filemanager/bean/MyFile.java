package com.ruysing.filemanager.bean;

import java.io.File;

import android.graphics.Bitmap;

public class MyFile {

	private File file;
	private Bitmap icon;
	private String aliasName;
	private String aliasPath;
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public Bitmap getIcon() {
		return icon;
	}
	public void setIcon(Bitmap icon) {
		this.icon = icon;
	}
	public String getAliasName() {
		return aliasName;
	}
	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
	public String getAliasPath() {
		return aliasPath;
	}
	public void setAliasPath(String aliasPath) {
		this.aliasPath = aliasPath;
	}
	
}
