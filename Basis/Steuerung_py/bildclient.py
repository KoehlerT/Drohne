import socket
from datetime import datetime
# from System.IO import MemoryStream  # Test mit MemoryStream. Wohl eleganteste Lösung

s = socket.socket()  # Socket Objekt. Übernimmt das Verbinden
host = 'Drohne'  # Name des RasPi im Netzwerk. Funktioniert. Braucht prinzipiell keine stat. IP
port = 5892  # Random Name

loop = True
s.connect((host, port))

while loop:
    # Bilddatei in die die empfangenen Daten geschrieben werden # wb: write binary
    f = open('empfangen.bmp', 'wb')
    # m = MemoryStream()
    start = datetime.now()  # Startzeit. Zeigt die Zeit zum empfangen eines Frames an

    # Sendet anforderung Bild zu senden. Wird weg müssen (Server wartet auf anforderung)
    s.send(str.encode('bild'))
    print("Empfängt")
    size = s.recv(32)  # Dateigröße empfangen. Bild ist gerade ein byte (sowas in der Art)
    size = int(size, 2)  # konvertierung zu einer ganzen Zahl
    print('empfängt ' + str(size) + ' bytes daten')
    empfangen = size  # Speichert die Dateigröße
    chunksize = 1024  # Wie viele Daten werden maximal empfangen. Sollte mit Buffer size im Bildserver übereinstimmen
    while size > 0:  # Solange es noch Daten zum empfagnen gibt
        if size < chunksize:  # Wenn weniger Daten übrig bleiben als die erwartete Blockgröße ist
            chunksize = size
        data = s.recv(chunksize)  # Empfängt daten (max. chunksize)
        f.write(data)  # Daten in Datei. Muss bald im Arbeitsspeicher passieren
        size -= len(data)  # von den erwarteten Daten die Empfangenen abziehen
        # m.write(data)

    f.close()  # Dateibearbeiten beenden
    diff = datetime.now() - start  # Zeitdifferenz von Start zu jetzt
    print(str(empfangen / 1000) + "KB Empfangen in " + str((diff.microseconds / 1000)) + " ms. ")
    loop = False  # Loop variable, damit das Programm nach einem durchlauf beendet. Final wird das nicht mehr so sein
