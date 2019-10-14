class Main inherits IO {
	i:Int <- 0;
	obj:A <- new A;
	main() : SELF_TYPE {{
			obj@IO.out_string();
		}};
};

class A inherits String {
	out() : Int {
		1
	};
};