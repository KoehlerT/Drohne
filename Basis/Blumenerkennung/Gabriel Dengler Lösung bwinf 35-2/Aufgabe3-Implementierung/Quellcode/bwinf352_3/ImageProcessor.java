package de.gabriel.bwinf352_3;

import de.gabriel.bwinf352_3.util.FastRGB;
import de.gabriel.bwinf352_3.util.GaussianBlur;
import de.gabriel.bwinf352_3.util.JpegReader;
import org.apache.sanselan.ImageReadException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Gabriel on 05.04.2017.
 */
public class ImageProcessor {

    private FastRGB firstImg;
    private FastRGB img;
    private HashMap<String, String> codeValues = new HashMap<>();

    public ImageProcessor(String fileName, String fileNameAsciiTabular) {
        // Bild wird eingelesen
        try {

            JpegReader reader = new JpegReader();
            BufferedImage readedImage = reader.readImage(new File(fileName));


            firstImg = new FastRGB(readedImage); // Für den schnellen Zugriff auf das Array
            img = firstImg.clone();

            for (String line : Files.readAllLines(Paths.get(fileNameAsciiTabular), Charset.forName("UTF-8"))) {
                if (!line.isEmpty()) {
                    String[] lineEntries = line.split(" ");
                    this.codeValues.put(lineEntries[0], lineEntries[1] + " " + lineEntries[2]);
                }
            }


        } catch (IOException e) {
            System.err.println("Error at opening image!");
        } catch (ImageReadException e) {
            System.err.println("Error at opening image!");
        }
    }


    public void gaussianBlur() { // Erster Schritt
        img = GaussianBlur.getGaussianBlur(img, 4);
    }

    public void doTreshhold() { // Zweiter Schritt
        // Teile das Bild zunächst in einen 2*2-Array ein
        int yParts = 14;
        int xParts = 18;

        FastRGB temporaryImg = img.clone();

        int yPartLength = img.getHeight() / yParts;
        int xPartLength = img.getWidth() / xParts;

        for (int yPart = 0; yPart <= yParts; yPart++) {
            for (int xPart = 0; xPart <= xParts; xPart++) {
                // Berechne maximale Koordinaten
                int beginningY = yPart * yPartLength;
                int beginningX = xPart * xPartLength;

                int endingY = Math.min(yPartLength * (yPart + 1), img.getHeight());
                int endingX = Math.min(xPartLength * (xPart + 1), img.getWidth());

                // Bestimme den Durchschnittswert
                int sum = 0;
                int count = 0;

                // Berechne die durchschnittliche Helligkeit
                for (int y = beginningY; y < endingY; y++) {
                    for (int x = beginningX; x < endingX; x++) {
                        int brightness = temporaryImg.getLuminance(x, y);

                        sum += brightness;
                        count++;
                    }
                }
                if (count == 0) continue;

                int average = sum / count;
                int thresholdValue = (average * 4) / 5;

                // Gehe noch mals durch und berechne erneut den optimalen Wert
                for (int y = beginningY; y < endingY; y++) {
                    for (int x = beginningX; x < endingX; x++) {
                        int brightness = temporaryImg.getRGB(x, y) & 0xFF;

                        if (brightness <= thresholdValue) img.setRGB(x, y, Color.BLACK.getRGB());
                        else img.setRGB(x, y, Color.WHITE.getRGB());

                    }
                }
            }
        }
    }

    public ArrayList<CodeCircle> findCircles() { // Dritter Schritt
        ArrayList<CodeCircle> circles = new ArrayList<>();

        ArrayList<ArrayList<int[]>> suspectedLines = new ArrayList<>();

        int stepSize = 2;

        // Schritt 1: Zählen der Schwarz-Weiß-Werte in einer Zeile
        for (int y = 0; y < img.getHeight(); y += stepSize) {
            ArrayList<Integer> steps = new ArrayList<>();
            int count = 0;
            boolean nowWhite = true;

            for (int x = 0; x < img.getWidth(); x++) {
                int colorXY = img.getRGB(x, y) & 0xFF;

                if (colorXY > 90) {
                    if (nowWhite) {
                        count += 1;
                    } else {
                        steps.add(count);
                        nowWhite = true;
                        count = 1;
                    }
                } else {
                    if (!nowWhite) {
                        count += 1;
                    } else {
                        steps.add(count);
                        nowWhite = false;
                        count = 1;
                    }
                }
            }
            steps.add(count);


            // Filtere diese Linien bezüglich Rauschen
            ArrayList<Integer> filteredSteps = new ArrayList<>();

            for (int i = 1; i < steps.size() - 1; i++) {
                // Mache eine Art "Rauschfilterung"

                int lastStepSize = steps.get(i - 1);
                int nextStepSize = steps.get(i + 1);
                int currentStepSize = steps.get(i);

                if (lastStepSize > 8 && nextStepSize > 8 && currentStepSize < 4) {
                    filteredSteps.add(lastStepSize + nextStepSize + currentStepSize);
                    i += 2; // Springe hier zwei weiter
                } else {
                    filteredSteps.add(lastStepSize);

                    // Behandle das Ende
                    if (i == steps.size() - 2) {
                        filteredSteps.add(currentStepSize);
                        filteredSteps.add(nextStepSize);
                    }
                }
            }

            steps = filteredSteps;

            // Schritt 2: Suchen nach den wichtigen Linien

            for (int i = 1; i < steps.size() - 4; i += 2) {
                // Überprüft Kreise an den Seiten

                int leftCircle = steps.get(i);
                int leftWhiteCircle = steps.get(i + 1);
                int midCircle = steps.get(i + 2);
                int rightWhiteCircle = steps.get(i + 3);
                int rightCircle = steps.get(i + 4);

                boolean isCircle = false;
                int xCoordinateCircle = 0;


                if (Math.abs(leftCircle - rightCircle) < 7 && Math.abs(leftWhiteCircle - rightWhiteCircle) < 7) {
                    // Berechne x-Coordinate des Kreises
                    for (int j = 0; j < i + 2; j++) {
                        xCoordinateCircle += steps.get(j);
                    }
                    xCoordinateCircle += midCircle / 2;
                    isCircle = true;
                }

                if (isCircle) {
                    // Hier Laufzeit ein bisschen ineffizient, hat aber keinen größeren Einfluss (vgl. Anmerkungen in der Doku)
                    boolean inLine = false;
                    for (ArrayList<int[]> foundedLine : suspectedLines) {
                        // Nehme immer letztes Element in O(1) heraus
                        int[] lastElement = foundedLine.get(foundedLine.size() - 1);

                        if (lastElement[1] == y - stepSize || lastElement[1] == y - 2 * stepSize) {
                            if (lastElement[0] == xCoordinateCircle - 2 || lastElement[0] == xCoordinateCircle - 1 ||
                                    lastElement[0] == xCoordinateCircle || (lastElement[0] == xCoordinateCircle + 1) ||
                                    lastElement[0] == xCoordinateCircle + 2) {
                                int[] newPoint = {xCoordinateCircle, y};
                                foundedLine.add(newPoint);

                                inLine = true;
                                break;
                            }
                        }
                    }

                    if (!inLine) { // Wenn nicht, füge neue Linie hinzu
                        ArrayList<int[]> newLine = new ArrayList<>();
                        int[] newPoint = {xCoordinateCircle, y};
                        newLine.add(newPoint);
                        suspectedLines.add(newLine);
                    }

                }

            }
        }

        // Schritt 3: Erschließe aus den möglichen Linien
        for (ArrayList<int[]> line : suspectedLines) {
            if (line.size() > 5) {
                int possibleRadius = line.size() / 2 * stepSize;

                int[] possibleMidPoint = line.get(possibleRadius / stepSize);
                circles.add(new CodeCircle(possibleMidPoint[0], possibleMidPoint[1], possibleRadius, img, firstImg, codeValues));

            }
        }

        // Schritt 4: Überprüfen der Kreise auf ihre Korrektheit
        ArrayList<CodeCircle> allowedCircles = new ArrayList<>();
        for (CodeCircle circle : circles) {
            circle.getDistortion();
            if (circle.getDistortion()) {
                allowedCircles.add(circle);
            }

            Graphics2D g2d = firstImg.getBufferedImage().createGraphics(); // Zeichne die gefundenen Puntke nun ein
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setPaint(Color.RED);
            g2d.fillOval(circle.getX() - 1, circle.getY() - 1, 3, 3);
            g2d.setPaint(Color.RED);
            g2d.drawOval(circle.getX() - circle.getFoundedR(), circle.getY() - circle.getFoundedR(), circle.getFoundedR() * 2, circle.getFoundedR() * 2);
            g2d.dispose();
        }


        return allowedCircles;
    }

    // Decodiere die Kreise
    public HashMap<CodeCircle, String> decodeCircles(ArrayList<CodeCircle> foundedCircles) { //Vierter Schritt
        HashMap<CodeCircle, String> results = new HashMap<>();
        HashSet<CodeCircle> notSenseMakingResults = new HashSet<>();

        for (CodeCircle circle : foundedCircles) {
            String code = circle.decodeVariableR();

            // Überprüft, ob das Ergebnis irgendwie Sinn macht
            if (code.length() > 4) {
                int count0and1 = 0;
                for (char letter : code.toCharArray()) {
                    if (letter == '0' || letter == '1') count0and1 += 1;
                }
                if (count0and1 == code.length()) {
                    notSenseMakingResults.add(circle);
                    continue;
                }
            }
            results.put(circle, code);

            Graphics2D g2d = firstImg.getBufferedImage().createGraphics(); // Zeichne die gefundenen Punkte mit entsprechenden Buchstaben nun ein
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setPaint(Color.CYAN);
            g2d.setFont(new Font("TimesRoman", Font.PLAIN, 45));
            g2d.drawString(results.get(circle), circle.getX() - 17, circle.getY() + 17);
            g2d.dispose();
        }

        // Ermittle den allgemeinen Radiusdurchschnitt aus allen Elementen
        if (results.size() > 0) {
            int radius = 0;
            for (CodeCircle circle : results.keySet()) {
                radius += circle.getFoundedR();
            }

            radius /= results.size();


            for (CodeCircle circle : notSenseMakingResults) {
                String code = circle.decodeFixedRadius(radius);

                // Überprüft, ob das Ergebnis irgendwie Sinn macht
                if (code.length() > 4) {
                    int count0and1 = 0;
                    for (char letter : code.toCharArray()) {
                        if (letter == '0' || letter == '1') count0and1 += 1;
                    }
                    if (count0and1 == code.length()) {
                        continue;
                    }
                }

                results.put(circle, code);

                Graphics2D g2d = firstImg.getBufferedImage().createGraphics(); // Zeichne die gefundenen Puntke nun ein
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setPaint(Color.CYAN);
                g2d.setFont(new Font("TimesRoman", Font.PLAIN, 45));
                g2d.drawString(results.get(circle), circle.getX() - 20, circle.getY() + 17);
                g2d.dispose();
            }


        }

        return results;
    }

    // Gebe des Bild aus
    public void showResult(int width, boolean outputFirstImg) {
        BufferedImage imageShown;

        if (outputFirstImg) {
            imageShown = firstImg.getBufferedImage();
        } else {
            imageShown = img.getBufferedImage();
        }

        BufferedImage imageShownScaled = new BufferedImage(width, (int) ((float) imageShown.getHeight() / (float) imageShown.getWidth() * width), BufferedImage.TYPE_INT_RGB);

        Graphics g = imageShownScaled.createGraphics();
        g.drawImage(imageShown, 0, 0, width, (int) ((float) imageShown.getHeight() / (float) imageShown.getWidth() * width), null);
        g.dispose();

        try {
            JFrame frame = new JFrame("Output");
            frame.getContentPane().add(new JLabel(new ImageIcon(imageShownScaled)));
            frame.pack();
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Speichere das Bild
    public void saveImage(boolean outputFirstImg, String fileName) {
        BufferedImage imageShown;

        if (outputFirstImg) {
            imageShown = firstImg.getBufferedImage();
        } else {
            imageShown = img.getBufferedImage();
        }

        try {
            ImageIO.write(imageShown, "jpg", new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}