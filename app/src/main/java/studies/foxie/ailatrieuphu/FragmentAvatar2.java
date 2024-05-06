package studies.foxie.ailatrieuphu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class FragmentAvatar2 extends Fragment {
    private DB database;
    private ArrayList<ShopItem> items;
    private ShopItemAdapter2 shopItemAdapter;
    RecyclerView recyclerView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_avatar, container, false);
        recyclerView = view.findViewById(R.id.rv_avatar);
        //Khởi tạo đối tượng database và khởi tạo database
        database = new DB(getContext());
        //Khởi tạo ArrayList chứa dữ liệu của những item trong cửa hàng
        items = database.getBoughtItemsByCategory(2);

        //Hiển thị các item trong cửa hàng lên recyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        shopItemAdapter = new ShopItemAdapter2(getContext(), items, new ShopItemAdapter2.OnItemClickListener() {
            @Override
            public void onBuyItemClick(ShopItem item) {
                if(item.isBought()) {
                    if(database.getUsingAvatarId() != item.getId()) {
                        database.setUsingAvatarId(item.getId());
                        shopItemAdapter.setItemUsingId(database.getUsingAvatarId());
                        shopItemAdapter.notifyDataSetChanged();
                        ActivityPlayerInfo.showAvatar(getContext());
                    }
                }
                else {
                    if(database.minusMoney(item.getPrice())) {
                        ShopActivity.showPlayerMoney();
                        item.setBought(true);
                        database.changeItemBoughtState(item.getId(), 1);

                        // Thông báo cho Adapter biết rằng một mục đã thay đổi
                        shopItemAdapter.notifyItemChanged(items.indexOf(item));
                    }
                    else {
                        Toast.makeText(getContext(), "Bạn không đủ tiền", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        shopItemAdapter.setItemUsingId(database.getUsingAvatarId());
        recyclerView.setAdapter(shopItemAdapter);
        return view;
    }
}