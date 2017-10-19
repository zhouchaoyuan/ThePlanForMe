# fragment

### 概述

碎片是一种可以嵌套在活动当中的UI片段，他能让程序更加合理和充分的利用大屏幕的空间并且它拥有自己的生命周期，接收自己的输入事件，可以再`activity`运行过程中添加或者移除。`Fragment`运行起来唯一需要我们重写的方法就是`onCreatView()`方法，它用来定义布局，如下：


	public class MyFragment extends Fragment {
    	@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    	    Bundle savedInstanceState) {
    	    return inflater.inflate(R.layout.my_view, container, false);
    	}
	}

同activity一样，fragment也有生命周期，如下：</br>![fragment_lifecycle](https://raw.githubusercontent.com/zhouchaoyuan/ThePlanForMe/master/M3-M4/W4/fragment_lifecycle.png)</br>

### 使用fragment
`fragment`是可重用的，模块化的UI组件，每个`Fragment`的实例都必须与一个`FragmentActivity`关联。我们可以在`activity`的`XML`布局文件中定义每一个`fragment`来实现这种关联。一般有两种方法使用`fragment`：

- 使用`<fragment/>`在XML布局中添加`fragment`
- 通过`FragmentManager`对`fragment`进行添加，移除，替换等操作
	- 在`activity`中，用`Support Library APIs`调用`getSupportFragmentManager()`方法获取`FragmentManager`对象，然后调用`beginTransaction()`方法创建一个`FragmentTransaction`对象，然后调用`add()`方法添加一个`fragment`
	- 可以使用同一个`FragmentTransaction`进行多次fragment事务。完成这些变化操作，准备开始执行改变时，必须调用`commit()`方法。</br>
	**ps：**若要使`fragment`有类似返回栈的功能，可以使用`transaction.addToBackStack()`

### 交互

- `Fragment`与`activity`之间的交互
	- 通过`getSupportFragmentManager().findViewById()`;
- `Fragment`与`Fragment`之间的交互
	- 通过中介`activity`交互

还是见[中文教学](http://hukai.me/android-training-course-in-chinese/basics/fragments/index.html)吧，详细。 