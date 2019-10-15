class Main inherits IO {
	obj:A <- obj;
	main() : IO {{
			obj.print("print");
			new A;
		}};
};

class A inherits IO {
	i:Int <- 0;
	i:Int <- 0;
	scan(num : Int) : IO {{
			new IO.out_string("scan");
		}};
};

class B inherits A {
	j:Int <- 0;
	j:Int <- 0;
	scan(num : String) : IO {{
			new IO.out_string("scan");
		}};
};