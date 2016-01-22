#####
# Title: 				Programming Assignment 5
# Main Class File: 		P5.java
# Files: 				Makefil
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

P5.class: P5.java parser.class Yylex.class ASTnode.class
	$(JC) -g -cp $(CP) P5.java

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
	java -cp $(CP) P5 test.cf test.out

	
testErrorGiven:
	# given test case online
	java -cp $(CP) P5 nameError_0.cf nameError_0.out

testError:
	#my test case
	java -cp $(CP) P5 nameError.cf nameError.out
	
###
# clean
###
clean:
	rm -f *~ *.class parser.java CSX.jlex.java sym.java *.out
