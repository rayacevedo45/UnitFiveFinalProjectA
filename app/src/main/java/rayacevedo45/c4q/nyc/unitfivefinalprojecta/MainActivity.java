package rayacevedo45.c4q.nyc.unitfivefinalprojecta;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    String artist;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.edid);
        listView = (ListView) findViewById(R.id.lvid);



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

    public void search(View v) {
        artist = editText.getText().toString();
        AsyncTime getDailyHoroscope = new AsyncTime();
        getDailyHoroscope.execute();
    }

    public class AsyncTime extends AsyncTask<Void, Void, ArrayList<String>> {
        @Override
        public ArrayList<String> doInBackground(Void... voids) {
//            Date date = new Date();
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
//            String sdfS = (sdf.format(date));
//            Log.d("!!!", sdfS);
//            String month2 = sdfS.substring(5, 7);
//            String day2 = sdfS.substring(8, 10);


            try {
                //String APISite = "http://widgets.fabulously40.com/horoscope.json?sign=aquarius&date=2010-" + month2 + "-" + day2;
                //String APIsite =  "http://widgets.fabulously40.com/horoscope.json?sign=aquarius&date=2010-" + "12" + "-" + "11";

                String APIsite = "https://itunes.apple.com/search?media=music&term=" + artist;

                URL url = new URL(APIsite);
                Log.d("|||", APIsite);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //Log.d("@@@", APIsite);
                connection.setRequestMethod("GET");
                //Log.d("@@@", APIsite);
                connection.connect();
                ///Log.d("@@@", APIsite);



                String Json = readStream(connection.getInputStream());
                Log.d("@@@", APIsite);
                Log.d("+++", Json);
                JSONObject allofit = new JSONObject(Json);

                JSONArray results = allofit.getJSONArray("results");
                ArrayList<String>songs = new ArrayList<>();
                Log.d("size", ""+results.length());
                for (int i = 0; i < results.length(); i++){
                    Log.d("test", ""+i);
                    JSONObject song = (JSONObject) results.get(i);
                    String trackName = song.getString("trackName");
                    Log.d ("???", trackName);
                    songs.add(trackName);
                }
               // JSONObject firstsong = results.getJSONObject(0);
                //String dailyHoroscope = firstsong.getString("trackName");
                //Log.d("^^^", dailyHoroscope);


                return songs;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public void onPostExecute(ArrayList arrayList) {
            //String q = s.replace("&apos;", "\'");
            //textView.setText(s);
            //ListAdapter arrayAdapter = new ArrayAdapter<String>(context, resource, collection);
            listView.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, arrayList));

        }

        private String readStream(InputStream in) throws IOException {
            char[] buffer = new char[1024 * 4];
            InputStreamReader reader = new InputStreamReader(in, "UTF8");
            StringWriter writer = new StringWriter();
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
            return writer.toString();

        }
    }
}
