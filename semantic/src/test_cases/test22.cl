class Main inherits IO {
	obj:A <- obj;
	main() : IO {{
			obj.scan("print");
		}};
};

class A {
	scan() : IO {{
			new IO.out_string("scan");
		}};
};
