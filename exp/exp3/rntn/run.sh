#!/bin/bash
java treePre -input movie_std.txt -output movie_in.txt -binary
java -cp "*" -mx5g movie_in.txt -output probabilities > movie_out.txt
java modelTest -fileG movie_std.txt -fileP movie_out.txt -binary -hardD -input soft > rntn_hard.txt
java modelTest -fileG movie_std.txt -fileP movie_out.txt -binary -input soft > rntn_soft.txt
