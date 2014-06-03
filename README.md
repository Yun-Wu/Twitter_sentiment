Twitter sentiment analysis
==========================

Content
----------------------
- Preprocessing (Yun)
  - fuzzy process with string pattern
  - replace emoticons and slang according to dictionary
  - use word clustering to help parse tweets
- Bag of word method  (Qiren)
  - SVM
  - Naive Bayes
  - Maximum Entropy
- Sentiemnt treebank  (Xiaofan)
  - Existing model 
  - Bootstrapping. 


Corpus:
----------------------------------
- Movie review (from original paper)
- Movie related tweet (collecting)
- General tweet (avaible soon)

Experiment:
----------------------------------
- Single sentence sentiment
- Multiple sentences sentiment
- SVM with different features
- Cross-domain traning

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
