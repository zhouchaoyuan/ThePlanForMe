package cn.zhouchaoyuan.myanimation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ScreenSlidePager extends AppCompatActivity {

    private ViewPager pager;

    private final int FRAGMENT_COUNT = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide_pager);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
    }

    private class MyViewPagerAdapter extends FragmentPagerAdapter {
        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ScreenSlidePageFragment.newInstance(position);
        }


        @Override
        public int getCount() {
            return FRAGMENT_COUNT;
        }
    }
}
