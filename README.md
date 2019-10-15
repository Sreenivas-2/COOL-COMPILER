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

* Class Information:
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
	* We check whether the classes are properly defined. For a class to be properly defined, the following conditions must be satisfied.
		* attributes/methods must not be defined multiple times within the class.
		* attributes that are inherited from other class must not be redefined.
		* parameters within same method must not be multiply defined.
		* no method overloading.
	* If a class is not properly defined, appropriate error messages are raised.
	* Classes are then added to the HashMap excluding the attributes or methods which are not properly defined as explained above.
	
* Scope Table:
	* We build out Scope Table, for which we traverse over all the AST nodes.
	* Whenever we traverse an AST node, we first enter to its scope, and add the attributes and also check the type of attribute conform (value assigned to the attribute must have same type of attribute or any of its descendants). We raise error if the return type doesnot conform.
	* We add methods to the present scope and add their corresponding parameters after entering to the scope of the method. We then exit the scope of the method and check whether the return type of method conform. We raise error if the return type doesnot conform.

* Finally we check whether the Main class is present (we raise an error if it is not present). We also check whether the main method in the Main class.
