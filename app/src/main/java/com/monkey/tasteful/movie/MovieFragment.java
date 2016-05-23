package com.monkey.tasteful.movie;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieFragment extends Fragment {
    public static final String LOG_TAG = "MovieFragment";
    ListView listView;
    ArrayAdapter adapter;
    ArrayList<String> movies;

    public MovieFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_refresh) {
            FetchMovieTask task = new FetchMovieTask();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            task.execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        getMovies();
    }

    public void getMovies() {
        FetchMovieTask task = new FetchMovieTask();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        task.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        String[] movies = {"A Bigger Splash", "Captain America: Civil War", "Money Monster", "Independence day 2"};

        listView = (ListView) rootView.findViewById(R.id.listview);
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.item_text, new ArrayList<String>());
        listView.setAdapter(adapter);


        // ------- TOAST --------

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> AdapterView, View view, int position, long l) {
                String review = (String) adapter.getItem(position);
                Toast.makeText(getActivity(), review, Toast.LENGTH_SHORT).show();
            }
        });

        //------- TOAST ---------


        return rootView;
    }

    public class FetchMovieTask extends AsyncTask<Void, Void, String[]> {
        @Override
        protected String[] doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;


            // Will contain the raw JSON response as a string.
            String JSON = null;
            String[] result = null;
            String format = "json";

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast


                final String MOVIE_BASE_URL = "http://api.nytimes.com/svc/movies/v2/reviews/search.json?";
                final String APPID_PARAM = "api-key";
                final String OFFSET_PARAM = "offset";


                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(OFFSET_PARAM, "20")
                        .appendQueryParameter(APPID_PARAM, BuildConfig.MOVIE_REVIEWS_MAP_API_KEY).build();


                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "ANYTHING? Url: " + url);


                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    JSON = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    JSON = null;
                }


                JSON = buffer.toString();
                Log.v(LOG_TAG, "JSON" + JSON);
                result = getMovies(JSON);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                JSON = null;
                result = null;
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            return result;
        }



        private String[] getMovies(String movieJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String allResults = "results";
            final String movieTitle = "display_title";

            JSONObject display_title = new JSONObject(movieJsonStr);

            JSONArray ArrayOfMovies = display_title.getJSONArray(allResults);


            int Reviews = 20;
            String[] results = new String[Reviews];

            for(int i = 0 ; i < Reviews ; i++)
            {
                JSONObject review = ArrayOfMovies.getJSONObject(i);

                String name=review.getString(movieTitle);

                results[i] = name;
            }

            return results;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            movies = new ArrayList<>(Arrays.asList(strings));
            adapter.clear();
            adapter.addAll(movies);
        }
    }

}


