WiseOwl
This is a Fact based Question Answering System using Apache Solr as backend search engine, Wikipedia dumps as information source, Apache velocity , Html, Css for Web interface Design. The project also uses Linux bash script to perform its various functions like start,stop,training and indexing.
Features:
* Fast and reliable searching using open source Apache Solr 6.3.0 and Apache Lucene 6.3.0 projects. Apache Solr is used as a search engine which uses capabilities of Apache Lucene to profide searching.
* Custom-made Query Parser based on Apache Lucene 6.3.0 specially optimized for Question Answering.
* Named Entity Recognition and Time normalization during indexing using StanfordCoreNLP.
* Automatic cleaning and parsing of Wikipedia Raw text from the wikipedia dumps. It is achieved by using Lucene 6.3 benchmark classes and WikiClean Project.
* Answer Type Classification of given question using Apache OpenNLP's Maxent Models, Models are trained on data taken from thesis by Tom Morton, tagging aroung 1800 handnpicked questions. 
* Currently the project is more optimised for Description Type Answers.
* Sleek user interface by combining elements of css, html and Apache Velocity.
* Bash script which uses underlying solr scripts to provide functionality of starting, stoping, indexing and training.
