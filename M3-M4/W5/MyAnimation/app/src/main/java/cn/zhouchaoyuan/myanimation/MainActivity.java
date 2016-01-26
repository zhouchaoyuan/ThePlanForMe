package cn.zhouchaoyuan.myanimation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<MyItem> list = new ArrayList<MyItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init_data();
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(new ItemAdapter(this, R.layout.fragment_item, list));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyItem myItem = list.get(position);
                startActivity(new Intent(MainActivity.this, myItem.getClassName()));
            }
        });
    }

    public void init_data() {
        list.add(new MyItem(R.string.title_card_flip, R.mipmap.arrow_right, CardFlipActivity.class));
        list.add(new MyItem(R.string.title_zoom_photo,R.mipmap.arrow_right,ZoomActivity.class));
        list.add(new MyItem(R.string.layout_change,R.mipmap.arrow_right,LayoutChangeActivity.class));
        list.add(new MyItem(R.string.slide_activity,R.mipmap.arrow_right,ScreenSlidePager.class));
    }

    class ItemAdapter extends ArrayAdapter<MyItem> {

        private int resourceID;
        private Context context;

        public ItemAdapter(Context context, int resource, List<MyItem> objects) {
            super(context, resource, objects);
            resourceID = resource;
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyItem item = getItem(position);
            View view = LayoutInflater.from(context).inflate(resourceID, null);
            TextView textView = (TextView) view.findViewById(R.id.item_title);
            ImageView imageView = (ImageView) view.findViewById(R.id.arrow);
            textView.setText(item.getName());
            imageView.setImageResource(item.getImageId());
            return view;
        }
    }

    class MyItem {
        private String name;
        private int imageId;
        private Class<? extends Context> className;

        MyItem(int titleId, int imageId, Class<? extends Activity> clss) {
            this.imageId = imageId;
            this.name = getResources().getString(titleId);
            this.className = clss;
        }

        public String getName() {
            return name;
        }

        public int getImageId() {
            return imageId;
        }

        public Class<? extends Context> getClassName() {
            return className;
        }
    }
}
