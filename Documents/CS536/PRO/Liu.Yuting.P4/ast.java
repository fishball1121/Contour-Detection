///////////////////////////////////////////////////////////
// Title: 				Programming Assignment 3
// Main Class File: 	P4.java
// Files: 				ast.java
// Semester: 			CS 536 Fall 2015
//
// Author: 				Yuting Liu
// Email:				liu487@wisc.edu
// CS Login: 			yuting
// Lecturer: 			Aws Albarghouthi
//////////////////////////////////////////////////////////

import java.io.*;
import java.util.*;

// **********************************************************************
// The ASTnode class defines the nodes of the abstract-syntax tree that
// represents a Mini program.
//
// Internal nodes of the tree contain pointers to children, organized
// either in a list (for nodes that may have a variable number of 
// children) or as a fixed set of fields.
//
// The nodes for literals and ids contain line and character number
// information; for string literals and identifiers, they also contain a
// string; for integer literals, they also contain an integer value.
//
// Here are all the different kinds of AST nodes and what kinds of children
// they have.  All of these kinds of AST nodes are subclasses of "ASTnode".
// Indentation indicates further subclassing:
//
//     Subclass            Kids
//     --------            ----
//     ProgramNode         DeclListNode
//     DeclListNode        linked list of DeclNode
//     DeclNode:
//       VarDeclNode       TypeNode, IdNode, int
//       FnDeclNode        TypeNode, IdNode, FormalsListNode, FnBodyNode
//       FormalDeclNode    TypeNode, IdNode
//       StructDeclNode    IdNode, DeclListNode
//
//     FormalsListNode     linked list of FormalDeclNode
//     FnBodyNode          DeclListNode, StmtListNode
//     StmtListNode        linked list of StmtNode
//     ExpListNode         linked list of ExpNode
//
//     TypeNode:
//       IntNode           -- none --
//       BoolNode          -- none --
//       VoidNode          -- none --
//       StructNode        IdNode
//
//     StmtNode:
//       AssignStmtNode      AssignNode
//       PostIncStmtNode     ExpNode
//       PostDecStmtNode     ExpNode
//       ReadStmtNode        ExpNode
//       WriteStmtNode       ExpNode
//       IfStmtNode          ExpNode, DeclListNode, StmtListNode
//       IfElseStmtNode      ExpNode, DeclListNode, StmtListNode,
//                                    DeclListNode, StmtListNode
//       WhileStmtNode       ExpNode, DeclListNode, StmtListNode
//       CallStmtNode        CallExpNode
//       ReturnStmtNode      ExpNode
//
//     ExpNode:
//       IntLitNode          -- none --
//       StrLitNode          -- none --
//       TrueNode            -- none --
//       FalseNode           -- none --
//       IdNode              -- none --
//       DotAccessNode       ExpNode, IdNode
//       AssignNode          ExpNode, ExpNode
//       CallExpNode         IdNode, ExpListNode
//       UnaryExpNode        ExpNode
//         UnaryMinusNode
//         NotNode
//       BinaryExpNode       ExpNode ExpNode
//         PlusNode     
//         MinusNode
//         TimesNode
//         DivideNode
//         AndNode
//         OrNode
//         EqualsNode
//         NotEqualsNode
//         LessNode
//         GreaterNode
//         LessEqNode
//         GreaterEqNode
//
// Here are the different kinds of AST nodes again, organized according to
// whether they are leaves, internal nodes with linked lists of kids, or
// internal nodes with a fixed number of kids:
//
// (1) Leaf nodes:
//        IntNode,   BoolNode,  VoidNode,  IntLitNode,  StrLitNode,
//        TrueNode,  FalseNode, IdNode
//
// (2) Internal nodes with (possibly empty) linked lists of children:
//        DeclListNode, FormalsListNode, StmtListNode, ExpListNode
//
// (3) Internal nodes with fixed numbers of kids:
//        ProgramNode,     VarDeclNode,     FnDeclNode,     FormalDeclNode,
//        StructDeclNode,  FnBodyNode,      StructNode,     AssignStmtNode,
//        PostIncStmtNode, PostDecStmtNode, ReadStmtNode,   WriteStmtNode   
//        IfStmtNode,      IfElseStmtNode,  WhileStmtNode,  CallStmtNode
//        ReturnStmtNode,  DotAccessNode,   AssignExpNode,  CallExpNode,
//        UnaryExpNode,    BinaryExpNode,   UnaryMinusNode, NotNode,
//        PlusNode,        MinusNode,       TimesNode,      DivideNode,
//        AndNode,         OrNode,          EqualsNode,     NotEqualsNode,
//        LessNode,        GreaterNode,     LessEqNode,     GreaterEqNode
//
// **********************************************************************

// **********************************************************************
// ASTnode class (base class for all other kinds of nodes)
// **********************************************************************

abstract class ASTnode { 
    // every subclass must provide an unparse operation
    abstract public void unparse(PrintWriter p, int indent);

	abstract public void nameAnalyzer(SymTable table);
	
    // this method can be used by the unparse methods to do indenting
    protected void doIndent(PrintWriter p, int indent) {
        for (int k=0; k<indent; k++) p.print(" ");
    }
   
}

// **********************************************************************
// ProgramNode,  DeclListNode, FormalsListNode, FnBodyNode,
// StmtListNode, ExpListNode
// **********************************************************************

class ProgramNode extends ASTnode {
    public ProgramNode(DeclListNode L) {
        myDeclList = L;
    }

    public void unparse(PrintWriter p, int indent) {
        myDeclList.unparse(p, indent);
    }
	
	public void nameAnalyzer(SymTable table) {
		myDeclList.nameAnalyzer(table);
	}
	
    // 1 kid
    private DeclListNode myDeclList;
}

class DeclListNode extends ASTnode {
    public DeclListNode(List<DeclNode> S) {
        myDecls = S;
    }

    public void unparse(PrintWriter p, int indent) {
        Iterator it = myDecls.iterator();
        try {
            while (it.hasNext()) {
                ((DeclNode)it.next()).unparse(p, indent);
            }
        } catch (NoSuchElementException ex) {
            System.err.println("unexpected NoSuchElementException in DeclListNode.print");
            System.exit(-1);
        }
    }
    
    public void nameAnalyzer(SymTable table) {
		for(int i = 0; i < myDecls.size(); ++i) {
			myDecls.get(i).nameAnalyzer(table);
		}
	}

	public void removeDecl(int i) {
		myDecls.remove(i);
	}
	
	public List<DeclNode> getDecls() {
		return myDecls;
	}
    // list of kids (DeclNodes)
    private List<DeclNode> myDecls;
}

class FormalsListNode extends ASTnode {
    public FormalsListNode(List<FormalDeclNode> S) {
        myFormals = S;
    }

    public void unparse(PrintWriter p, int indent) {
        Iterator<FormalDeclNode> it = myFormals.iterator();
        if (it.hasNext()) { // if there is at least one element
            it.next().unparse(p, indent);
            while (it.hasNext()) {  // print the rest of the list
                p.print(", ");
                it.next().unparse(p, indent);
            }
        } 
    }
	public LinkedList<String> getFormals() {
		LinkedList<String> types = new LinkedList<String>();
		for(int i = 0; i < myFormals.size(); ++i) {
			if(myFormals.get(i).getFormalType().equals("void")) {
				ValidFlag = false;
			} else {
				types.addLast(myFormals.get(i).getFormalType());
			}
		}
		return types;
	}
	
	public boolean FormalListValid() {
		return ValidFlag;
	}
	
	public void nameAnalyzer(SymTable table) {
		for(int i = 0; i < myFormals.size(); ++i) {
			myFormals.get(i).nameAnalyzer(table);
		}
	}
    // list of kids (FormalDeclNodes)
    private List<FormalDeclNode> myFormals;
    private boolean ValidFlag = true;
}

class FnBodyNode extends ASTnode {
    public FnBodyNode(DeclListNode declList, StmtListNode stmtList) {
        myDeclList = declList;
        myStmtList = stmtList;
    }

    public void unparse(PrintWriter p, int indent) {
        myDeclList.unparse(p, indent);
        myStmtList.unparse(p, indent);
    }
	
	public void nameAnalyzer(SymTable table) {
		myDeclList.nameAnalyzer(table);
		myStmtList.nameAnalyzer(table);
	}
    // 2 kids
    private DeclListNode myDeclList;
    private StmtListNode myStmtList;
}

class StmtListNode extends ASTnode {
    public StmtListNode(List<StmtNode> S) {
        myStmts = S;
    }

    public void unparse(PrintWriter p, int indent) {
        Iterator<StmtNode> it = myStmts.iterator();
        while (it.hasNext()) {
            it.next().unparse(p, indent);
        }
    }
	
	public void nameAnalyzer(SymTable table) {
		for(int i = 0; i < myStmts.size(); ++i) {
			myStmts.get(i).nameAnalyzer(table);
		}
	}
	
    // list of kids (StmtNodes)
    private List<StmtNode> myStmts;
}

class ExpListNode extends ASTnode {
    public ExpListNode(List<ExpNode> S) {
        myExps = S;
    }

    public void unparse(PrintWriter p, int indent) {
        Iterator<ExpNode> it = myExps.iterator();
        if (it.hasNext()) { // if there is at least one element
            it.next().unparse(p, indent);
            while (it.hasNext()) {  // print the rest of the list
                p.print(", ");
                it.next().unparse(p, indent);
            }
        } 
    }

	public void nameAnalyzer(SymTable table) {
		for(int i = 0; i < myExps.size(); ++i) {
			myExps.get(i).nameAnalyzer(table);
		}
	}
	
    // list of kids (ExpNodes)
    private List<ExpNode> myExps;
}

// **********************************************************************
// DeclNode and its subclasses
// **********************************************************************

abstract class DeclNode extends ASTnode {
}

class VarDeclNode extends DeclNode {
    public VarDeclNode(TypeNode type, IdNode id, int size) {
        myType = type;
        myId = id;
        mySize = size;
    }

    public void unparse(PrintWriter p, int indent) {
        doIndent(p, indent);
        myType.unparse(p, 0);
        p.print(" ");
        myId.unparse(p, 0);
        p.println(";");
    }

	public void nameAnalyzer(SymTable table) {
		// not struct type
		if(myType.getType().equals("int") || myType.getType().equals("bool") 
				|| myType.getType().equals("void")) {
			// void type of non-function declaration
			if(myType.getType().equals("void")) {
				ErrMsg.fatal(myId.getLineNum(), myId.getCharNum(), 
						"Non-function declared void");
			}
			// look up ID in local scope
			SemSym localId = table.lookupLocal(myId.getStrVal());
			SemSym globalID = table.lookupGlobal(myId.getStrVal());
			
			// use the name of the struct type
			if(globalID != null && globalID.getType().equals("struct")) {
				ErrMsg.fatal(myId.getLineNum(), myId.getCharNum(), "Multiply declared identifier");
			}
			// not found in local lookup
			// add the new declaration in the table
			else if(localId == null) {
				try {
					table.addDecl(myId.getStrVal(), new SemSym(myType.getType()));
				} catch (Exception ex) {
					ErrMsg.fatal(myId.getLineNum(), myId.getCharNum(), 
								"Exception caught");
				}
			}
			// found in local lookup
			// error report: Multiply declared identifier
			else {
				ErrMsg.fatal(myId.getLineNum(), myId.getCharNum(), 
							"Multiply declared identifier");
			}
		}
		
		// struct type
		else {
			// look up the id name in local lookup
			SemSym localId = table.lookupLocal(myId.getStrVal());
			// look up struct type in global view
			SemSym curStruct = table.lookupGlobal(myType.getType());
			// not find the struct type
			if(curStruct == null || !curStruct.getType().equals("struct")) {
				ErrMsg.fatal(myId.getLineNum(), myId.getCharNum(), 
							"Invalid name of struct type");
			}
			// the id name is invalid
			else if(localId != null){
				ErrMsg.fatal(myId.getLineNum(), myId.getCharNum(), 
							"Multiply decalred identifier");
			}
			else {
				try {
					SemSym s = new SemSym(myType.getType());
					table.addDecl(myId.getStrVal(), s);
				} catch (Exception ex) {
					System.err.println("Exception caught");
				}
			}
		}
	}
	
    // 3 kids
    private TypeNode myType;
    private IdNode myId;
    private int mySize;  // use value NOT_STRUCT if this is not a struct type
    public static int NOT_STRUCT = -1;
}

class FnDeclNode extends DeclNode {
    public FnDeclNode(TypeNode type,
                      IdNode id,
                      FormalsListNode formalList,
                      FnBodyNode body) {
        myType = type;
        myId = id;
        myFormalsList = formalList;
        myBody = body;
    }

    public void unparse(PrintWriter p, int indent) {
        doIndent(p, indent);
        myType.unparse(p, 0);
        p.print(" ");
        myId.unparse(p, 0);
        p.print("(");
        myFormalsList.unparse(p, 0);
        p.println(") {");
        myBody.unparse(p, indent+4);
        p.println("}\n");
    }

	public void nameAnalyzer(SymTable table) {
		// local lookup
		SemSym IdName = table.lookupGlobal(myId.getStrVal());
		if(IdName != null) {
			ErrMsg.fatal(myId.getLineNum(), myId.getCharNum(), 
						"Multiply declared identifier");	
		} else{
			LinkedList<String> fl = myFormalsList.getFormals();
			if(myFormalsList.FormalListValid()){
				FuncSym myFunc = new FuncSym(myType.getType(), fl);
				try {
					table.addDecl(myId.getStrVal(), myFunc);
				} catch (Exception ex) {
					System.err.println("Exception caught");
				}
			}
		}
		table.addScope();
		myFormalsList.nameAnalyzer(table);
		myBody.nameAnalyzer(table);
		try {
			table.removeScope();
		} catch (EmptySymTableException ex) {
			System.err.println("EmptySymTableException caught");
		}
	}
	
    // 4 kids
    private TypeNode myType;
    private IdNode myId;
    private FormalsListNode myFormalsList;
    private FnBodyNode myBody;
}

class FormalDeclNode extends DeclNode {
    public FormalDeclNode(TypeNode type, IdNode id) {
        myType = type;
        myId = id;
    }

    public void unparse(PrintWriter p, int indent) {
        myType.unparse(p, 0);
        p.print(" ");
        myId.unparse(p, 0);
    }

	public void nameAnalyzer(SymTable table) {
		SemSym IdName = table.lookupLocal(myId.getStrVal());
		if(IdName != null) {
			ErrMsg.fatal(myId.getLineNum(), myId.getCharNum(), 
						"Multiply declared identifier");
		} else {
			// not found in local scope
			if(myType.getType().equals("void")){
				ErrMsg.fatal(myId.getLineNum(), myId.getCharNum(), 
						"Non-function declared void");
			} else {
				IdName = new SemSym(myType.getType());
				try {
					table.addDecl(myId.getStrVal(), IdName);
				} catch (Exception ex) {
					System.err.println("Exception caught");
				}
			}
		}
	}
	
	public String getFormalType() {
		return myType.getType();
	}
    // 2 kids
    private TypeNode myType;
    private IdNode myId;
}

class StructDeclNode extends DeclNode {
    public StructDeclNode(IdNode id, DeclListNode declList) {
        myId = id;
        myDeclList = declList;
    }

    public void unparse(PrintWriter p, int indent) {
        doIndent(p, indent);
        p.print("struct ");
		myId.unparse(p, 0);
		p.println(" {");
        myDeclList.unparse(p, indent+4);
        doIndent(p, indent);
        p.println("};\n");

    }

	public void nameAnalyzer(SymTable table) {
		// global look up struct name
		SemSym IdName = table.lookupGlobal(myId.getStrVal());
		// id name is already used
		if(IdName != null) {
			ErrMsg.fatal(myId.getLineNum(), myId.getCharNum(), 
						"Multiply declared identifier");
			try{
				table.addScope();
			} catch (Exception ex) {
				System.err.println("Exception caught!");
			}
			myDeclList.nameAnalyzer(table);
			try {
				table.removeScope();
			} catch (Exception ex) {
				System.err.println("Exception caught!");
			}
		}
		else {
			//add scope
			table.addScope();
			// add DeclList in structBody
			try {
				table.addDecl(myId.getStrVal(), new SemSym("struct"));
			} catch (Exception ex) {
				System.err.println("Exception caught");
			}
			myDeclList.nameAnalyzer(table);
			SymTable scope = new SymTable();
			try {
				table.removeDecl(myId.getStrVal());
			} catch (Exception ex) {
				System.err.println("Exception caught");
			}
			try {
				scope = table.getScope();
			} catch (EmptySymTableException ex) {
				System.err.println("EmptySymTableException caught");
			}
			try {
				table.removeScope();
			} catch (EmptySymTableException ex) {
				System.err.println("EmptySymTableException caught");
			}
			StructSym stSym = new StructSym("struct", scope);
			try {
				table.addDecl(myId.getStrVal(), stSym);
			} catch (Exception ex) {
				System.err.println("Exception caught");
			}
		}
	}
	
    // 2 kids
    private IdNode myId;
	private DeclListNode myDeclList;
	private List<SemSym> myNameList;
}

// **********************************************************************
// TypeNode and its Subclasses
// **********************************************************************

abstract class TypeNode extends ASTnode {
	abstract public String getType();
}

class IntNode extends TypeNode {
    public IntNode() {
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("int");
    }
    
    public String getType() {
		return "int";
	}
	public void nameAnalyzer(SymTable table){}
}

class BoolNode extends TypeNode {
    public BoolNode() {
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("bool");
    }
    
    public String getType() {
		return "bool";
	}
	
	public void nameAnalyzer(SymTable table){}
}

class VoidNode extends TypeNode {
    public VoidNode() {
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("void");
    }
    
    public String getType() {
		return "void";
	}
	
	public void nameAnalyzer(SymTable table){}
}

class StructNode extends TypeNode {
    public StructNode(IdNode id) {
		myId = id;
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("struct ");
		myId.unparse(p, 0);
    }
	
	public String getType() {
		return myId.getStrVal();
	}
	
	public void nameAnalyzer(SymTable table){
		myId.nameAnalyzer(table);
	}
	// 1 kid
    private IdNode myId;
}

// **********************************************************************
// StmtNode and its subclasses
// **********************************************************************

abstract class StmtNode extends ASTnode {
}

class AssignStmtNode extends StmtNode {
    public AssignStmtNode(AssignNode assign) {
        myAssign = assign;
    }

    public void unparse(PrintWriter p, int indent) {
        doIndent(p, indent);
        myAssign.unparse(p, -1); // no parentheses
        p.println(";");
    }

	public void nameAnalyzer(SymTable table) {
		myAssign.nameAnalyzer(table);
	}
    // 1 kid
    private AssignNode myAssign;
}

class PostIncStmtNode extends StmtNode {
    public PostIncStmtNode(ExpNode exp) {
        myExp = exp;
    }

    public void unparse(PrintWriter p, int indent) {
        doIndent(p, indent);
        myExp.unparse(p, 0);
        p.println("++;");
    }
	
	public void nameAnalyzer(SymTable table) {
		myExp.nameAnalyzer(table);
	}
    // 1 kid
    private ExpNode myExp;
}

class PostDecStmtNode extends StmtNode {
    public PostDecStmtNode(ExpNode exp) {
        myExp = exp;
    }

    public void unparse(PrintWriter p, int indent) {
        doIndent(p, indent);
        myExp.unparse(p, 0);
        p.println("--;");
    }

	public void nameAnalyzer(SymTable table) {
		myExp.nameAnalyzer(table);
	}
	
    // 1 kid
    private ExpNode myExp;
}

class ReadStmtNode extends StmtNode {
    public ReadStmtNode(ExpNode e) {
        myExp = e;
    }

    public void unparse(PrintWriter p, int indent) {
        doIndent(p, indent);
        p.print("cin >> ");
        myExp.unparse(p, 0);
        p.println(";");
    }
    
    public void nameAnalyzer(SymTable table) {
		myExp.nameAnalyzer(table);
	}

    // 1 kid (actually can only be an IdNode or an ArrayExpNode)
    private ExpNode myExp;
}

class WriteStmtNode extends StmtNode {
    public WriteStmtNode(ExpNode exp) {
        myExp = exp;
    }

    public void unparse(PrintWriter p, int indent) {
        doIndent(p, indent);
        p.print("cout << ");
        myExp.unparse(p, 0);
        p.println(";");
    }

	public void nameAnalyzer(SymTable table) {
		myExp.nameAnalyzer(table);
	}
	
    // 1 kid
    private ExpNode myExp;
}

class IfStmtNode extends StmtNode {
    public IfStmtNode(ExpNode exp, DeclListNode dlist, StmtListNode slist) {
        myDeclList = dlist;
        myExp = exp;
        myStmtList = slist;
    }

    public void unparse(PrintWriter p, int indent) {
        doIndent(p, indent);
        p.print("if (");
        myExp.unparse(p, 0);
        p.println(") {");
        myDeclList.unparse(p, indent+4);
        myStmtList.unparse(p, indent+4);
        doIndent(p, indent);
        p.println("}");
    }

	public void nameAnalyzer(SymTable table) {
		// add local scope
		table.addScope();
		myExp.nameAnalyzer(table);
		myDeclList.nameAnalyzer(table);
		myStmtList.nameAnalyzer(table);
		// remove local scope
		try {
			table.removeScope();
		} catch (EmptySymTableException ex) {
			System.err.println("Exception caught!");
		}
	}
	
    // e kids
    private ExpNode myExp;
    private DeclListNode myDeclList;
    private StmtListNode myStmtList;
}

class IfElseStmtNode extends StmtNode {
    public IfElseStmtNode(ExpNode exp, DeclListNode dlist1,
                          StmtListNode slist1, DeclListNode dlist2,
                          StmtListNode slist2) {
        myExp = exp;
        myThenDeclList = dlist1;
        myThenStmtList = slist1;
        myElseDeclList = dlist2;
        myElseStmtList = slist2;
    }

    public void unparse(PrintWriter p, int indent) {
        doIndent(p, indent);
        p.print("if (");
        myExp.unparse(p, 0);
        p.println(") {");
        myThenDeclList.unparse(p, indent+4);
        myThenStmtList.unparse(p, indent+4);
        doIndent(p, indent);
        p.println("}");
        doIndent(p, indent);
        p.println("else {");
        myElseDeclList.unparse(p, indent+4);
        myElseStmtList.unparse(p, indent+4);
        doIndent(p, indent);
        p.println("}");        
    }

	public void nameAnalyzer(SymTable table) {
		table.addScope();
		myExp.nameAnalyzer(table);
		myThenDeclList.nameAnalyzer(table);
		myThenStmtList.nameAnalyzer(table);
		myElseDeclList.nameAnalyzer(table);
		myElseStmtList.nameAnalyzer(table);
		try {
			table.removeScope();
		} catch (EmptySymTableException ex) {
			System.err.println("EmptySymTableException caught");
		}
	}
	
    // 5 kids
    private ExpNode myExp;
    private DeclListNode myThenDeclList;
    private StmtListNode myThenStmtList;
    private StmtListNode myElseStmtList;
    private DeclListNode myElseDeclList;
}

class WhileStmtNode extends StmtNode {
    public WhileStmtNode(ExpNode exp, DeclListNode dlist, StmtListNode slist) {
        myExp = exp;
        myDeclList = dlist;
        myStmtList = slist;
    }
	
    public void unparse(PrintWriter p, int indent) {
        doIndent(p, indent);
        p.print("while (");
        myExp.unparse(p, 0);
        p.println(") {");
        myDeclList.unparse(p, indent+4);
        myStmtList.unparse(p, indent+4);
        doIndent(p, indent);
        p.println("}");
    }

	public void nameAnalyzer(SymTable table) {
		myExp.nameAnalyzer(table);
		table.addScope();
		myDeclList.nameAnalyzer(table);
		myStmtList.nameAnalyzer(table);
		try {
			table.removeScope();
		} catch (EmptySymTableException ex) {
			System.err.println("EmptySymTableException caught");
		}
	}
	
    // 3 kids
    private ExpNode myExp;
    private DeclListNode myDeclList;
    private StmtListNode myStmtList;
}

class CallStmtNode extends StmtNode {
    public CallStmtNode(CallExpNode call) {
        myCall = call;
    }

    public void unparse(PrintWriter p, int indent) {
        doIndent(p, indent);
        myCall.unparse(p, indent);
        p.println(";");
    }

	public void nameAnalyzer(SymTable table) {
		myCall.nameAnalyzer(table);
	}
	
    // 1 kid
    private CallExpNode myCall;
}

class ReturnStmtNode extends StmtNode {
    public ReturnStmtNode(ExpNode exp) {
        myExp = exp;
    }

    public void unparse(PrintWriter p, int indent) {
        doIndent(p, indent);
        p.print("return");
        if (myExp != null) {
            p.print(" ");
            myExp.unparse(p, 0);
        }
        p.println(";");
    }

	public void nameAnalyzer(SymTable table) {
		if(myExp != null) {
			myExp.nameAnalyzer(table);
		}
	}
	
    // 1 kid
    private ExpNode myExp; // possibly null
}

// **********************************************************************
// ExpNode and its subclasses
// **********************************************************************

abstract class ExpNode extends ASTnode {
	public LinkedList<String> getLocList() {
		return null;
	}
	public SymTable getLocTable(SymTable table, LinkedList<String> sy) {
		return null;
	}
	public int getLineNum() {
		return -1;
	}
	public int getCharNum() {
		return -1;
	}
}
class IntLitNode extends ExpNode {
    public IntLitNode(int lineNum, int charNum, int intVal) {
        myLineNum = lineNum;
        myCharNum = charNum;
        myIntVal = intVal;
    }

    public void unparse(PrintWriter p, int indent) {
        p.print(myIntVal);
    }
    
    public void nameAnalyzer(SymTable table) {}

    private int myLineNum;
    private int myCharNum;
    private int myIntVal;
}

class StringLitNode extends ExpNode {
    public StringLitNode(int lineNum, int charNum, String strVal) {
        myLineNum = lineNum;
        myCharNum = charNum;
        myStrVal = strVal;
    }

    public void unparse(PrintWriter p, int indent) {
        p.print(myStrVal);
    }

	public void nameAnalyzer(SymTable table) {}
    private int myLineNum;
    private int myCharNum;
    private String myStrVal;
}

class TrueNode extends ExpNode {
    public TrueNode(int lineNum, int charNum) {
        myLineNum = lineNum;
        myCharNum = charNum;
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("true");
    }

	public void nameAnalyzer(SymTable table){}
    private int myLineNum;
    private int myCharNum;
}

class FalseNode extends ExpNode {
    public FalseNode(int lineNum, int charNum) {
        myLineNum = lineNum;
        myCharNum = charNum;
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("false");
    }
    public void nameAnalyzer(SymTable table) {}
    private int myLineNum;
    private int myCharNum;
}

class IdNode extends ExpNode {
    public IdNode(int lineNum, int charNum, String strVal) {
        myLineNum = lineNum;
        myCharNum = charNum;
        myStrVal = strVal;
    }

    public void unparse(PrintWriter p, int indent) {
        p.print(myStrVal);
        if(mySym != null){
			p.print("(");
			p.print(mySym.getType());
			p.print(")");
		}
    }
	
	public void nameAnalyzer(SymTable table) {
		mySym = table.lookupGlobal(myStrVal);
		// not found in global lookup
		if(mySym == null) {
			ErrMsg.fatal(myLineNum, myCharNum, "Undeclared identifer");
		}
		// found in global lookup
		// but is a function name
		else if(mySym.getType().equals("struct")) {
			ErrMsg.fatal(myLineNum, myCharNum, "Multiply declared identifier");
		}
	}
	
	public LinkedList<String> getLocList() {
		locList = new LinkedList<String>();
		locList.addLast(myStrVal);
		return locList;
	}
	
	public SymTable getLocTable(SymTable table, LinkedList<String> sy) {
		SemSym s = table.lookupGlobal(myStrVal);
		if(s == null) {
			ErrMsg.fatal(myLineNum, myCharNum, "Dot-access of non-struct type");
			return null;
		}
		else {
			if(table.lookupGlobal(s.getType()) == null) {
				ErrMsg.fatal(myLineNum, myCharNum, "Dot-access of non-struct type");
				return null;
			}
			else {
				sy.addLast(s.getType());
				return ((StructSym)table.lookupGlobal(s.getType())).getTable();
			}
		}
	}
	
	public int getLineNum(){
		return myLineNum;
	}
	
	public int getCharNum() {
		return myCharNum;
	}
	
	public String getStrVal() {
		return myStrVal;
	}
	
	public SemSym getSym() {
		return mySym;
	}
	
	public void setSym(SemSym a) {
		mySym = a;
	}
    private int myLineNum;
    private int myCharNum;
    private String myStrVal;
    // add link to corresponding table entry
    private SemSym mySym;
    private LinkedList<String> locList;
}

class DotAccessExpNode extends ExpNode {
    public DotAccessExpNode(ExpNode loc, IdNode id) {
        myLoc = loc;	
        myId = id;
    }

    public void unparse(PrintWriter p, int indent) {
		doIndent(p, indent);
		for(int i = 0; i < locList.size(); ++i) {
			p.print(locList.get(i) + "(" + symList.get(i) + ").");
		}
		myId.unparse(p, 0);
    }
    
    public LinkedList<String> getLocList() {
		locList = new LinkedList<String>();
		if(myLoc != null) {
			locList.addAll(((ExpNode)myLoc).getLocList());
		}
		locList.addLast(myId.getStrVal());
		return locList;
	}
	
	public int getLineNum() {
		return ((ExpNode)myLoc).getLineNum();
	}
	
	public int getCharNum() {
		return ((ExpNode)myLoc).getCharNum();
	}
	public SymTable getLocTable(SymTable table, LinkedList<String> sy) {
		SymTable structTable = table;
                int locLineNum = ((ExpNode)myLoc).getLineNum();
                int locCharNum = ((ExpNode)myLoc).getCharNum();
		for(int i = 0; i < locList.size(); ++i) {
			SemSym field = structTable.lookupGlobal(locList.get(i));
			if(structTable == null) {
				ErrMsg.fatal(locLineNum, locCharNum, 
							"Dot-access of non-struct type");
				return null;
			}
			else {
				if(field == null || table.lookupGlobal(field.getType()) == null) {
					ErrMsg.fatal(locLineNum, locCharNum, 
								"Dot-access of non-struct type");
					return null;
				}
				else {
					sy.addLast(field.getType());
					structTable = ((StructSym)table.lookupGlobal(field.getType())).getTable();
				}
			}
		}
		return structTable;
	}
	
	public void nameAnalyzer(SymTable table) {
		locList = ((ExpNode)myLoc).getLocList();
                int locLineNum = ((ExpNode)myLoc).getLineNum();
                int locCharNum = ((ExpNode)myLoc).getCharNum();
		if(table.lookupGlobal(locList.get(0)) == null) {
			ErrMsg.fatal(locLineNum, locCharNum, "Undeclared identifier");
		}
                else {
		        SymTable t = ((ExpNode)myLoc).getLocTable(table, symList);
		        if(t != null) {
			        SemSym IdName = t.lookupGlobal(myId.getStrVal());
			        if(IdName == null) {
				        ErrMsg.fatal(myId.getLineNum(), myId.getCharNum(), 
									"Invalid struct field name");
			        }
			        else {
				        myId.setSym(IdName);
			        }
		        }
                }
	}
    // 2 kids
    private ExpNode myLoc;	
    private IdNode myId;
    private LinkedList<String> locList;
    private LinkedList <String> symList = new LinkedList<String>();
}

class AssignNode extends ExpNode {
    public AssignNode(ExpNode lhs, ExpNode exp) {
        myLhs = lhs;
        myExp = exp;
    }

    public void unparse(PrintWriter p, int indent) {
		if (indent != -1)  p.print("(");
	    myLhs.unparse(p, 0);
		p.print(" = ");
		myExp.unparse(p, 0);
		if (indent != -1)  p.print(")");
    }
	
	public void nameAnalyzer(SymTable table) {
		myLhs.nameAnalyzer(table);
		myExp.nameAnalyzer(table);
	}
    // 2 kids
    private ExpNode myLhs;
    private ExpNode myExp;
}

class CallExpNode extends ExpNode {
    public CallExpNode(IdNode name, ExpListNode elist) {
        myId = name;
        myExpList = elist;
    }

    public CallExpNode(IdNode name) {
        myId = name;
        myExpList = new ExpListNode(new LinkedList<ExpNode>());
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
		p.print(myId.getStrVal() + "(");
		LinkedList<String> l = myId.getSym().getFormals();
		if(l.size() != 0) {
			p.print(l.get(0));
		}
		for(int i = 1; i < l.size(); ++i) {
			p.print("," + l.get(i));
		}
		p.print("->" + myId.getSym().getType() + ")(");
		if (myExpList != null) {
			myExpList.unparse(p, 0);
		}
		p.print(")");
    }

	public void nameAnalyzer(SymTable table) {
		myId.nameAnalyzer(table);
		myExpList.nameAnalyzer(table);
	}
	
    // 2 kids
    private IdNode myId;
    private ExpListNode myExpList;  // possibly null
}

abstract class UnaryExpNode extends ExpNode {
    public UnaryExpNode(ExpNode exp) {
        myExp = exp;
    }
	
	public void nameAnalyzer(SymTable table) {
		myExp.nameAnalyzer(table);
	}
    // one child
    protected ExpNode myExp;
}

abstract class BinaryExpNode extends ExpNode {
    public BinaryExpNode(ExpNode exp1, ExpNode exp2) {
        myExp1 = exp1;
        myExp2 = exp2;
    }
	
	public void nameAnalyzer(SymTable table) {
		myExp1.nameAnalyzer(table);
		myExp2.nameAnalyzer(table);
	}
	
    // two kids
    protected ExpNode myExp1;
    protected ExpNode myExp2;
}

// **********************************************************************
// Subclasses of UnaryExpNode
// **********************************************************************

class UnaryMinusNode extends UnaryExpNode {
    public UnaryMinusNode(ExpNode exp) {
        super(exp);
    }

    public void unparse(PrintWriter p, int indent) {
	    p.print("(-");
		myExp.unparse(p, 0);
		p.print(")");
    }
}

class NotNode extends UnaryExpNode {
    public NotNode(ExpNode exp) {
        super(exp);
    }

    public void unparse(PrintWriter p, int indent) {
	    p.print("(!");
		myExp.unparse(p, 0);
		p.print(")");
    }
   
}

// **********************************************************************
// Subclasses of BinaryExpNode
// **********************************************************************

class PlusNode extends BinaryExpNode {
    public PlusNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
	    p.print("(");
		myExp1.unparse(p, 0);
		p.print(" + ");
		myExp2.unparse(p, 0);
		p.print(")");
    }
}

class MinusNode extends BinaryExpNode {
    public MinusNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
	    p.print("(");
		myExp1.unparse(p, 0);
		p.print(" - ");
		myExp2.unparse(p, 0);
		p.print(")");
    }
}

class TimesNode extends BinaryExpNode {
    public TimesNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
	    p.print("(");
		myExp1.unparse(p, 0);
		p.print(" * ");
		myExp2.unparse(p, 0);
		p.print(")");
    }
}

class DivideNode extends BinaryExpNode {
    public DivideNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
	    p.print("(");
		myExp1.unparse(p, 0);
		p.print(" / ");
		myExp2.unparse(p, 0);
		p.print(")");
    }
    
}

class AndNode extends BinaryExpNode {
    public AndNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
	    p.print("(");
		myExp1.unparse(p, 0);
		p.print(" && ");
		myExp2.unparse(p, 0);
		p.print(")");
    }
}

class OrNode extends BinaryExpNode {
    public OrNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
	    p.print("(");
		myExp1.unparse(p, 0);
		p.print(" || ");
		myExp2.unparse(p, 0);
		p.print(")");
    }
   
}

class EqualsNode extends BinaryExpNode {
    public EqualsNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
	    p.print("(");
		myExp1.unparse(p, 0);
		p.print(" == ");
		myExp2.unparse(p, 0);
		p.print(")");
    }
   
}

class NotEqualsNode extends BinaryExpNode {
    public NotEqualsNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
	    p.print("(");
		myExp1.unparse(p, 0);
		p.print(" != ");
		myExp2.unparse(p, 0);
		p.print(")");
    }
}

class LessNode extends BinaryExpNode {
    public LessNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
	    p.print("(");
		myExp1.unparse(p, 0);
		p.print(" < ");
		myExp2.unparse(p, 0);
		p.print(")");
    }
}

class GreaterNode extends BinaryExpNode {
    public GreaterNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
	    p.print("(");
		myExp1.unparse(p, 0);
		p.print(" > ");
		myExp2.unparse(p, 0);
		p.print(")");
    }
    
}

class LessEqNode extends BinaryExpNode {
    public LessEqNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
	    p.print("(");
		myExp1.unparse(p, 0);
		p.print(" <= ");
		myExp2.unparse(p, 0);
		p.print(")");
    }
    
    public void nameAnalyzer(SymTable table) {
		myExp1.nameAnalyzer(table);
		myExp2.nameAnalyzer(table);
	}
}

class GreaterEqNode extends BinaryExpNode {
    public GreaterEqNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
	    p.print("(");
		myExp1.unparse(p, 0);
		p.print(" >= ");
		myExp2.unparse(p, 0);
		p.print(")");
    }
}
