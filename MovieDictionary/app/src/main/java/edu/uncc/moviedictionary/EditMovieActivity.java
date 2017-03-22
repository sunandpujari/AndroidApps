package edu.uncc.moviedictionary;

/*
a.Assignment #. Homework 2
b.File Name.  HW2_Group09.zip
c.Name. Sunand Kumar Pujari, Sai Yesaswy Mylavarapu
*/
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Calendar;

public class EditMovieActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movie);

        final SeekBar rating = (SeekBar) findViewById(R.id.sbRating);
        rating.setLeft(0);
        rating.setMax(5);
        rating.incrementProgressBy(1);
        rating.setProgress(0);

        final TextView ratingDisplay = (TextView) findViewById(R.id.tvRatingDisplay);

        rating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ratingDisplay.setText(progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final Spinner genre = (Spinner) findViewById(R.id.spGenre);
        genre.setAdapter(new ArrayAdapter<Genre>(this, android.R.layout.simple_spinner_item, Genre.values()));
        final EditText name = (EditText) findViewById(R.id.etName);
        final EditText description = (EditText) findViewById(R.id.etDescription);
        final EditText year = (EditText) findViewById(R.id.etYear);
        final EditText imdb = (EditText) findViewById(R.id.etIMDB);

        if (getIntent().getExtras() != null) {
            Movie movie = getIntent().getExtras().getParcelable(MainActivity.MOVIE);

            name.setText(movie.getName());
            description.setText(movie.getDescription());
            genre.setSelection(getIndex(genre, movie.getGenre().toString()));
            rating.setProgress(movie.rating);
            year.setText(movie.getYear() + "");
            imdb.setText(movie.getImdb());

        }

        Button saveChanges = (Button) findViewById(R.id.btnSaveMovie);

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Movie movie;

                if (isValidForm()) {
                    movie = new Movie(name.getText().toString(),
                            description.getText().toString(),
                            Genre.valueOf(genre.getSelectedItem().toString()),
                            rating.getProgress(),
                            Integer.parseInt(year.getText().toString()),
                            imdb.getText().toString()
                    );

                    Intent intent = new Intent();
                    intent.putExtra(MainActivity.MOVIE, movie);
                    setResult(RESULT_OK, intent);

                    finish();
                } else {
                    Toast message = Toast.makeText(getApplicationContext(), "Invalid Input", Toast.LENGTH_SHORT);
                    message.show();
                }

            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private boolean isValidForm() {
        boolean isValid = true;

        EditText name = (EditText) findViewById(R.id.etName);
        EditText description = (EditText) findViewById(R.id.etDescription);
        EditText year = (EditText) findViewById(R.id.etYear);
        EditText imdb = (EditText) findViewById(R.id.etIMDB);
        Spinner genre = (Spinner) findViewById(R.id.spGenre);

        if (isEmptyOrNull(name.getText().toString()) ||
                isEmptyOrNull(description.getText().toString()) ||
                isEmptyOrNull(year.getText().toString()) ||
                isEmptyOrNull(imdb.getText().toString()) ||
                isEmptyOrNull(genre.getSelectedItem().toString()))
            isValid = false;


        if(Integer.parseInt(year.getText().toString()) > Calendar.getInstance().get(Calendar.YEAR) || Integer.parseInt(year.getText().toString())<999)
            isValid=false;

        return isValid;
    }

    private boolean isEmptyOrNull(String str) {
        return (str == null || str.isEmpty());
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("EditMovie Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private int getIndex(Spinner spinner, String myString){
        int index = 0;
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equals(myString)){

                index = i;
            }
        }
        return index;
    }
}
