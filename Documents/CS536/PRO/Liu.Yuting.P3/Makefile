###
# This Makefile can be used to make a parser for the YES language
# (parser.class) and to make a program (P3.class) that tests the parser and
# the unparse methods in ast.java.
#
# make clean removes all generated files.
#
###

JC = javac
CP = $$CLASSPATH
CP2 = $$CLASSPATH

P3.class: P3.java parser.class Yylex.class ASTnode.class
	$(JC) -g -cp $(CP) P3.java

parser.class: parser.java ASTnode.class Yylex.class ErrMsg.class
	$(JC) -g -cp $(CP)   parser.java

parser.java: CFlat.cup
	java -cp $(CP) java_cup.Main < CFlat.cup

Yylex.class: CFlat.jlex.java sym.class ErrMsg.class
	$(JC) -cp $(CP) CFlat.jlex.java

ASTnode.class: ast.java
	$(JC) -g ast.java

CFlat.jlex.java: CFlat.jlex sym.class
	java  -cp $(CP2) JLex.Main CFlat.jlex

sym.class: sym.java
	$(JC) -g -cp $(CP) sym.java

sym.java: CFlat.cup
	java  -cp $(CP) java_cup.Main < CFlat.cup

ErrMsg.class: ErrMsg.java
	$(JC) ErrMsg.java

##test
test:
	java -cp $(CP) P3 test.cf test.out
	diff test.out output.out > error.out

###
# clean
###
clean:
	rm -f *~ *.class parser.java CFlat.jlex.java sym.java test.out error.out
