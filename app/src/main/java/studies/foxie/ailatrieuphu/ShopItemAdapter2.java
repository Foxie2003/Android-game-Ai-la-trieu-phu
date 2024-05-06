package studies.foxie.ailatrieuphu;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ShopItemAdapter2 extends RecyclerView.Adapter<ShopItemAdapter2.ViewHolder> {
    private static DB database;
    private ArrayList<ShopItem> shopItems;
    private LayoutInflater mInflater;
    private OnItemClickListener listener;
    private Context context;
    private int itemUsingId = 0;
    public interface OnItemClickListener {
        void onBuyItemClick(ShopItem item);
    }

    public ShopItemAdapter2(Context context, ArrayList<ShopItem> shopItems, OnItemClickListener listener) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.shopItems = shopItems;
        this.listener = listener;
    }

    public int getItemUsingId() {
        return itemUsingId;
    }

    public void setItemUsingId(int itemUsingId) {
        this.itemUsingId = itemUsingId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Khởi tạo đối tượng database và khởi tạo database
        database = new DB(context.getApplicationContext());
        if (shopItems != null) {
            // Lấy id của hình ảnh từ tên hình ảnh trong drawable
            int resourceId = context.getResources().getIdentifier(shopItems.get(position).getImage(), "drawable", context.getPackageName());
            // Kiểm tra xem id của hình ảnh có hợp lệ không
            if (resourceId != 0) {
                // Nếu id hợp lệ, đặt hình ảnh vào ImageView
                holder.ivImage.setImageResource(resourceId);
            } else {
                // Nếu không tìm thấy hình ảnh, xử lý theo nhu cầu của bạn, ví dụ đặt một hình ảnh mặc định
//                holder.ivImage.setImageResource(R.drawable.default_image);
            }
            holder.btnItemBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onBuyItemClick(shopItems.get(holder.getAdapterPosition()));
                    }
                }
            });
            if(shopItems.get(position).getPrice() != 0) {
                holder.tvItemPrice.setText(DB.formatNumber(shopItems.get(position).getPrice()));
            }
            else {
                holder.tvItemPrice.setVisibility(View.INVISIBLE);
            }
            if (shopItems.get(position).isBought()) {
                holder.btnItemBuy.setText("Sử dụng");
                holder.btnItemBuy.setTextColor(Color.parseColor("#38B6FF"));
                holder.btnItemBuy.setShadowLayer(16, 0, 0, Color.parseColor("#38B6FF"));
                if(itemUsingId == shopItems.get(position).getId()) {
                    holder.btnItemBuy.setText("Đang dùng");
                }
            } else {
                holder.btnItemBuy.setText("Mua");
                holder.btnItemBuy.setTextColor(Color.parseColor("#FFA6FF"));
                holder.btnItemBuy.setShadowLayer(16, 0, 0, Color.parseColor("#FFA6FF"));
            }
        }
    }

    @Override
    public int getItemCount() {
        return shopItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvItemPrice;
        AppCompatButton btnItemBuy;
        ViewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_item_image);
            tvItemPrice = itemView.findViewById(R.id.tv_item_price);
            btnItemBuy = itemView.findViewById(R.id.btn_item_buy);
        }
    }
}
