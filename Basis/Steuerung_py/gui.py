from tkinter import *

root = Tk()
root.title("Drohnensteuerung")
root.geometry("500x600")

app = Frame(root)
app.grid()

label = Label(app, text="CPU - Temperatur: ")
label.grid()
button = Button(app, text="GET")
button.grid()

root.mainloop()
