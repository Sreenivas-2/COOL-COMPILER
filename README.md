*******************************************************************************************************************************
						Design of Semantic Analysis 
*******************************************************************************************************************************

(*) The program traverses the AST to prepare the Inheritance graph of all classes. We check for inheritance cycles using DFS, which can also detect multiple cycles. In the case of inheritance graph errors, we do not recover, and exit after raising error messages.

(*) Then we store the class information in a HashMap. In this, each class name is mapped to an object whose blue-print (class) is as follows.

>	public class ClassBlock {
>		public AST.class_ cls;
>		public HashMap <String, AST.attr> attributeList;
>		public HashMap <String, AST.method> methodList;
>	}

(*) Then we build out Scope Table, for which we traverse over all the AST nodes and add the corresponding attributes. Appropriate checks are in place to ensure that all types conform, and variables are in scope.

(*) Finally we check whether the Main class is present (we raise an error if it is not present). We also check whether the main method in the Main class.
