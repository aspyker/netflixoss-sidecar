package com.ibm.ibmcsf.simplesidecar.checks;

import java.util.concurrent.ThreadFactory;

public class DaemonNamedThreadFactory implements ThreadFactory {
	private String threadName;
	
	public DaemonNamedThreadFactory(String threadName) {
		this.threadName = threadName;
	}
	
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, threadName);
        thread.setDaemon(true);
        return thread;
    }
}
