package studies.foxie.ailatrieuphu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class FragmentAvatar extends Fragment {
    private DB database;
    private ArrayList<ShopItem> items;
    private ShopItemAdapter shopItemAdapter;
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
        items = database.getItemsByCategory(2);

        //Hiển thị các item trong cửa hàng lên recyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        shopItemAdapter = new ShopItemAdapter(getContext(), items, new ShopItemAdapter.OnItemClickListener() {
            @Override
            public void onBuyItemClick(ShopItem item) {
                if(item.isBought()) {
                    Toast.makeText(getContext(), "Vật phẩm này đã được mua", Toast.LENGTH_SHORT).show();
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
        recyclerView.setAdapter(shopItemAdapter);
        return view;
    }
}