package cn.zhouchaoyuan.myanimation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LayoutChangeActivity extends AppCompatActivity {

    /**
     * 开启了动画效果的布局容器，在这里是一个{@link android.widget.LinearLayout}.
     */
    private ViewGroup mContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_change);
        mContainerView = (ViewGroup) findViewById(R.id.change_container);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_layout_change, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.Add_item:
                findViewById(R.id.hint_text).setVisibility(View.GONE);
                addItem();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addItem(){
        // 加载一行的布局
        final View newView = LayoutInflater.from(this).inflate(
                R.layout.activity_list_item_change, mContainerView, false);

        // 设置新加入的一行的文本
        ((TextView) newView.findViewById(R.id.title_text)).setText(
                COUNTRIES[(int) (Math.random() * COUNTRIES.length)]);

        // 因为父级容器的android:animateLayoutChanges属性设置为true,
        // 所以移除的时候有动画效果
        //并且将这个View添加在第0行
        mContainerView.addView(newView, 0);

        newView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 移除被点击的一行
                // 因为父级容器的android:animateLayoutChanges属性设置为true,
                // 所以移除的时候有动画效果
                mContainerView.removeView(newView);

                // 如果已经移除光了，提示添加的文本可见
                if (mContainerView.getChildCount() == 0) {
                    findViewById(R.id.hint_text).setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * 存颜文字的静态数组
     */
    private static final String[] COUNTRIES = new String[]{
            "╭(′▽`)╭(′▽`)╯", "三c⌒っﾟДﾟ)っ", "╭∩╮(︶︿︶)╭∩╮", "(ˉ▽￣～) 切~~", "╭(′▽`)╭(′▽`)╯",
            "ε=ε=ε=┏(゜ロ゜;)┛", "━━∑(￣□￣*|||━━", "ヾ(￣▽￣)Bye~Bye~", "( ￣ ￣)σ…( ＿ ＿)ノ｜壁", "( ￣ー￣)人(^▽^ )",
            "~o( =∩ω∩= )m", "( ￣ー￣)人(^▽^ )", "O(∩_∩)O谢谢", "☆⌒(*＾-゜)v THX!!", "( *^-^)ρ(^0^* )", "( *^-^)ρ(*╯^╰)"
    };

}
