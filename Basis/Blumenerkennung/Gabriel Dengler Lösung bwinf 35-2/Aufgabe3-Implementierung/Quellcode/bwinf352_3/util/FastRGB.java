package de.gabriel.bwinf352_3.util;

import java.awt.*;
import java.awt.image.*;

/**
 * Created by Gabriel on 18.04.2017.
 */
public class FastRGB implements Cloneable {
    private BufferedImage img;
    private int width;
    private int height;

    private int[] pixels;

    public FastRGB(BufferedImage image) {
        this.img = image;

        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        width = image.getWidth();
        height = image.getHeight();
    }

    public int[] getPixels() {
        return pixels;
    }

    public void setPixels(int[] pixels) {
        int pos = 0;
        // Kopiere die Pixels
        for (int i : this.pixels) {
            this.pixels[pos] = pixels[pos++];
        }

    }

    // Methode zum Bestimmen der Helligkeit
    public int getLuminance(int x, int y) {
        Color color = new Color(this.getRGB(x, y));

        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();

        return (int) (red * 0.2126f + green * 0.7152f + blue * 0.0722f);
    }



    public int getRGB(int x, int y) {
        // Direktes Erhalten des Pixels aus dem Bild
        int pos = y * width + x;
        return pixels[pos];

    }

    public void setRGB(int x, int y, int rgb) {
        // Direktes Setzten des Pixels in das Bild
        int pos = y * width + x;
        pixels[pos] = rgb;

    }

    public BufferedImage getBufferedImage() {
        // Keine weiteren Änderungen notwendig, da ja Objekt des Bildes selbst geändert
        return this.img;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public FastRGB clone() {
        ColorModel cm = img.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = img.copyData(null);
        return new FastRGB(new BufferedImage(cm, raster, isAlphaPremultiplied, null));
    }

}
