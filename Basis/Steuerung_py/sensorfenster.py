from tkinter import Label, Tk, Frame, W, E, StringVar, Button
from tkinter import *


class WerteFenster:
    # events (neue Werte)

    def get_temp(self):
        temp = self.client.temperatur
        self.textTemp.set(str(temp) + '°C')
        self.root.after(1000, self.get_temp)

    def quit(self):
        self.client.loop = False
        self.root.quit()

    def __init__(self, sensorCl):
        # TemperaturLabel
        self.client = sensorCl

    def run(self):
        self.root = Tk()  # Root: Fenster
        self.root.title("Sensorwerte")
        self.root.geometry("500x600")

        frame = Frame(self.root)
        frame.grid()
        self.textTemp = StringVar()
        Label(frame, text="CPU Temperatur: ").grid(row=0, column=0, sticky=W)
        labelTemp = Label(frame, textvariable=self.textTemp)
        labelTemp.grid(row=0, column=1, sticky=E, pady=5)

        # Beispiele
        Label(frame, text="Beschleunigung: ").grid(row=1, column=0, sticky=W, pady=5)
        Label(frame, text="Geschwindigkeit: ").grid(row=2, column=0, sticky=W, pady=5)
        Label(frame, text="Höhe: ").grid(row=3, column=0, sticky=W, pady=5)
        Label(frame, text="Ort: ").grid(row=4, column=0, sticky=W, pady=5)
        Label(frame, text="Außentemperatur: ").grid(row=5, column=0, sticky=W, pady=5)
        Label(frame, text="Himmelsrichtung: ").grid(row=6, column=0, sticky=W, pady=5)
        # Update Button
        button = Button(frame, text="Beenden", anchor="w", command=self.quit)
        button.grid(row=10, column=1, sticky=E)
        self.root.after(1000, self.get_temp)
        self.root.mainloop()
