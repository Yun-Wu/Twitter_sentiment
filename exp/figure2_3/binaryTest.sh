#!/bin/bash
for i in `seq 1 26`
do	
	java modelTest -fileG ../dataB/pcfg_$1_std.txt -fileP ../dataB/pcfg_${i}_out.txt -binary -input soft > ../dataB/result_${i}.txt
 tail ../dataB/result_${i}.txt
done
