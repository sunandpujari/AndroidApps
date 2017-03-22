package edu.uncc.itunestoppaidapps;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.util.ArraySet;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Set;

import static android.app.AlertDialog.THEME_HOLO_LIGHT;
import static android.content.Context.MODE_PRIVATE;
import static android.os.ParcelFileDescriptor.MODE_WORLD_READABLE;

/**
 * Created by NANDU on 20-02-2017.
 */

public class AppsAdapter extends ArrayAdapter<App> {
    List<App> mData;
    Context mContext;
    int mResource;

    public AppsAdapter(Context context, int resource, List<App> objects) {
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
        final App app = mData.get(position);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imgAppIcon);
        TextView textViewDetails = (TextView)convertView.findViewById(R.id.tvGameName);
        TextView textViewPrice = (TextView)convertView.findViewById(R.id.tvPrice);
        final ImageButton imageButton = (ImageButton)convertView.findViewById(R.id.btnFavorite);

        app.getName();
        String imageURL =IsNullorEmpty(app.getThumbnailURL())?"":app.getThumbnailURL().trim();

        if(!IsNullorEmpty(imageURL)){
            Picasso.with(convertView.getContext()).load(imageURL)
                    .placeholder(R.mipmap.user_placeholder)
                    .error(R.mipmap.user_placeholder_error)
                    .into(imageView);

        }

        textViewDetails.setText(app.getName());
        textViewPrice.setText(app.getCurrencyType() +" "+ app.getPrice());

        final SharedPreferences myPrefs = getContext().getSharedPreferences("edu.uncc.itunestoppaidapps", MODE_PRIVATE);
        final SharedPreferences.Editor prefsEditor = myPrefs.edit();
        final Set<String> nullSet = new ArraySet<String>();
        final Set<String> set = myPrefs.getStringSet("app_favorites",nullSet);

        if(set.contains(app.getName()))
            imageButton.setBackgroundResource(R.drawable.black_star);
        else
            imageButton.setBackgroundResource(R.drawable.white_star);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(set.contains(app.getName())) {

                    AlertDialog dialog = new AlertDialog.Builder(getContext())
                            .setTitle(Html.fromHtml("<font color='#00bcd4'>Add to Favorites</font>"))
                            .setMessage("Are you sure that you want to remove this App from favorites?")
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    set.remove(app.getName());
                                    imageButton.setBackgroundResource(R.drawable.white_star);
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            }).create();
                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            //
                            Button positiveButton = ((AlertDialog) dialog)
                                    .getButton(AlertDialog.BUTTON_POSITIVE);
                            positiveButton.setTextColor(getContext().getResources().getColor(R.color.colorBlack));

                            Button negativeButton = ((AlertDialog) dialog)
                                    .getButton(AlertDialog.BUTTON_NEGATIVE);
                            negativeButton.setTextColor(getContext().getResources().getColor(R.color.colorBlack));
                        }
                    });
                    dialog.show();

                }
                else {

                    AlertDialog dialog = new AlertDialog.Builder(getContext())
                            .setTitle(Html.fromHtml("<font color='#00bcd4'>Add to Favorites</font>"))
                            .setMessage("Are you sure that you want to add this App from favorites?")
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    set.add(app.getName());
                                    imageButton.setBackgroundResource(R.drawable.black_star);
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            }).create();
                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            //
                            Button positiveButton = ((AlertDialog) dialog)
                                    .getButton(AlertDialog.BUTTON_POSITIVE);
                            positiveButton.setTextColor(getContext().getResources().getColor(R.color.colorBlack));

                            Button negativeButton = ((AlertDialog) dialog)
                                    .getButton(AlertDialog.BUTTON_NEGATIVE);
                            negativeButton.setTextColor(getContext().getResources().getColor(R.color.colorBlack));
                        }
                    });
                    dialog.show();
                }
                prefsEditor.putStringSet("app_favorites",set);
                prefsEditor.apply();
            }
        });

        return convertView;
    }

    private Boolean IsNullorEmpty(String str)
    {
        return (str == null || str.isEmpty());
    }

}
