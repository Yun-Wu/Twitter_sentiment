#!bin/bash


java treePre -input expected50.txt -output bin_no_50.txt -binary
java treePre -input expected50.txt -output bin_T_50.txt -binary -tag

java -cp "*" -mx5g edu.stanford.nlp.sentiment.SentimentPipeline -file bin_no_50.txt -output probabilities > bin50_out.txt

java modelTest -fileG bin_T_50.txt -fileP bin50_out.txt -input Soft -binary > out_50.txt