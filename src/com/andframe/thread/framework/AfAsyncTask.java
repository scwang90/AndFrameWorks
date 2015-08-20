package com.andframe.thread.framework;

import android.os.AsyncTask;

public class AfAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result>{

	@Override
	protected Result doInBackground(Params... params) {
		try {
			
		} catch (Throwable e) {
		}
		return null;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(Result result) {
		super.onPostExecute(result);
	}

	@Override
	protected void onProgressUpdate(Progress... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected void onCancelled(Result result) {
		super.onCancelled(result);
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

}
