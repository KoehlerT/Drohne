import subprocess
import sys
import socket
import os

TCP_IP = ''  # IP muss verwendet werden asdf das ist ein TEST
TCP_Port = 5892  # random Port. Der gleiche wie bei bildclient
BUFFER_SIZE = 1024  # Maximale sendegröße

if len(sys.argv) == 1:  # Zu übertragnedes Bild ermitteln (Startparameter)
    pfad = "../Bilder/pic1.bmp"
else:
    pfad = '../Bilder/' + sys.argv[1]

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)  # Socket Objekt zum Senden
s.bind((TCP_IP, TCP_Port))  # Port und Addresse zuweisen
print("Warte auf Verbindung")
s.listen(1)  # Maximal eine Verbindung erlauben! Wartet, bis Verbindung aufgebaut wurde

conn, addr = s.accept()
print("Verbunden mit: ", addr)
while True:
    data = conn.recv(BUFFER_SIZE).decode('utf-8')  # Empfängt anweisung von Basis. To go
    if not data:
        break  # Break = Continue in diesem Fall
    if data == "temp":
        output = subprocess.check_output(["/opt/vc/bin/vcgencmd", "measure_temp"])
        conn.send(output)  # Funktionalität, wird von Sensorserver / -client übernommen. To go
    elif data == "bild":
        print("Bildgroeße wird ermittelt")
        size = os.path.getsize(pfad)  # Bildgröße ermitteln
        size = bin(size)[2:].zfill(32)  # Integer zu einer 32 bit Binärfolge.
        conn.send(size.encode('utf-8'))  # Binörfolge zu einem String, zum Senden
        print("Bild wird geoeffnet")
        bild = open(pfad, 'rb')  # Bilddatei öffnen rb: read binary
        print('Bild wird gesendet')
        data = bild.read()  # Binäres einlesen des Bildes
        conn.sendall(data)  # Senden der Daten. Chunksize wird in dieser Version nicht verwendet
        bild.close()  # Datei schließen
        print(str(int(size, 2) / float(1000)) + " KB Bild wurde gesendet. Fertig!")
    else:
        print('Unbekannte Anweisung: ' + data)
