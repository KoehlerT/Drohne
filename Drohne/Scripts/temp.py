from subprocess import call
import time

while True:
    call(["/opt/vc/bin/vcgencmd", "measure_temp"])
    time.sleep(0.5)
