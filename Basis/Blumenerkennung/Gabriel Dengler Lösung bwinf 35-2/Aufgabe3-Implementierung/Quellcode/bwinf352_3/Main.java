package de.gabriel.bwinf352_3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

import static jdk.nashorn.internal.objects.NativeArray.lastIndexOf;

/**
 * Created by Gabriel on 03.04.2017.
 */
public class Main {

    public static void main(String[] args) {
        // Einlesen der Dateinamen
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\\n");

        System.out.print("Gebe den Datei-Namen an: (Dieser sollte sich im selben Ordner wie die Jar-Datei befinden) ");
        String fileName = scanner.next();

        System.out.print("Gebe den Namen der Kreiscode-Binary-Datei an: ");
        String scannerFileName = scanner.next();

        System.out.print("Möchten Sie die Ausgabe abspeichern? [Ja, Nein] ");
        String saveImageInput = scanner.next();

        boolean saveImage = saveImageInput.toLowerCase().contains("ja");

        // Einlesen der Dateien
        long start_time = System.nanoTime();
        ImageProcessor imageProcessor = new ImageProcessor(fileName, scannerFileName);

        long readTime = System.nanoTime();

        // Ausführen der Vorbearbeitung
        imageProcessor.gaussianBlur();
        long gaussianTime = System.nanoTime();
        imageProcessor.doTreshhold();
        long treshholdTime = System.nanoTime();



        // Decodieren der einzelnen Elemente
        ArrayList<CodeCircle> codes = imageProcessor.findCircles();

        long findCircleTime = System.nanoTime();

        HashMap<CodeCircle, String> result = imageProcessor.decodeCircles(codes);

        ArrayList<CodeCircle> sortedCircles = new ArrayList<>();
        sortedCircles.addAll(result.keySet());
        sortedCircles.sort((CodeCircle code1, CodeCircle code2) -> (code1.getY() - code2.getY()));

        for (CodeCircle code : sortedCircles) {
            if (!result.get(code).equals("")) {
                System.out.println("x: " + code.getX() + " y: " + code.getY() + " r: " + code.getFoundedR() + " -> " +
                        result.get(code));
            }
        }

        long decodeTime = System.nanoTime();

        // Ausgabe des Bildes
        if (!saveImage) imageProcessor.showResult(1800, true);
        else imageProcessor.saveImage(true, fileName.substring(0, fileName.lastIndexOf(46)) + " - Ausgabe.jpg");


        long end_time = System.nanoTime();
        System.out.println("Einlesezeit: " + (readTime - start_time) * 0.000000001 + " s");
        System.out.println("Gauß-Filter-Zeit: " + (gaussianTime - readTime) * 0.000000001 + " s");
        System.out.println("Schwellenwertverfahren-Zeit: " + (treshholdTime - gaussianTime) * 0.000000001 + " s");
        System.out.println("Kreis-Finden-Zeit: " + (findCircleTime - treshholdTime) * 0.000000001 + " s");
        System.out.println("Decodier-Zeit: " + (decodeTime - findCircleTime) * 0.000000001 + " s");
        System.out.println("Komplette Zeit: " + (end_time - start_time) * 0.000000001 + " s");

    }

}
