package app.util;

import java.lang.Thread.UncaughtExceptionHandler;

import app.common.ErrorHandler;

public class UncaughtHandler implements UncaughtExceptionHandler {

    public static void initialize() {
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtHandler());
	}

	public void uncaughtException(Thread t, Throwable e) {
		System.err.println("Exception in thread \"" + t.getName() + "\" ");
		System.err.println(ErrorHandler.toString(e));
	}

}
