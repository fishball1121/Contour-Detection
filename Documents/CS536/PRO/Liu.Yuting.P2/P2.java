///////////////////////////////////////////////////////////
//		MAIN FILE HEADER
// Title: 			Programming Assignment 2
// Files: 			YES.jlex
//				ErrMsg.java
//				sym.java
//				Makefile
// Semester: 			CS 536 Fall 2015
// Author: 			Yuting Liu
// Email:			liu487@wisc.edu
// CS Login: 			yuting
// Lecturer: 			Aws Albarghouthi
//////////////////////////////////////////////////////////

// import files needed
import java.util.*;
import java.io.*;
import java.lang.*;
import java_cup.runtime.*;  // defines Symbol

/**
 * This program is to be used to test the Scanner.
 * This version is set up to test all legal and illegal tokens.
 *
 * Function testAllTokens() read in the input file allTokens.in,
 * and output to the file allTokens.out. After comparing two files, if there
 * is no difference at all, it is proved to be correct
 *  
 * Function testcolLines() read in the input file checkLineCharNum.in,
 * and output to the file checkLineCHarNum.out. After comparing its result
 * with our given file checkLineCharNum.txt,if rere is no difference at all,
 * it is proved to be correct.
 *
 * Function testAllError() read in the input file allError.in,
 * and output to the file allError.out. After printing all error and warning report
 * into allError.err, we could compare allError.err with our given allError.txt. If
 * there is no difference at all, it is proved to be correct.
 */
public class P2 {
	/**
	 * Main method to call all test functions
	 *
	 * @throws IOexception
	 */
    public static void main(String[] args) throws IOException {
        // exception may be thrown by yylex
        // test all legal tokens including:
        // reserve words & identifier & integer literal
        // & string literal & reserved symbols
        testAllTokens("allTokens.in", "allTokens.out");
        
        // reset CharNum.num to 1
        CharNum.num = 1;
        
        // test whether the scanner get the correct line num and charnum
        testColLine("checkLineCharNum.in", "checkLineCharNum.out");
        
        CharNum.num = 1;
        // test different illegal tokens with error and warn messages
        // the output is stored in allError.out file
        // all error messages are stored in allError.err file
        testAllError("allError.in", "allError.out", "allError.err");
    }
    
    /**
     * test all legal tokens with correct output, line num and char num
     *
     * Open and read from file <inputFile>.in
     * For each token read, write the corresponding string to <outputFile>.out
     * If the input file contains all tokens, one per line, we can verify
     * correctness of the scanner by comparing the input and output files
     * with 'diff' command in linux
     * 
     * @throws IOexception
     */
    private static void testAllTokens(String inputFile, String outputFile) throws IOException {
        // open input and output files
        // catch exceptions duing IO
        FileReader inFile = null;
        PrintWriter outFile = null;
        try {
            inFile = new FileReader(inputFile);
            outFile = new PrintWriter(new FileWriter(outputFile));
        } catch (FileNotFoundException ex) {
            System.err.println("File " + inputFile + ".in not found.");
            System.exit(-1);
        } catch (IOException ex) {
            System.err.println(outputFile + ".out cannot be opened.");
            System.exit(-1);
        }
        
        // create and call the scanner
        Yylex scanner = new Yylex(inFile);
        Symbol token = scanner.next_token();
        while (token.sym != sym.EOF) {
            switch (token.sym) {
                case sym.BOOL:
                    outFile.println("bool");
                    break;
                case sym.INT:
                    outFile.println("int");
                    break;
                case sym.VOID:
                    outFile.println("void");
                    break;
                case sym.TRUE:
                    outFile.println("true");
                    break;
                case sym.FALSE:
                    outFile.println("false");
                    break;
                case sym.STRUCT:
                    outFile.println("struct");
                    break;
                case sym.CIN:
                    outFile.println("cin");
                    break;
                case sym.COUT:
                    outFile.println("cout");
                    break;
                case sym.IF:
                    outFile.println("if");
                    break;
                case sym.ELSE:
                    outFile.println("else");
                    break;
                case sym.WHILE:
                    outFile.println("while");
                    break;
                case sym.RETURN:
                    outFile.println("return");
                    break;
                case sym.ID:
                    outFile.println(((IdTokenVal)token.value).idVal);
                    break;
                case sym.INTLITERAL:
                    outFile.println(((IntLitTokenVal)token.value).intVal);
                    break;
                case sym.STRINGLITERAL:
                    outFile.println(((StrLitTokenVal)token.value).strVal);
                    break;
                case sym.LCURLY:
                    outFile.println("{");
                    break;
                case sym.RCURLY:
                    outFile.println("}");
                    break;
                case sym.LPAREN:
                    outFile.println("(");
                    break;
                case sym.RPAREN:
                    outFile.println(")");
                    break;
                case sym.SEMICOLON:
                    outFile.println(";");
                    break;
                case sym.COMMA:
                    outFile.println(",");
                    break;
                case sym.DOT:
                    outFile.println(".");
                    break;
                case sym.WRITE:
                    outFile.println("<<");
                    break;
                case sym.READ:
                    outFile.println(">>");
                    break;
                case sym.PLUSPLUS:
                    outFile.println("++");
                    break;
                case sym.MINUSMINUS:
                    outFile.println("--");
                    break;
                case sym.PLUS:
                    outFile.println("+");
                    break;
                case sym.MINUS:
                    outFile.println("-");
                    break;
                case sym.TIMES:
                    outFile.println("*");
                    break;
                case sym.DIVIDE:
                    outFile.println("/");
                    break;
                case sym.NOT:
                    outFile.println("!");
                    break;
                case sym.AND:
                    outFile.println("&&");
                    break;
                case sym.OR:
                    outFile.println("||");
                    break;
                case sym.EQUALS:
                    outFile.println("==");
                    break;
                case sym.NOTEQUALS:
                    outFile.println("!=");
                    break;
                case sym.LESS:
                    outFile.println("<");
                    break;
                case sym.GREATER:
                    outFile.println(">");
                    break;
                case sym.LESSEQ:
                    outFile.println("<=");
                    break;
                case sym.GREATEREQ:
                    outFile.println(">=");
                    break;
                case sym.ASSIGN:
                    outFile.println("=");
                    break;
                default:
                    outFile.println("UNKNOWN TOKEN");
            } // end switch
            
            token = scanner.next_token();
        } // end while
        outFile.close();
    }
    
    /**
     * Open and read file from <inputFile>.in
     * For each token read, write the corresponding error report to <errorFile>.err
     * The input file contains different illegal tokens, one per line,
     * we can verify correctness of the scanner 
     * by comparing the errFile and the given error file
     * (diff allError.err allError.txt)
     * 
     * @throws IOException
     */
    private static void testAllError(String inputFile, String outputFile, String errorFile) throws IOException {
    	// open error files
        PrintStream errFile = null;
        try {
            errFile = new PrintStream(new FileOutputStream(errorFile));
            // redirectory the error report to our errorFile
            System.setErr(errFile);
        } catch (FileNotFoundException ex) {
            System.err.println("File " + inputFile + ".in not found.");
            System.exit(-1);
        }

        // call function of testAllTokens for a scanner
        // to scan through our inputfile
        testAllTokens(inputFile, outputFile);
        errFile.close();
    }
    
    /**
     * testColLine
     * Open and read file from <inputFile>.in
     * The input file contains different tokens, several on the same line
     * we can verigy correctness of the scanner with correct line num and char num
     * by comapring the outfile with our given outfile
     * (diff checkLineCharNum.out LineCharNum.txt)
     */
    private static void testColLine(String inputFile, String outputFile) throws IOException {
        // open input and output files and exceptions
        FileReader inFile = null;
        PrintWriter outFile = null;
        try {
            inFile = new FileReader(inputFile);
            outFile = new PrintWriter(new FileWriter(outputFile));
        } catch (FileNotFoundException ex) {
            System.err.println("File " + inputFile + ".in not found.");
            System.exit(-1);
        } catch (IOException ex) {
            System.err.println(outputFile + ".out cannot be opened.");
            System.exit(-1);
        }
        
        // create and call the scanner
        Yylex scanner = new Yylex(inFile);
        Symbol token = scanner.next_token();
        //test charNum and lineNum whether matches
        while(token.sym != sym.EOF) {
			outFile.println("line num: " + ((TokenVal)token.value).linenum + ","
                          + "char num: " + ((TokenVal)token.value).charnum);
            token = scanner.next_token();
        } // end while
        outFile.close();
    }
}
