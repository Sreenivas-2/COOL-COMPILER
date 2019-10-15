class Main inherits IO {
    pal(s : String) : Bool {
  if s.length() = 0
  then true
  else if s.length() = 1
  then true
  else if s.substr(0, 1) = s.substr(s.length() - 1, 1)
  then new Bool
  else false
  fi fi fi
    };

    i : Int <-  0;

    main() : Int {
  {
      if 1 < 2
      then 1
      else 2
      fi;
  }
    };
};