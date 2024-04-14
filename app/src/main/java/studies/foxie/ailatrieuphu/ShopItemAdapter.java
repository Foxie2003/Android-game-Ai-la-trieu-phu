package studies.foxie.ailatrieuphu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ShopItemAdapter extends RecyclerView.Adapter<ShopItemAdapter.ViewHolder> {
    private ArrayList<ShopItem> shopItems;
    private LayoutInflater mInflater;
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onBuyItemClick(ShopItem item);
    }

    public ShopItemAdapter(Context context, ArrayList<ShopItem> shopItems, OnItemClickListener listener) {
        this.mInflater = LayoutInflater.from(context);
        this.shopItems = shopItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ivImage.setImageResource(shopItems.get(position).getImage());
        holder.tvItemPrice.setText(DB.formatNumber(shopItems.get(position).getPrice()));
        holder.ivItemBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onBuyItemClick(shopItems.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return shopItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage, ivItemBuy;
        TextView tvItemPrice;
        ViewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_item_image);
            tvItemPrice = itemView.findViewById(R.id.tv_item_price);
            ivItemBuy = itemView.findViewById(R.id.iv_item_buy);
        }
    }
}
