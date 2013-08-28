package com.unict.geophoto;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

public class SendFragment extends Fragment {

	/**
	 * Callback interface through which the fragment will report the task's
	 * progress and results back to the Activity.
	 */
	static interface SendCallbacks {
		void onPostExecute();
	}

	private SendCallbacks sendCallbacks;
	private SendTask sendTask;
	private SendFragment fragment = this;

	/**
	 * Hold a reference to the parent Activity so we can report the task's
	 * results. The Android framework will pass us a reference to the newly
	 * created Activity after each configuration change.
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.d("GeoPhoto", "Fragment Activity attached");
		sendCallbacks = (SendCallbacks) activity;
	}

	/**
	 * This method will only be called once when the retained Fragment is first
	 * created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Retain this fragment across configuration changes.
		setRetainInstance(true);
	}

	public void start(String serverUrl, String xml) {
		// Create and execute the background task.
		sendTask = new SendTask();
		sendTask.execute(serverUrl, xml);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.d("GeoPhoto", "Fragment detached");
		// Set the callback to null so we don't accidentally leak the Activity
		// instance.
		sendCallbacks = null;
	}

	public void onPostExecute() {
		sendTask.cancel(false);
		sendTask = null;
	}

	private class SendTask extends AsyncTask<String, Void, Integer> {
		@Override
		protected Integer doInBackground(String... data) {
			try {
				return sendData(data[0], data[1]);
			} catch (IOException e) {
				Log.d("GeoPhoto", "IOException");
				return 400;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (sendCallbacks != null) {
				sendCallbacks.onPostExecute();
				String msg = "";
				if (result != null) {
					if (result == 200) {
						msg = "Data sent correctly";
					} else {
						msg = "Error " + result;
					}
				} else {
					msg = "An error occurred";
				}
				Toast.makeText((Context) sendCallbacks, msg, Toast.LENGTH_LONG)
						.show();
			}
			fragment.onPostExecute();
		}

		private int sendData(String serverUrl, String xml) throws IOException {
			OutputStreamWriter wr = null;
			try {
				URL url = new URL(serverUrl);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setReadTimeout(10000 /* milliseconds */);
				conn.setConnectTimeout(15000 /* milliseconds */);
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);

				// prepare the parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("data", xml));

				wr = new OutputStreamWriter(conn.getOutputStream());
				wr.write(getQuery(params));
				wr.close();
				conn.connect();
				int response = conn.getResponseCode();
				Log.d("GeoPhoto", "The response is: " + response);
				return response;
			} finally {
				if (wr != null) {
					wr.close();
				}
			}
		}

		private String getQuery(List<NameValuePair> params)
				throws UnsupportedEncodingException {
			StringBuilder result = new StringBuilder();
			boolean first = true;

			for (NameValuePair pair : params) {
				if (first)
					first = false;
				else
					result.append("&");

				result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
				result.append("=");
				result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
			}

			return result.toString();
		}
	}

}
