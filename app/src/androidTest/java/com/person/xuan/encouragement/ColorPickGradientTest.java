package com.person.xuan.encouragement;

import android.graphics.Color;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.person.xuan.encouragement.util.ColorPickGradient;

import junit.framework.Assert;

/**
 * Created by chenxiaoxuan1 on 16/3/25.
 */
public class ColorPickGradientTest extends InstrumentationTestCase {
    private static final String TAG = "ColorPickLinearGradientTest";

    public void testGetColor() {
        ColorPickGradient colorPickGradient = new ColorPickGradient();
        for (int i = 1; i < 100; i++) {
            float radio = (float) (i * 0.01);
            int color = colorPickGradient.getColor(radio);
            Log.e(TAG, Integer.toHexString(color));
            for (int k = 0; k < ColorPickGradient.PICKCOLORBAR_POSITIONS.length; k++) {
                if (radio <= ColorPickGradient.PICKCOLORBAR_POSITIONS[k]) {
                    testColorArea(ColorPickGradient.PICKCOLORBAR_COLORS[i - 1], ColorPickGradient.PICKCOLORBAR_COLORS[i], color);
                }
            }
        }

    }

    public void testColorArea(int startColor, int endColor, int color) {
        int redStart = Color.red(startColor);
        int blueStart = Color.blue(startColor);
        int greenStart = Color.green(startColor);
        int redEnd = Color.red(endColor);
        int blueEnd = Color.blue(endColor);
        int greenEnd = Color.green(endColor);
        Log.e(TAG, " " + redStart + "  " + greenStart + "  " + blueStart);
        Log.e(TAG, " " + redEnd + "  " + greenEnd + "  " + blueEnd);
        testRGB(redStart, redEnd, Color.red(color));
        testRGB(greenStart, greenEnd, Color.green(color));
        testRGB(blueStart, blueEnd, Color.blue(color));
    }

    private void testRGB(int start, int end, int gradientColor) {
        Log.e(TAG, "start : " + start + "  end : " + end + "  gradient " + gradientColor);
        int endRGB = start + (end - start);
        if (end < start) {
            Assert.assertEquals(gradientColor <= start, true);
            Assert.assertEquals(gradientColor >= endRGB, true);
        } else {
            Assert.assertEquals(gradientColor >= start, true);
            Assert.assertEquals(gradientColor <= endRGB, true);
        }

    }
}
