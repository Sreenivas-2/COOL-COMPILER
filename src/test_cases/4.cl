class Main inherits IO {
  main(): A {
    case new IO of
      i:String => new A;
      j:Int => new C;
      esac
  };  
};

class A inherits IO{

};

class B inherits IO{
  
};

class C inherits A{
  
};