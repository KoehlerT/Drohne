# Imports
import sensorfenster
import sensorclient
import threading

version = '0.1'
name = '<cooler Gruppenname>'
print('Drohnensteuerung ' + version + ' der ' + name + ' gestartet')

# Sensorclient starten
cSensor = sensorclient.sensorclient()
cSensorThread = threading.Thread(target=cSensor.run, args=())
cSensorThread.start()

# GUI Starten
sFenster = sensorfenster.WerteFenster(cSensor)
sFensterThread = threading.Thread(target=sFenster.run, args=())
sFensterThread.start()
