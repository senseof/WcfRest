package exluo.wcfrest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HttpRequestTask().execute();
                // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    // TODO : Add
    private class HttpRequestTask extends AsyncTask<Void, Void, Inventory> {


        public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);
        }

        private String downloadUrl(String urlString) throws IOException {
            InputStream is = null;
            int len = 30000;

            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);  // milliseconds
                conn.setConnectTimeout(15000);  // milliseconds
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                conn.connect();
                int response = conn.getResponseCode();
                is = conn.getInputStream();
                String contentAsString = readIt(is, len);
                return contentAsString;
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }

        // TODO : pull or fetch
        // TODO : Stash
        // TODO : Unstash
        // TODO : Commit & push
        final String SERVICE_URL = "http://192.168.1.64/WcfService/InventoryService.svc/inventory/567";

        @Override
        protected Inventory doInBackground(Void... params) {
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                try {
                    String result = downloadUrl(SERVICE_URL); // "http://feeds.feedburner.com/DailyHopeWithRickWarrenPodcast?format=xml");
                    Log.e(MainActivity.class.getSimpleName(), result);
                } catch (Exception e) {
                    Log.e(MainActivity.class.getSimpleName(), e.getMessage(), e);
                }
            }

            /*
            try {

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new SimpleXmlHttpMessageConverter());
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Inventory result = restTemplate.getForObject(url, Inventory.class);
                // Log.e(MainActivity.class.getSimpleName(), result);
                return result;
            } catch (Exception e) {
                Log.e(MainActivity.class.getSimpleName(), e.getMessage(), e);
            }
            */

            return null;
        }

        @Override
        protected void onPostExecute(Inventory inventory) {
            // Toast.makeText(MainActivity.this, inventory.getBarcode(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
