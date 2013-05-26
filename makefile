# Makefile for the scanner and parser and analyzer built in Java

FLAnalyse: FLAnalyse.java FLParser.java FLScanner.java Stack.java Pair.java RefObject.java RunLibrary.java FLSemantic.java
	javac -g FLAnalyse.java FLParser.java FLScanner.java Stack.java Pair.java RefObject.java RunLibrary.java FLSemantic.java

FLSemantic.class: FLAnalyse.java FLParser.java FLScanner.java Stack.java Pair.java RefObject.java RunLibrary.java FLSemantic.java
	javac -g FLAnalyse.java FLParser.java FLScanner.java Stack.java Pair.java RefObject.java RunLibrary.java FLSemantic.java

RubLibrary.class: FLAnalyse.java FLParser.java FLScanner.java Stack.java Pair.java RefObject.java RunLibrary.java FLSemantic.java
	javac -g FLAnalyse.java FLParser.java FLScanner.java Stack.java Pair.java RefObject.java RunLibrary.java FLSemantic.java

FLParser.class: FLParser.java FLScanner.java Stack.java Pair.java
	javac -g FLParser.java FLScanner.java Stack.java Pair.java

FLScanner.class: FLScanner.java Stack.java Pair.java
	javac -g FLScanner.java Stack.java Pair.java

Stack.class: Stack.java
	javac -g Stack.java

Pair.class: Pair.java
	javac -g Pair.java

RefObject.class: RefObject.java
	javac -g RefObject.java