espeakParams="-ven-us+f4 -s130"

rm out.mp4
rm *.wav
rm *.png

espeak $espeakParams "Here is my phrase one." -w 1.wav
espeak $espeakParams "And there is no second phrase." -w 2.wav

convert -size 320x240 -pointsize 70 label:frame1 1.png
convert -size 320x240 -pointsize 70 label:frame2 2.png

melt \
  -track 1.wav 2.wav \
  -track -blank 20 1.png out=30 -blank 20 2.png out=60 \
  -consumer avformat:out.mp4 acodec=aac ab=128k vcodec=libx264 b=5000k
