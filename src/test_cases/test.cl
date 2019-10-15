class Main {
  x:String <-  "Hello";
  main():Int {{
      -- (new A).print(x);
      1;
    }};
};

class A {
  print(a : String, a : Int):Int {
    1
  };
};

class B inherits A {
  print(a : String):Int {
    1
  };

  print(a : Int):Int {
    1
  };
};