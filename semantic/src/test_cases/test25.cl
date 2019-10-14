class Main inherits IO {
	obj1:A <- obj1;
	obj2:B <- obj2;
	main() : IO {{
			obj1@B.print("print");
			new IO;
		}};
};

class A {
	print(num : String) : IO {{
			new IO.out_string("A");
		}};
};

class B {
	print(num : String) : IO {{
			new IO.out_string("B");
		}};
};
