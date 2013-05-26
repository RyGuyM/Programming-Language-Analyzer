import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/* This is a run-time library for the semantic analyzer for the FL language. It implements
 * all the built-in functions that exist in the language and also can be used to run user
 * defined functions. It has the ability to read user input and prints out the answer to std
 * out.
 */
public class RunLibrary 
{
	/* The library uses three structures. THe Stack is what is used to operate the language. THe
	 * array list is used to represent the global array and the hash map is used to stored variables.
	 */
	private Stack<Object> runStack;
	private ArrayList<RefObject<Object>> globalArray;
	private HashMap<Integer, RefObject<Object>> variables;
	
	// A constructor to start before the library initializes the stack, global array and hash map.
	public RunLibrary()
	{
		runStack = new Stack<Object>();
		globalArray = new ArrayList<RefObject<Object>>();
		variables = new HashMap<Integer, RefObject<Object>>();
	}
	
	// Put item on the stack depending on if it is a number, string, boolean, or character.
	public void addTo(String type, String value)
	{
		// If the token is a number than it has to be pushed onto the stack.
		if(type.equals("-Number-"))
		{
			Integer i = Integer.parseInt(value);
			runStack.push(i);
		}
		// If the token is a string than it has to be pushed onto the stack.
		else if(type.equals("-String-"))
		{
			runStack.push(value);
		}
		// If the token is a character than it has to be pushed onto the stack.
		else if(type.equals("-Character-"))
		{
			Character c = '\0';
			// This if-else block checks for special escape characters to use.
			if(value.equals("\\n"))
				c = '\n';
			else if(value.equals("\""))
				c = '\"';
			else if(value.equals("\\'"))
				c = '\'';
			else
				c = value.charAt(0);
			// Push the character onto the stack.
			runStack.push(c);
		}
		else
		{
			Boolean b = Boolean.parseBoolean(value);
			runStack.push(b);
		}
	}
	// Adds a reference to the stack which is a named variable in a hash table.
	public void addReference(String val)
	{
		// Sum up the characters of the variable name to use as the key.
		Integer sum = 0;
		for(int b = 0; b < val.length(); b++)
		{
			sum = sum + val.charAt(b);
		}
		// Check if this variable has already been declared.
		if(variables.containsKey(sum))
		{
			runStack.push(variables.get(sum));
		}
		// Otherwise create a new key, object and push it onto the stack.
		else
		{
			variables.put(sum, new RefObject<Object>());
			runStack.push(variables.get(sum));
		}
	}
	// Reads a character from the standard input using a input stream and buffered reader.
	public void read() throws IOException
	{
		// Create a new input stream reader and buffered reader.
		InputStreamReader reader = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(reader);
		
		// Place the character in a character object.
		Character c = (char)in.read(); 
		
		// Push this character onto the stack.
		runStack.push(c);
	}
	// Writes a character from the top of the stack to the standard output.
	public void write()
	{
		System.out.print(runStack.pop());
	}
	
	/* Adds a reference to a global array onto the stack. The array index is an integer
	 * value on top of the stack.
	 */
	public void ref()
	{
		Integer i = (Integer)runStack.pop();
		
		// Try to get the array index of the global array if it has initialized yet.
		try
		{
			runStack.push(globalArray.get(i));
		}
		// If it throws an error it doesn't exist. 
		catch(IndexOutOfBoundsException e)
		{
			globalArray.add(i, new RefObject<Object>());
			runStack.push(globalArray.get(i));	
		}
	}
	
	/* Load takes a reference from the top of stack and pushes the value stored in
	 * this reference location and pushes that onto the stack. Gives a runtime error
	 * if the reference hasnt been initialized yet.
	 */
	public void load()
	{
		RefObject<Object> r = (RefObject<Object>)runStack.pop();
		
		runStack.push(r.getValue());
	}
	/* Takes a reference from the top of the stack and an object below it and assigns
	 * the value of the object to the reference and pushes nothing onto the stack.
	 */
	public void save()
	{
		// Pop a reference object.
		RefObject<Object> r = (RefObject<Object>)runStack.pop();
		// Pop another object.
		Object x = runStack.pop();
		
		r.setValue(x);
	}
	// Add the two top values of the integer together and pushes this value onto the stack.
	public void add()
	{
		Integer y = (Integer)runStack.pop();
		Integer x = (Integer)runStack.pop();
		
		Integer add = x + y;
		runStack.push(add);
	}
	// Subtracts the two top integers on the stack and pushes the value onto the stack.
	public void subtract()
	{
		Integer y = (Integer)runStack.pop();
		Integer x = (Integer)runStack.pop();
		
		Integer subtract = x - y;
		runStack.push(subtract);
	}
	// Multiplies the two top integers on the stack and pushes that value onto the stack.
	public void multiply()
	{
		Integer y = (Integer)runStack.pop();
		Integer x = (Integer)runStack.pop();
		
		Integer multiply = x * y;
		runStack.push(multiply);
	}
	// Divides the two top integers on the stack and pushes the division of this and the mod of this onto the stack.
	public void divide()
	{
		Integer y = (Integer)runStack.pop();
		Integer x = (Integer)runStack.pop();
		
		// Divide the two integers.
		Integer divide1 = x / y;
		// Mod the two integers.
		Integer divide2 = x % y;
		
		runStack.push(divide1);
		runStack.push(divide2);
	}
	// Negate the top integer value on the stack and push it back onto the stack.
	public void negative()
	{
		Integer y = -((Integer)runStack.pop());
		
		runStack.push(y);
	}
	// Returns the boolean value of the comparison less than of the top two objects on the stack.
	public void lessThan()
	{
		// If the object is an integer.
		if(runStack.peek() instanceof Integer)
		{
			Integer y = (Integer)runStack.pop();
			Integer x = (Integer)runStack.pop();
			// Compare the two values.
			if(x.compareTo(y) < 0) 
				runStack.push(true);
			else
				runStack.push(false);
		}
		// If the object is a boolean.
		else if(runStack.peek() instanceof Boolean)
		{
			Boolean y = (Boolean)runStack.pop();
			Boolean x = (Boolean)runStack.pop();
			
			// Compare the two values.
			if(x.compareTo(y) < 0) 
				runStack.push(true);
			else
				runStack.push(false);
		}
		// If the object is a character.
		else
		{
			Character y = (Character)runStack.pop();
			Character x = (Character)runStack.pop();
			
			// Compare the two values.
			if(x.compareTo(y) < 0) 
				runStack.push(true);
			else
				runStack.push(false);
		}
	}
	// Returns the boolean value of the comparison greater than of the top two objects on the stack.
	public void greaterThan()
	{
		// If the object is an integer.
		if(runStack.peek() instanceof Integer)
		{
			Integer y = (Integer)runStack.pop();
			Integer x = (Integer)runStack.pop();
			// Compare the two values.
			if(x.compareTo(y) > 0) 
				runStack.push(true);
			else
				runStack.push(false);
		}
		// If the object is a boolean.
		else if(runStack.peek() instanceof Boolean)
		{
			Boolean y = (Boolean)runStack.pop();
			Boolean x = (Boolean)runStack.pop();
			
			// Compare the two values.
			if(x.compareTo(y) > 0) 
				runStack.push(true);
			else
				runStack.push(false);
		}
		// If the object is a character.
		else
		{
			Character y = (Character)runStack.pop();
			Character x = (Character)runStack.pop();
			
			// Compare the two values.
			if(x.compareTo(y) > 0) 
				runStack.push(true);
			else
				runStack.push(false);
		}
	}
	// Returns the boolean value of the comparison less than or equal to of the top two objects on the stack.
	public void lessThanEq()
	{
		// If the object is an integer.
		if(runStack.peek() instanceof Integer)
		{
			Integer y = (Integer)runStack.pop();
			Integer x = (Integer)runStack.pop();
			// Compare the two values.
			if(x.compareTo(y) <= 0) 
				runStack.push(true);
			else
				runStack.push(false);
		}
		// If the object is a boolean.
		else if(runStack.peek() instanceof Boolean)
		{
			Boolean y = (Boolean)runStack.pop();
			Boolean x = (Boolean)runStack.pop();
			
			// Compare the two values.
			if(x.compareTo(y) <= 0) 
				runStack.push(true);
			else
				runStack.push(false);
		}
		// If the object is a character.
		else
		{
			Character y = (Character)runStack.pop();
			Character x = (Character)runStack.pop();
			
			// Compare the two values.
			if(x.compareTo(y) <= 0) 
				runStack.push(true);
			else
				runStack.push(false);
		}
	}
	// Returns the boolean value of the comparison greater than or equal to the top two objects on the stack.
	public void greaterThanEq()
	{
		// If the object is an integer.
		if(runStack.peek() instanceof Integer)
		{
			Integer y = (Integer)runStack.pop();
			Integer x = (Integer)runStack.pop();
			// Compare the two values.
			if(x.compareTo(y) >= 0) 
				runStack.push(true);
			else
				runStack.push(false);
		}
		// If the object is a boolean.
		else if(runStack.peek() instanceof Boolean)
		{
			Boolean y = (Boolean)runStack.pop();
			Boolean x = (Boolean)runStack.pop();
			
			// Compare the two values.
			if(x.compareTo(y) >= 0) 
				runStack.push(true);
			else
				runStack.push(false);
		}
		// If the object is a character.
		else
		{
			Character y = (Character)runStack.pop();
			Character x = (Character)runStack.pop();
			
			// Compare the two values.
			if(x.compareTo(y) >= 0) 
				runStack.push(true);
			else
				runStack.push(false);
		}
	}
	// Returns the boolean value of the comparison equals to of top two objects on the stack.
	public void equal()
	{
		// If the object is an integer.
		if(runStack.peek() instanceof Integer)
		{
			Integer y = (Integer)runStack.pop();
			Integer x = (Integer)runStack.pop();
			
			// Compare the two values.
			if(x.compareTo(y) == 0) 
				runStack.push(true);
			else
				runStack.push(false);
		}
		// If the object is a boolean.
		else if(runStack.peek() instanceof Boolean)
		{
			Boolean y = (Boolean)runStack.pop();
			Boolean x = (Boolean)runStack.pop();
			
			// Compare the two values.
			if(x.compareTo(y) == 0) 
				runStack.push(true);
			else
				runStack.push(false);
		}
		// If the object is a character.
		else
		{
			Character y = (Character)runStack.pop();
			Character x = (Character)runStack.pop();
			
			// Compare the two values.
			if(x.compareTo(y) == 0) 
				runStack.push(true);
			else
				runStack.push(false);
		}
	}
	// Returns the boolean value of the comparison not equals to of the top two objects on the stack.
	public void notEqual()
	{
		// If the object is an integer.
		if(runStack.peek() instanceof Integer)
		{
			Integer y = (Integer)runStack.pop();
			Integer x = (Integer)runStack.pop();
			// Compare the two values.
			if(x.compareTo(y) != 0) 
				runStack.push(true);
			else
				runStack.push(false);
		}
		// If the object is a boolean.
		else if(runStack.peek() instanceof Boolean)
		{
			Boolean y = (Boolean)runStack.pop();
			Boolean x = (Boolean)runStack.pop();
			
			// Compare the two values.
			if(x.compareTo(y) != 0) 
				runStack.push(true);
			else
				runStack.push(false);
		}
		// If the object is a character.
		else
		{
			Character y = (Character)runStack.pop();
			Character x = (Character)runStack.pop();
			
			// Compare the two values.
			if(x.compareTo(y) != 0) 
				runStack.push(true);
			else
				runStack.push(false);
		}
	}
	// Takes two boolean values and logically ANDs them.
	public void and()
	{
		Boolean value = (Boolean)runStack.pop() && (Boolean)runStack.pop();
		
		runStack.push(value);
	}
	// Takes two boolean values and logically ORs them.
	public void or()
	{
		Boolean value = (Boolean)runStack.pop() || (Boolean)runStack.pop();
		
		runStack.push(value);
	}
	// Take the top boolean value on the stack and logically NOTs it.
	public void not()
	{
		Boolean value = !(Boolean)runStack.pop();
		
		runStack.push(value);
	}
	// Pops two strings from the top of the stack and concatenates them and pushes this value back.
	public void concat()
	{
		String y = (String)runStack.pop();
		String x = (String)runStack.pop();
		
		runStack.push(x + y);
	}
	// Gets the substring by popping two integers and a string from the top of the stack and pushes the substring back.
	public void substr()
	{
		Integer l = (Integer)runStack.pop();
		Integer p = (Integer)runStack.pop();
		String s = (String)runStack.pop();
		
		// If the the string section is not long enough push the whole this back.
		if(l > s.length() - p)
			runStack.push(s);
		else
			runStack.push(s.substring(p, l));
	}
	// Pops a string from the top of the stack and pushes the length of this string back onto the stack.
	public void length()
	{
		runStack.push(((String)runStack.pop()).length());
	}
	// Pops a integer and a string from the top of the stack and pushes the character at the integer i in the string.
	public void getChar()
	{
		Integer i = (Integer)runStack.pop();
		String s = (String)runStack.pop();
		
		runStack.push((Character)s.charAt(i));
	}
	// Replaces a character with c at index i in a string s and pushes this value onto the stack.
	public void putChar()
	{
		Character c = (Character)runStack.pop();
		Integer i = (Integer)runStack.pop();
		String s = (String)runStack.pop();
		
		char[] sArray = s.toCharArray();
		sArray[i] = c;
		
		String str = new String(sArray);
		
		runStack.push(str);
	}
	// Takes a boolean or integer value from the top of the stack and converts them into a string and pushes that value.
	public void tostring()
	{
		String s;
		// If it is an integer convert the integer to a string.
		if(runStack.peek() instanceof Integer)
			s = Integer.toString((Integer)runStack.pop());
		// Otherwise it must be a boolean so this is converted to a string.
		else
			s = Boolean.toString((Boolean)runStack.pop());
		// This value is then pushed onto the stack.
		runStack.push(s);
	}
	// Takes a string value from the top of the stack and converts this into an integer.
	public void toint()
	{
		String s = (String)runStack.pop();
		
		Integer convert = Integer.parseInt(s);
		
		runStack.push(convert);
	}
	// Takes a string value from the top of the stack and converts this into a boolean.
	public void toBool()
	{
		String s = (String)runStack.pop();
		
		Boolean convert = Boolean.parseBoolean(s);
		
		runStack.push(convert);
	}
	// Drops the topmost value on the stack.
	public void drop()
	{
		runStack.drop();
	}
	// Duplicates the topmost value on the stack.
	public void dup()
	{
		runStack.dup();
	}
	// Swaps the top two entries on the stack.
	public void swap()
	{
		runStack.swap();
	}
	// Rotates the top three entries on the stack.
	public void rot()
	{
		runStack.rot();
	}
	/* Branch takes a boolean off the top of the stack, if this value is true than
	 * the branch jumps to the label location in the main or subroutine. Otherwise
	 * it doesnt nothing.
	 */
	public int branch(ArrayList<Pair> tokens, String label)
	{
		// Find the location to branch to.
		int place = -1;
		
		if((Boolean)runStack.pop())
		{
			// Holds the type of the token currently being analyzed.
			String type;
			// Holds the value of the token currently being analyzed.
			String value;
			
			// Find the location in the program to branch to.
			for(int i = 0; i < tokens.size(); i++)
			{
				type = tokens.get(i).getToken();
				value = tokens.get(i).getValue();
				
				if(type.equals("-Label-") && value.equals(label))
				{
					place = i;
				}
			}
		}
		return place;
	}
	/* The jump functions returns an integer value of the location being jumped to in the 
	 * routine. This integer resets the counter and location of the program back to where
	 * the label is found.
	 */
	public int jump(ArrayList<Pair> tokens, String label)
	{
		// Find the location to branch to.
		int place = -1;
		
		// Holds the type of the token currently being analyzed.
		String type;
		// Holds the value of the token currently being analyzed.
		String value;
		
		// Finds the location to jump to.
		for(int i = 0; i < tokens.size(); i++)
		{
			type = tokens.get(i).getToken();
			value = tokens.get(i).getValue();
			
			if(type.equals("-Label-") && value.equals(label))
			{
				place = i;
			}
		}
		
		return place;
	}
	/* Create a subroutine that acts exactly like the main method but it exits when it reaches the 
	 * end token and returns back to the calling function in the main area. 
	 */
	public void subroutine(ArrayList<Pair> tokens, ArrayList<ArrayList<Pair>> subRout) throws IOException
	{
		// Holds the type of the token currently being analyzed.
		String type;
		// Holds the value of the token currently being analyzed.
		String value;
		
		// Start loop at 1 to bypass the name of the defined function.
		for(int i = 1; i < tokens.size(); i++)
		{
			
			// Get the information about the type and value of the token currently pointed to.
			type = tokens.get(i).getToken();
			value = tokens.get(i).getValue();
			
			/* Try the appropriate function and catch any errors. If an error is returned than the run
			 * time semantic check has failed and the appropraite message is return and the program 
			 * returns to the main method.
			 */
			try
			{		
				// If the token is a number than it has to be pushed onto the stack.
				if(type.equals("-Number-"))
				{
					addTo(type, value);
				}
				// If the token is a string than it has to be pushed onto the stack.
				else if(type.equals("-String-"))
				{
					addTo(type, value);
				}
				// If the token is a character than it has to be pushed onto the stack.
				else if(type.equals("-Character-"))
				{
					addTo(type, value);
				}
				// If the token is a label no action is required at this point.
				else if(type.equals("-Label-"))
				{
					// Do nothing with a label since this is just points to a place in the program sequence.
				}
				// If the token is jump than move to the label that jump is telling the program to.
				else if(type.equals("-Jump-"))
				{
					// Get the next token that is the place where jump jumps to.
					i++;
					type = tokens.get(i).getToken();
					value = tokens.get(i).getValue();
					// Returns an integer of the position of the label and moves i to that location.
					i = jump(tokens, value);
				}
				// If the token is branch than move to the label if the value on the top of the stack is true.
				else if(type.equals("-Branch-"))
				{
					// Get the next token that is the place where branch jumps to.
					i++;
					type = tokens.get(i).getToken();
					value = tokens.get(i).getValue();
					// Returns an integer of the position of the label and moves i to that location if >= 0.
					int place = branch(tokens, value);
					if(place < 0)
					{
						// Do nothing because branch wasn't done
					}
					else
					{
						i = place;
					}
				}
				// If the token is an identifier it can be one of many built in or defined functions. Check.
				else if(type.equals("-Identifier-"))
				{
				
					// Add the top two values on the stack.
					if(value.equals("+"))
						add();
					// Subtract the top two values on the stack.
					else if(value.equals("-"))
						subtract();
					// Multiply the top two values on the stack.
					else if(value.equals("*"))
						multiply();
					// Divide and mode the top two values on the stack.
					else if(value.equals("/"))
						divide();
					// Negate the top value on the stack.
					else if(value.equals("neg"))
						negative();
					// Return a boolean comparison of the top two values on the stack.
					else if(value.equals("<"))
						lessThan();
					// Return a boolean comparison of the top two values on the stack.
					else if(value.equals(">"))
						greaterThan();
					// Return a boolean comparison of the top two values on the stack.
					else if(value.equals("<="))
						lessThanEq();
					// Return a boolean comparison of the top two values on the stack.
					else if(value.equals(">="))
						greaterThanEq();
					// Return a boolean comparison of the top two values on the stack.
					else if(value.equals("="))
						equal();
					// Return a boolean comparison of the top two values on the stack.
					else if(value.equals("!="))
						notEqual();
					// Return a boolean of the logical operation of the top two values on the stack.
					else if(value.equals("and"))
						and();
					// Return a boolean of the logical operation of the top two values on the stack.
					else if(value.equals("or"))
						or();
					// Return a boolean of the logical operation of the top value on the stack.
					else if(value.equals("not"))
						not();
					// Concatenate the top two values on the stack.
					else if(value.equals("concat"))
						concat();
					// Get a substring of the top value on the stack.
					else if(value.equals("substr"))
						substr();
					// Get the length of the string on top of the stack.
					else if(value.equals("length"))
						length();
					// Get a character from the string on top of the stack.
					else if(value.equals("getchar"))
						getChar();
					// Put a character in the top string in the stack.
					else if(value.equals("putchar"))
						putChar();
					// Change a boolean or a number to a string on the stack.
					else if(value.equals("tostring"))
						tostring();
					// Convert the string on top of the stack to an integer.
					else if(value.equals("toint"))
						toint();
					// Convert the string on top of the stack to a boolean.
					else if(value.equals("tobool"))
						toBool();
					// Drop the topmost item on the stack.
					else if(value.equals("drop"))
						drop();
					// Duplicate the topmost item on the stack.
					else if(value.equals("dup"))
						dup();
					// Rotate some of the items on top of the stack.
					else if(value.equals("rot"))
						rot();
					// Swap some of the items on top of the stack.
					else if(value.equals("swap"))
						swap();
					// Read a character from standard input.
					else if(value.equals("read"))
						read();
					// Write a character to standard output.
					else if(value.equals("write"))
						write();
					// Push the boolean value true onto the stack.
					else if(value.equals("true"))
						addTo(type, value);
					// Push the boolean value false onto the stack.
					else if(value.equals("false"))
						addTo(type, value);
					// Load a reference into memory.
					else if(value.equals("load"))
						load();
					// Save a value to referenced memory location.
					else if(value.equals("save"))
						save();
					// Reference a location on the global array and push it onto the stack.
					else if(value.equals("ref"))
						ref();
					// If it not a built in function it must be a created one.
					else
					{
						// Moves through all the defined subroutines to find the correct one.
						for(int j = 0; j < subRout.size(); j++)
						{
							// Finds the subroutine that has the same name as the value calling it.
							if(subRout.get(j).get(0).getValue().equals(value))
							{
								subroutine(subRout.get(j), subRout);
								j = subRout.size();
							}
						}
					}
				}
				// Return from the subroutine
				else if(type.equals("-End-"))
				{
					return;
				}
				// If the token is a variable if must be pushed onto the stack.
				else
				{
					// The actual value doesn't matter since the stack will keep track from now on.
					addReference(value);
				}
			}
			/* There will be a number of errors that need to be caught by these statements. They
			 * will let us know if the program is semantically correct or if a runtime error has
			 * occurred during the interpretation.
			 */
			catch(Throwable e)
			{
				System.out.println("\nError: the program is semantically incorrect due to the following." 
						           + "\n" + e);
				System.exit(0);
			}
		}
	}
}
