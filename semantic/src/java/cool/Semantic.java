package cool;

import java.util.*;
import java.util.Map.Entry;

public class Semantic {
	private static boolean errorFlag = false;
	public static void reportError(String filename, int lineNo, String error){
		errorFlag = true;
		System.err.println(filename+":"+lineNo+": "+error);
	}
	public boolean getErrorFlag() {
		return errorFlag;
	}

/*
	Don't change code above this line
*/

	public InheritanceGraph inheritance;
	public ClassData classData;
	public ScopeTable<AST.ASTNode > scopeTable;
	public ArrayList<String> basicClasses = new ArrayList<String>(Arrays.asList("Object","IO","String","Int","Bool"));
	public void insertClasses(AST.class_ cls, boolean[] visited) {
		if (visited[inheritance.ClasstoIndex.get(cls.parent)]) {
			//if (!basicClasses.contains(cls.name))
			classData.insertClass(cls);
			visited[inheritance.ClasstoIndex.get(cls.name)] = true;
			return;
		}
		insertClasses(inheritance.ClassReference.get(cls.parent), visited);
		classData.insertClass(cls);
		visited[inheritance.ClasstoIndex.get(cls.name)] = true;
		return;
	}

	public String AssignType(AST.ASTNode node, AST.class_ paramcls) {
		if(node == null) {
			return "Object";
		}
		else if(node instanceof AST.attr) {
			AST.attr mthdattr = (AST.attr) node;
			 return mthdattr.typeid;
		}
		else if(node instanceof AST.formal) {
			AST.formal mthdparm = (AST.formal) node;
			return mthdparm.typeid;
		}
		else if(node instanceof AST.localvar) {
			AST.localvar mthdobjexp = (AST.localvar) node;
			return mthdobjexp.type;
		}
		else if(node instanceof AST.object) { 	// test12.cl
			System.out.println("entered_object");
			AST.object obj = (AST.object) node;
			AST.ASTNode newnode = scopeTable.lookUpGlobal(obj.name);
			if(newnode == null)
			{
				String err = "Undeclared identifier " + obj.name + ".";
				reportError(paramcls.filename, paramcls.lineNo, err);
				////System.out.println("Undeclared_type");
				return "Object";
			}
			return AssignType(newnode, paramcls);
		}
		else if(node instanceof AST.plus) {  //test13.cl
			//System.out.println("entered_plus");
			AST.plus mthdpls = (AST.plus) node;
			mthdpls.e1.type = AssignType((AST.ASTNode) mthdpls.e1, paramcls);
			mthdpls.e2.type = AssignType((AST.ASTNode) mthdpls.e2, paramcls);
			// if(mthdpls.e1.type.equals("Int") && mthdpls.e2.type.equals("Int"))
			// 	//System.out.println("No error_plus");
			// else {
			if(!(mthdpls.e1.type.equals("Int") && mthdpls.e2.type.equals("Int"))) {
				String err = "non-Int arguments: " + mthdpls.e1.type + " + " + mthdpls.e2.type;
				reportError(paramcls.filename, paramcls.lineNo, err);
				////System.out.println("error_plus");
			}
			return "Int";
		}
		else if(node instanceof AST.sub) { //test14.cl
			//System.out.println("entered_sub");
			AST.sub mthdsub= (AST.sub) node;
			mthdsub.e1.type = AssignType((AST.ASTNode) mthdsub.e1, paramcls);
			mthdsub.e2.type = AssignType((AST.ASTNode) mthdsub.e2, paramcls);
			// if(mthdsub.e1.type.equals("Int") && mthdsub.e2.type.equals("Int"))
			// 	//System.out.println("No error_sub");
			// else {
			 if(!(mthdsub.e1.type.equals("Int") && mthdsub.e2.type.equals("Int"))) {
				String err = "non-Int arguments: " + mthdsub.e1.type + " - " + mthdsub.e2.type;
				reportError(paramcls.filename, paramcls.lineNo, err);
				////System.out.println("error_sub");
			}
			return "Int";
		}
		else if(node instanceof AST.mul) { //test15.cl
			//System.out.println("entered_mul");
			AST.mul mthdmul= (AST.mul) node;
			mthdmul.e1.type = AssignType((AST.ASTNode) mthdmul.e1, paramcls);
			mthdmul.e2.type = AssignType((AST.ASTNode) mthdmul.e2, paramcls);
			// if(mthdmul.e1.type.equals("Int") && mthdmul.e2.type.equals("Int"))
			// 	//System.out.println("No error_mul");
			// else {
			if(!(mthdmul.e1.type.equals("Int") && mthdmul.e2.type.equals("Int"))) {
				String err = "non-Int arguments: " + mthdmul.e1.type + " * " + mthdmul.e2.type;
				reportError(paramcls.filename, paramcls.lineNo, err);
				////System.out.println("error_mul");
			}
			return "Int";
		}
		else if(node instanceof AST.divide) { //test16.cl
			//System.out.println("entered_divide");
			AST.divide mthddvd= (AST.divide) node;
			mthddvd.e1.type = AssignType((AST.ASTNode) mthddvd.e1, paramcls);
			mthddvd.e2.type = AssignType((AST.ASTNode) mthddvd.e2, paramcls);
			// if(mthddvd.e1.type.equals("Int") && mthddvd.e2.type.equals("Int"))
			// 	//System.out.println("No error_divide");
			// else {
			if(!(mthddvd.e1.type.equals("Int") && mthddvd.e2.type.equals("Int"))) {
				String err = "non-Int arguments: " + mthddvd.e1.type + " / " + mthddvd.e2.type;
				reportError(paramcls.filename, paramcls.lineNo, err);
				////System.out.println("error_divide");
			}
			return "Int";
		}
		else if(node instanceof AST.assign) { //test17.cl
			//System.out.println("entered_assign");
			AST.assign mthdassgn= (AST.assign) node;
			AST.ASTNode newnode = scopeTable.lookUpGlobal(mthdassgn.name);
			String a = AssignType(newnode, paramcls);
			mthdassgn.e1.type = AssignType((AST.ASTNode) mthdassgn.e1, paramcls);
			// if(mthdassgn.e1.type.equals(a))
			// 	//System.out.println("No error_assign");
			// else{
			if(!(mthdassgn.e1.type.equals(a))) {
				String err = "Type " + mthdassgn.e1.type + " of assigned expression does not conform to declared type " + a + " of identifier " + mthdassgn.name + ".";
				reportError(paramcls.filename, paramcls.lineNo, err);
				////System.out.println("error_assign");
			}
			return mthdassgn.e1.type;
		}
		else if(node instanceof AST.block) { 
			//System.out.println("entered_block");
			AST.block mthdblck= (AST.block) node;
			String a = "";
			for (AST.expression exp : mthdblck.l1) {
				AST.ASTNode newnode = (AST.ASTNode) exp;
				a = AssignType(newnode, paramcls);
				exp.type = a;
			}
			return a;
		}
		else if(node instanceof AST.lt) { //test18.cl
			// System.out.println("entered_less");
			AST.lt mthdlt= (AST.lt) node;
			mthdlt.e1.type = AssignType((AST.ASTNode) mthdlt.e1, paramcls);
			mthdlt.e2.type = AssignType((AST.ASTNode) mthdlt.e2, paramcls);
			// if(mthdlt.e1.type.equals("Int") && mthdlt.e2.type.equals("Int"))
			// System.out.println("No error_less");
			// else {
			if(!(mthdlt.e1.type.equals("Int") && mthdlt.e2.type.equals("Int"))) {
				String err = "non-Int arguments: " + mthdlt.e1.type + " < " + mthdlt.e2.type;
				reportError(paramcls.filename, paramcls.lineNo, err);
				// System.out.println("error_less");
			}
			return "Bool";
		}
		else if(node instanceof AST.leq) { // test19.cl
			// System.out.println("entered_lessequal");
			AST.leq mthdleq= (AST.leq) node;
			mthdleq.e1.type = AssignType((AST.ASTNode) mthdleq.e1, paramcls);
			mthdleq.e2.type = AssignType((AST.ASTNode) mthdleq.e2, paramcls);
			// if(mthdleq.e1.type.equals("Int") && mthdleq.e2.type.equals("Int"))
			// System.out.println("No error_lessequal");
			// else {
			if(!(mthdleq.e1.type.equals("Int") && mthdleq.e2.type.equals("Int"))) {
				String err = "non-Int arguments: " + mthdleq.e1.type + " <= " + mthdleq.e2.type;
				reportError(paramcls.filename, paramcls.lineNo, err);
				// System.out.println("error_lessequal");
			}
			return "Bool";
		}
		else if(node instanceof AST.new_) {
			// System.out.println("entered_new");
			AST.new_ mthdnew= (AST.new_) node;
			return mthdnew.typeid;
		}
		else if(node instanceof AST.loop) { // test20.cl
			// System.out.println("entered_loop");
			AST.loop mthdloop= (AST.loop) node;
			mthdloop.predicate.type = AssignType((AST.ASTNode) mthdloop.predicate, paramcls);
			mthdloop.body.type = AssignType((AST.ASTNode) mthdloop.body, paramcls);
			// if(mthdloop.predicate.type.equals("Bool"))
			// System.out.println("No error_loop");
			// else {
			if(!(mthdloop.predicate.type.equals("Bool"))) {
				String err = "Loop condition does not have type Bool.";
				reportError(paramcls.filename, paramcls.lineNo, err);
				// System.out.println("error_loop");
			}
			return "Object";
		}
		else if(node instanceof AST.dispatch) {
			System.out.println("entered_dispatch");
			AST.dispatch mthddsptch = (AST.dispatch) node;
			System.out.println(mthddsptch.caller.type);
			mthddsptch.caller.type = AssignType((AST.ASTNode) mthddsptch.caller, paramcls);
			HashMap <String, AST.method> mthds = classData.classBlock.get(mthddsptch.caller.type).methodList;
			if(!mthds.containsKey(mthddsptch.name)) { //test21.cl
				String err = "Dispatch to undefined method " + mthddsptch.name + ".";
				reportError(paramcls.filename, paramcls.lineNo, err);
				// System.out.println("error_dispatch_nomethod");
				return "Object";
			}
			if(!(mthddsptch.actuals.size() == mthds.get(mthddsptch.name).formals.size())) { //test22.cl
				String err = "Method " + mthddsptch.name + " called with wrong number of arguments.";
				reportError(paramcls.filename, paramcls.lineNo, err);
				////System.out.println("error_dispatch_diffparam");
				return mthds.get(mthddsptch.name).typeid;
			}
			int size = mthddsptch.actuals.size();
			for (int i = 0;i < size; i++) {
				mthddsptch.actuals.get(i).type = AssignType((AST.ASTNode) mthddsptch.actuals.get(i), paramcls);
				if(!mthddsptch.actuals.get(i).type.equals(mthds.get(mthddsptch.name).formals.get(i).typeid)) { //test23.cl
					String err = "In call of method " + mthddsptch.name + ", type " + mthddsptch.actuals.get(i).type + " of parameter "  + mthds.get(mthddsptch.name).formals.get(i).name + " does not conform to declared type " + mthds.get(mthddsptch.name).formals.get(i).typeid + ".";
					reportError(paramcls.filename, paramcls.lineNo, err);
					// System.out.println("error_dispatch_diffparamtype");
				}
			}
			return mthds.get(mthddsptch.name).typeid;
		}
		else if(node instanceof AST.let) { //test24.cl
			//System.out.println("entered_let");
			AST.let mthdlet = (AST.let) node;
			scopeTable.enterScope();
			AST.localvar mthdletlv = new AST.localvar(mthdlet.typeid);
			scopeTable.insert(mthdlet.name, (AST.ASTNode) mthdletlv);
			mthdlet.value.type = AssignType((AST.ASTNode) mthdlet.value, paramcls);
			if(!mthdlet.value.type.equals(mthdlet.typeid)) {
				String err = "Inferred type " + mthdlet.value.type + " of initialization of " + mthdlet.name + " does not conform to identifier's declared type " + mthdlet.typeid + ".";
				reportError(paramcls.filename, paramcls.lineNo, err);
				////System.out.println("error_let");
			}
			mthdlet.body.type = AssignType((AST.ASTNode) mthdlet.body, paramcls);
			scopeTable.exitScope();
			return mthdlet.body.type;
		}
		else if(node instanceof AST.static_dispatch) {
			//System.out.println("entered_staticdispatch");
			AST.static_dispatch mthdstdsptch = (AST.static_dispatch) node;
			HashMap <String, AST.method> mthds = classData.classBlock.get(mthdstdsptch.typeid).methodList;
			mthdstdsptch.caller.type = AssignType((AST.ASTNode) mthdstdsptch.caller, paramcls);
			AST.class_ cls = inheritance.ClassReference.get(mthdstdsptch.caller.type);
			if(mthdstdsptch.typeid.equals("Object"));
			else {
				while(!cls.parent.equals("Object")) {
					if(cls.parent.equals(mthdstdsptch.typeid)) {
						break;
					}
				}
				if(cls.parent.equals("Object")) { //test25.cl
					String err = "Expression type "+ cls.name + " does not conform to declared static dispatch type " + mthdstdsptch.typeid + ".";
					reportError(paramcls.filename, paramcls.lineNo, err);
					////System.out.println("error_staticdispatch_inhertiance");
					return "Object";
				}
			}
			if(!mthds.containsKey(mthdstdsptch.name)) { //test26.cl
				String err = "Static dispatch to undefined method " + mthdstdsptch.name + ".";
				reportError(paramcls.filename, paramcls.lineNo, err);
				////System.out.println("error_staticdispatch_nomethod");
				return "Object";
			}
			if(!(mthdstdsptch.actuals.size() == mthds.get(mthdstdsptch.name).formals.size())) { //test27.cl
				String err = "Method " + mthdstdsptch.name + " invoked with wrong number of arguments.";
				reportError(paramcls.filename, paramcls.lineNo, err);
				////System.out.println("error_staticdispatch_diffparam");
				return mthds.get(mthdstdsptch.name).typeid;
			}
			int size = mthdstdsptch.actuals.size();
			for (int i = 0;i < size; i++) {
				mthdstdsptch.actuals.get(i).type = AssignType((AST.ASTNode) mthdstdsptch.actuals.get(i), paramcls);
				if(!mthdstdsptch.actuals.get(i).type.equals(mthds.get(mthdstdsptch.name).formals.get(i).typeid)) { //test28.cl
					String err = "In call of method " + mthdstdsptch.name + ", type " + mthdstdsptch.actuals.get(i).type + " of parameter " + mthds.get(mthdstdsptch.name).formals.get(i).name +" does not conform to declared type " + mthds.get(mthdstdsptch.name).formals.get(i).typeid + ".";
					reportError(paramcls.filename, paramcls.lineNo, err);
					////System.out.println("error_staticdispatch_diffparamtype");
				}
			}
			return mthds.get(mthdstdsptch.name).typeid;
		}
		else
		{
			//System.out.println("entered_const");
			return ((AST.expression) node).type;
		}
	}

	public Semantic(AST.program program) {
		//Write Semantic analyzer code here
		// Checks whether Inheritance Graph has cycle(s) and abort the program.
		inheritance = new InheritanceGraph(program);
		if(errorFlag || inheritance.isCyclic()) {
			return;
		}

		// Creating class block for the basic classes 
		classData = new ClassData(inheritance.ClassReference);
		// Creating class block for the remaining classes 


		boolean[] visited = new boolean[inheritance.class_size];
		visited[0] = true;
		visited[1] = true;
		for(AST.class_ cls : program.classes) {
			if (!basicClasses.contains(cls.name))
					insertClasses(cls, visited);
		}

		//building ScopeTable
		scopeTable = new ScopeTable<AST.ASTNode>();

		for (AST.class_ cls : program.classes) {
			scopeTable.insert(cls.name, (AST.ASTNode) cls);
			scopeTable.enterScope();
			for (AST.feature clsft : cls.features) {
				String name = "";
				if (clsft instanceof AST.attr) {
					name = ((AST.attr) clsft).name;
					scopeTable.insert(name, (AST.ASTNode) clsft);
					AST.attr attr = (AST.attr) clsft;
					String attrid = attr.typeid;
					attr.value.type = AssignType((AST.ASTNode) attr.value, cls);
					if (!(attrid.equals(attr.value.type))) { //test33.cl
						String err = "Inferred type " + attr.value.type + " of initialization of attribute " + attr.name + " does not conform to declared type " + attrid + ".";
						reportError(cls.name, cls.lineNo, err);
					}
				}
				else {
					name = ((AST.method) clsft).name;
					scopeTable.insert(name, (AST.ASTNode) clsft);
					AST.method mthd = (AST.method) clsft;
					scopeTable.enterScope();
					for (AST.formal param : mthd.formals) {
						scopeTable.insert(param.name, (AST.ASTNode) param);
					}
					//
					String mthdid = mthd.typeid;
					mthd.body.type = AssignType((AST.ASTNode) mthd.body, cls);
					scopeTable.exitScope();
					if (!(mthdid.equals(mthd.body.type))) {	//test34.cl
						String err = "Inferred return type " + mthd.body.type + " of method " + name + " does not conform to declared return type " + mthdid + ".";
						reportError(cls.name, cls.lineNo, err);
					}
				}
			}
			scopeTable.exitScope();
		}

		boolean check1 = false;
		boolean check2 = false;
		for (AST.class_ cls : program.classes) {
			if (cls.name.equals("Main")) {
				check1 = true;
				for (AST.feature clsft : cls.features) {
					if (clsft instanceof AST.method) {
						AST.method newMthd = (AST.method) clsft;
						if(newMthd.name.equals("main"))
							check2 = true;
					}
				}
			}
		}

		if(!check1) {	//test35.cl
			System.out.println("Class Main is not defined.");
			errorFlag = true;
			return;
		}
		else if(!check2) {	//test36.cl
			System.out.println("Main method is not defined.");
			errorFlag = true;
		}

	}
}
