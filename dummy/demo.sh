rm *.jpg
rm *.png

# generate images using ImageMagik
for i in {001..010};
do
    convert -size 320x240 -pointsize 70 label:frame${i} ${i}.png
done

# take all images and generate a video
# force overwrite
# take 2 images per second
# make output stream have 30 FPS
ffmpeg -y -framerate 2 -i %03d.png -r 30 out.mp4

# take video and extract 1 frame
# time = 0.1s
# thumbnail size 160x120
ffmpeg -i out.mp4 -ss 0.1 -s 160x120 -vframes 1 thumbnail.jpg
