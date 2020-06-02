# Batch-Text-to-PDF-Converter
Converting huge amount of text files into PDF files using Hadoop MapReduce.


## Usage

1. Put all the text files into the HDFS.
##### cmd: hdfs dfs -copyFromLocal <path to local directory(of text files)> \<path to HDFS directory>
   
2. export "itext" jar file present in "lib" folder to HADOOP_CLASSPATH.
##### cmd: export HADOOP_CLASSPATH=path/to/itext/jar
   
3. Now run the Mapreduce jar present in "target" folder.
##### cmd: hadoop jar \<path to jar file> \<name of main class> \<path to HDFS directory where text files stored> \<path to directory to store the output files>

4. You can check out the output files.
##### cmd: hadoop fs -ls \<path to output directory>
