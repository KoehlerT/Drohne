import socket
import subprocess
from time import sleep

host = ''
port = 5893

# Verbinde mit server
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((host, port))
print("Warte auf Verbindung")
s.listen(1)

conn, addr = s.accept()
print("Verbunden mit: ", addr)

# Sendet ununterbrochen Daten
while True:
    # Cpu Temperatur ermitteln
    # Temporär! Wird später von Sensorenleseklasse übernommen
    output = subprocess.check_output(["/opt/vc/bin/vcgencmd", "measure_temp"])
    # Format: temp=37.9'C
    zahl = output[5:-3].zfill(16)
    conn.send(zahl)
    sleep(1)
