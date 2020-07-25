import sys
sys.path.append("/data/data/net.pupil.newlife/assets/python/PIL")
# sys.path.append("/data/data/net.pupil.newlife/assets/python/PIL/lib_imaging.so")

# from ctypes import cdll
# cdll.LoadLibrary('PIL/_imaging.so')
# cdll.LoadLibrary('PIL/_imagingmath.so')
# cdll.LoadLibrary('PIL/_imagingmorph.so')
# cdll.LoadLibrary('PIL/_imagingtk.so')

from PIL import Image

# img1 = ImageGrab.grab((0, 0, 600, 400))
# img1.resize((20, 20), Image.ANTIALIAS).convert("L")
# print('pil data-------', list(img1.getdata()))

print('Imageeeeeeeeeeeeeeeeeeeeee', Image.ANTIALIAS)
im=Image.open("/sdcard/chapter.png")
print(im.size)
print(im.mode)
