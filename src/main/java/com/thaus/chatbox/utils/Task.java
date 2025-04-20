package com.thaus.chatbox.utils;

import javafx.application.Platform;

public class Task {
	public static void runTask(Runnable task) {
		new Thread(task).start();
	}

	public static void runUITask(Runnable task) {
		if (Platform.isFxApplicationThread()) {
			task.run();
		} else {
			Platform.runLater(task);
		}
	}
}
