class Main inherits IO {
	i:Int <- 0;
	obj:A <- new A;
	main() : SELF_TYPE {{
			obj@IO.out_string();
		}};
};

class IO {
	out() : Int {
		1
	};
};