package fr.masciulli.android_quantizer.lib;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ColorQuantizer {
    public static final int DEFAULT_DIVISIONS_NUMBER = 2;

    private ArrayList<Integer>[][][] mHistogram;
    private int mDivisionsNumber;
    private ArrayList<Integer> mQuantizedColors;

    public ColorQuantizer(int divisions) {
        mDivisionsNumber = divisions;
        mHistogram = new ArrayList[divisions][divisions][divisions];
        for (int i = 0; i < divisions; i++)
            for (int j = 0; j < divisions; j++)
                for (int k = 0; k < divisions; k++) {
                    mHistogram[i][j][k] = new ArrayList<Integer>();
                }
    }

    public ColorQuantizer() {
        this(DEFAULT_DIVISIONS_NUMBER);
    }

    public ColorQuantizer load(Bitmap image) {
        int[] imagePixels = convertToTable(image);

        for (int i = 0; i < imagePixels.length; i++) {
            addPixel(imagePixels[i]);
        }
        return this;
    }

    private void addPixel(int color) {
        int divisionLength = 256 / mDivisionsNumber;
        mHistogram[Color.red(color) / divisionLength][Color.green(color) / divisionLength][Color.blue(color) / divisionLength].add(color);
    }

    public String toString() {
        String s = "";
        for (int i = 0; i < mDivisionsNumber; i++)
            for (int j = 0; j < mDivisionsNumber; j++)
                for (int k = 0; k < mDivisionsNumber; k++) {
                    s += "[" + i + "][" + j + "][" + k + "] = " + mHistogram[i][j][k] + "\n";
                }

        return s;
    }

    public ColorQuantizer quantize() {
        ArrayList<ColorBox> boxResult = new ArrayList<ColorBox>();
        mQuantizedColors = new ArrayList<Integer>();

        for (int i = 0; i < mDivisionsNumber; i++)
            for (int j = 0; j < mDivisionsNumber; j++)
                for (int k = 0; k < mDivisionsNumber; k++) {
                    if (mHistogram[i][j][k].size() == 0) break;

                    Collections.sort(mHistogram[i][j][k], new Comparator<Integer>() {
                        @Override
                        public int compare(Integer a, Integer b) {
                            return ((Integer)Color.red(a)).compareTo(Color.red(b));
                        }
                    });
                    int redMedian = Color.red(mHistogram[i][j][k].get(mHistogram[i][j][k].size() / 2));

                    Collections.sort(mHistogram[i][j][k], new Comparator<Integer>() {
                        @Override
                        public int compare(Integer a, Integer b) {
                            return ((Integer)Color.green(a)).compareTo(Color.green(b));
                        }
                    });
                    int greenMedian = Color.green(mHistogram[i][j][k].get(mHistogram[i][j][k].size() / 2));

                    Collections.sort(mHistogram[i][j][k], new Comparator<Integer>() {
                        @Override
                        public int compare(Integer a, Integer b) {
                            return ((Integer)Color.blue(a)).compareTo(Color.blue(b));
                        }
                    });
                    int blueMedian = Color.blue(mHistogram[i][j][k].get(mHistogram[i][j][k].size() / 2));

                    boxResult.add(new ColorBox(redMedian, greenMedian, blueMedian, mHistogram[i][j][k].size()));
                }

        Collections.sort(boxResult, new ColorBoxReverseComparator());

        for (ColorBox colorBox : boxResult) {
            mQuantizedColors.add(Color.rgb(colorBox.red, colorBox.green, colorBox.blue));
        }

        return this;
    }

    public int getDivisionNumber() {
        return mDivisionsNumber;
    }

    public ArrayList<Integer> getQuantizedColors() {
        return mQuantizedColors;
    }

    private class ColorBox {

        public ColorBox(int r, int g, int b, int count) {
            red = r;
            green = g;
            blue = b;
            this.count = count;
        }

        public int red = 0;
        public int green = 0;
        public int blue = 0;

        public int count = 0;
    }

    private class ColorBoxReverseComparator implements Comparator<ColorBox> {
        @Override
        public int compare(ColorBox a, ColorBox b) {
            return ((Integer)b.count).compareTo(a.count);
        }
    }

    private int[] convertToTable(Bitmap bitmap) {
        int[] pixels = new int[bitmap.getHeight() * bitmap.getWidth()];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        return pixels;
    }
}
