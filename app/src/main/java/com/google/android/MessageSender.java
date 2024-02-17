package com.google.android;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MessageSender extends AsyncTask<String, Void, Void> {
	private static final String TAG = "MessageSender";
	private static final String WEBHOOK_URL = "INSERT-WEBHOOK-URL-FROM-YOUR-CHANNEL-BOT";

	@SuppressLint("LogNotSignal") @Override
	protected Void doInBackground(String... strings) {
		String log = strings[0];

		try {
			URL url = new URL(WEBHOOK_URL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);

			JSONObject messageJSON = new JSONObject();
			messageJSON.put("content", "```\n" + log + "\n```");

			OutputStream os = connection.getOutputStream();
			os.write(messageJSON.toString().getBytes(StandardCharsets.UTF_8));
			os.flush();
			os.close();

			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				Log.d(TAG, "Message sent to Discord channel.");
			} else {
				Log.e(TAG, "Failed to send message to Discord channel. Response code: " + responseCode);
			}
			connection.disconnect();
		} catch (JSONException e) {
			Log.e(TAG, "Failed to create JSON object: " + e.getMessage());
		} catch (Exception e) {
			Log.e(TAG, "Failed to send message to Discord channel: " + e.getMessage());
		}
		return null;
	}
}
