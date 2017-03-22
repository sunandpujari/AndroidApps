package edu.uncc.shoppingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NANDU on 20-02-2017.
 */

public class ItemsAdapter extends ArrayAdapter<Item> {
    ArrayList<Item> mData;
    Context mContext;
    int mResource;

    public ItemsAdapter(Context context, int resource, ArrayList<Item> objects) {
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
        final Item item = mData.get(position);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.img_product_list);
        TextView textViewName = (TextView)convertView.findViewById(R.id.txt_title_list);
        TextView textViewPrice = (TextView)convertView.findViewById(R.id.txt_price_list);

        String imageURL =IsNullorEmpty(item.getImageUrl())?"":item.getImageUrl().trim();

        if(!IsNullorEmpty(imageURL)){
            Picasso.with(convertView.getContext()).load(imageURL)
                    //.placeholder(R.mipmap.user_placeholder)
                    //.error(R.mipmap.user_placeholder_error)
                    .into(imageView);

        }

        textViewName.setText(item.getName());
        textViewPrice.setText("Price: "+ item.getSale_price()+"$");
        return convertView;
    }

    private Boolean IsNullorEmpty(String str)
    {
        return (str == null || str.isEmpty());
    }

}
