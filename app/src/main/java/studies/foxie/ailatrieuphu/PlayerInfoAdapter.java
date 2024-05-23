package studies.foxie.ailatrieuphu;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlayerInfoAdapter extends RecyclerView.Adapter<PlayerInfoAdapter.PlayerInfoViewHolder> {
    private DB database;
    private List<PlayerInfo> playerInfoList;
    private String infoName;
    private Context context;

    public PlayerInfoAdapter(Context context, List<PlayerInfo> playerInfoList, String infoName) {
        this.context = context;
        this.playerInfoList = playerInfoList;
        this.infoName = infoName;
    }

    @NonNull
    @Override
    public PlayerInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_info_item, parent, false);
        database = new DB(context);
        return new PlayerInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerInfoViewHolder holder, int position) {
        PlayerInfo playerInfo = playerInfoList.get(position);
        holder.tvName.setText(playerInfo.getPlayerName());
        // Lấy id của hình ảnh từ tên hình ảnh trong drawable
        int resourceId = context.getResources().getIdentifier(database.getItemById(playerInfoList.get(position).getAvatarId()).getImage(), "drawable", context.getPackageName());
        // Kiểm tra xem id của hình ảnh có hợp lệ không
        if (resourceId != 0) {
            // Nếu id hợp lệ, đặt hình ảnh vào ImageView
            holder.ivAvatar.setImageResource(resourceId);
        } else {
            // Nếu không tìm thấy hình ảnh, xử lý theo nhu cầu của bạn, ví dụ đặt một hình ảnh mặc định
//                holder.ivImage.setImageResource(R.drawable.default_image);
        }
        switch (infoName) {
            case("money"):
                holder.tvInfo.setText(DB.formatNumber(playerInfo.getMoney()));
                holder.tvInfo.setTextColor(Color.parseColor("#1ACF14"));
                holder.tvInfo.setShadowLayer(16, 0, 0, Color.parseColor("#1ACF14"));
                holder.ivImage.setImageResource(R.drawable.money);
                break;
            case("diamond"):
                holder.tvInfo.setText(DB.formatNumber(playerInfo.getDiamond()));
                holder.tvInfo.setTextColor(Color.parseColor("#38B6FF"));
                holder.tvInfo.setShadowLayer(16, 0, 0, Color.parseColor("#38B6FF"));
                holder.ivImage.setImageResource(R.drawable.diamond);
                break;
        }
        // Bind other views if necessary
    }

    @Override
    public int getItemCount() {
        return playerInfoList.size();
    }

    public class PlayerInfoViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar, ivImage;
        TextView tvName, tvInfo;
        public PlayerInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.iv_leaderboard_info_avatar);
            ivImage = itemView.findViewById(R.id.iv_leaderboard_info_image);
            tvName = itemView.findViewById(R.id.tv_leaderboard_info_name);
            tvInfo = itemView.findViewById(R.id.tv_leaderboard_info_info);
        }
    }
}