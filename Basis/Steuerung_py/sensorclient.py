import socket


class sensorclient:

    def __init__(self):
        print("Verbindungsaufbau...")
        self.s = socket.socket()
        host = 'Drohne'
        port = 5893
        self.s.connect((host, port))
        self.temperatur = float()
        self.loop = True
        print("Client - Sensorenauslesung gestartet")

    def run(self):
        while self.loop:
            data = self.s.recv(16)
            if data:
                self.temperatur = float(data)
