class Main inherits IO {
	obj:A <- obj;
	main() : IO {{
			obj.scan("print");
		}};
};

class A {
	scan(num : Int) : IO {{
			new IO.out_string("scan");
		}};
};
