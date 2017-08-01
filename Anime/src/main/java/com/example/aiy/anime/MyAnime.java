package com.example.aiy.anime;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * <p>功能简述：这是自定义的animation类
 * <p>Created by Aiy on 2017/7/31.
 */

public class MyAnime extends Animation {
    /**
     * 方法简述： 构造方法可以传入我们需要的值，比如控件的长宽等等
     */
    public MyAnime() {

    }

    /**
     *任务描述： 这里如名字一样是初始化，同时我们可以设置那些Animation类里面的方法。
     *创建时间： 2017/7/31 16:48
     */
    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        //这里设置动画时间 下面那个interpolateTime就是根据这个时间去平均分成0~1的
        setDuration(3000);
    }
    /**
     *任务描述： 这是要实现animation自定义类比较重要的一个方法.查看其他补间动画的源码也可以发现代码量不多，都是这两个方法在实现，上面是初始化比较简单，这里是控制值的变化，同时我们可以利用camera这个类简单的实现XYZ轴旋转.<br>
     * @param interpolatedTime 这个值是自动变化值，0(动画开始前)~1(动画结束)所以我们要改变位置就是靠这个值来计算<br>
     *        比如100f*interpolatedTime 就是从0移动到100f的位置.
     *        比如X为当前坐标值(传进来) x+(100f-100f*inter)就是从坐标开始移动到坐标+100f的位置.
     *创建时间： 2017/7/31 16:48
     */
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        //这里可以使用Camera来实现各种效果也行（可以操作Z轴，这个跟相机没有关系）,使用t的那个Matrix来实现也行
        //t.getMatrix().setTranslate();
        Camera camera=new Camera();
        //开始调用这个
        camera.save();
        //平移 XYZ三个参数
        camera.translate(100f *interpolatedTime,0,100f);
        //绕Y轴旋转360° 同样有XZ轴的方法 参数是度数
        camera.rotateY(360*interpolatedTime);
        //获得t的Matrix
        Matrix matrix=t.getMatrix();
        //最后matrix变成camera相关的东西
        camera.getMatrix(matrix);
        //这个可以指定这个旋转位置
        matrix.postTranslate(200,400);
        //这句话不是很理解 虽然也是指定旋转位置但是位置有点偏移
        matrix.preTranslate(11,11);
        //这里应该是close一样的
        camera.restore();
    }

}
