class Main inherits IO {
	i:Int <- 2;
	main() : Int {{
			while i loop {
				i;
			}
			pool;
			i;
		}};
};

