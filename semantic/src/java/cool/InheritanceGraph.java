package cool;

import java.util.*;

public class InheritanceGraph {
	
	public HashMap<String, Integer> ClasstoIndex = new HashMap<String, Integer>();;
	public HashMap<String, AST.class_ > ClassReference;
	public AST.program program;
	Integer class_size;
	ArrayList<ArrayList<String>> graph;
	ArrayList<String> Added_Classes ;
	ArrayList<String> Inherit_classes; 

	public InheritanceGraph(AST.program programName) {
		program = programName;
		ClassReference = new HashMap<String, AST.class_>();
		graph = new ArrayList< ArrayList<String>> ();
		Inherit_classes = new ArrayList<String>(Arrays.asList("String","Int","Bool"));
		ClasstoIndex.put("Object", 0);
		ClasstoIndex.put("IO", 1);
		graph.add(new ArrayList<String>());
		graph.add(new ArrayList<String>());
		graph.get(ClasstoIndex.get("Object")).add("IO");
		class_size = 2;
		Added_Classes = new ArrayList<String>();
		Added_Classes.add("Object");	
		Added_Classes.add("IO");
		addClasses();
		addEdges();
	}
	
	public ArrayList<String> addClasses() {
		ArrayList<String> Redefine_classes = new ArrayList<String>(Arrays.asList("Object","IO","String","Int","Bool"));
		for (AST.class_ cls : program.classes) {
			if (Redefine_classes.contains(cls.name)) {
				String err = "Redefinition of basic class " + cls.name + ".";
				Semantic.reportError(cls.filename, cls.lineNo, err);
				// System.exit(1);
			}
			else if (Inherit_classes.contains(cls.parent)) {
				String err = "Class " + cls.name + " cannot inherit class " + cls.parent + ".";
				Semantic.reportError(cls.filename, cls.lineNo, err);
				// System.exit(1);
			}
			else if (Added_Classes.contains(cls.name)) {
				String err = "Class " + cls.name + " was previously defined.";
				Semantic.reportError(cls.filename, cls.lineNo, err);
				// System.exit(1);
			}
			else {
				Added_Classes.add(cls.name);
				ClasstoIndex.put(cls.name, class_size);
				class_size++;
				graph.add(new ArrayList<String>());
			}
		}
		return Added_Classes;
	}

	public void addEdges() {
		for (AST.class_ cls : program.classes) {
			if (!Added_Classes.contains(cls.parent)) {
				if (!Inherit_classes.contains(cls.parent)) {
					String err =  "Class " + cls.name + " inherits from an undefined class " + cls.parent + "." ;
					Semantic.reportError(cls.filename, cls.lineNo, err);
				}
			}
			else
				graph.get(ClasstoIndex.get(cls.parent)).add(cls.name);
		}
	}


	//change names for dfs method
	public boolean isCyclicUtil(int i, boolean[] visited,boolean[] recStack) {
		if (recStack[i]) 
			return true; 
		if (visited[i]) 
			return false; 
		visited[i] = true; 
		recStack[i] = true; 
		List<String> children = graph.get(i);
		for (String c: children) 
			if (isCyclicUtil(ClasstoIndex.get(c), visited, recStack)) 
				return true; 		
		recStack[i] = false;
		return false; 
	}


	public boolean isCyclic() {
		boolean[] visited = new boolean [class_size]; 
		boolean[] recStack = new boolean [class_size]; 
		boolean[] isinCyle = new boolean [class_size];
		boolean cycle = false;
		for (AST.class_ cls : program.classes) {
			ClassReference.put(cls.name, cls);
			if(isinCyle[ClasstoIndex.get(cls.parent)]) {
				isinCyle[ClasstoIndex.get(cls.name)] = true;
				String err = "Class " + cls.name + ", or an ancestor of " + cls.name + ", is involved in an inheritance cycle.";
				Semantic.reportError(cls.filename, 1, err); 
			}
			else if (isCyclicUtil(ClasstoIndex.get(cls.name), visited, recStack)) {
				cycle = true;
				isinCyle[ClasstoIndex.get(cls.name)] = true;
				String err = "Class " + cls.name + ", or an ancestor of " + cls.name + ", is involved in an inheritance cycle.";
				Semantic.reportError(cls.filename, 1, err); 
			}
		}
		if (cycle)
			return true;
		return false;
	}
}
