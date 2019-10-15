class Main inherits IO {
	obj:B <- new A;
	main() : IO {{
			obj.scan();
			--new A;
		}};
};

class A inherits IO {
	scan() : Int {{
			1;
		}};
};