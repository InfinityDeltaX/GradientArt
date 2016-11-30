ffmpeg -framerate 25 -f image2 -i 'output-%d.png' -vcodec libx264 out.mp4
PAUSE
ffmpeg -r 25 -f image2 -i output%d.png -qscale 0 output.avi
PAUSE