class Main inherits IO {
	main() : IO {
		{
			out_string("This program checks whether given input is a PALINDROME (CASE-SENSITIVE)\n");
			out_string("Enter the string: ");
			if palindrome(in_string()) then
			 	out_string("Entered string is a palindrome\n")
			 else
			 	out_string("Entered string is not a palindrome\n")
			 fi;
		}
	};
	palindrome(w : String) : Bool {                    -- Function for finding whether given string is palindrome or not
		if w.length() = 0 then true                    -- If string length is 0 then yes
		else if w.length() = 1 then true               -- If string length is 1 then yes
		else if w.substr(0, 1) = w.substr(w.length()-1, 1) then palindrome(w.substr(1,w.length()-2))   -- if first and last elements are same then we move to substring excluding these two elements by making a recursive call over substring.
		else false                                     -- if none happens it is not palindrome
		fi fi fi
	};
};
