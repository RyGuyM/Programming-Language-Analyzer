import java.io.*;
import java.util.*;

// The program that is called to actually perform the scan of the file.
public class FLScanner
{
	public static ArrayList<Pair> scan(String fileName) throws IOException
	{
		
		// Create a file to read from using the user input filename.
		File file = new File(fileName);
		// Create a scanner to read the input of the file.
		Scanner inputFile = new Scanner(file);
		
		// This variable will hold the number of the line which the program is reading.
		int line = 0;
		// Will provide the area where the error was found to provide more meaning.
		String type = "";
		// Will signal if an error in reading have occurred.
		boolean error = false;
		
		/* A pair object is two strings that give the type of token is stored and the string 
		 * representation of the value of that token. This can be cast back to its original 
		 * value in terms of numerical values in the parser.
		 */
		Pair t = new Pair();
		
		// Create a stack to hold all the tokens.
		ArrayList<Pair> tokens = new ArrayList<Pair>();
		
		/* Read the file line by line until the end of the file has been reached. During
		 * the read the code inside will break each line down into their respective tokens.
		 */
		while(inputFile.hasNext())
		{
			// Holds the next line in the file and increment the line count.
			String next = inputFile.nextLine();
			line++;
			error = false;
			
			/* While i is less than the length of the line read each character
			 * in the line and add one to the frequency of that character. This
			 * is only done if the character is not a space.
			 */
			for(int i = 0 ; i < next.length(); i++)
			{
				// Reset the value of the type back to nothing at the start of a new token.
				type = "";
				
				// Whitespace is ignored and is the delimiter between tokens.
				if(next.charAt(i) == ' ')
				{
					//Do nothing with white space.
				}
				// If it is not a whitespace character check it against types of tokens allowed.
				else
				{
					/* Comments are a set of characters enclosed in a set of round brackets ()
					 */
				    if(next.charAt(i) == '(')
					{
				    	String temp = "";
				    	// Continue moving through the text until the closing brace is found.
						while(next.charAt(i) != ')')
						{
							temp = temp + next.charAt(i);
							i++;
							// Continue onto multiple lines if necessary.
							if(i == next.length())
							{
								 // Ensure the file has a next time.
								 if(inputFile.hasNext())
								 {
									 next = inputFile.nextLine();
									 i = 0;
									 line++;
								 }
								 // If end of file is reached with no closing brace an error is given.
								 else
								 {
									 error = true;
									 type = "-No closing brace on comments-";
								 }
							}	
						}
						// Add the closing bracket and creat the token.
						temp = temp + next.charAt(i);
						t = new Pair("-Comments-", temp);
					}
					
					/* String are non-empty sets of characters enclosed in a set of double quotations ""
					 */
					else if(next.charAt(i) == '"')
					{
						// Hold the value of the string.
						String temp = "";
						// Move it forward to start collecting the string characters.
						i++;
						
						// If the next character is a " that means this is an empty string which is invalid
						if(next.charAt(i) == '"')
						{
							error=true;
							type = "-Empty String-";
						}
						// If it is not empty it will start reading each character of the string and concatenating.
						else
						{
							// Continue reading the string until the end of the line or " is reached.
							while(i != next.length() && next.charAt(i) != '"')
							{
								temp = temp + next.charAt(i);
								i++;
							}
							// ASSSUMED - Stopped copy string because it reached the end of the line instead of ".
							if(next.charAt(i) == '\n')
							{
								error=true;
								type = "-No closing double quote-";
							}
							// Otherwise create a token that holds the value of the string.
							else
							{
								t = new Pair("-String-", temp);
							}
						}
					}
					
					/* A character is a single or special character enclosed in two single quotations ''. An error
					 * would be caught it is longer than a single character when normal or not a valid special
					 * character.
					 */
					else if(next.charAt(i) == '\'')
					{
						// Hold a temporary string of the value of the token and increment
						String temp = "";
						i++;
						// Special Characters
						if(next.charAt(i) == '\\')
						{
							// Concatenate the next character to the temp string and increment.
							temp = temp + next.charAt(i);
							i++;
							// Check to see if it is a correct special character.
							if(next.charAt(i) == 'n' || next.charAt(i) == '"' || next.charAt(i) == '\'')
							{
								// Concatenate the next character to the temp string and increment.
								temp = temp + next.charAt(i);
								i++;
								// Must have closing ' to complete the character, otherwise it is incorrect.
								if(next.charAt(i) != '\'')
								{
									error=true;
									type = "-Incorrect character format-";
								}
								// Otherwise create a special character object.
								else
								{
									t = new Pair("-Character-", temp);
								}
							}
						}
						// Empty character which is an error so it turns error to true.
						else if(next.charAt(i) == '\'')
						{
							error=true;
							type = "-Empty Character-";
						}
						// Regular Character
						else
						{
							// Hold the value of the character and move to the next character on the line.
							temp = temp + next.charAt(i);
							i++;
							
							// Must have closing ' to complete the character, otherwise it is incorrect.
							if(next.charAt(i) != '\'')
							{
								error=true;
								type = "-Incorrect character format-";
							}
							// Otherwise create character object.
							else
							{
								t = new Pair("-Character-", temp);
							}
						}
					}
					/* Signed Integers and Number Literals. Signed integers can be followed by a negative signed
					 * and any number of digits. Number literals must be in decimal form. An error would be if
					 * there are multiple decimals.
					 */
					else if((next.charAt(i) >= '0' && next.charAt(i) <= '9') || next.charAt(i) == '-' || next.charAt(i) == '+')
					{
						String temp = "";
						// Check to see if the character is negative and then check to see if it is a negation sign.
						if(next.charAt(i) == '-' || next.charAt(i) == '+')
						{
							temp = temp + next.charAt(i);
							i++;
							// As long as the character is a number then it is a negative number
							if(i != next.length() && next.charAt(i) >= 0 && next.charAt(i) <=9)
							{
								// Continue reading through the line until a whitespace is encountered.
								while(i != next.length() && next.charAt(i) != ' ')
								{
									// Concatenate.
									temp = temp + next.charAt(i);
									
									// If the character is not a number than it is an incorrect format.
									if(next.charAt(i) <= '0' || next.charAt(i) >= '9')
									{
										error = true;
										type = "-Incorrect Number Format-";
									}
									i++;
								}
								// Create a token.
								t = new Pair("-Number-", temp);
							}
							// Reset character back to previous if - is operator not sign of integer.
							else
							{
								i--;
								temp = "" + next.charAt(i);
								t = new Pair("-Identifier-", temp);
							}
						}
						// Otherwise it must be a number and figure out if it is a literal or integer.
						else
						{
							// Add the character to the temp string and increment.
							temp = temp + next.charAt(i);
							i++;
							
							// Check if it is a literal (1), integer (0), or error (>1).
							int check = 0;
							while(i != next.length() && next.charAt(i) != ' ')
							{
								temp = temp + next.charAt(i);
								if(next.charAt(i) == '.')
								{
									check = check + 1;
								}
								if(next.charAt(i) <= '0' || next.charAt(i) >= '9' || next.charAt(i) == '.')
								{
									error = true;
									type = "-Incorrect Number Format-";
								}
								i++;
							}
							// Integer if no decimal point was found.
							if(check == 0)
							{
								t = new Pair("-Number-", temp);
							}
							// Literal if there was only one decimal point found.
							else if(check == 1)
							{
								t = new Pair("-Number-", temp);
							}
							// Multiple decimal points signify an error.
							else
							{
								error = true;
								type = "-Incorrect Number Format-";
							}
						}
					}
					/* Variables start with a $ and can be named with any set of characters other than whitespace, :, or $. 
					 * The variables must not start with a digit.
					 */
					else if(next.charAt(i) == '$')
					{
						// Start a temporary string to hold the variable name.
						String temp = "";
						i++;
						// Incorrect naming notation for variables.
						if(i == next.length() || next.charAt(i) == ':' || next.charAt(i) == '$' || (next.charAt(i) >= 0 && next.charAt(i) <=9))
						{
							error=true;
							type = "-Variable not named correctly-";
						}
						else
						{
							while(i != next.length() && next.charAt(i) != ' ')
							{
								temp = temp + next.charAt(i);
								// If any of the characters in the name is a : or a $ it is incorrect.
								if(next.charAt(i) == ':' || next.charAt(i) == '$')
								{
									error=true;
									type = "-Variable not named correctly-";
								}
								i++;
							}
							t = new Pair("-Variable-", temp);
						}
					}
				    /* Label start with a : and can be named with any set of characters other than whitespace, :, or $. 
					 * The variables must not start with a digit.
					 */
					else if(next.charAt(i) == ':')
					{
						// Start a temporary string to hold the label name.
						String temp = "";
						i++;
						// Incorrect naming notation for label.
						if(i == next.length() || next.charAt(i) == ':' || next.charAt(i) == '$' || (next.charAt(i) >= 0 && next.charAt(i) <=9))
						{
							error=true;
							type = "-Label not named correctly-";
						}
						else
						{
							while(i != next.length() && next.charAt(i) != ' ')
							{
								temp = temp + next.charAt(i);
								// If any of the characters in the name is a : or a $ it is incorrect.
								if(next.charAt(i) == ':' || next.charAt(i) == '$')
								{
									error=true;
									type = "-Variable not named correctly-";
								}
								i++;
							}
							t = new Pair("-Label-", temp);
						}
					}
					/* Arithmetic Operations for addition, subtraction, multiplication and division. This is done after signed
					 * integers to ensure that the negative sign does not belong to a number and indeed is arithmetic subtraction.
					 * There can be no token error since the scanner does not check if a line is syntactically correct.
					 */
					else if(next.charAt(i) == '+' || next.charAt(i) == '-' || next.charAt(i) == '*' || next.charAt(i) == '/')
					{
						String temp = "" + next.charAt(i);
						t = new Pair("-Identifier-", temp);
					}
					/* Comparison operators to compare either numerical or boolean values. This set of operations does not work
					 * on string values. No error check is necessary for comparison operators.
					 */
					else if(next.charAt(i) == '=' || next.charAt(i) == '<' || next.charAt(i) == '>' || next.charAt(i) == '!')
					{
						String temp = "";
						temp = temp + next.charAt(i);
						// Check to ensure that the if any of this symbols occur they are possibly followed by an =.
						if(next.charAt(i) == '!' || next.charAt(i) == '<' || next.charAt(i) == '>')
						{
							i++;
							// If it is not an equals sign step back to original operator,
							if(next.charAt(i) != '=')
							{
								i--;
								if(next.charAt(i) == '!')
								{
									error = true;
									type = "-Incorrect Comparison-";
								}
							}
							// Otherwise add it to the operator for additional functionality.
							else
							{
								temp = temp + next.charAt(i);
							}
						}
						// Create a new token to hold the comparison operator.
						t = new Pair("-Identifier-", temp);
					}
					/* Functions, Control, Operators, References and Booleans. These values are all represented by groups
					 * of characters in the program. They are case insensitive so that True, TRUE, true, and tRUe all 
					 * represent the same value. Any other word that is not part of this language will create an error.
					 */
					else
					{
						// Create a string to hold the word, then continue reading until the whole word is captured.
						String temp = "";
						
						while(i != next.length() && next.charAt(i) != ' ')
						{
							temp = temp + next.charAt(i);
							// If any of the characters in the word are : or $ it is incorrect and causes an error.
							if(next.charAt(i) == ':' || next.charAt(i) == '$')
							{
								error = true;
								type = "-Incorrect character in name-";
							}
							i++;
						}
						// Make the string all lower case since FL isn't case sensitive
						temp = temp.toLowerCase();
						
						// Stack Manipulation to perform the newly added FL functions on the stack.
						if(temp.equals("drop") || temp.equals("dup") || temp.equals("swap") || temp.equals("rot") || temp.equals("ref"))
						{
							t = new Pair("-Identifier-", temp);
						}
						// String Operations to perform different things to any strings that exist within FL.
						else if(temp.equals("concat") || temp.equals("substr") || temp.equals("length") || temp.equals("getchar")
								|| temp.equals("putchar") || temp.equals("tostring") || temp.equals("toint") || temp.equals("tobool"))
						{
							t = new Pair("-Identifier-", temp);
						}
						// Boolean Operations for and, or, not.
						else if(temp.equals("and") || temp.equals("or") || temp.equals("not"))
						{
							t = new Pair("-Identifier-", temp);
						}
						
						// I/O to read and write information to a file.
						else if(temp.equals("read") || temp.equals("write"))
						{
							t = new Pair("-Identifier-", temp);
						}
						// References to load or save information from and to a variable.
						else if(temp.equals("save") || temp.equals("load"))
						{
							t = new Pair("-Identifier-", temp);
						}
						// Booleans are described by the terms true and false.
						else if(temp.equals("true") || temp.equals("false"))
						{
							t = new Pair("-Identifier-", temp);
						}
						// Control Constructs are defined by the terms sub, and branch. A name comes after these types.
						else if(temp.equals("sub"))
						{
							t = new Pair("-Sub-", temp);
						}
						else if(temp.equals("branch"))
						{
							t = new Pair("-Branch-", temp);
						}
						// Has a different syntactic structure than the other control types so it is on its own.
						else if(temp.equals("jump"))
						{
							t = new Pair("-Jump-", temp);
						}
						// Has a different syntactic structure than the other control types so it is on its own.
						else if(temp.equals("end"))
						{
							t = new Pair("-End-", temp);
						}
						// Must be a function name if it doesn't fall into any other category.
						else
						{
							t = new Pair("-Identifier-", temp);
						}
					}
					
					/* If an error occurred at some point in reading the token than the error value would have been set to true.
					 * If this is the case than the system will print out the type and the line it was entered on. Then it will
					 * exit the program.
					 */
					if(error == true)
					{
						System.out.println("Error: Incorrect token type " + type + " entered on line " + line + ".");
						return null;
					}
					// Otherwise add the newly created token to the stack.
					else
					{
						if(!t.getToken().equals("-Comments-"))
						{
							tokens.add(t);
						}
					}
				}
			}
		}
		// Closes the input file.
		inputFile.close();
		
		// If the scanner has completed it will return a stack of tokens back to the user.
		return tokens;
	}
}
