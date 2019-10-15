class Main inherits IO {
	obj:A <- obj;
	main() : Object {{
			obj.print("print");
		}};
};

class A {
	scan(num : Int) : IO {{
			new IO.out_string("scan");
		}};
};
