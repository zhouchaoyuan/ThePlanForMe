package cn.zhouchaoyuan.myanimation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by chaoyuan on 2016/1/26.
 */
public class ScreenSlidePageFragment extends Fragment {

    private static String ARG_PAGE = "page";
    private int pageNumber;

    public ScreenSlidePageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARG_PAGE);
    }

    public static ScreenSlidePageFragment newInstance(int pageNumber) {
        ScreenSlidePageFragment sspf = new ScreenSlidePageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_PAGE, pageNumber);
        sspf.setArguments(bundle);
        return sspf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView content = (TextView) view.findViewById(R.id.content);
        title.setText(titles[pageNumber]);
        content.setText(contents[pageNumber]);
        return view;
    }

    private final static String[] titles = {"一、获取安装文件", "二、安装IDE", "三、建立AVD", "四、自动更新SDK"};

    private final static String[] contents = {"1、首先进入Android官网并下载安装文件 https://developer.android.com/sdk/index.html\n" +
            "\n" +
            "下载完毕即得到一个exe文件。",

            //split

            "1、安装Android Studio之前必须确保安装了JDK，对于Android 5.0以上版本必须保证JDK7版本以上。 安装JDK(Java SE Development Kit 7)，下载完JDK之后配置系统环境变量JAVA_HOME，操作步骤为“系统高级设置->高级->环境变量”，然后添加系统变量，名字为JAVA_HOME，变量值为C:\\Program Files\\Java\\jdk1.8.0_45（假设JDK为此安装路径）。\n\n" +
                    "2、双击之前下载的exe文件，启动之后他将检索JAVA_HOME变量并在我们连续点击下一步的情况下自动安装Android Studio。\n\n" +
                    "3、在已经安装了的Android Studio添加SKD包，分几小步：" + "\n" + "\t点击 Tools > Android > SDK Manager打开如下界面 " + "\t获得最新的SDK tools，勾选以下选项\n" +
                    "a、Android SDK Tools(必选，最新的Android软件开发工具)\n" +
                    "b、Android SDK Platform-tools(必选，最新的Android稳定版软件开发工具)\n" +
                    "c、Android SDK Build-tools (建议最高版本)\n" +
                    "d、SDK Platform(必选，开发环境中至少要一个SDK Platform你才能编译app)\n" +
                    "e、ARM EABI v7a System Image(建议勾选，模拟器的系统，选择合适的版本就行)\n" +
                    "f、Android Support Repository(建议勾选，包含支持Library的专用库)\n" +
                    "g、Android Support Library(建议勾选，可以让你使用最新的Android APIs)\n" +
                    "h、Google Repository(建议勾选，包含支持Library的专用库)\n" +
                    "i、Google Play services(建议勾选，包含谷歌服务和客户端库以及示例代码)\n\n" +
                    "点击Install X packages，即可完成安装，若是安装受阻,说明被墙，可以通过设置代理服务器(见Proxy Settings)安装。其他可以勾选的选项包可自行勾选，每项的详细用途参考这里\n" +
                    "经过上述的步骤，你现在就可以在Android Studio上构建app了。",

            //split

            "1、启动 Android Virtual Device Manager（AVD Manager）的两种方式：\n" +
                    "\n" +
                    "a、用Android Studio, Tools > Android > AVD Manager,或者点击工具栏里面Android Virtual Device Manager\n" +
                    "b、在命令行窗口中，把当前目录切换到<sdk>/tools/ 后执行\n\n" + "2、点击Create Virtual Device.\n\n" + "3、在Select Hardware窗口，选择一个设备，比如 Nexus 6，点击Next。\n" +
                    "\n" +
                    "4、选择列出的合适系统镜像.\n" +
                    "\n" +
                    "5、校验模拟器配置，点击Finish",

            //split

            "对于安装了的SDK，Android官方往往更新很快，我们可以设置自动检查更新，以获得最新的SDK，自动检查更新SDK根据以下步骤：\n" +
                    "\n" +
                    "1、选择File > Settings > Appearance & Behavior > System Settings > Updates.\n\n" +
                    "2、勾选Android SDK自动检测更新并选择更新channel，一般选择stable channel。\n\n" +
                    "3、点击OK或者Apply就可以自动检查更新了\n"};
}
