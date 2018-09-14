package com.gwg.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.File;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class FileMonitor implements Runnable {

	/**
	 * 监听器
	 */
	public interface Listener {
		public void onModifyFile(File file);
	}

	/**
	 * 内部监听容器
	 */
	private static class InnerListeners {
		public final String filePath;
		public final CopyOnWriteArrayList<Listener> listeners = Lists.newCopyOnWriteArrayList();
		public long mdate;

		public InnerListeners(String filePath) {

			this.filePath = filePath;
			File file = new File(filePath);
			mdate = file.exists() ? file.lastModified() : -1;
		}

		public void add(Listener listener) {
			if (listener != null) {
				listeners.add(listener);
			}
		}

		public void remove(Listener listener) {
			if (listener != null) {
				listeners.remove(listener);
			}
		}

		public boolean isEmpty() {
			return listeners.isEmpty();
		}

		public void checkModify() {
			File file = new File(filePath);
			long new_mdate = file.exists() ? file.lastModified() : -1;
			if (new_mdate != mdate) {
				for (Listener listener : listeners) {
					listener.onModifyFile(file);
				}
				mdate = new_mdate;
			}
		}
	}

	/**
	 * 文件监听器
	 */

	private final ConcurrentMap<String, InnerListeners> listenersMap = Maps.newConcurrentMap();

	public void listen(String filePath, Listener listener) {
		InnerListeners listeners = listenersMap.get(filePath);
		if (listeners == null) {
			listeners = new InnerListeners(filePath);
			listenersMap.put(filePath, listeners);
		}
		listeners.add(listener);
	}

	public void unListen(String filePath, Listener listener) {
		InnerListeners listeners = listenersMap.get(filePath);
		if (listeners != null) {
			listeners.remove(listener);
			if (listeners.isEmpty()) {
				listenersMap.remove(filePath);
			}
		}
	}

	@Override
	public void run() {
		for (InnerListeners listeners : listenersMap.values()) {
			listeners.checkModify();
		}
	}

}
