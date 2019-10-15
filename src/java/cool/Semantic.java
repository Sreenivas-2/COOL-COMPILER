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

	public AST.program program;
	public InheritanceGraph inheritance;
	public ClassData classData;
	public ScopeTable<AST.ASTNode > scopeTable;
	public ArrayList<String> basicClasses = new ArrayList<String>(Arrays.asList("Object","IO","String","Int","Bool"));
	public void insertClasses(AST.class_ cls, boolean[] visited) {
		if (visited[inheritance.ClasstoIndex.get(cls.parent)]) {
			classData.insertClass(cls);
			visited[inheritance.ClasstoIndex.get(cls.name)] = true;
			return;
		}
		insertClasses(inheritance.ClassReference.get(cls.parent), visited);
		classData.insertClass(cls);
		visited[inheritance.ClasstoIndex.get(cls.name)] = true;
		return;
	}

	ClassBlock leastCommonAncestor(ClassBlock cls1, ClassBlock cls2) {
		if (cls1.level == 0)
			return cls1;
		if (cls2.level == 0)
			return cls2;
	    if (cls1.cls.name.equals(cls2.cls.name))
	        return cls1;
	    else if (cls1.level > cls2.level)
	        return leastCommonAncestor(classData.classBlock.get(cls1.cls.parent), cls2);
	    return leastCommonAncestor(cls1, classData.classBlock.get(cls2.cls.parent));
	}

	/*
		Method for assigning types to the expressions.
		Type caste the features into AST.node and send it as a parameter 1.
		Based on the instance of the node corresponding block in the method is executed.
	*/
	public String AssignType(AST.ASTNode node, AST.class_ paramcls) {

		// If the node is not found then we assign Object as node type.
		if(node == null) {
			return "Object";
		}

		// If the node is an instance of a class.
		else if(node instanceof AST.class_) {
			AST.class_ mthdclass = (AST.class_) node;
			return mthdclass.name;
		}

		// If the node is an instance of an attribute.
		else if(node instanceof AST.attr) {
			AST.attr mthdattr = (AST.attr) node;
			return mthdattr.typeid;
		}

		// If the node is an instance of parameter("formal").
		else if(node instanceof AST.formal) {
			AST.formal mthdparm = (AST.formal) node;
			return mthdparm.typeid;
		}

		// If the node is an instance of localvariable("It is local to the cuurent block").
		else if(node instanceof AST.localvar) {
			AST.localvar mthdobjexp = (AST.localvar) node;
			return mthdobjexp.type;
		}

		// If the node is an instance of Object.
		else if(node instanceof AST.object) {
			AST.object obj = (AST.object) node;

			// Searching node it in the ScopeTable.
			AST.ASTNode newnode = scopeTable.lookUpGlobal(obj.name);

			// if node is not found throw an error and assign type as "Object".
			if(newnode == null)
			{
				if(classData.classBlock.get(paramcls.name).attributeList.containsKey(obj.name)) {
					return classData.classBlock.get(paramcls.name).attributeList.get(obj.name).typeid;
				}
				String err = "Undeclared identifier " + obj.name + ".";
				reportError(paramcls.filename, paramcls.lineNo, err);
				return "Object";
			}
			return AssignType(newnode, paramcls);
		}

		// If the node is an instance of plus.
		else if(node instanceof AST.plus) {
			AST.plus mthdpls = (AST.plus) node;
			mthdpls.e1.type = AssignType((AST.ASTNode) mthdpls.e1, paramcls);
			mthdpls.e2.type = AssignType((AST.ASTNode) mthdpls.e2, paramcls);

			// if any of them is non-Int throw an error.
			if(!(mthdpls.e1.type.equals("Int") && mthdpls.e2.type.equals("Int"))) {
				String err = "non-Int arguments: " + mthdpls.e1.type + " + " + mthdpls.e2.type;
				reportError(paramcls.filename, paramcls.lineNo, err);
			}

			// Default type is Int.
			return "Int";
		}

		// If the node is an instance of sub.
		else if(node instanceof AST.sub) { 
			AST.sub mthdsub = (AST.sub) node;
			mthdsub.e1.type = AssignType((AST.ASTNode) mthdsub.e1, paramcls);
			mthdsub.e2.type = AssignType((AST.ASTNode) mthdsub.e2, paramcls);

			// if any of them is non-Int throw an error.
			if(!(mthdsub.e1.type.equals("Int") && mthdsub.e2.type.equals("Int"))) {
				String err = "non-Int arguments: " + mthdsub.e1.type + " - " + mthdsub.e2.type;
				reportError(paramcls.filename, paramcls.lineNo, err);
			}

			// Default type is Int.
			return "Int";
		}

		// If the node is an instance of mul.
		else if(node instanceof AST.mul) {
			AST.mul mthdmul = (AST.mul) node;
			mthdmul.e1.type = AssignType((AST.ASTNode) mthdmul.e1, paramcls);
			mthdmul.e2.type = AssignType((AST.ASTNode) mthdmul.e2, paramcls);

			// if any of them is non-Int throw an error.
			if(!(mthdmul.e1.type.equals("Int") && mthdmul.e2.type.equals("Int"))) {
				String err = "non-Int arguments: " + mthdmul.e1.type + " * " + mthdmul.e2.type;
				reportError(paramcls.filename, paramcls.lineNo, err);
			}
			// Default type is Int.
			return "Int";
		}

		// If the node is an instance of divide.
		else if(node instanceof AST.divide) {
			AST.divide mthddvd = (AST.divide) node;
			mthddvd.e1.type = AssignType((AST.ASTNode) mthddvd.e1, paramcls);
			mthddvd.e2.type = AssignType((AST.ASTNode) mthddvd.e2, paramcls);

			// if any of them is non-Int throw an error.
			if(!(mthddvd.e1.type.equals("Int") && mthddvd.e2.type.equals("Int"))) {
				String err = "non-Int arguments: " + mthddvd.e1.type + " / " + mthddvd.e2.type;
				reportError(paramcls.filename, paramcls.lineNo, err);
			}

			// Default type is Int.
			return "Int";
		}

		// If the node is an instance of assign.
		else if(node instanceof AST.assign) { 	// 		referred as : " A <- B "
			AST.assign mthdassgn = (AST.assign) node;
			AST.ASTNode newnode = scopeTable.lookUpGlobal(mthdassgn.name);
			String a = AssignType(newnode, paramcls);
			mthdassgn.e1.type = AssignType((AST.ASTNode) mthdassgn.e1, paramcls);

			// if node is not found throw an error
			if(newnode == null) {
				String err =  "Assignment to undeclared variable " + mthdassgn.name + ".";
				reportError(paramcls.filename, paramcls.lineNo, err);
				return mthdassgn.e1.type;
			}
			ClassBlock c1 = classData.classBlock.get(a);
			ClassBlock c2 = classData.classBlock.get(mthdassgn.e1.type);

			// Checking if A,B has the LeastCommonAncestor as A in the AST.
			if(!(c1 == leastCommonAncestor(c1, c2))) {
				String err = "Type " + mthdassgn.e1.type + " of assigned expression does not conform to declared type " + a + " of identifier " + mthdassgn.name + ".";
				reportError(paramcls.filename, paramcls.lineNo, err);
			}

			// Default type is type of B.
			return mthdassgn.e1.type;
		}

		// If the node is an instance of block.
		else if(node instanceof AST.block) {
			AST.block mthdblck = (AST.block) node;
			String a = "";

			// Traversing over all the expressions inside the block.
			for (AST.expression exp : mthdblck.l1) {
				AST.ASTNode newnode = (AST.ASTNode) exp;
				a = AssignType(newnode, paramcls);
				exp.type = a;
			}

			// Returning the type of last expression in the block.
			return a;
		}

		// If the node is an instance of lessthan.
		else if(node instanceof AST.lt) {
			AST.lt mthdlt = (AST.lt) node;
			mthdlt.e1.type = AssignType((AST.ASTNode) mthdlt.e1, paramcls);
			mthdlt.e2.type = AssignType((AST.ASTNode) mthdlt.e2, paramcls);

			// if any of them is non-Int throw an error.
			if(!(mthdlt.e1.type.equals("Int") && mthdlt.e2.type.equals("Int"))) {
				String err = "non-Int arguments: " + mthdlt.e1.type + " < " + mthdlt.e2.type;
				reportError(paramcls.filename, paramcls.lineNo, err);
			}

			// Default type is Bool.
			return "Bool";
		}

		// If the node is an instance of lessthanequal.
		else if(node instanceof AST.leq) {
			AST.leq mthdleq = (AST.leq) node;
			mthdleq.e1.type = AssignType((AST.ASTNode) mthdleq.e1, paramcls);
			mthdleq.e2.type = AssignType((AST.ASTNode) mthdleq.e2, paramcls);

			// if any of them is non-Int throw an error.
			if(!(mthdleq.e1.type.equals("Int") && mthdleq.e2.type.equals("Int"))) {
				String err = "non-Int arguments: " + mthdleq.e1.type + " <= " + mthdleq.e2.type;
				reportError(paramcls.filename, paramcls.lineNo, err);

			}

			// Default type is Bool.
			return "Bool";
		}

		// If the node is an instance of equal.
		else if(node instanceof AST.eq) { //   referred as ' A = B '
			AST.eq mthdeq = (AST.eq) node;
			mthdeq.e1.type = AssignType((AST.ASTNode) mthdeq.e1, paramcls);
			mthdeq.e2.type = AssignType((AST.ASTNode) mthdeq.e2, paramcls);
			ArrayList<String> eqClasses = new ArrayList<String>(Arrays.asList("String","Int","Bool"));

			// Checking if A,B are from {"Int","Bool","String"} and don't have same type (OR) Only one of A,B is from {"Int","Bool","String"} throw an error.
			if (!(mthdeq.e1.type.equals(mthdeq.e2.type))) {
				if((eqClasses.contains(mthdeq.e1.type) && eqClasses.contains(mthdeq.e2.type)) || (eqClasses.contains(mthdeq.e1.type) && !eqClasses.contains(mthdeq.e2.type)) ||  (!eqClasses.contains(mthdeq.e1.type) && eqClasses.contains(mthdeq.e2.type))) {
					String err = "Illegal comparison with a basic type.";
					reportError(paramcls.filename, paramcls.lineNo, err);
				}
			}

			// Default type is Bool.
			return "Bool";
		}

		// If the node is an instance of new.
		else if(node instanceof AST.new_) {//			referred as ' new A '
			AST.new_ mthdnew = (AST.new_) node;

			// If class is not present in the AST throw an error and return type as Object.
			if(!mthdnew.typeid.equals("Object") && !(program.classes.contains(inheritance.ClassReference.get(mthdnew.typeid)))){
				String err = "'new' used with undefined class " + mthdnew.typeid + ".";
				reportError(paramcls.filename, paramcls.lineNo, err);
				return "Object";
			}

			// Else return type of the A
			return mthdnew.typeid;
		}

		// If the node is an instance of loop.
		else if(node instanceof AST.loop) { 
			AST.loop mthdloop = (AST.loop) node;
			mthdloop.predicate.type = AssignType((AST.ASTNode) mthdloop.predicate, paramcls);
			mthdloop.body.type = AssignType((AST.ASTNode) mthdloop.body, paramcls);

			// If predicate type is not bool throw an error.
			if(!(mthdloop.predicate.type.equals("Bool"))) {
				String err = "Loop condition does not have type Bool.";
				reportError(paramcls.filename, paramcls.lineNo, err);
			}

			//  Default type is Object.
			return "Object";
		}

		// If the node is an instance of dispatch.
		else if(node instanceof AST.dispatch) {
			AST.dispatch mthddsptch = (AST.dispatch) node;
			mthddsptch.caller.type = AssignType((AST.ASTNode) mthddsptch.caller, paramcls);

			// Throw an error if the caller is not well defined.
			if(!mthddsptch.caller.type.equals("Object") && !(program.classes.contains(inheritance.ClassReference.get(mthddsptch.caller.type)))){
				String err = "Dispatch on undefined class " + mthddsptch.caller.type + ".";
				reportError(paramcls.filename, paramcls.lineNo, err);
				return "Object";
			}

			HashMap <String, AST.method> mthds = classData.classBlock.get(mthddsptch.caller.type).methodList;

			// Throw an error if the caller has no such method.
			if(!mthds.containsKey(mthddsptch.name)) { 
				String err = "Dispatch to undefined method " + mthddsptch.name + ".";
				reportError(paramcls.filename, paramcls.lineNo, err);
				return "Object";
			}

			// Throw an error if the caller invokes method with wrong number of parameters.
			if(!(mthddsptch.actuals.size() == mthds.get(mthddsptch.name).formals.size())) { 
				String err = "Method " + mthddsptch.name + " called with wrong number of arguments.";
				reportError(paramcls.filename, paramcls.lineNo, err);
				return mthds.get(mthddsptch.name).typeid;
			}
			int size = mthddsptch.actuals.size();
			for (int i = 0;i < size; i++) {
				mthddsptch.actuals.get(i).type = AssignType((AST.ASTNode) mthddsptch.actuals.get(i), paramcls);
				ClassBlock c1 = classData.classBlock.get(mthddsptch.actuals.get(i).type);
				ClassBlock c2 = classData.classBlock.get(mthds.get(mthddsptch.name).formals.get(i).typeid);
				// Throw an error if the caller invokes method with wrong parameter type.
				if(!(c2 == leastCommonAncestor(c1, c2))) {	
					String err = "In call of method " + mthddsptch.name + ", type " + mthddsptch.actuals.get(i).type + " of parameter "  + mthds.get(mthddsptch.name).formals.get(i).name + " does not conform to declared type " + mthds.get(mthddsptch.name).formals.get(i).typeid + ".";
					reportError(paramcls.filename, paramcls.lineNo, err);
				}
			}
			return mthds.get(mthddsptch.name).typeid;
		}

		// If the node is an instance of let.
		else if(node instanceof AST.let) {
			AST.let mthdlet = (AST.let) node;
			scopeTable.enterScope();
			AST.localvar mthdletlv = new AST.localvar(mthdlet.typeid);
			scopeTable.insert(mthdlet.name, (AST.ASTNode) mthdletlv);
			mthdlet.value.type = AssignType((AST.ASTNode) mthdlet.value, paramcls);

			// Throw an error if declared type doesn't match with the assigned type.
			if(!mthdlet.value.type.equals("_no_type") && !mthdlet.value.type.equals(mthdlet.typeid)) {
				String err = "Inferred type " + mthdlet.value.type + " of initialization of " + mthdlet.name + " does not conform to identifier's declared type " + mthdlet.typeid + ".";
				reportError(paramcls.filename, paramcls.lineNo, err);
			}

			// Assigning type for body of let
			mthdlet.body.type = AssignType((AST.ASTNode) mthdlet.body, paramcls);
			scopeTable.exitScope();
			return mthdlet.body.type;
		}

		// If the node is an instance of static_dispatch.
		else if(node instanceof AST.static_dispatch) {
			AST.static_dispatch mthdstdsptch = (AST.static_dispatch) node;
			HashMap <String, AST.method> mthds = classData.classBlock.get(mthdstdsptch.typeid).methodList;
			mthdstdsptch.caller.type = AssignType((AST.ASTNode) mthdstdsptch.caller, paramcls);
			AST.class_ cls = inheritance.ClassReference.get(mthdstdsptch.caller.type);

			ClassBlock a1 = classData.classBlock.get(mthdstdsptch.caller.type);
			ClassBlock a2 = classData.classBlock.get(mthdstdsptch.typeid);

			if(!(a2 == leastCommonAncestor(a1, a2))) {	
				String err = "Expression type "+ cls.name + " does not conform to declared static dispatch type " + mthdstdsptch.typeid + ".";
				reportError(paramcls.filename, paramcls.lineNo, err);					
				return "Object";
			}
			
			if(!mthds.containsKey(mthdstdsptch.name)) {
				String err = "Static dispatch to undefined method " + mthdstdsptch.name + ".";
				reportError(paramcls.filename, paramcls.lineNo, err);
				return "Object";
			}
			if(!(mthdstdsptch.actuals.size() == mthds.get(mthdstdsptch.name).formals.size())) { 
				String err = "Method " + mthdstdsptch.name + " invoked with wrong number of arguments.";
				reportError(paramcls.filename, paramcls.lineNo, err);
				return mthds.get(mthdstdsptch.name).typeid;
			}
			int size = mthdstdsptch.actuals.size();
			for (int i = 0;i < size; i++) {
				mthdstdsptch.actuals.get(i).type = AssignType((AST.ASTNode) mthdstdsptch.actuals.get(i), paramcls);
				ClassBlock c1 = classData.classBlock.get(mthdstdsptch.actuals.get(i).type);
				ClassBlock c2 = classData.classBlock.get(mthds.get(mthdstdsptch.name).formals.get(i).typeid);
				if(!(c2 == leastCommonAncestor(c1, c2))) {	
					String err = "In call of method " + mthdstdsptch.name + ", type " + mthdstdsptch.actuals.get(i).type + " of parameter " + mthds.get(mthdstdsptch.name).formals.get(i).name +" does not conform to declared type " + mthds.get(mthdstdsptch.name).formals.get(i).typeid + ".";
					reportError(paramcls.filename, paramcls.lineNo, err);
				}
			}
			return mthds.get(mthdstdsptch.name).typeid;
		}

		// If the node is an instance of conditionals.
		else if(node instanceof AST.cond) {
			AST.cond mthdcond = (AST.cond) node;
			mthdcond.predicate.type = AssignType((AST.ASTNode) mthdcond.predicate, paramcls);
			mthdcond.ifbody.type = AssignType((AST.ASTNode) mthdcond.ifbody, paramcls);
			mthdcond.elsebody.type = AssignType((AST.ASTNode) mthdcond.elsebody, paramcls);
			ClassBlock ifBody = classData.classBlock.get(mthdcond.ifbody.type);
			ClassBlock elseBody = classData.classBlock.get(mthdcond.elsebody.type);
			ClassBlock clsblck = leastCommonAncestor(ifBody, elseBody);
			if (clsblck.level == 0)
				return "Object";
			return clsblck.cls.name;
		}

		// If the node is an instance of typecase.
		else if(node instanceof AST.typcase) {
			AST.typcase mthdtypcase = (AST.typcase) node;
			mthdtypcase.predicate.type = AssignType((AST.ASTNode) mthdtypcase.predicate, paramcls);
			ClassBlock clsblck = classData.classBlock.get(mthdtypcase.branches.get(0).type);
			String type = mthdtypcase.branches.get(0).type;
			for (int i = 1; i < mthdtypcase.branches.size(); i++) {
				AST.branch branch = mthdtypcase.branches.get(i);


				if(!classData.classBlock.containsKey(branch.type)) {
			 		String err = "Class " + branch.type + " of case branch is undefined.";
			 		reportError(paramcls.filename, paramcls.lineNo, err);
		 		}


				if (type.equals(branch.type) ) {
					String err = "Duplicate branch " + type + " in case statement.";
					reportError(paramcls.filename, paramcls.lineNo, err);
					type = branch.type;
				}
				clsblck = leastCommonAncestor(clsblck, classData.classBlock.get(branch.type));

				
			}


			if (clsblck.level == 0)
				return "Object";
			return clsblck.cls.name;
		}

		// If the node is an instance of no_expression.
		else if(node instanceof AST.no_expr) {
			return "_no_type";
		}

		// If the node is an instance of negative.
		else if(node instanceof AST.neg) {
			AST.neg mthdneg = (AST.neg) node;
			mthdneg.e1.type = AssignType((AST.ASTNode) mthdneg.e1, paramcls);

			if(!mthdneg.e1.type.equals("Int")) {
				String err = "Argument of '~' has type " + mthdneg.e1.type + " instead of Int.";
				reportError(paramcls.filename, paramcls.lineNo, err);
			}
			return "Int";
		}


		// If the node is an instance of complimnet.
		else if(node instanceof AST.comp) {
			AST.comp mthdcomp = (AST.comp) node;
			mthdcomp.e1.type = AssignType((AST.ASTNode) mthdcomp.e1, paramcls);

			if(!mthdcomp.e1.type.equals("Bool")) {
				String err = "Argument of 'not' has type " + mthdcomp.e1.type + " instead of Bool.";
				reportError(paramcls.filename, paramcls.lineNo, err);
			}
			return "Bool";
		}

		// If the node is an instance of isvoid.
		else if(node instanceof AST.isvoid) {
			AST.isvoid mthdisvoid = (AST.isvoid) node;
			mthdisvoid.e1.type = AssignType((AST.ASTNode) mthdisvoid.e1, paramcls);	
			return "Bool";
		}

		// If the node is an instance of constant{"int_const","string_const","bool_const",}.
		else
		{
			return ((AST.expression) node).type;
		}
	}

	public Semantic(AST.program program) {
		//Write Semantic analyzer code here
		
		this.program = program;
		//	Checks whether Inheritance Graph has cycle(s).
		inheritance = new InheritanceGraph(program);
		if(errorFlag || inheritance.isCyclic()) {
			return;
		}
		
		//	Creating classblock for the basicclasses("IO","String","Int","Bool"). 
		classData = new ClassData(inheritance.ClassReference);
		
		//	Creating classblock for the remaining classes in AST.
		boolean[] visited = new boolean[inheritance.class_size];
		visited[0] = true;
		visited[1] = true;
		for(AST.class_ cls : program.classes) {
			if (!basicClasses.contains(cls.name))
					insertClasses(cls, visited);
		}

		//	Building ScopeTable.
		scopeTable = new ScopeTable<AST.ASTNode>();

		// Traversing over all the classes to build the scope table.
		for (AST.class_ cls : program.classes) {
			if (!basicClasses.contains(cls.name)) {
				scopeTable.insert(cls.name, (AST.ASTNode) cls);
				scopeTable.enterScope();
				scopeTable.insert("self", (AST.ASTNode) cls);
				//	Traversing over all the features in a class.
				for (AST.feature clsft : cls.features) {
					String name = "";

					// If the feature is an attribute.
					if (clsft instanceof AST.attr) {
						name = ((AST.attr) clsft).name;
						scopeTable.insert(name, (AST.ASTNode) clsft);
						AST.attr attr = (AST.attr) clsft;
						String attrid = attr.typeid;

						//	Assigning type to the attribute.
						attr.value.type = AssignType((AST.ASTNode) attr.value, cls);
						
						// Checking if the attribute type is well defined.
						if (!attrid.equals("Object") && !(program.classes.contains(inheritance.ClassReference.get(attrid)))) {
							String err = "Class " + attrid + " of attribute " + attr.name + " is undefined" + ".";
							reportError(cls.filename, cls.lineNo, err);
						}
						else if(!attr.value.type.equals("_no_type")){
							ClassBlock c1 = classData.classBlock.get(attrid);
							ClassBlock c2 = classData.classBlock.get(attr.value.type);

							// Checking if the assigned and declared types of attribute match. 
							if (!(c1 == leastCommonAncestor(c1, c2))) {	
								String err = "Inferred type " + attr.value.type + " of initialization of attribute " + attr.name + " does not conform to declared type " + attrid + ".";
								reportError(cls.filename, cls.lineNo, err);
							}
						}
					}

					// If the feature is a method.
					else {
						name = ((AST.method) clsft).name;
						scopeTable.insert(name, (AST.ASTNode) clsft);
						AST.method mthd = (AST.method) clsft;
						scopeTable.enterScope();

						// Traversing over all the parameters of the method and inserting them into the ScopeTable.
						for (AST.formal param : mthd.formals) {
							scopeTable.insert(param.name, (AST.ASTNode) param);
						}
						
						String mthdid = mthd.typeid;

						//	Assigning type to the method.
						mthd.body.type = AssignType((AST.ASTNode) mthd.body, cls);
						scopeTable.exitScope();

						// Checking if the method type is well defined.
						if ( !mthdid.equals("Object") && !(program.classes.contains(inheritance.ClassReference.get(mthdid)))) {
							String err = "Undefined return type " + mthdid + " in method "+ mthd.name + ".";
							reportError(cls.filename, cls.lineNo, err);
						}
						else {
							ClassBlock c1 = classData.classBlock.get(mthdid);
							ClassBlock c2 = classData.classBlock.get(mthd.body.type);

							// Checking if the assigned and declared types of method match. 
							if (!(c1 == leastCommonAncestor(c1, c2))) {
								String err = "Inferred return type " + mthd.body.type + " of method " + name + " does not conform to declared return type " + mthdid + ".";
								reportError(cls.filename, cls.lineNo, err);
							}
						}
					}
				}
				scopeTable.exitScope();
			}
		}

		// Checking whether Class 'Main' is defined.
		if(!program.classes.contains(inheritance.ClassReference.get("Main"))) {
			System.out.println("Class Main is not defined.");
			errorFlag = true;
		}

		// Checking whether Method 'main' is defined in Class 'Main'.
		else {
			boolean check = false;
			AST.class_ cls = inheritance.ClassReference.get("Main");
			for (AST.feature clsft : cls.features) {
				if (clsft instanceof AST.method) {
					AST.method newMthd = (AST.method) clsft;
					if (newMthd.name.equals("main"))
						check = true;
				}
				
			}
			if (!check) {
				String err = "No 'main' method in class Main.";
				reportError(cls.filename, cls.lineNo, err);
			}
		}

		program.classes.removeAll(Arrays.asList(inheritance.ClassReference.get("IO"),inheritance.ClassReference.get("String"),inheritance.ClassReference.get("Int"),inheritance.ClassReference.get("Bool")));
	}
}
