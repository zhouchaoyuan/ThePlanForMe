package cn.zhouchaoyuan.myanimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

public class ZoomActivity extends AppCompatActivity {

    /**
     * 动画的运行时长
     * */
    private int expandTime = 0;

    /**
     * 当前动画集的引用
     * */
    private Animator currentAnimator = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);
        //获得动画的时长
        expandTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
    }

    //点击缩略图1
    public void expandImage1(View v){
        expandThumb(v, R.mipmap.large_image1);
    }

    //点击缩略图2
    public void expandImage2(View v){
        expandThumb(v, R.mipmap.large_image2);
    }

    /**
     * 将缩略图放大成高分辨率的图片，并充满整个容器，具体如下：
     *
     * <ol>
     *   <li>加载高清图片</li>
     *   <li>计算将要扩大的View的开始边界和结束边界</li>
     *   <li>同时在开始边界和结束边界缩放V高清图片的四个属性(X, Y, SCALE_X, SCALE_Y)</li>
     *   <li>再次点击将会做相反运动</li>
     * </ol>
     *
     * @param thumbImage  要放大的缩略图
     * @param ResourceId  缩略图高清图片的资源ID
     */
    public void expandThumb(final View thumbImage, int ResourceId){
        //两次点击取消动画
        if(currentAnimator != null){
            currentAnimator.cancel();
            return ;
        }
        final ImageView largeImage = (ImageView) findViewById(R.id.expand_image);
        largeImage.setImageResource(ResourceId);

        //开始运行的边界，结束运行的边界，相对坐标原点和绝对坐标原点的偏移量
        final Rect beginBound = new Rect();
        Rect endBound = new Rect();
        Point offset = new Point();

        //获得视图的全局可是边界
        thumbImage.getGlobalVisibleRect(beginBound);
        findViewById(R.id.zoom_container).getGlobalVisibleRect(endBound, offset);

        beginBound.offset(-offset.x, -offset.y);
        endBound.offset(-offset.x, -offset.y);

        //将要移动的矩形长宽比例和移动之后的矩形长宽比例调整为一致的
        float ratio = 0;
        if((float)endBound.width()/endBound.height()>(float)beginBound.width()/beginBound.height()){
            ratio = (float)beginBound.height()/endBound.height();
            float width = endBound.width()*ratio;
            float deltaWidth = (width-beginBound.width())/2f;
            beginBound.left -= deltaWidth;//拉长缩略图的宽度，使得两个矩形相似
            beginBound.right += deltaWidth;
        }
        else{
            ratio = (float)beginBound.width()/endBound.width();
            float height = endBound.height()*ratio;
            float deltaHeight = (height-beginBound.height())/2f;
            beginBound.top -= deltaHeight;//拉长缩略图的高度，使得两个矩形相似
            endBound.bottom += deltaHeight;
        }

        //缩略图设置为透明，动画图片由不可见设置为可见
        thumbImage.setAlpha(0f);
        largeImage.setVisibility(View.VISIBLE);

        //设置缩放的中枢点为左上角
        //pivotX、pivotY: 缩放和旋转时横纵向中心点，默认情况下是View的中心，如果想以View的左上坐标为中心进行旋转或者缩放，应该将其值都设置为0
        largeImage.setPivotX(0f);
        largeImage.setPivotY(0f);

        //动画集使四个属性动画并行，动画方式先快后慢，时长300ms
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(ObjectAnimator.ofFloat(largeImage,View.X,beginBound.left,endBound.left))
                .with(ObjectAnimator.ofFloat(largeImage,View.Y,beginBound.top,endBound.top))
                .with(ObjectAnimator.ofFloat(largeImage,View.SCALE_X,ratio,1f))
                .with(ObjectAnimator.ofFloat(largeImage,View.SCALE_Y,ratio,1f));//从当前状态往回，只指定一个参数
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.setDuration(expandTime);
        animatorSet.start();
        currentAnimator = animatorSet;
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                currentAnimator = null;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                currentAnimator = null;
            }
        });

        //点击大图片缩小
        final float finalRatio = ratio;//内部类使用final变量
        largeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentAnimator != null){
                    currentAnimator.cancel();
                    return ;
                }
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator.ofFloat(largeImage,View.X,beginBound.left))
                        .with(ObjectAnimator.ofFloat(largeImage,View.Y,beginBound.top))
                        .with(ObjectAnimator.ofFloat(largeImage,View.SCALE_X, finalRatio))
                        .with(ObjectAnimator.ofFloat(largeImage,View.SCALE_Y, finalRatio));
                set.setInterpolator(new DecelerateInterpolator());
                set.setDuration(expandTime);
                set.start();
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbImage.setAlpha(1f);
                        currentAnimator = null;
                        largeImage.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbImage.setAlpha(1f);
                        currentAnimator = null;
                        largeImage.setVisibility(View.GONE);
                    }
                });
                currentAnimator = set;
            }
        });
    }

}
