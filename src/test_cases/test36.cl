class A inherits B {
	print(num : String) : IO {{
			new IO.out_string("A");
		}};
};

class B {
	print(num : String) : IO {{
			new IO.out_string("B");
		}};
};
