package com.person.xuan.encouragement.util;

import android.graphics.Color;

/**
 * 线性颜色取色器
 * Created by chenxiaoxuan1 on 16/3/25.
 */
public class ColorPickGradient {
    //设置的颜色
    public static final int[] PICKCOLORBAR_COLORS = new int[]{0xFFFFFFFF,0xFFFF3030, 0xFFF4A460,
            0xFFFFFF00, 0xFF66CD00,
            0xFF458B00, 0xFF0000EE,
            0xFF912CEE,0xFF000000};
    //每个颜色的位置
    public static final float[] PICKCOLORBAR_POSITIONS = new float[]{0f, 0.1f, 0.2f, 0.3f, 0.5f, 0.65f,0.8f,0.9f,1f};

    private int[] mColorArr = PICKCOLORBAR_COLORS;
    private float[] mColorPosition = PICKCOLORBAR_POSITIONS;

    public ColorPickGradient(int[] colorArr) {
        this.mColorArr = colorArr;
    }

    public ColorPickGradient() {
    }

    /**
     * 获取某个百分比位置的颜色
     * @param radio 取值[0,1]
     * @return
     */
    public int getColor(float radio) {
        int startColor;
        int endColor;
        if (radio >= 1) {
            return mColorArr[mColorArr.length - 1];
        }
        for (int i = 0; i < mColorPosition.length; i++) {
            if (radio <= mColorPosition[i]) {
                if (i == 0) {
                    return mColorArr[0];
                }
                startColor = mColorArr[i - 1];
                endColor = mColorArr[i];
                float areaRadio = getAreaRadio(radio,mColorPosition[i-1],mColorPosition[i]);
                return getColorFrom(startColor,endColor,areaRadio);
            }
        }
        return -1;
    }

    public float getAreaRadio(float radio, float startPosition, float endPosition) {
        return (radio - startPosition) / (endPosition - startPosition);
    }

    /**
     *  取两个颜色间的渐变区间 中的某一点的颜色
     * @param startColor
     * @param endColor
     * @param radio
     * @return
     */
    public int getColorFrom(int startColor, int endColor, float radio) {
        int redStart = Color.red(startColor);
        int blueStart = Color.blue(startColor);
        int greenStart = Color.green(startColor);
        int redEnd = Color.red(endColor);
        int blueEnd = Color.blue(endColor);
        int greenEnd = Color.green(endColor);

        int red = (int) (redStart + ((redEnd - redStart) * radio + 0.5));
        int greed = (int) (greenStart + ((greenEnd - greenStart) * radio + 0.5));
        int blue = (int) (blueStart + ((blueEnd - blueStart) * radio + 0.5));
        return Color.argb(255, red, greed, blue);
    }

}
