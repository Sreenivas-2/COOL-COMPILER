class Main inherits IO {
  obj:A <- obj;
  main(): IO {
    case new IO of
      i:String => obj;
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