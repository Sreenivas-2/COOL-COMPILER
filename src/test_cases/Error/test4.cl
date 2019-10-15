class Main inherits IO {
	print() : IO {{
			new IO.out_string("print");
		}};
};

class A inherits B{
	scan() : IO {{
			new IO.out_string("scan");
		}};
};
