package de.gabriel.bwinf352_3.util;

import java.util.ArrayList;

/**
 * Created by Gabriel on 18.04.2017.
 *
 * Quelle: http://blog.ivank.net/fastest-gaussian-blur.html
 */

public class GaussianBlur {

    public static FastRGB getGaussianBlur(FastRGB input, int radius) {
        ArrayList<Integer> gaussianBoxes = createGaussianBoxes(radius, 3);
        FastRGB output = boxBlur(input,(gaussianBoxes.get(0) - 1) / 2);
        input = boxBlur(output, (gaussianBoxes.get(1) - 1) / 2);
        output = boxBlur(input, (gaussianBoxes.get(2) - 1) / 2);

        return output;
    }

    // Erstellung des Gauss-Boxen für den 1D-Filter
    private static ArrayList<Integer> createGaussianBoxes(double sigma, int n) {
        double idealFilterWidth = Math.sqrt((12 * sigma * sigma / n) + 1);

        int filterWidth = (int) Math.floor(idealFilterWidth);

        if (filterWidth % 2 == 0) {
            filterWidth--;
        }

        int filterWidthU = filterWidth + 2;

        double mIdeal = (12 * sigma * sigma - n * filterWidth * filterWidth - 4 * n * filterWidth - 3 * n) / (-4 * filterWidth - 4);
        double m = Math.round(mIdeal);

        ArrayList<Integer> result = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            result.add(i < m ? filterWidth : filterWidthU);
        }

        return result;
    }

    // Führe einen linearen Box Blur aus
    private static FastRGB boxBlur(FastRGB input, int radius) {
        FastRGB output = input.clone();
        input = boxBlurHorizontal(output, radius);
        output = boxBlurVertical(input, radius);

        return output;
    }

    // Box Blur in horizontaler Richtung (zeilenweise)
    private static FastRGB boxBlurHorizontal(FastRGB input, int radius) {
        FastRGB output = input.clone();

        int width = input.getWidth();
        int height = input.getHeight();

        int resultingColorPixel;
        float iarr = 1f / (radius + radius);
        for (int y = 0; y < height; y++) {
            int outputIndex = 0;
            int li = outputIndex;
            int sourceIndex = outputIndex + radius;

            int fv = Byte.toUnsignedInt((byte) input.getRGB(outputIndex, y));
            int lv = Byte.toUnsignedInt((byte) input.getRGB(outputIndex + width - 1, y));
            float val = (radius) * fv;

            for (int j = 0; j < radius; j++) {
                val += Byte.toUnsignedInt((byte) (input.getRGB(outputIndex + j, y)));
            }

            for (int j = 0; j < radius; j++) {
                val += Byte.toUnsignedInt((byte) input.getRGB(sourceIndex++, y)) - fv;
                resultingColorPixel = Byte.toUnsignedInt(((Integer) Math.round(val * iarr)).byteValue());
                output.setRGB(outputIndex++, y, (0xFF << 24) | (resultingColorPixel << 16) | (resultingColorPixel << 8) | (resultingColorPixel));
            }

            for (int j = (radius + 1); j < (width - radius); j++) {
                val += Byte.toUnsignedInt((byte) input.getRGB(sourceIndex++, y)) - Byte.toUnsignedInt((byte) input.getRGB(li++, y));
                resultingColorPixel = Byte.toUnsignedInt(((Integer) Math.round(val * iarr)).byteValue());
                output.setRGB(outputIndex++, y, (0xFF << 24) | (resultingColorPixel << 16) | (resultingColorPixel << 8) | (resultingColorPixel));
            }

            for (int j = (width - radius); j < width; j++) {
                val += lv - Byte.toUnsignedInt((byte) input.getRGB(li++, y));
                resultingColorPixel = Byte.toUnsignedInt(((Integer) Math.round(val * iarr)).byteValue());
                output.setRGB(outputIndex++, y, (0xFF << 24) | (resultingColorPixel << 16) | (resultingColorPixel << 8) | (resultingColorPixel));
            }
        }

        return output;
    }

    // Box Blur in vertikaler Richtung (spaltenweise)
    private static FastRGB boxBlurVertical(FastRGB input, int radius) {
        FastRGB output = input.clone();

        int width = input.getWidth();
        int height = input.getHeight();

        int resultingColorPixel;
        float iarr = 1f / (radius + radius);
        for (int x = 0; x < width; x++) {
            int outputIndex = 0;
            int li = outputIndex;
            int sourceIndex = outputIndex + radius;

            int fv = Byte.toUnsignedInt((byte) input.getRGB(x, outputIndex));
            int lv = Byte.toUnsignedInt((byte) input.getRGB(x, outputIndex + height - 1));
            float val = (radius) * fv;

            for (int j = 0; j < radius; j++) {
                val += Byte.toUnsignedInt((byte) (input.getRGB(x, outputIndex + j)));
            }

            for (int j = 0; j < radius; j++) {
                val += Byte.toUnsignedInt((byte) input.getRGB(x, sourceIndex++)) - fv;
                resultingColorPixel = Byte.toUnsignedInt(((Integer) Math.round(val * iarr)).byteValue());
                output.setRGB(x, outputIndex++, (0xFF << 24) | (resultingColorPixel << 16) | (resultingColorPixel << 8) | (resultingColorPixel));
            }

            for (int j = (radius + 1); j < (height - radius); j++) {
                val += Byte.toUnsignedInt((byte) input.getRGB(x, sourceIndex++)) - Byte.toUnsignedInt((byte) input.getRGB(x, li++));
                resultingColorPixel = Byte.toUnsignedInt(((Integer) Math.round(val * iarr)).byteValue());
                output.setRGB(x, outputIndex++, (0xFF << 24) | (resultingColorPixel << 16) | (resultingColorPixel << 8) | (resultingColorPixel));
            }

            for (int j = (height - radius); j < height; j++) {
                val += lv - Byte.toUnsignedInt((byte) input.getRGB(x, li++));
                resultingColorPixel = Byte.toUnsignedInt(((Integer) Math.round(val * iarr)).byteValue());
                output.setRGB(x, outputIndex++, (0xFF << 24) | (resultingColorPixel << 16) | (resultingColorPixel << 8) | (resultingColorPixel));
            }
        }

        return output;
    }
}
