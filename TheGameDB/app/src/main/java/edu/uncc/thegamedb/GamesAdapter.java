package edu.uncc.thegamedb;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by NANDU on 20-02-2017.
 */

public class GamesAdapter extends ArrayAdapter<Game> {
    List<Game> mData;
    Context mContext;
    int mResource;

    public GamesAdapter(Context context, int resource, List<Game> objects) {
        super(context, resource, objects);
        this.mContext=context;
        this.mResource=resource;
        this.mData=objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(mResource,parent,false);
        }
        Game game = mData.get(position);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imgGameIcon);
        TextView textView = (TextView)convertView.findViewById(R.id.tvGameDetails);


        String imageBaseUrl =IsNullorEmpty(game.getBaseImageUrl())?"":game.getBaseImageUrl().trim();
        String imageURL="";

        if(!IsNullorEmpty(imageBaseUrl)){
            if(!IsNullorEmpty(game.getClearlogo()))
                imageURL=imageBaseUrl+game.getClearlogo().trim();
            else if(!IsNullorEmpty(game.getImagePath()))
                imageURL=imageBaseUrl+game.getImagePath().trim();
            else if(!IsNullorEmpty(game.getBoxart()))
                imageURL=imageBaseUrl+game.getBoxart().trim();
        }


        if(!IsNullorEmpty(imageURL)){
            Picasso.with(convertView.getContext()).load(imageURL)
                    .placeholder(R.mipmap.user_placeholder)
                    .error(R.mipmap.user_placeholder_error)
                    .into(imageView);

        }

        StringBuilder sb = new StringBuilder();
        sb.append(game.getGameTitle());
        sb.append(". ");

        if(!IsNullorEmpty(game.getReleaseDate()) && game.getReleaseDate().length()>3)
        {
            sb.append("Released in ");
            sb.append(game.getReleaseDate().substring(game.getReleaseDate().length()-4));
            sb.append(". ");
        }
        sb.append("Platform: ");
        sb.append(game.getPlatform());
        textView.setText(sb.toString());

        return convertView;
    }

    private Boolean IsNullorEmpty(String str)
    {
        return (str == null || str.isEmpty());
    }
}
