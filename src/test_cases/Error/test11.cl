class Main inherits IO {
	i:Int <- 0;
	main() : IO {{
			new IO.out_string("HI");
		}};
};

class A inherits B {
	print(num : Bool) : IO {{
			new IO.out_string("print");
		}};
};

class B {
	print(num : Int) : IO {{
			new IO.out_string("print");
		}};
};
