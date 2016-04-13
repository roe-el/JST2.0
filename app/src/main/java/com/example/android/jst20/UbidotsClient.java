package com.example.android.jst20;


import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class UbidotsClient {

private UbiListener listener;

public UbiListener getListener() {
    return listener;
}

public void setListener(UbiListener listener) {
    this.listener = listener;
}

public void handleUbidots(String varId, String apiKey, final UbiListener listener) {

    final List<Value> results = new ArrayList<>();

    OkHttpClient client = new OkHttpClient();
    Request req = new Request.Builder().addHeader("X-Auth-Token", apiKey)
            .url("http://things.ubidots.com/api/v1.6/variables/" + varId + "/values?page_size=1")
            .build();

    client.newCall(req).enqueue(new Callback() {
        @Override
        public void onFailure(Request request, IOException e) {
            Log.d("Data", "Network error");
            e.printStackTrace();
        }

        @Override
        public void onResponse(Response response) throws IOException {
            String body = response.body().string();
            Log.d("Data", body);

            try {
                JSONObject jObj = new JSONObject(body);
                JSONArray jRes = jObj.getJSONArray("results");
                for (int i = 0; i < jRes.length(); i++) {
                    JSONObject obj = jRes.getJSONObject(i);
                    Value val = new Value();
                    val.context = obj.getString("context");
                    val.timestamp = obj.getLong("timestamp");
                    val.value = (float) obj.getDouble("value");
                    results.add(val);
                }

                listener.onDataReady(results);

            } catch (JSONException jse) {
                jse.printStackTrace();
            }

        }
    });

}


protected interface UbiListener {
    void onDataReady(List<Value> result);
}

protected static class Value {
    float value;
    long timestamp;
    String context;
}
}