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
    print("Empfangen : ", data)
    if data == "temp":
        output = subprocess.check_output(["/opt/vc/bin/vcgencmd", "measure_temp"])
        conn.send(output)
