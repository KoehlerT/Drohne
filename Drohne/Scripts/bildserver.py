import subprocess
import sys
import socket
import os

TCP_IP = ''
TCP_Port = 5892
BUFFER_SIZE = 1024

if len(sys.argv) == 1:
    pfad = "../Bilder/pic1.bmp"
else:
    pfad = '../Bilder/' + sys.argv[1]

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((TCP_IP, TCP_Port))
print("Warte auf Verbindung")
s.listen(1)

conn, addr = s.accept()
print("Verbunden mit: ", addr)
while True:
    data = conn.recv(BUFFER_SIZE).decode('utf-8')
    if not data:
        break
    if data == "temp":
        output = subprocess.check_output(["/opt/vc/bin/vcgencmd", "measure_temp"])
        conn.send(output)
    elif data == "bild":
        print("Bildgroeße wird ermittelt")
        size = os.path.getsize(pfad)
        size = bin(size)[2:].zfill(32)  # Size: 32 bit binär
        conn.send(size.encode('utf-8'))
        print("Bild wird geoeffnet")
        bild = open(pfad, 'rb')
        print('Bild wird gesendet')
        data = bild.read()
        conn.sendall(data)
        bild.close()
        print(str(int(size, 2) / float(1000)) + " KB Bild wurde gesendet. Fertig!")
    else:
        print('Unbekannte Anweisung: ' + data)
