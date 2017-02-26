
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


public class Project1 {
	/**
	 * Demonstrating process creation in Java.
	 */





	public static void main(String[] args) throws IOException {
		// Case 1 - Throw Error when there's more than 1 argument passed in 
		if (args.length > 1) {
			System.err.println("error: input ONE argument");
			System.exit(0);
		}
		// Case 2 - Activating interactive mode passing NO arguments
		else if(args.length == 0){
			System.out.println("interactive mode");
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));


			String userName = "bemi1107";
			//continue looping and waiting for user input
			while(true){
				try{
					// displays user name prompt and awaits user input
					System.out.print(userName+"> ");
					String userInput = input.readLine();



					// when user decides to exit program
					if(userInput.equals("quit")){
						System.out.println("good bye");
						System.exit(0);
					}
					// stores any user input that's been parsed to string array list 
					else{
						ArrayList<String[]> commandList = new ArrayList<String[]>();
						commandList = stringParser(userInput);
						for(int i =0;i<commandList.size(); i++){
							String[] command = commandList.get(i);
							//for valid commands with length 1
							if(command.length==1){
								//when "quit" command is found in the commandList terminate program
								if(command[0].equals("quit")){
									System.out.println("terminating program");
									System.exit(0);
								}


							} 
							//user input that's been parse will be passed on to process builder
							runCommand(command);


						}
					}
				}
				// catches any case where there's more than 512 characters 
				catch(IllegalArgumentException iae){
					System.err.println("Invalid input - Input exceeds limit");	


				}
				catch(NullPointerException Npe){
					System.out.println(" you pressed Ctrl 'D'- TERMINATING PROGRAM");
					System.exit(0);
				}

			}
		}

		// Case 3 - when a single argument is passed in, activation of "batch mode"
		else{

			System.out.println("batch mode");
			try{
				Scanner infile = new Scanner(new File(args[0]));
				String batchCommands = infile.nextLine();
				ArrayList<String[]> commandList = new ArrayList<String[]>();
				commandList = stringParser(batchCommands);
				for(int i=0;i<commandList.size();i++){
					String[] command = commandList.get(i);
					//for valid commands with length 1
					if(command.length==1){
						//when "quit" command is found in the commandList terminate program
						if(command[0].equals("quit")){
							System.out.println("terminating program");
							System.exit(0);
						}
						//user input that's been parse will be passed on to process builder	
						runCommand(command);
					}

				}
			}
			// When file is not found throws an error message
			catch(FileNotFoundException Fne){
				System.err.println("File Not Found");
			}
			// When input is over 512 characters,in throws an error message
			catch(IllegalArgumentException iae){
				System.err.println("Invalid input - Input exceeds limit");
			}


		}



	}

	/**Function that reads in user input.  If input matches "bemi1107" 
	 *it will activate interActive mode.
	 *
	 *@param input - BufferedReader passed in on function call.  Will be used to verify log in.
	 *@return - returns true if input matches "shju5737" else returns false;
	 *@exceptions - an exception for when user presses ctrl "D" and all other exception
	 */
	public static Boolean interActiveLogIn(BufferedReader input)throws IOException{
		try{
			System.out.print("Log In:");
			// User input is stored into String 'userName'
			String userName = input.readLine();
			// If userName matches 'bemi1107' returns true
			if(userName.equals("bemi1107")){
				return true;

			}
			// if userName does not match, outputs error message and returns false
			else{ 
				System.out.println("Wrong Username");
				return false;
			}
			// When ctrl 'D' is passed in as argument, user input	
		}catch(NullPointerException Npe){
			System.out.println(" you pressed Ctrl 'D'- TERMINATING PROGRAM");
			System.exit(0);
		}
		catch(Exception e){
			System.out.println("Some Exception");

		}
		return null;

	}

	/**Function that takes in Strings and returns an Arraylist of parsed commands
	 * @param - unParsed - unparsed string passed in on function call from user input or batch file
	 * @return - ArrayList with original strings parsed for the process builder to read
	 * @throws IlegalArgumentException - if string passed in has a length greater than 512 throws an error and a message
	 */

	public static ArrayList <String[]> stringParser(String unParsed){


		if(unParsed.length() >= 512){
			throw new IllegalArgumentException("Invalid input - Input greater than 512");


		}
		else{
			ArrayList <String[]> Array =  new ArrayList <String[]>();

			// splits based on ';'
			String[] parsed = unParsed.trim().split("\\;+");

			// flag used to see if 'quit' is in the String argument
			boolean quit = false;

			// parses each string based on white space 
			for(int i=0; i< parsed.length; i++){
				String[] parsedSpaces = parsed[i].trim().split("\\s+",-1);
				//flag used to see if there's any string that are blank
				boolean foundNull = false;
				// checks if there's an 'quit' in the parsed command that has a length of 1
				if(parsedSpaces.length==1){
					if(parsedSpaces[0].equals("quit")){
						quit = true;
						break;
					}
				}
				// checks if there's any blank in the parsed commands
				for(int j =0; j< parsedSpaces.length; j++){
					String checkNull = parsedSpaces[j];
					if(checkNull.equals("")){
						foundNull = true;
					}


				}
				// if there's no blank then stores parsed commands in to Array
				if(foundNull == false){
					Array.add(parsedSpaces);

				}
			}
			// if 'quit' command is found then adds 'quit' command to the end of the list no matter where it was found
			if(quit){
				String[] terminator = {"quit"};
				Array.add(terminator);
			}

			for(String[] a: Array){
				for(String s : a){
					System.out.println("\""+s+"\"");
				}
			}
			return Array;
		}
	}; 

	/**Function that takes in String Array of commands and passes in to Process Builder
	 * @param commands - String array of commands
	 * @exception IOException - catches any commands that doesn't make sense
	 */
	public static void runCommand(String[] commands){

		ProcessBuilder pb = new ProcessBuilder(commands);
		Process process;
		try {
			process = pb.start();
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			String line;
			while ( (line = br.readLine()) != null)
				System.out.println(line);

			br.close();

		} catch(IOException ioe){
			System.out.println("command not found.");
		}

	}
}