package com.kmitl58070042.dnyopr.comparizon.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kmitl58070042.dnyopr.comparizon.R;
import com.kmitl58070042.dnyopr.comparizon.fragment.AddItemFragment;
import com.kmitl58070042.dnyopr.comparizon.fragment.SelectItemSide;
import com.kmitl58070042.dnyopr.comparizon.model.ItemInfo;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ItemInfoRecyclerAdapter extends RecyclerView.Adapter<ItemInfoRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<ItemInfo> data;
    private String selectedSide;

    private ItemInfoRecyclerAdapterListener listener;

    public ItemInfoRecyclerAdapter(Context context, ItemInfoRecyclerAdapterListener listener, String selectedSide) {
        this.context = context;
        this.data = new ArrayList<>();
        this.listener = listener;
        this.selectedSide = selectedSide;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.iteminfo_list, parent, false);

        ViewHolder holder = new ViewHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemInfo itemInfo = data.get(position);
//        String imageUri = itemInfo.getImage();
//        Bitmap bitmap = null;
//        if (imageUri!=null){
//            Log.wtf("where","maii null ja");
//            Log.wtf("where",imageUri);
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(imageUri));
//                Log.wtf("bitmap", bitmap.toString());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }else {
//            Log.wtf("where","null ja");
//            Log.wtf("test null",itemInfo.getBrand());
//        }

        final Float cost = itemInfo.getCost();
        final Float size = itemInfo.getSize();
        final String image = itemInfo.getImage();
        final String brand = itemInfo.getBrand();
        final String detail = itemInfo.getDetail();

        holder.txt_brand.setText(brand);
        holder.txt_detail.setText(detail);
        holder.txt_cost.setText(Float.toString(cost));
        holder.txt_size.setText(Float.toString(size));
//         holder.imageView.setImageBitmap(bitmap);
        Glide.with(context).load(image).into(holder.imageView);


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, cost + "", Toast.LENGTH_LONG).show();
                Toast.makeText(context, size + "", Toast.LENGTH_LONG).show();
                listener.onItemInfoSelected(cost, size);
                if (brand != null && detail != null && image != null && selectedSide != null) {
                    Toast.makeText(context, listener.toString(), Toast.LENGTH_LONG).show();
                    listener.setItem(brand, detail, image, selectedSide);
                }
                else {
                    Log.wtf("what",brand+"/"+detail+"/"+image+"/"+selectedSide);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public List<ItemInfo> getData() {
        return data;
    }

    public void setData(List<ItemInfo> data) {
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_brand;
        TextView txt_detail;
        TextView txt_cost;
        TextView txt_size;
        ImageView imageView;
        ItemInfo itemInfo;
        LinearLayout linearLayout;


        public ViewHolder(View itemView) {
            super(itemView);

            txt_brand = itemView.findViewById(R.id.item_brand);
            txt_detail = itemView.findViewById(R.id.item_detail);
            txt_cost = itemView.findViewById(R.id.item_cost);
            txt_size = itemView.findViewById(R.id.item_size);
            imageView = itemView.findViewById(R.id.item_image);
            linearLayout = itemView.findViewById(R.id.item_list);

        }
    }

    public interface ItemInfoRecyclerAdapterListener {
        void onItemInfoSelected(float cost, float size);
        void setItem(String brand, String detail, String image, String selectedSide);
    }

    public void setSelectedSide(String selectedSide) {
        this.selectedSide = selectedSide;
    }
}
