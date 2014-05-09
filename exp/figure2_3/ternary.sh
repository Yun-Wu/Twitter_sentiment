#!/bin/bash
# experiment 3.2 ternary
head -n $1 ../dataT/fuzzy_processed_pcfg.txt > ../dataT/pcfg_$1.txt
java treePre -input ../dataT/pcfg_$1.txt -output ../dataT/pcfg_$1_in.txt 
java treePre -input ../dataT/pcfg_$1.txt -output ../dataT/pcfg_$1_std.txt -tag

