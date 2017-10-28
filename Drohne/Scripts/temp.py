import subprocess
import datetime
import socket

TCP_IP = ''
TCP_Port = 5892
BUFFER_SIZE = 1024

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((TCP_IP, TCP_Port))
print("Warte auf Verbindung")
s.listen(1)

conn, addr = s.accept()
print("Verbunden mit: ", addr)
while True:
    data = conn.recv(BUFFER_SIZE)
    if not data:
        break
    if data == "temp":
        output = subprocess.check_output(["/opt/vc/bin/vcgencmd", "measure_temp"])
        conn.send(output)
    elif data == "bild":
        print("Bild wird geoeffnet")
        bild = open("../Bilder/pic1.bmp", 'rb')
        data = bytearray(bild.read())
        print("Bild wird konvertiert")
	senden = str(data)
	print('Bild wird gesendet '+str(len(senden))+' Stringlaenge '+str(len(data))+' Arraylaenge')
	conn.send(senden)
	print("Bild wurde gesendet. Fertig!")
    else:
        print('Unbekannte Anweisung: ' + data)
