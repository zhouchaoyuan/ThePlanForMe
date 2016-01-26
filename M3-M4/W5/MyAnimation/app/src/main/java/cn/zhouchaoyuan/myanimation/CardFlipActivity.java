package cn.zhouchaoyuan.myanimation;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CardFlipActivity extends AppCompatActivity implements View.OnTouchListener{

    //the width of the FrameLayout
    private int width;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_flip);
        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.flip_container, new FragmentCard())
                    .commit();
        }
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.flip_container);
        frameLayout.setOnTouchListener(this);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        width = metrics.widthPixels;
    }

    /**
     * judge flipCardForWard or flipCardBackWard
     * */
    private float initx,offsetx;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                initx = event.getX();
                break;
            case  MotionEvent.ACTION_UP:
                offsetx = event.getX() - initx;
                /*if(Math.abs(offsetx) < 5){//just click the view
                     //click the view
                    if(event.getX() > width/2){
                        flipCardForWard();
                    }
                    else{
                        flipCardBackWard();
                    }
                }
                else*/
                if(offsetx <= -5){// error of margin
                    flipCardForWard();
                }
                else if(offsetx >= 5){
                    flipCardBackWard();
                }
                break;
        }
        return true;
    }

    /**
     * card flip forward
     * */
    public void flipCardForWard() {
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.card_filp_right_in,R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                .replace(R.id.flip_container, new FragmentCard())
                .addToBackStack(null)
                .commit();
    }

    /**
     * card flip backward
     * */
    public void flipCardBackWard(){
        if(getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStack();
        }
        else{
            Toast.makeText(this,"First Page",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * static inner class
     * */
    public static class FragmentCard extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_flip_card, container, false);
            TextView textView = (TextView) view.findViewById(R.id.des_text);
            textView.setText(datas[index++ % datas.length]);
            return view;
        }
    }

    private static int index = 0;
    private final static String[] datas = {
            "明明可以靠脸吃饭，你却要靠才华，这就是你和明明的区别",
            "用“要么…要么…”造句，\n小明：冰棍五毛啊！要么…要么。",
            " 你认为最有影响力的物理学家是谁？\n我写的是“牛顿”。结果，全班只有我一个人没及格，原来，大家都把导师的名字写了上去……kao，什么世道！",
            "每天早上起床后我都要看一遍福布斯富翁排行榜，如果上面没有我的名字，我就去上班",
            "马云是首富啊，他有1500亿，咱中国13亿人，他给咱每人分1亿，咱们都是亿万富翁了，他还有1487亿，他依然是首富啊。我被这句话深深的感动了。",
            "在下姓聂，刚才到机场去接个客户，见面后客户非常热情的迎过来握手：聂总您好！您好！这时他的秘书用怪怪的目光在看我……你妹啊！你才孽种，你全家都孽种！",
            "有一朋友去找大师给他儿子取名。\n朋友：大师我给我儿子起名要有英文名字和中文名字，我姓陆。大师：叫陆由器，英文名Wi-Fi！",
            "早晨刚出小区门口，一个五六岁的小萝莉，一下抱住我的大腿哭着喊：叔叔，你娶了我吧！！！ 我正凌乱中，忽然听背后一个声音说：你就是结婚了，今天也得给我上学去！"
    };
}
