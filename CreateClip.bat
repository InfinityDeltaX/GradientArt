mencoder *.png w=440:h=440:type=jpg -noskip -ovc lavc \ -lavcopts vcodec=mpeg4:mbd=2:trell -oac copy -o output.avi
PAUSE