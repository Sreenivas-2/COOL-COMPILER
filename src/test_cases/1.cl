class Main inherits IO {
  obj:A <- new B;
  main() : Object {{
      obj.scan(new A);
    }};
};

class A inherits IO{
  scan(num : IO) : IO {{
      new IO.out_string("scan");
    }};
};

class B inherits A{
};