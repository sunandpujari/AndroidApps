package edu.uncc.tedradiohourpodcast;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by NANDU on 08-03-2017.
 */

public class PodsAdapter extends RecyclerView.Adapter<PodsAdapter.PodsHolder> {

    private List<Pod> podsList;
    ICallBack activity;
    String flag="1";

    public PodsAdapter(List<Pod> podsList,String flag, Context context){
        this.podsList=podsList;
        this.flag=flag;
        this.activity = (ICallBack)context;
    }

    @Override
    public PodsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(flag.equals("1")) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pod_list_item, parent, false);
            return  new PodsHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pod_card_item, parent, false);
            return  new PodsHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final PodsHolder holder, final int position) {

        final Pod pod = podsList.get(position);

        if(flag.equals("1")) {
            holder.title.setText(pod.getTitle());

            if (pod.getPublicationDate() != null) {

                SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy");
                String date = format.format(pod.getPublicationDate());

                holder.date.setText("posted: " + date);
            }

            String imageURL = IsNullorEmpty(pod.getImageURL()) ? "" : pod.getImageURL().trim();

            if (!IsNullorEmpty(imageURL)) {
                Picasso.with(holder.container.getContext()).load(imageURL)
                        // .placeholder(R.mipmap.user_placeholder)
                        //.error(R.mipmap.user_placeholder_error)
                        .into(holder.imageView);

            }
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(holder.container.getContext(),"on click called",Toast.LENGTH_SHORT).show();
                    activity.playOrPauseMedia(pod);
                }
            });
        }
        else
        {
            holder.titlecardview.setText(pod.getTitle());
            holder.imagecardview.setTag(pod.getImageURL());

            holder.imagecardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.playOrPauseMedia(pod);
                }
            });

            String imageURL = IsNullorEmpty(pod.getImageURL()) ? "" : pod.getImageURL().trim();

            if (!IsNullorEmpty(imageURL)) {
                Picasso.with(holder.container.getContext()).load(imageURL)
                        .into(holder.imagecardview);

            }
        }


    }

    @Override
    public int getItemCount() {
        return podsList.size();
    }

    class PodsHolder extends RecyclerView.ViewHolder{
        private TextView title,date;
        private ImageView imageView;
        private View container;
        private ImageButton btnPlay;
        private final Context context;

        public ImageView imagecardview;
        public TextView titlecardview;
        public LinearLayout ll;

        public PodsHolder(View itemView){
            super(itemView);
            if(flag.equals("1")) {
                title = (TextView) itemView.findViewById(R.id.txt_item_title);
                date = (TextView) itemView.findViewById(R.id.txt_item_date);
                imageView = (ImageView) itemView.findViewById(R.id.img_item_icon);
                btnPlay = (ImageButton) itemView.findViewById(R.id.btn_item_play);
                container = itemView.findViewById(R.id.cont_item_root);

            }
            else{
                titlecardview=(TextView)itemView.findViewById(R.id.txt_card_title);
                imagecardview=(ImageView)itemView.findViewById(R.id.img_card_pod);
                container = itemView.findViewById(R.id.cont_card_root);

            }
            context = itemView.getContext();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //activity.removelay();
                    Intent intent = new Intent(v.getContext() , PlayActivity.class);
                    intent.putExtra(MainActivity.POD_ITEM,podsList.get(getAdapterPosition()));
                    v.getContext().startActivity(intent);

                }
            });
        }

    }

    public interface ICallBack{
        void removelay();
        void playOrPauseMedia(Pod pod);
    }

    private Boolean IsNullorEmpty(String str)
    {
        return (str == null || str.isEmpty());
    }
}
