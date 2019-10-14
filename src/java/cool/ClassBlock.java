package cool;
import java.util.*;

public class ClassBlock {
	public AST.class_ cls;
	public HashMap <String, AST.attr> attributeList;
	public HashMap <String, AST.method> methodList;
	public int level;

	ClassBlock (AST.class_ classReference, HashMap<String, AST.attr> attrList, HashMap<String, AST.method> methList, int l) {
        cls  = classReference;
        level = l;
		attributeList = new HashMap <String, AST.attr>();
		attributeList.putAll(attrList);
		methodList = new HashMap <String, AST.method>();
		methodList.putAll(methList);
	}
}