package com.lp.neweditor.common;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

import com.lp.client.util.NamedThreadFactory;
import com.lp.neweditor.ui.editor.HvBlockEditor;

/**
 * Stellt einen Executor f&uuml;r Hintergrundtasks des {@link HvBlockEditor}s
 * bereit
 * 
 * @author Alexander Daum
 *
 */
public class EditorBackgroundScheduler {
	private static ScheduledExecutorService executor;

	public static ScheduledExecutorService getExecutor() {
		if (executor == null) {
			executor = new ScheduledThreadPoolExecutor(0, new NamedThreadFactory("EditorBackground"));
		}
		return executor;
	}
	
}
