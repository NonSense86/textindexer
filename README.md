Information Retrieval - Text Indexer
=====

Developer:
Petar Petrov 0508142
Filip Hianik 0426173

## Install and Run
Generally 'mvn clean install' should donwload all libraries and setup the environment.
With 'mvn assembly:assembly' you can generate an executable which is copied in the target folder
and it's called bwoindexer-jar-with-dependencies.

### Unix:
After building the project with maven you can import it in your favorite IDE and run the App.java
class or use the generated fat jar as described above.
If you choose the jar you have to provide the config and the collection path.
The config file is optional. If you don't provide config it will use the default. There is more info
about that in the config section.
The command is as follows.
java -Xmx1024 -jar bowindexer.jar [config_path] [collection]

### Windows
In 2 Easy steps:
1: Install linux
2: go to section Unix

In more (painful) steps:
Because of the mapreduce jobs you will have to install cygwin and run the program in a specific environment.
There are many resources on the web how to do this. Here is the first one i found.
http://pi3.informatik.uni-mannheim.de/downloads/hauptstudium/dbs2/hws10/Small_tutorial_for_installing_Hadoop_MapReduce_on_Windows.pdf

## Config File
==
It is a simple .properties file in the form:

at.tuwien.ir.textindexer.weighting=bool
at.tuwien.ir.textindexer.stemming=false
at.tuwien.ir.textindexer.frequency.threshold.low=10
at.tuwien.ir.textindexer.frequency.threshold.high=90
at.tuwien.ir.textindexer.output.path=./output

There are three weighting options: bool, tf, idf;
Anything else will trigger the default boolean weighting.
Stemming is turned off per default.
The frequency thresholds are percents, so you may also use floats.
If you mess up with the frequencies, e.g. you provide negative values, string values or a higher lower bound the
defaults will be used.
The default frequencies are 0 and 100 and thus no filtering is done.