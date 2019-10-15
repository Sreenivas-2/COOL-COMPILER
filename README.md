# DESIGN & CODE STRUCTURE FOR SEMANTIC ANALYSIS

## Overview:

* Semantic analysis is divided into multiple sub-tasks as follows.

* Inheritance: 
	* We traverse the AST to build the Inheritance graph of all classes. Vertices of the graph and its corresponding class is mapped using HashMap.
			
			public HashMap<String, Integer> ClasstoIndex;
			public HashMap<String, AST.class_ > ClassReference;
	
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
	* During method over-riding check, the order of checking is:
		* check whether the return type is same.
		* check whether the number of parameters are same.
		* check whether the types of all formal parameters are same.
	 * If any of the above check has failed, we raise error and need not check the later (eg, if return type is not same, we raise error and need not check for formal parameters of the method).
	* If a class is not properly defined, appropriate error messages are raised.
	* Classes are then added to the HashMap excluding the attributes or methods which are not properly defined as explained above.
	
* Scoping and Type Checking:
	* We build out Scope Table, for which we traverse over all the AST nodes.
	* Based on the instance of the node, types are assigned to the expressions. The prototype of the function that assigns types to expressions is as follows.
	
			public String AssignType(AST.ASTNode node, AST.class_ paramcls);
	
	* Whenever we traverse an AST.class_ , we first enter to its scope, and add methods to the present scope and add their corresponding parameters after entering to the scope of the method. We then exit the scope of the method and check whether the return type of method conform. We raise error if the return type doesnot conform (value assigned to the attribute must have same type of attribute or any of its descendants).
	* We add the attributes and also check the type of attribute conform. We raise error if the return type doesnot conform.

* Finally we check whether the Main class is present (we raise an error if it is not present). We also check whether the main method in the Main class.

## Code Structure:

* Analysis related to Inheritance is performed in Inheritance.java
* Blue-print to store the information of class is defined in ClassBlock.java
* Information of all defined and basic classes are added to a HashMap in ClassData.java
* Scoping and Type Checking are done in Semantic.java itself.
* Also, the presence of Main class and main method in Main class, are verified in Semantic.java at the end of our analysis.

## Test Cases:
Several Test cases are written to verify each possible semantic error. Some of the interesting test cases are as follows.
* Whenever a class is involved in Inheritance cycle, error must be raised under the name of its descendant classes also.
* When a method has multiply defined formal parameters, error is raised and also the method is added to the methodList of class.
* In the initialization of attributes, we must consider the use of attributes that are declared later.
* For attributes with undefined types, raise error and set its type as **Object**.
* In dispatch, in the place of original formal type, we can even pass object of one of its descendant classes.
