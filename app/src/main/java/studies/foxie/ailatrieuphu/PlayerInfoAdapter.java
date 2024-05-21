package studies.foxie.ailatrieuphu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlayerInfoAdapter extends RecyclerView.Adapter<PlayerInfoAdapter.PlayerInfoViewHolder> {
    private List<PlayerInfo> playerInfoList;

    public PlayerInfoAdapter(List<PlayerInfo> playerInfoList) {
        this.playerInfoList = playerInfoList;
    }

    @NonNull
    @Override
    public PlayerInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_info_item, parent, false);
        return new PlayerInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerInfoViewHolder holder, int position) {
        PlayerInfo playerInfo = playerInfoList.get(position);
        holder.topTextView.setText("Top " + (position + 1));
        holder.playerNameTextView.setText("Tên người chơi: " + playerInfo.getPlayerName());
        holder.moneyTextView.setText("Tiền: " + DB.formatNumber(playerInfo.getMoney()));
        // Bind other views if necessary
    }

    @Override
    public int getItemCount() {
        return playerInfoList.size();
    }

    public class PlayerInfoViewHolder extends RecyclerView.ViewHolder {
        TextView topTextView;
        TextView playerNameTextView;
        TextView moneyTextView;
        // Other views...

        public PlayerInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            topTextView = itemView.findViewById(R.id.topTextView);
            playerNameTextView = itemView.findViewById(R.id.playerNameTextView);
            moneyTextView = itemView.findViewById(R.id.moneyTextView);
            // Initialize other views...
        }
    }
}