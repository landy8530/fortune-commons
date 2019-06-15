package org.fortune.doc.common.domain;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class Account {
	private String userName;
	private String password;
	private String rootPath;
	private int level;
	private int thumbHeight = 20;
	private int thumbWidth = 20;

	public Account() {
	}

	public Account(String userName, String password, String rootPath, int level) {
		this.userName = userName;
		this.password = password;
		this.rootPath = rootPath;

		if (!this.rootPath.endsWith(File.separator)) {
			this.rootPath += File.separator;
		}
		this.level = level;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRootPath() {
		return this.rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
		if (!this.rootPath.endsWith("/"))
			this.rootPath += "/";
	}

	@Override
	public String toString() {
		return "Account{" +
				"userName='" + userName + '\'' +
				", password='" + password + '\'' +
				", rootPath='" + rootPath + '\'' +
				", level=" + level +
				", thumbHeight=" + thumbHeight +
				", thumbWidth=" + thumbWidth +
				'}';
	}

	public int getLevel() {
		return this.level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean auth(String password) {
		return StringUtils.equals(this.password, password);
	}

	public int getThumbHeight() {
		return this.thumbHeight;
	}

	public void setThumbHeight(int thumbHeight) {
		this.thumbHeight = thumbHeight;
	}

	public int getThumbWidth() {
		return this.thumbWidth;
	}

	public void setThumbWidth(int thumbWidth) {
		this.thumbWidth = thumbWidth;
	}
}