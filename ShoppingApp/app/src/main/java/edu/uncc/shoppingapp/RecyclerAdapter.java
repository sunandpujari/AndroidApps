package edu.uncc.shoppingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunand on 20/03/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{

    public List<Item> itemList;
    public Context mContext;

    OncallBackListener activity;

    public RecyclerAdapter(Context context, List<Item> itemList) {
        this.itemList = itemList;
        this.mContext = context;
        this.activity = (OncallBackListener)mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grid_layout, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Item item = itemList.get(position);
        holder.titleView.setText(item.getName());
        holder.priceView.setText("Price:"+ item.getSale_price()+"$");
        holder.discountview.setText("Discount: "+ item.getDiscount()+"%");
        Picasso.with(mContext).load(item.getImageUrl()).into(holder.imageView);

        ArrayList<Item> cartItems = activity.getCart();
        boolean disableButton=false;
        if(cartItems.size()>0){
            if(cartItems.contains(item))
                disableButton=true;
        }

        Button btnDelete = (Button)holder.btnAddToCart;
        if(disableButton){
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext.getApplicationContext(),"Already in cart",Toast.LENGTH_LONG);
                }
            });
        }
        else {
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.callback(item);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titleView, priceView,discountview;
        public ImageView imageView;
        public Button btnAddToCart;

        public MyViewHolder(View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.txt_title);
            priceView = (TextView) itemView.findViewById(R.id.txt_price);
            discountview = (TextView) itemView.findViewById(R.id.txt_discount);
            imageView = (ImageView) itemView.findViewById(R.id.img_product);
            btnAddToCart = (Button) itemView.findViewById(R.id.btn_addtocart);
        }
    }

    static public interface OncallBackListener{
        public void callback(Item app);
        public ArrayList<Item> getCart();
    }
}


