import java.io.IOException;
import java.util.ArrayList;

/* The parser runs through the stream of tokens and checks for any syntactic errors that
 * have occurred in the writing of the program. It will return true if the program is 
 * syntactically correct and false otherwise.
 */
public class FLParser
{
	public static boolean parse(ArrayList<Pair> tokens) throws IOException
	{
		// This value will be returned depending on correctness.
		boolean correct = true;
		
		// Will hold the next token.
		Pair next = new Pair();
		// Will hold the type of the next token.
		String type = "";
		// Will count the number of sub and end keywords.
		int subEnd = 0;
		
		// Runs through every token in the stream and checks for syntax.
		for(int i = 0; i < tokens.size(); i++)
		{
			// Gets the next token.
			next = tokens.get(i);
			// Get the type of the token.
			type = next.getToken();
			
			// Number
			if(type.equals("-Number-"))
			{
				// Pushes the number onto the stack, requires no syntactic analysis.
			}
			// String
			else if(type.equals("-String-"))
			{
				// Pushes a string onto the stack, requires no syntactic analysis.
			}
			// Character
			else if(type.equals("-Character-"))
			{
				// Pushes a character onto the stack, requires no syntactic analysis.
			}
			// Label
			else if(type.equals("-Label-"))
			{
				// Does not require any preceding or following tokens, always syntactically correct.
			}
			/* Keyword Jump
			 * The keyword jump must be followed by an identifier.
			 */
			else if(type.equals("-Jump-"))
			{
				// Move to the next token.
				i++;
				// Ensure there are tokens left, if not error.
				if(i == tokens.size())
				{
					correct = false;
					break;
				}
				// Get the values of the next tokens.
				else
				{
					next = tokens.get(i);
					type = next.getToken();
					// If it is not an identifier give error, otherwise it is correct.
					if(!type.equals("-Identifier-"))
					{
						correct = false;
						break;
					}
				}
			}
			/* Keyword Branch
			 * Branch must be followed by an identifier or a keyword name.
			 */
			else if(type.equals("-Branch-"))
			{
				// Move to the next token.
				i++;
				// Ensure there are tokens left, if not error.
				if(i == tokens.size())
				{
					correct = false;
					break;
				}
				// Get the values of the next tokens.
				else
				{
					next = tokens.get(i);
					type = next.getToken();
					// If it is non of the token types below than it is an error, otherwise it is correct.
					if(!type.equals("-Identifier-") && !type.equals("-End-") 
							&& !type.equals("-Sub-") && !type.equals("-Jump-"))
					{
						correct = false;
						break;
					}
				}
			}
			/* Keyword End
			 * End must be the closing section of a sub function. These functions can not be defined
			 * within each other. If it the counter goes below zero it means there is no sub function
			 * to end and an error is printed.
			 */
			else if(type.equals("-End-"))
			{
				// Ensure that this occurs at the end of a function.
				subEnd = subEnd - 1;
				// If there was no sub that belongs to this end it is a syntactic error.
				if(subEnd < 0)
				{
					correct = false;
					break; 
				}
			}
			/* Keyword Sub
			 * The keyword sub must be followed by an identifier that names the function. THe function
			 * body must also end with the keyword end. This section users a counter to ensure that end is
			 * found. No next subs are allowed.
			 */
			else if(type.equals("-Sub-"))
			{
				// Ensures that an end token is at the end of the function definition.
				subEnd = subEnd + 1;
				i++;
				// Ensures there are tokens left to check and that there is no next subs.
				if(i == tokens.size() || subEnd > 1)
				{
					correct = false;
					break;
				}
				// Otherwise it gets the next token and checks if it is an identifier.
				else
				{
					next = tokens.get(i);
					type = next.getToken();
					if(!type.equals("-Identifier-"))
					{
						correct = false;
						break;
					}
				}
			}
			// Identifier
			else if(type.equals("-Identifier-"))
			{
				// Pulls and pushes items off of and onto the stack, requires no syntactic analysis.
			}
			// Variable
			else
			{
				/* A variable must be loaded onto the stack or it must be saved. Syntactically it does not
				 * need to be followed or preceded by any tokens. Semantically this is untrue but unnecessary
				 * to define at this point.
				 */
			}
		}
		
		// Ensures that all the subs have been ended, if they have not it is an error.
		if(subEnd > 0)
		{
			correct = false;
		}
			
		// Returns whether the program is syntactically correct or not depending on what was found.
		return correct;
			
	}
}
