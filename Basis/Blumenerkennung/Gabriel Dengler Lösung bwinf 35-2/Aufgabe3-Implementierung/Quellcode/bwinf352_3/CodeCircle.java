package de.gabriel.bwinf352_3;

import de.gabriel.bwinf352_3.util.FastRGB;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Gabriel on 04.04.2017.
 */
public class CodeCircle {

    private int x; // x-Koorinate
    private int y; // y-Koordinate
    private int foundedR; // Radius
    private FastRGB img;
    private FastRGB firstImg;

    private ArrayList<Integer> realRadia;
    private HashMap<String, String> codeValues;

    public CodeCircle(int x, int y, int foundedR, FastRGB img, FastRGB firstImg, HashMap<String, String> codeValues) {
        this.x = x;
        this.y = y;
        this.foundedR = foundedR;

        this.img = img;
        this.firstImg = firstImg;
        this.codeValues = codeValues;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getFoundedR() {
        return foundedR;
    }

    public boolean getDistortion() { // Ermittle Verzerrung des Kreises und überprüfe, inwiefern der Kreis die vorgegebenen Bedingungen erfüllt
        if (foundedR < 12) return false;

        this.realRadia = new ArrayList<>();

        for (int angle = 0; angle < 360; angle += 1) {
            try {
                boolean hasVisitInnerCircle = false;

                int[] point1 = null; // äußester Punkt des inneren Kreises
                int[] point2 = null; // innerer Punkt des äußeren Kreises Kreises

                // Berechnet jeweils die Entfernungen vom Mittelpunkt aus, betrachte dabei inneren Kreis bis hin zum ersten äußeren Ring
                for (int distance = 0; distance < 3 * foundedR; distance++) {
                    int xCoordinate = x + (int) (distance * Math.cos(Math.toRadians(angle)));
                    int yCoordinate = y + (int) (distance * Math.sin(Math.toRadians(angle)));

                    if (img.getLuminance(xCoordinate, yCoordinate) < 90) {
                        if (hasVisitInnerCircle) {
                            point2 = new int[]{xCoordinate, yCoordinate};
                            break;
                        }
                    } else {
                        hasVisitInnerCircle = true;
                        if (point1 == null) point1 = new int[]{xCoordinate, yCoordinate};
                    }
                }

                if (point1 != null && point2 != null) {
                    float innerCircleDistance = (float) Math.sqrt(Math.pow(Math.abs(x - point1[0]), 2) + Math.pow(Math.abs(y - point1[1]), 2));
                    float distanceToSecondCircle = (float) Math.sqrt(Math.pow(Math.abs(point1[0] - point2[0]), 2) + Math.pow(Math.abs(point1[1] - point2[1]), 2));

                    // Überprüfen, ob diese gefundene Menge den Anforderungen entspricht
                    if (Math.abs(1f / 1.5 * innerCircleDistance - distanceToSecondCircle) < 15 && Math.abs((innerCircleDistance + 1.5 / 1f * distanceToSecondCircle) / 2 - foundedR) < 15) {
                        realRadia.add((int) (innerCircleDistance + 1.5 / 1f * distanceToSecondCircle) / 2);
                    } else {
                        realRadia.add(0);
                    }
                } else {
                    realRadia.add(0); // Sonst füge hier einfach 0 hinzu
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                realRadia.add(0);
            }
        }

        // Schaut, ob es sich um einen Kreis handeln kann (auch in Hinsicht auf bwinf-challenge-test-cam8.jpg)
        int zeroCount = Collections.frequency(realRadia, 0);
        if (zeroCount > 320) {
            return false;
        }

        // Kommen Radien für einen Winkel alpha vor, die keinen Sinn machen, bilde Mittelwerte
        if (zeroCount > 0) {
            for (int i = 0; i < 360; i++) {
                if (realRadia.get(i) == 0) {
                    // Bestimmt die letzten drei Werte, die vorkommt und bildet daraus einen gewichteten Durchschnitt
                    int averageDistance = 0;

                    int skipedElements = 0;
                    for (int j = 1; j <= 3 + skipedElements; j++) {
                        int nextIndex = i - j;
                        if (nextIndex < 0) nextIndex += 360;
                        int nextRadius = realRadia.get(nextIndex);

                        if (nextRadius != 0) {
                            averageDistance += (j - skipedElements) * nextRadius;
                        } else skipedElements++;
                    }

                    realRadia.set(i, averageDistance / 6);

                }
            }
        }
        return true;
    }

    // Decodiert mit unterschiedlichem Durchschnitt
    public String decodeVariableR() {
        boolean[] binaryCodes = new boolean[360];
        try {
            for (int angle = 0; angle < 360; angle++) {
                int currentRadius = this.realRadia.get(angle);
                if (currentRadius == 0) continue;

                int blackCount = 0;
                int whiteCount = 0;

                // Zählt die Anzahl der verschiedenen Farben zusammen
                for (int distance = (int) ((1.5f + 3f) / 1.5f * (float) currentRadius); distance < (int) ((1.5f + 4f) / 1.5f * (float) currentRadius); distance++) {
                    int xCoordinate = x + (int) (distance * Math.cos(Math.toRadians(angle)));
                    int yCoordinate = y + (int) (distance * Math.sin(Math.toRadians(angle)));

                    if (img.getLuminance(xCoordinate, yCoordinate) < 90) {
                        blackCount += 1;
                    } else {
                        whiteCount += 1;
                    }
                }

                binaryCodes[angle] = (blackCount * 8 > whiteCount); // Füge hinzu, wenn genügend schwarze Punkte vorhanden sind

                // Zusätzliches Malen
                for (int distance = (int) ((1.5f + 3f) / 1.5f * (float) currentRadius); distance < (int) ((1.5f + 4f) / 1.5f * (float) currentRadius); distance++) {
                    int xCoordinate = x + (int) (distance * Math.cos(Math.toRadians(angle)));
                    int yCoordinate = y + (int) (distance * Math.sin(Math.toRadians(angle)));

                    if (blackCount * 8 > whiteCount) {
                        firstImg.setRGB(xCoordinate, yCoordinate, Color.RED.getRGB());
                    } else {
                        firstImg.setRGB(xCoordinate, yCoordinate, Color.GREEN.getRGB());
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            // Hier ist Randbild -> rausschmeißen
            return "";
        }

        return decodeBooleanArray(binaryCodes, true);
    }

    public String decodeFixedRadius(int fixedR) {
        boolean[] binaryCodes = new boolean[360];
        try {

            // Geht mit allen Winkeln durch, mache aber keine Transformation
            for (int angle = 0; angle < 360; angle++) {
                int blackCount = 0;
                int whiteCount = 0;

                for (int distance = (int) ((1.5f + 3f) / 1.5f * (float) fixedR); distance < (int) ((1.5f + 4f) / 1.5f * (float) fixedR); distance++) {
                    int xCoordinate = x + (int) (distance * Math.cos(Math.toRadians(angle)));
                    int yCoordinate = y + (int) (distance * Math.sin(Math.toRadians(angle)));

                    if (img.getLuminance(xCoordinate, yCoordinate) < 90) {
                        blackCount += 1;
                    } else {
                        whiteCount += 1;
                    }
                }

                binaryCodes[angle] = (blackCount * 8 > whiteCount); // Füge hinzu, wenn überwiegend schwarze Punkte vorhanden sind

                // Zusätzliches Malen (nur ein Test)
                for (int distance = (int) ((1.5f + 3f) / 1.5f * (float) fixedR); distance < (int) ((1.5f + 4f) / 1.5f * (float) fixedR); distance++) {
                    int xCoordinate = x + (int) (distance * Math.cos(Math.toRadians(angle)));
                    int yCoordinate = y + (int) (distance * Math.sin(Math.toRadians(angle)));

                    if (blackCount * 8 > whiteCount) {
                        firstImg.setRGB(xCoordinate, yCoordinate, Color.RED.getRGB());
                    } else {
                        firstImg.setRGB(xCoordinate, yCoordinate, Color.GREEN.getRGB());
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            // Hier ist Randbild -> rausschmeißen
            return "";
        }

        // Decodiert das ermittelte Booleanarray
        return decodeBooleanArray(binaryCodes, false);

    }

    // Mache hier eine Transformation der Boolean Arrays bei Ellipsen
    public boolean[] transformBooleanArray(boolean[] binaryCodes) {
        // Berechne Verhältnis und Drehung
        int maxIndex = 0;
        for (int i = 0; i < 180; i++) {
            if (this.realRadia.get(i) - this.realRadia.get(i + 90) > this.realRadia.get(maxIndex) - this.realRadia.get(i + 90))
                maxIndex = i;
        }

        float ratio = ((float) this.realRadia.get(maxIndex + 90)) / ((float) this.realRadia.get(maxIndex));

        if (ratio > 0.9) {
            return binaryCodes;
        } else {
            boolean[] transformedBinaryCodes = new boolean[360];

            // Mache Unterscheidung, wie in Doku besprochen wurde
            for (int i = 0; i < 360; i++) {
                int transformedAngle = 0;

                if (i % 90 == 0) transformedAngle = i;
                else if (0 <= i && i < 90) {
                    transformedAngle = (int) Math.toDegrees(Math.atan(ratio * Math.tan(Math.toRadians(i))));
                } else if (90 <= i && i < 180) {
                    transformedAngle = 180 + (int) Math.toDegrees(Math.atan(ratio * Math.tan(Math.toRadians(i))));
                } else if (180 <= i && i < 270) {
                    transformedAngle = 180 + (int) Math.toDegrees(Math.atan(ratio * Math.tan(Math.toRadians(i))));
                } else if (270 <= i && i < 360) {
                    transformedAngle = 360 + (int) Math.toDegrees(Math.atan(ratio * Math.tan(Math.toRadians(i))));
                }

                transformedBinaryCodes[i] = binaryCodes[Math.floorMod(transformedAngle + maxIndex, 360)];
            }

            return transformedBinaryCodes;
        }
    }

    public String decodeBooleanArray(boolean[] binaryCodes, boolean useRatio) {
        if (binaryCodes.length == 0) {
            return "";
        }

        StringBuilder code = new StringBuilder();

        // Berechne das Verhältnis der Ellipse, falls gefordert
        if (useRatio) binaryCodes = transformBooleanArray(binaryCodes);

        boolean hasChanges = false;
        boolean lastElement = binaryCodes[0];
        int beginningIndex = 1;
        for (int i = 1; i < 360; i++) {
            if (lastElement != binaryCodes[i]) {
                beginningIndex = i + 1;
                hasChanges = true;
                break;
            }
            lastElement = binaryCodes[i];
        }

        if (!hasChanges) { // Überall selbes Element
            if (binaryCodes[0]) code.append("1111111111111111");
            else code.append("0000000000000000");

        } else { // Sonst mindestens ein Wechsel vorhanden
            int lastColorElements = 1; // Setze zuerst das richtige Element hin und versuche dann, zuzuordnen
            boolean lastColor = binaryCodes[beginningIndex - 1];
            for (int i = beginningIndex; i < beginningIndex + 360; i++) {
                int realIndex = i % 360; // Berechne den richtigen Index

                boolean newColor = binaryCodes[realIndex];
                if (lastColor == newColor) {
                    lastColorElements += 1;
                } else {
                    int codeSegments = (int) Math.round((float) lastColorElements / 22.5);


                    for (int codeSegment = 0; codeSegment < codeSegments && code.length() <= 16; codeSegment++) {
                        if (lastColor) code.append("1");
                        else code = code.append("0");
                    }


                    // Fange wieder von vorne an
                    lastColorElements = 1;


                }
                lastColor = newColor; // Die letzte Farbe ist gleich der neuen Farbe
            }
        }



        // Ordnet den Binärcode einem Buchstaben zu
        String rawCode = code.toString();

        if (rawCode.length() == 16) {
            for (int i = 0; i < 16; i++) {
                if (codeValues.containsKey(rawCode)) {
                    return codeValues.get(rawCode);
                }

                rawCode = rawCode.substring(15) + rawCode.substring(0, 15); // Setzte jeweils den ersten Buchstaben an den Anfang
            }
        }

        return rawCode;
    }

}