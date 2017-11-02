import socket
from datetime import datetime
from System.IO import MemoryStream
import wx

s = socket.socket()
host = 'Drohne'
port = 5892

loop = True
s.connect((host, port))

while loop:
    f = open('empfangen.bmp', 'wb')
    m = MemoryStream()
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
        data = s.recv(chunksize)
        f.write(data)
        size -= len(data)
        m.write(data)

    f.close()
    diff = datetime.now() - start
    print(str(empfangen / 1000) + "KB Empfangen in " + str((diff.microseconds / 1000)) + " ms. ")
    loop = False
