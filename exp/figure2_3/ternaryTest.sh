#!/bin/bash
for i in `seq 1 19`
do
	java modelTest -fileG ../dataT/pcfg_$1_std.txt -fileP ../dataT/pcfg_${i}_out.txt -input soft > ../dataT/result_${i}.txt
	tail ../dataT/result_${i}.txt
done