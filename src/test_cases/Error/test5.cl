class Main inherits IO {
	main() : IO {{
			new IO.out_string("HI");
		}};
};

class A inherits B {
	print() : IO {{
			new IO.out_string("print");
		}};
};

class B inherits A {
	scan() : IO {{
			new IO.out_string("scan");
		}};
};
