class Main inherits IO {
        ans : Int;
		ext : Int;
    	main() : Int {
		{
			ext <- 3;
            if 0 <= ext then {
				ans <- 0;
				ans <- ans + 1;
            } else ans <- ans - 1 fi;
		}
	};
};
