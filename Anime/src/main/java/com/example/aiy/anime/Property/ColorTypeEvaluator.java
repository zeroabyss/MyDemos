package com.example.aiy.anime.Property;

import android.animation.TypeEvaluator;
import android.annotation.TargetApi;
import android.graphics.Color;

/**
 * <p>功能简述：这是个颜色变化的TypeEvaluator，因为颜色是int值所以这里选用泛型
 * <p>Created by Aiy on 2017/8/1.
 */
@TargetApi(11)
public class ColorTypeEvaluator implements TypeEvaluator<Integer> {
    @Override
    public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
        //x=start+(end-start)*fraction
        int red= (int) (Color.red(startValue)+(Color.red(endValue)-Color.red(startValue))*fraction);
        int blue= (int) (Color.blue(startValue)+(Color.blue(endValue)-Color.blue(startValue))*fraction);
        int green= (int) (Color.green(startValue)+(Color.green(endValue)-Color.green(startValue))*fraction);
        int alpha= (int) (Color.alpha(startValue)+(Color.alpha(endValue)-Color.alpha(startValue))*fraction);
        //然后返回一个Color的int值
        return  Color.argb(alpha,red,green,blue);
    }
}
