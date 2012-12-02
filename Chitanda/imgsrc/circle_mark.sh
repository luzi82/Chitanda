#!/bin/bash

convert -size 256x256 "xc:#FFFFFF00" \
 -stroke "#FFFFFFFF" -strokewidth 16 \
 -fill "#FFFFFF7F" \
 -draw "circle 127.5,127.5 127.5,7.5" \
 $1
