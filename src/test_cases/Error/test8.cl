class Main inherits IO {
	main() : IO {{
			new IO.out_string("HI");
		}};
};

class A {
	print(num : Int, num : Int) : IO {{
			new IO.out_string("print");
		}};
};
