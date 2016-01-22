///////////////////////////////////////////////////////////
// 			MAIN FILE HEADER
// Title: 	Programming Assignment 1
// Files: 	Sym.java
//		  	SymTable.java
//		  	DuplicateSymException.java
//		  	EmptySymTableException.java
//Semester: CS 536 Fall 2015
//
//Author: 	Yuting Liu
//Email:	liu487@wisc.edu
//CS Login: yuting
//Lecturer: Aws Albarghouthi
/////////////////////////////////////////////////

import java.io.*; //Needed for input/output files 

/**
 * The main purpose is to test all operaions in Sym and SymTable class,
 * which provides the following operations:
 * 	in Sym class:
 * 		one-argument constructor -- create a Sym with given type(String)
 * 		String getType() -- return the type of the Sym 
 * 		String toString() -- (CHANGED LATER) return the type of the Sym
 * 
 * 	in SymTable class:
 * 		no-argument constructor -- create a List with a single, empty HashMap
 * 		void addDecl(String name, Sym sym) 
 * 			throws DuplicateSymException, EmptySymTableException
 * 			-- throw different exceptions and add object into SymTable
 * 		void addScope -- add a new, empty HashMap in the list
 * 		Sym lookupLocal(String name) throws EmptySymTableException
 * 			-- throw EmptySymTableException if symbol table is empty
 * 			   lookup the given name in the first HashMap, 
 * 			   return its associated Sym; Otherwise, return null;
 * 		Sym lookupGlobal(String name) throws EmptySymTableException
 * 			-- throw EmptySymTableException if symbol table is empty
 * 			   lookup the given name through the whole list, 
 * 			   return its associated Sym; Otherwise, return null;
 * 		void removeScope() throws EmptySymTableException
 * 			-- throw EmptySymTableException if symbol table is empty
 * 			   remove the HashMap from the front of the list
 * 		void print() -- print the symbol table
 * 
 * This code test all operations in Sym and SymTable classes, including both 
 * correct and bad calls to the operation that that can throw an exception.
 * ONLY if a test fails except the void print() function in SymTable class
 * 		
 * @author Yuting Liu
 *
 */

public class P1 {
	public static void main(String[] args) 
			throws IOException, EmptySymTableException, DuplicateSymException {
		/**
		 * Test: Sym class
		 * Read in the test file which contains different types of basic Java classes
		 * int, double, long, short, char, string, boolean
		 * Extra: we added " " and ""
		 */
		BufferedReader bufferR = null;
		//Read in the test file of Sym class line by line
		bufferR = new BufferedReader(new FileReader("testSym.txt"));
		try {
			String currentLine;
			while((currentLine = bufferR.readLine()) != null) {
				//Test instance constructor for Sym class
				Sym testSym = new Sym(currentLine);
				
				//Test getType(): whether return string == constructor's type
				if(!testSym.getType().equals(currentLine)) {
					System.out.println
						("Error: type returned from getType() not match!");
				}
				
				//Test whether String toString() == String type in the Constructor()
				if(!testSym.toString().equals(currentLine)) {
					System.out.println
						("Error: type returned from toString() not match!");
				}
			}
		} 
		catch(IOException e) {
			e.printStackTrace();
		}
		
		/**
		 * Test: SymTable class
		 * Read in the test file 
		 */
		//Test instance for SymTable class
		SymTable testTable = new SymTable();
		
		/** Test Constructor: with a single, empty HashMap */
		//Remove the 1st HashMap to make it empty
		try {
			testTable.removeScope();
		} 
		catch(EmptySymTableException e){
			//Catch empty SymTabl exception after constructor
			//Report error: constructor should contain a single, empty HashMap
			System.out.println
				("Error: EmptySymTableException follows Constructor!");
		}
		
		/** Test EmptyExceptions in different functions */
		//Test EmptyExceptions for addDecl()
		try {
			testTable.addDecl("puppy", new Sym("List<String>"));
			System.out.println
				("Error: EmptySymTableException not thrown by addDecl()!");
		} 
		catch(EmptySymTableException e) {
			
		}
		
		//Test  EmptyExceptions for lookupLocal()
		try {
			testTable.lookupLocal("PRICE");
			System.out.println
				("Error: EmptySymTableException not thrown by lookupLocal()!");
		} 
		catch(EmptySymTableException e) {
	
		}
		
		//Test EmptyExceptions for lookupGlobal()
		try {
			testTable.lookupGlobal("puppy");
			System.out.println
				("Exception: EmptySymTableException not thrown by lookupGlobal()!");
		} 
		catch(EmptySymTableException e) {
			
		}
		

		//Test EmptyExceptions for removeScope()
		try {
			testTable.removeScope();
			System.out.println
				("Exception: EmptySymTableException not thrown by removeScope()!");
		} 
		catch(EmptySymTableException e){
	
		}
		
		/** SymTable is empty now --> Test addScope() */
		testTable.addScope();
		
		//Read in the test file line by line
		//Add the pair of <String name, Sym sym> into the symTable
		bufferR = new BufferedReader(new FileReader("testSymTable.txt"));
		String nameLine, symTypeLine;
		try {
			if((nameLine = bufferR.readLine()) != null 
					&& (symTypeLine = bufferR.readLine()) != null) {
				//The 1st line is the name
				String[]name = nameLine.split(" ");
				//The second line is the sym type
				String[]symType = symTypeLine.split(" ");
				
				//The first string of two lines are useless identifiers
				for(int i = 1; i < name.length; ++i){
					/** Test adDecl(String name, Sym sym)
					 * Add the tuples given in the file to the sym Table */
					try {
						testTable.addDecl(name[i], new Sym(symType[i]));
					} 
					catch(EmptySymTableException e) {
						/** Test whether addScope() works before
						 *  Once detected EmptySymTableExpection*/
						System.out.println
							("Error: addScope() doesn't work properly!");
					}
				}
			} 
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		/** Test print() to see how addDecl works*/
		testTable.print();
		
		/** Test DuplicateSymException for addDecl()*/
		try {
			testTable.addDecl("PRICE", new Sym("float"));
			System.out.println
				("Error: DuplicateSymException not thrown for addDecl()!");
		} 
		catch (DuplicateSymException d){
			
		}
		
		/** Test NullPointerException for addDecl()*/
		//Sym is null
		try {
			testTable.addDecl("David", null);
			System.out.println
				("Exception: throw NullPointerException for addDecl()!");
		} 
		catch (NullPointerException n) {
		
		}
		
		//Name is null
		try {
			testTable.addDecl(null, new Sym("Integer"));
			System.out.println
				("Exception: unexpected NullPointerException for addDecl()!");
		} 
		catch (NullPointerException n){
			
		}
		
		/** Test lookupLocal() */
		//Look up the element exists in the symTable
		try {
			//Once it find null --> report the error
			if(testTable.lookupLocal("PRICE") == null) {
				System.out.println
					("Error: lookupLocal() cannot find the item in the 1st HashMap!");
			}
			//Once it find the wrong sym Type --> report the error
			else if(!testTable.lookupLocal("PRICE").getType().equals("double")) {
				System.out.println
					("Error: lookupLocal() cannot get the wrong Sym type!");
			}
		} 
		catch(EmptySymTableException e){
			System.out.println
				("Error: throw EmptySymTableException!");
		}
		
		//Look up the element doesn't exist in the symTable
		try {
			//Once it doesn't find null --> report the error
			if(testTable.lookupLocal("David") != null) {
				System.out.println
					("Error: find the wrong item in the SymTable!");
			}
		} 
		catch(EmptySymTableException e) {
			System.out.println
				("Error: unexpected EmptySymTableException!");
		}
		
		//Add more HashMaps into the symTable
		try {
			while((nameLine = bufferR.readLine()) != null 
					&& (symTypeLine = bufferR.readLine()) != null){
				//The 1st line is the name
				String[]name = nameLine.split(" ");
				//The second line is the sym type
				String[]symType = symTypeLine.split(" ");
				
				//Add a new HashMap in the symTable
				testTable.addScope();
				try{
					for(int i = 1; i < name.length; ++i){
						testTable.addDecl(name[i], new Sym(symType[i]));
					}
				}
				catch (EmptySymTableException e) {
					System.out.println
						("Error: unexpected EmptySymTableException!");
				}
				catch (DuplicateSymException d){
					System.out.println("Error: unexpected DuplicateSymException!");
				}
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		/** Test print() --> print out the table with 3 HashMaps*/
		testTable.print();
		
		/** Test lookupGlobal*/
		//Look up the element exists in the symTable
		try {
			//Once it doesn't find anything --> report error
			if(testTable.lookupGlobal("StockNum") == null){
				System.out.println
					("Error: cannot find the item throughout the SymTable!");
			}
			//Once it returns the wrong type --> report error
			else if(!testTable.lookupGlobal("Capital").getType().equals("long")) {
				System.out.println
					("Error: lookupGlobal() cannot get the wrong Sym type!");
			}
			
		} 
		catch(EmptySymTableException e) {
			System.out.println
				("Error: unexpected EmptySymTableException!");
		}
		
		//Lookup the element doesn't exists throughout the symTable
		try {
			if(testTable.lookupGlobal("Zibra") != null){
				System.out.println
					("Error: find the wrong item in the SymTable!");
			}
			
		} 
		catch(EmptySymTableException e) {
			System.out.println
				("Error: unexpected EmptySymTableException!");
		}
		
		/** Test removeScope()
		 *  Use void print() to see whether remove is correct */
		try {
			testTable.removeScope();
		} 
		catch(EmptySymTableException e) {
			System.out.println
				("Error: the addDecl() doesn't work as exepected!");
		}
	}
}
