Programming-Language-Analyzer
=============================

This program takes a "fake" programming language, parses it, checks the syntaxes, and derives semantic meaning from each command. This program works with two programs, one that finds the GCD of two numbers and the other the Fibonacci number of a point in the sequence.

---README for Java FL Scanner and Parser and Semantic Analyzer---

1. Open command window or terminal depending on operating system.

2. Move current directory to that containing the unzipped files.

3. Type "make" onto the command line and press enter. Ignore any warnings that may occur during this process.

4. Run the FLAnalyse.class file by typing the following into the command line, "java FLAnalyse -filename- (output filename-optional)". This will run the scanner, parser, and semantic analyzer program.

5. If the filename was listed in the command line it should print the tokens to that file. Otherwise it will send the stream of tokens to the parser and check for syntactic correctness.

6. If it passes the two initial checks the static semantic rules will be checked followed by the runtime rules using a runtime library.

7. A note should be made that each line of input can only accept a single character. To accept this character 'enter' must be hit on the keyboard. After the final character has been read hit enter again to move to the next section.

EX:
dc226:Analyzer-B00183931 Ryan$ java FLAnalyse gcd.txt
The program is syntactically correct.
The static semantic rules are held.
Enter a: 5        <-- equal to 54
4
  	  <-- press enter again
Enter b: 1        <-- equal to 18
8
		  <-- press enter again
GCD(a, b) = 18

This should be intuitive once running the program. Added in here for completeness.

8. This is all that is required to run the scanner, parser, and semantic analyzer.
