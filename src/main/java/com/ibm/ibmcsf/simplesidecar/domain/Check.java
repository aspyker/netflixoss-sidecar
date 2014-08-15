package com.ibm.ibmcsf.simplesidecar.domain;

public class Check {
	String id;
	String description;
	int interval;
	String scriptToRun;
	String workingDir;
	String classToCall;
	StatusType lastStatus;
	long lastStatusRepeated;
	
	public enum StatusType { UNKNOWN, HEALTHY, WARN, UNHEALTHY };
	
	public Check(String id, String description, int interval, String scriptToRun, String workingDir, String classToCall) {
		super();
		this.id = id;
		this.description = description;
		this.interval = interval;
		this.scriptToRun = scriptToRun;
		this.workingDir = workingDir;
		this.classToCall = classToCall;
		this.lastStatus = StatusType.UNKNOWN;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getInterval() {
		return interval;
	}
	
	public void setInterval(int interval) {
		this.interval = interval;
	}
	
	public String getScriptToRun() {
		return scriptToRun;
	}
	
	public void setScriptToRun(String scriptToRun) {
		this.scriptToRun = scriptToRun;
	}
	
	public String getClassToCall() {
		return classToCall;
	}
	
	public void setClassToCall(String classToCall) {
		this.classToCall = classToCall;
	}

	public String getWorkingDir() {
		return workingDir;
	}

	public void setWorkingDir(String workingDir) {
		this.workingDir = workingDir;
	}
	
	public void addStatus(StatusType type) {
		if (type == lastStatus) {
			lastStatusRepeated++;
		}
		else {
			lastStatusRepeated = 1;
		}
		lastStatus = type;
	}
	
	public StatusType getCurrentStatus(StatusType type) {
		return lastStatus;
	}

	@Override
	public String toString() {
		return "Check [id=" + id + ", description=" + description
				+ ", interval=" + interval + ", scriptToRun=" + scriptToRun
				+ ", workingDir=" + workingDir
				+ ", classToCall=" + classToCall
				+ ", lastStatus=" + lastStatus.name() + "(" + lastStatusRepeated + " times)"
				+ "]";
	}
}
