package cool;

import java.util.*;

import cool.AST.*;

public class ClassData {

    // A HashMap form class name to ClassBlock 
	public HashMap <String, ClassBlock> classBlock = new HashMap<String, ClassBlock>();
	 
	// Constructor adds the information of basic classes. 
	public ClassData(HashMap<String, AST.class_> clsMap) {
        // methods for "Object" and put into ClassBlock
		HashMap<String, AST.method> objectMethods = new HashMap <String, AST.method>();
		objectMethods.put("abort", new AST.method("abort", new ArrayList<AST.formal>(), "Object", new AST.no_expr(0), 0));
        objectMethods.put("type_name", new AST.method("type_name", new ArrayList<AST.formal>(), "String", new AST.no_expr(0), 0));
        objectMethods.put("copy", new AST.method("copy", new ArrayList<AST.formal>(), "Object", new AST.no_expr(0), 0));
		
		classBlock.put("Object", new ClassBlock(clsMap.get("Object"), new HashMap<String, AST.attr>(), objectMethods, 0));

        // methods for "IO" and put into ClassBlock
		HashMap <String, AST.method> IOMethods = new HashMap<String, AST.method>();
		IOMethods.put("out_string", new AST.method("out_string", Arrays.asList(new AST.formal("out_string", "String", 0)), "IO", new AST.no_expr(0), 0));
		IOMethods.put("out_int", new AST.method("out_int", Arrays.asList(new AST.formal("out_int", "Int", 0)), "IO", new AST.no_expr(0), 0));
		IOMethods.put("in_string", new AST.method("in_string", new ArrayList<AST.formal>(), "String", new AST.no_expr(0), 0));
		IOMethods.put("in_int", new AST.method("in_int", new ArrayList<AST.formal>(), "Int", new AST.no_expr(0), 0));
		
		classBlock.put("IO", new ClassBlock(clsMap.get("IO"), new HashMap<String, AST.attr>(), IOMethods, 1));
		classBlock.get("IO").methodList.putAll(objectMethods);	

        // methods for "Int" and put into ClassBlock
		classBlock.put("Int", new ClassBlock(clsMap.get("Int"), new HashMap<String, AST.attr>(), new HashMap<String, AST.method>(), 1));
		classBlock.get("Int").methodList.putAll(objectMethods);

        // methods for "Bool" and put into ClassBlock
		classBlock.put("Bool", new ClassBlock(clsMap.get("Bool"), new HashMap<String, AST.attr>(), new HashMap<String, AST.method>(), 1));
		classBlock.get("Bool").methodList.putAll(objectMethods);	

        // methods for "String" and put into ClassBlock 
		HashMap <String, AST.method> stringMethods = new HashMap<String, AST.method>();
		stringMethods.put("length", new AST.method("length", new ArrayList<AST.formal>(), "Int", new AST.no_expr(0), 0));
		stringMethods.put("concat", new AST.method("concat", Arrays.asList(new AST.formal("s", "String", 0)) , "String", new AST.no_expr(0), 0));
		stringMethods.put("substr", new AST.method("substr", Arrays.asList(new AST.formal("i", "Int", 0), new AST.formal("l", "Int", 0)) , "String", new AST.no_expr(0), 0));
		
		classBlock.put("String", new ClassBlock(clsMap.get("String"), new HashMap<String, AST.attr>(), stringMethods, 1));
		classBlock.get("String").methodList.putAll(objectMethods);	
	}
	
	public void insertClass (AST.class_ cls) {
		ClassBlock c = new ClassBlock(cls, classBlock.get(cls.parent).attributeList, classBlock.get(cls.parent).methodList, classBlock.get(cls.parent).level + 1);
		ArrayList<String> attr = new ArrayList<String>();
		ArrayList<String> mthd = new ArrayList<String>();
		for (AST.feature clsft : cls.features) {
			if (clsft instanceof AST.attr) {
				AST.attr newAttr = (AST.attr) clsft;
				// Check if the attribute is already defined in the class.
				if(attr.contains(newAttr.name)){
					String err = "Attribute " + newAttr.name + " is multiply defined in class.";
					Semantic.reportError(cls.filename, newAttr.lineNo, err);
				}
				// Check if the attribute is already defined in the inherited class.
				else if (classBlock.get(cls.parent).attributeList.containsKey(newAttr.name)) {
					String err = "Attribute " + newAttr.name + " is an attribute of an inherited class";
					Semantic.reportError(cls.filename, newAttr.lineNo, err);
				}
				// Add the attribute to the attributeList.
				else {
					c.attributeList.put(newAttr.name, newAttr);
					attr.add(newAttr.name);
				}
			}
			else if(clsft instanceof AST.method) {
				AST.method newMthd = (AST.method) clsft;
				// Check if the method is already defined in the class.
                if(mthd.contains(newMthd.name)) {
                	String err = "Method " + newMthd.name +" is multiply defined.";
					Semantic.reportError(cls.filename, newMthd.lineNo, err);
				}
				else {
					ArrayList<String> param = new ArrayList<String> ();
					boolean error = false;
					// *error* variable decides whether method is properly defined.

					// Check whether parameters within the method are multiply defined.
					for (AST.formal clsparam : newMthd.formals) {
						if (param.contains(clsparam.name)) {
							String err = "Formal parameter " + clsparam.name + " is multiply defined.";
							Semantic.reportError(cls.filename, newMthd.lineNo, err);
						}
						else
							param.add(clsparam.name);
					}
					// Check whether the method is overloaded.
					if (classBlock.get(cls.parent).methodList.containsKey(newMthd.name)) {
						AST.method initMthd = classBlock.get(cls.parent).methodList.get(newMthd.name);
						if (!(initMthd.typeid.equals(newMthd.typeid))){
							String err = "In redefined method " + newMthd.name + ", return type " + newMthd.typeid + " is different from original return type " + initMthd.typeid + ".";
							Semantic.reportError(cls.filename, newMthd.lineNo, err);
							error = true;
						}
						else if (initMthd.formals.size() != newMthd.formals.size()) {
							String err = "Incompatible number of formal parameters in redefined method " + newMthd.name + ".";
							Semantic.reportError(cls.filename, newMthd.lineNo, err);
							error = true;
						}
						else {
							for (Integer i = 0; i < initMthd.formals.size(); i++) {
								if (!(initMthd.formals.get(i).typeid.equals(newMthd.formals.get(i).typeid))) {
									String err = "In redefined method " + newMthd.name + ", parameter type " + newMthd.formals.get(i).typeid + " is different from original type " + initMthd.formals.get(i).typeid + ".";
									Semantic.reportError(cls.filename, newMthd.lineNo, err);
									error = true;
								} 
							}
						}
					}
					// We add the method to the methodList if the method is not overloaded.
					if (!error) {
						c.methodList.put(newMthd.name, newMthd);
						mthd.add(newMthd.name);
					}
				}
			}
		}
		classBlock.put(cls.name, c);
	}
}
