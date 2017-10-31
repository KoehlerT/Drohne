import socket
from datetime import datetime

s = socket.socket()
host = 'Drohne'
port = 5892

s.connect((host, port))
f = open('empfangen.bmp', 'wb')
start = datetime.now()

s.send(str.encode('bild'))
print("Empfängt")
size = s.recv(32)  # Dateigröße empfangen
size = int(size, 2)  # Binärdaten zum Integer
print('empfängt ' + str(size) + ' bytes daten')
empfangen = size
chunksize = 1024
while size > 0:
    if size < chunksize:
        chunksize = size
        print('chunksize war zu groß ' + str(chunksize))
    data = s.recv(chunksize)
    f.write(data)
    size -= len(data)

c = 0
d = s.recv(512)
while d:
    f.write(d)
    d = s.recv(512)
    c += 1
f.close()
diff = datetime.now() - start
print(str(empfangen / 1000) + "KB Empfangen in " +
      str((diff.microseconds / 1000)) + " ms. " + str(c / 2) + " KB Verloren")
