#####
# Title: 				Programming Assignment 6
# Main Class File: 		P6.java
# Files: 				Makefile
# Semester: 			CS 536 Fall 2015
#
# Author: 				Yuting Liu
# Email:				liu487@wisc.edu
# CS Login: 			yuting
# Lecturer: 			Aws Albarghouthi
#####
#####
# This Makefile can be used to make a parser for the CFlat language
# (parser.class) and to make a program (P5.class) that tests the parser and
# the unparse methods in ast.java.
#
# make clean removes all generated files.
#
###

JC = javac
CP = $$CLASSPATH
CP2 = $$CLASSPATH

P6.class: P6.java parser.class Yylex.class ASTnode.class
	$(JC) -g -cp $(CP) P6.java

parser.class: parser.java ASTnode.class Yylex.class ErrMsg.class
	$(JC) -g -cp $(CP) parser.java

parser.java: CFlat.cup
	java -cp $(CP) java_cup.Main < CFlat.cup

Yylex.class: CFlat.jlex.java sym.class ErrMsg.class
	$(JC) -cp $(CP) CFlat.jlex.java

ASTnode.class: ast.java Type.java
	$(JC) -g ast.java

CFlat.jlex.java: CFlat.jlex sym.class
	java -cp $(CP) JLex.Main CFlat.jlex

sym.class: sym.java
	$(JC) -g -cp $(CP) sym.java

sym.java: CFlat.cup
	java -cp $(CP) java_cup.Main < CFlat.cup

ErrMsg.class: ErrMsg.java
	$(JC) ErrMsg.java

Sym.class: Sym.java Type.java ast.java
	$(JC) Sym.java
	
SymTable.class: SymTable.java Sym.java DuplicateSymException.java EmptySymTableException.java
	$(JC) -g SymTable.java
	
Type.class: Type.java
	$(JC) -g Type.java

DuplicateSymException.class: DuplicateSymException.java
	$(JC) -g DuplicateSymException.java
	
EmptySymTableException.class: EmptySymTableException.java
	$(JC) -g EmptySymTableException.java

###
# test
###
test:
	# my test with correct grammar
	java -cp $(CP) P6 test0.cf test0.s
	java -cp $(CP) P6 test1.cf test1.s
	java -cp $(CP) P6 test2.cf test2.s
	#java -cp $(CP) P6 test3.cf test3.s
	java -cp $(CP) P6 test4.cf test4.s
	java -cp $(CP) P6 test5.cf test5.s
	java -cp $(CP) P6 test6.cf test6.s
	java -cp $(CP) P6 test7.cf test7.s
	
###
# clean
###
clean:
	rm -f *~ *.class parser.java CSX.jlex.java sym.java *.out *.s
