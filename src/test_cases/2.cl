class Main inherits IO {
  obj:A <- obj;
  main() : Object {{
      obj.scan(new A);
    }};
};

class A inherits IO{
  scan(num : IO) : IO {{
      new IO.out_string("scan");
    }};
};