package org.fortune.doc.common.domain.result;

import org.fortune.doc.common.enums.DocResultCode;

public class Result {
	private int code;
	private String msg;
	private String action;
	private String filePath;

	public void buildSuccess() {
		this.setCode(DocResultCode.SUCCESS.getValue());
		this.setMsg("成功");
	}

	public void buildFailed() {
		this.setCode(DocResultCode.FAILED.getValue());
		this.setMsg("失败");
	}

	public void buildCustomMsg(String msg) {
		this.setMsg(msg);
	}

	public boolean isSuccess() {
		return this.code == DocResultCode.SUCCESS.getValue();
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return this.msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}