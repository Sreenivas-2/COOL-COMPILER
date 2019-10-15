class Main inherits IO {
	i:Int <- j;
	j:Int <- 0;
	obj:Main <- new Main;
	main() : Int{{
		let k:Int <- j,
		j:Int <- 0 in
		1;
		}};
	print() : Int{{
	1;
	}};
};
