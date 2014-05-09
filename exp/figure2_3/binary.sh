#!/bin/bash
head -n $1 ../dataB/fuzzy_processed_pcfg.txt > ../dataB/pcfg_$1.txt
java treePre -input ../dataB/pcfg_$1.txt -output ../dataB/pcfg_$1_in.txt -binary
java treePre -input ../dataB/pcfg_$1.txt -output ../dataB/pcfg_$1_std.txt -binary -tag

