package edu.uncc.moviedictionary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/*
a.Assignment #. Homework 2
b.File Name.  HW2_Group09.zip
c.Name. Sunand Kumar Pujari, Sai Yesaswy Mylavarapu
*/

public class MainActivity extends Activity {
    public static final String MOVIE = "movie";
    public static final String MOVIES = "movies";
    public static final int REQ_CODE_ADD = 1010;
    public static final int REQ_CODE_EDIT = 1011;
    private int Edited_Movie_Index;
    ArrayList<Movie> Movies = new ArrayList<Movie>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnSaveMovie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , AddMovieActivity.class);
                startActivityForResult(intent,REQ_CODE_ADD);

            }
        });

        findViewById(R.id.btnEditMovie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> movieList = new ArrayList<String>();
                for(Movie movie : Movies){
                    movieList.add(movie.getName());
                }

                final CharSequence[] movies = movieList.toArray(new CharSequence[movieList.size()]);

                if(Movies.size()==0)
                    Toast.makeText(getApplicationContext(),"There are no movies in database to edit.",Toast.LENGTH_SHORT).show();
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Pick a movie").
                            setItems( movies ,new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int item){
                                    Movie selectedMovie=null;
                                    for(Movie movie : Movies){
                                        if(movie.getName()==movies[item]) {
                                            selectedMovie = movie;
                                            Edited_Movie_Index=item;
                                            break;
                                        }
                                    }

                                    Intent intent = new Intent(MainActivity.this , EditMovieActivity.class);
                                    intent.putExtra(MainActivity.MOVIE,selectedMovie);
                                    startActivityForResult(intent,REQ_CODE_EDIT);

                                }
                            });

                    AlertDialog editAlert = builder.create();
                    editAlert.show();
                }
            }
        });
        findViewById(R.id.btnDeleteMovie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> movieList = new ArrayList<String>();
                for(Movie movie : Movies){
                    movieList.add(movie.getName());
                }

                final CharSequence[] movies = movieList.toArray(new CharSequence[movieList.size()]);

                if(Movies.size()==0)
                    Toast.makeText(getApplicationContext(),"There are no movies in database to Delete.",Toast.LENGTH_SHORT).show();
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Pick a movie").
                            setItems( movies ,new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int item){
                                    Movie selectedMovie=null;
                                    for(Movie movie : Movies){
                                        if(movie.getName()==movies[item]) {
                                            selectedMovie = movie;
                                            break;
                                        }
                                    }

                                     Movies.remove(selectedMovie);

                                    Toast.makeText(getApplicationContext(),"Movie "+selectedMovie.getName()+ " is Deleted.",Toast.LENGTH_SHORT).show();

                                }
                            });

                    AlertDialog editAlert = builder.create();
                    editAlert.show();
                }
            }
        });

        findViewById(R.id.btnListbyYear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Movies.size()==0)
                    Toast.makeText(getApplicationContext(),"There are no movies in database to show.",Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent("edu.uncc.moviedictionary.intent.action.SORTMOVIESBYYEAR");
                    intent.putParcelableArrayListExtra(MainActivity.MOVIES,Movies);
                    startActivity(intent);
                }
            }
        });

        findViewById(R.id.btnListbyRating).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Movies.size()==0)
                    Toast.makeText(getApplicationContext(),"There are no movies in database to show.",Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent("edu.uncc.moviedictionary.intent.action.SORTMOVIESBYRATING");
                    intent.putParcelableArrayListExtra(MainActivity.MOVIES,Movies);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQ_CODE_ADD){
            if(resultCode==RESULT_OK){
                Movies.add((Movie)data.getExtras().getParcelable(MainActivity.MOVIE));
            }
        }
        else if(requestCode==REQ_CODE_EDIT && resultCode==RESULT_OK){
            Movie editedMovie = (Movie)data.getExtras().getParcelable(MainActivity.MOVIE);

            Movies.remove(Edited_Movie_Index);
            Movies.add(editedMovie);

        }
    }

}
