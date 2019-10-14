class Main inherits A {
	a:A <- a;
 main() : IO {{
	 a.print();
  }};
};

class A inherits IO {
	io:IO<-io;
 print() : IO {{
	 io.out_string("A");
  }};
};

