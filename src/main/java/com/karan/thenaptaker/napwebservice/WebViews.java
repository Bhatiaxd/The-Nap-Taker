package com.karan.thenaptaker.napwebservice;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;

import com.karan.thenaptaker.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class WebViews extends AppCompatActivity {
    private WebView site;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        site = (WebView) findViewById(R.id.showSite);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading  data Please Wait....");
        pDialog.setCancelable(false);
        new SimpleTask().execute(Site.site);
    }


    @SuppressWarnings("deprecation")
    /**
     * {@link SimpleTask} is an {@link AsyncTask} to open the given webpage and show a
     * dialog box in the mean time
     */
    private class SimpleTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar
            pDialog.show();
        }

        protected String doInBackground(String... urls)   {
            String result = "";
            try {

                HttpGet httpGet = new HttpGet(urls[0]);
                HttpClient client = new DefaultHttpClient();

                HttpResponse response = client.execute(httpGet);

                int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode == 200) {
                    InputStream inputStream = response.getEntity().getContent();
                    BufferedReader reader = new BufferedReader
                            (new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result += line;
                    }
                }

            } catch (ClientProtocolException e) {
                Log.e("CPE",e.getMessage());
            } catch (IOException e) {
                Log.e("IO",e.getMessage());
            }catch (Exception e) {
                Log.e("E",e.getMessage());
            }
            return result;
        }

        protected void onPostExecute(String result)  {
            // Dismiss ProgressBar
            updateWebView(result);
            pDialog.dismiss();
        }
    }


    /**
     * updateWebView loads data to {@link WebView}
     */
    private void updateWebView(String result) {
        site.getSettings().setJavaScriptEnabled(true);
        site.loadData(result, "text/html; charset=utf-8", "utf-8");
    }







}


