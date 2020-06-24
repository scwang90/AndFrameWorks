package com.andframe.task;

public abstract class DataTask<T> extends HandlerTask {

	protected T data;

	@Override
	protected void onWorking() throws Exception {
		data = onLoadData();
	}

	@Override
	protected void onHandle() {
		onHandle(data);
	}

	protected abstract void onHandle(T data);
	protected abstract T onLoadData() throws Exception;
}
