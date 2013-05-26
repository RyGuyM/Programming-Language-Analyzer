import java.io.*;
import java.util.*;

/* This is the main method for the scanner, parser, and analysis that was created to work with 
 * the FL language  defined by a few type of language rules. This section of the program receives 
 * the name of a file to be scanned. This is then sent 
 */
public class FLAnalyse
{
	public static void main(String[] args) throws IOException
	{	
		// Takes a filename from the command line.
		String fileName = args[0];
		
		// Will hold the value if the text is syntactically correct.
		boolean syntax = false;
		
		// Creates a new array-list that contains Pair objects, this will store the tokens
		ArrayList<Pair> tokens = new ArrayList<Pair>();
		// Call the BuildScan program to scan the filename and send back an array of tokens.
		tokens = FLScanner.scan(fileName);
		
		// If an error message has occurred exit the program.
		if(tokens == null)
		{
			System.exit(0);
		}
		// Otherwise print the contents of the arraylist to stdout and file(if supplied).
		else
		{
			// If file name is supplied write to screen and file.
			if(args.length > 1)
			{
				FileWriter fstream = new FileWriter(args[1]);
				BufferedWriter out = new BufferedWriter(fstream);
				for(int i = 0; i < tokens.size(); i++)
				{
					out.write(tokens.get(i).toString());
				}
				
				syntax = FLParser.parse(tokens);
				if(syntax == true)
				{
					System.out.println("The program is syntactically correct.");
					if(FLSemantic.statRules(tokens))
					{
						System.out.println("The static semantic rules are held.");
						
						FLSemantic.runTime(tokens);
					}
					else
					{
						System.out.println("The static semantic rules are broken.");
					}
				}
				else
					System.out.println("The program is NOT syntactically correct.");
		
			}
			// Otherwise just print if the program was OK or not to the screen.
			else
			{
				syntax = FLParser.parse(tokens);
				if(syntax == true)
				{
					System.out.println("The program is syntactically correct.");
					if(FLSemantic.statRules(tokens))
					{
						System.out.println("The static semantic rules are held.");
						
						FLSemantic.runTime(tokens);
					}
					else
					{
						System.out.println("The static semantic rules are broken.");
					}
				}
				else
					System.out.println("The program is NOT syntactically correct.");
			}
		}
	}
}