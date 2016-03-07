package com.kk.lp.AndroidStudioDemo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.kk.lp.R;

import java.util.List;

/**
 * Created by ljpww72729 on 16/1/30.
 */
public class EditorDemo {

    private static final String TAG = "EditorDemo";

    public void dra(){
    String a = "abc";
        a.replace("a", "b");
    }
    public boolean abc(){
        String a= "abc";
        return a.isEmpty();
    }

    public static class InitialFields{
        private final String a;
        private final boolean b;
        private final int c;
        private String d;

        public InitialFields(String a, boolean b, int c, String d) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
        }
    }

    public void instanceCheck(Object object){
        if (object instanceof Context){
            Context context = (Context) object;

        }
    }
    public int suppressStatements(){
        //Suppress warnings
        //noinspection UnnecessaryLocalVariable
        int result = 0;
        return result;
    }

    public void liveTemplates1(List<String> list){
        //for(int k = 0; k < 100; k++){ ... }
        //fori i->j
        for (int j = 0; j < list.size(); j++) {

        }
        //post-fix list.fori
        for (int i = 0; i < list.size(); i++) {

        }
    }
    public int liveTemplates2(String p1, String p2){
        //Loging: logt, logm, logr, logi
        //logt：创建TAG变量

        //logm:方法开始打印方法名及参数值
        Log.d(TAG, "liveTemplates2() called with: " + "p1 = [" + p1 + "], p2 = [" + p2 + "]");

        //logi:创建Log.i(TAG, "liveTemplates2: ");
        //TAG未定义，按alt+enter无反应，可以在TAG后面回车后，再backspace，再按alt+enter就可以创建TAG了，
        Log.i(TAG, "liveTemplates2: ");

        int result = 0;
        result ++;

        //logr:方法结束打印返回值
        Log.d(TAG, "liveTemplates2() returned: " + result);
        return result;
    }

    public void filterSearch(){
        //In Generate Action  快捷键：Command+N
        //In Popup Menus - vec 在generate list列表中查找vector
        //In Refactor This 快捷键：Ctrl+T
        //In Project View 在工程目录中也可以通过输入字符查找文件

        //Command+N 调用generate list列表，在此时可直接输入查找一些未给出的生成方法，如toString
        //在包上新建一个类或者其他如vector，快捷键Command+N 也可以输入要新建的名称，如vector

    }

    /**
     * 用于提示某些代码的格式不正确，比如，string字符串不应该有translation_descriptiotan这个选项，可以通过
     * replace structural(action)查找并替换
     * 在setting-inspection-structual search inspection中设置，勾选后可以添加要查询的不正确格式的代码，同时给出提示
     * 提示的内容就是创建的structural replace的名称 具体在7分钟的位置
     */
    public void structuralReplace(){
        //switcher没有快捷方式，通过action(shift+command+a)查找并打开
        //<string name="$name$" translation_descriptiotan="$desc$">$text$</string>

        //可以通过search structurally(action)查找Thread.sleep($duration$)的structural
        try{
            Thread.sleep(500);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void replaceCalls(View view, Drawable drawable){
        //可以设置replace structural 告诉开发人员用setBackground去替换
    view.findViewById(R.id.btn_pw).setBackgroundDrawable(drawable);
        this.setBackgroundDrawable(drawable);
    }

    private void setBackgroundDrawable(Drawable drawable){

    }

    /**
     * 主要讲的是布局文件中的tools命名空间的功能
     */
    public void designtimeAttributes(){
        //Topeka:Show fragment_sign_in & getting things look
        //安卓项目android-topeka

        //inInEditMode()判断是否处于该模式，如果是则执行块中的代码，主要用于自定义view
        //在11分钟左右
    }


    /**
     * 类库资源的可见与不可见
     * 在类库values中创建一个public.xml并将公共的资源公布出去，如下
     * <resource>
     *     <public name="abc" type="string"></public>
     * </resource>
     */
    public void privateResources(){
        //build.gradle的android下设置resourcePrefix 'cc_'，表示资源应该以什么开头
    }

}
