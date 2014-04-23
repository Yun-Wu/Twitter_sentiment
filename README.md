Twitter sentiment analysis
==========================

What to do?
----------------------
- Preprocessing (Yun)
  - target removal 
  - url removal 
  - slang translation 
  - spell correction with noisy channel model
     http://alias-i.com/lingpipe/demos/tutorial/querySpellChecker/read-me.html
  - word clustering
  - ...
- Bag of word method  (Qiren)
  - SVM
  - Naive Bayes
  - Maximum Entropy
- Sentiemnt treebank  (Xiaofan)
  - Existing model 
  - Bootstrapping. (like HW3) 
- Tweet corpus (Xiaofan)


Experiment:
----------------------------------
- Movie review (from original paper)
- Movie related tweet (collecting)
- General tweet (avaible soon)



Component:
-------------------------------------

- SlangRM
  translate slangs to corresponding plain English using slang dictionary. 
  Usage: [dictName] [inputName] [outputName]
  Example:
  -> remember when i used to have a boyfriend and he tried to hack my account bc i do lmao¬
  <- remember when i used to have a boyfriend and he tried to hack my account because i do laughing my a** off 


- tweetCorpus.txt
  annotated tweet sentiment corpus from http://www.cs.york.ac.uk/semeval-2013/task2/index.php?id=data. I combined the training set and test set. This can be used as our general tweet corpus. 
