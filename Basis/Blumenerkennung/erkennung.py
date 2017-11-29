import cv2
import numpy as np

bild = cv2.imread("bsp.JPG")
img = cv2.GaussianBlur(bild, (25, 25), 0)
hsv = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)
lower_range = np.array([0, 0, 0], dtype=np.uint8)
upper_range = np.array([50, 50, 50], dtype=np.uint8)
img = cv2.inRange(hsv, lower_range, upper_range)
cv2.imshow('image', img)
while True:
    if cv2.waitKey(10) & 0xFF == ord('q'):
        break
