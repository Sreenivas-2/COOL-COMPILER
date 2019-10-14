class Main inherits IO {
	main() : IO {{
			new IO.out_string("HI");
		}};
};

class A {
	print() : IO {{
			new IO.out_string("print");
		}};
	print() : IO {{
		new IO.out_string("print");
	}};
};
