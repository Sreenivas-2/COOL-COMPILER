# DESIGN OF SEMANTIC ANALYSIS

## Overview:

* Semantic analysis is divided into multiple sub-tasks as follows.

* Inheritance: 
	* We traverse the AST to build the Inheritance graph of all classes.
	* While adding to the Inheritance graph, we check that Basic classes (Object, IO, String, Int, Bool) are not redefined.
	* We also check that classes do not inherit from basic classes like String, Int, Bool.
	* We, then, check that classes are not defined multiple times.
	* We check whether parent classes of all classes are defined in the program.
	* If any of the above conditions fails, we stop our analysis after giving error messages.
	* We check for inheritance cycles using DFS, which can also detect multiple cycles.
	* If there are one or more cycles, we do not recover and exit after giving error messages for all those cycles.

* Store the Class Information:
	* We define a Class Block for each class which stores the basic information about the class like attributes, methods, level of the class in Inheritance graph.
			
			public class ClassBlock {
				public AST.class_ cls;
				public HashMap <String, AST.attr> attributeList;
				public HashMap <String, AST.method> methodList;
				public int level;
			}
			
	* We will have a mapping from class name to corresponding ClassBlock using HashMap.
	
			public HashMap <String, ClassBlock> classBlock;
			
	* We add the information of basic classes (Object, IO, String, Int, Bool) to the HashMap.
	* We check whether the classes are properly defined (i.e., attributes are not defined multiple times, attributes that are inherited from other class are not redefined, no method overloading)

* Then we build out Scope Table, for which we traverse over all the AST nodes and add the corresponding attributes. Appropriate checks are in place to ensure that all types conform, and variables are in scope.

* Finally we check whether the Main class is present (we raise an error if it is not present). We also check whether the main method in the Main class.
