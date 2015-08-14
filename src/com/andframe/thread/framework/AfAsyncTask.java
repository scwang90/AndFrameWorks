package com.andframe.thread.framework;

import android.os.AsyncTask;

public class AfAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result>{

	@Override
	protected Result doInBackground(Params... params) {
		// TODO Auto-generated method stub
		try {
			
		} catch (Throwable e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(Result result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

	@Override
	protected void onProgressUpdate(Progress... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

	@Override
	protected void onCancelled(Result result) {
		// TODO Auto-generated method stub
		super.onCancelled(result);
	}

	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
	}

}
