package edu.uncc.moviedictionary;


/*
a.Assignment #. Homework 2
b.File Name.  HW2_Group09.zip
c.Name. Sunand Kumar Pujari, Sai Yesaswy Mylavarapu
*/
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MoviesByYearActivity extends AppCompatActivity {

    ArrayList<Movie> movies;
    int counter =0,size;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_by_year);
        if(getIntent().getExtras()!=null){
            movies = (ArrayList<Movie>)getIntent().getExtras().<Movie>getParcelableArrayList(MainActivity.MOVIES);
            size = movies.size();
            Log.d("demo",movies.size()+"");
            Collections.sort(movies,new CustomComparator());

            FillMovieDetails(movies.get(0));
        }

        findViewById(R.id.btnFirst).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter=0;
                FillMovieDetails(movies.get(counter));

            }
        });
        findViewById(R.id.btnLast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter=size-1;
                FillMovieDetails(movies.get(counter));

            }
        });
        findViewById(R.id.btnPrevious).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(counter>0){
                    counter-=1;
                    FillMovieDetails(movies.get(counter));
                }

            }
        });
        findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(counter<size-1){
                    counter+=1;
                    FillMovieDetails(movies.get(counter));
                }
            }
        });
        findViewById(R.id.btnFinish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public class CustomComparator implements Comparator<Movie> {
        @Override
        public int compare(Movie o1, Movie o2) {
            return (o1.getYear()==o2.getYear())?0:(o1.getYear()>o2.getYear())?1:-1 ;
        }
    }

    private void FillMovieDetails(Movie movie){
        TextView title = (TextView)findViewById(R.id.tvTitleValue);
        TextView description = (TextView)findViewById(R.id.etDescriptionValue);
        TextView genre = (TextView)findViewById(R.id.tvGenreValue);
        TextView rating = (TextView)findViewById(R.id.tvRatingValue);
        TextView year = (TextView)findViewById(R.id.tvYearValue);
        TextView imdb = (TextView)findViewById(R.id.tvIMDBValue);

        try{
            title.setText(movie.getName());
            description.setText(movie.getDescription());
            genre.setText(movie.getGenre().toString());
            rating.setText(movie.getRating() +" / 5");
            year.setText(movie.getYear()+"");
            imdb.setText(movie.getImdb());
        }
        catch (Exception ex){
            throw ex;
        }
    }
}
