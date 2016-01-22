///////////////////////////////////////////////////////////
// Title: 				Programming Assignment 3
// Main Class File: 	P4.java
// Files: 				SemSym.java
// Semester: 			CS 536 Fall 2015
//
// Author: 				Yuting Liu
// Email:				liu487@wisc.edu
// CS Login: 			yuting
// Lecturer: 			Aws Albarghouthi
//////////////////////////////////////////////////////////

import java.util.*;

class SemSym {
	private String type;
	//private LinkedList<String> formals = null;
	//private LinkedList<String> structFields = null;
	
	public SemSym(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public String toString() {
		return type;
	}
	
	public SymTable getTable(){
		return null;
	}
	
	public LinkedList<String> getFormals() {
		return null;
	}
	public boolean isFunction() {
		return false;
	}
}

/*
 * This class extends from class SemSym
 * It is the semsym created especially for function
 * with its return type and formal list
 */
class FuncSym extends SemSym{
	LinkedList<String> formals;
	
	public FuncSym(String type, LinkedList<String> formals) {
		super(type);
		this.formals = formals;
	}
	
	public LinkedList<String> getFormals() {
		return formals;
	}
	public boolean isFunction() {
		return true;
	}
}

/*
 * This class extends from class SemSyms
 * It is the semsym created especially for struct
 * with its struct fields list
 */
class StructSym extends SemSym {
	SymTable structFields;
	
	public StructSym(String type, SymTable structFields) {
		super(type);
		this.structFields = structFields;
	}
	
	public SymTable getTable(){
		return structFields;
	}
	public void print(){
		structFields.print();
	}
}
