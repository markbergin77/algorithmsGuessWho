import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

/**
 * Random guessing player. This player is for task B.
 *
 * You may implement/extend other interfaces or classes, but ensure ultimately
 * that this class implements the Player interface (directly or indirectly).
 */
public class RandomGuessPlayer implements Player {
	String name;

    //used to store all remaing attributes that haven't been guessed
	ArrayList<LinkedList<String>> totalAttributes = new ArrayList<LinkedList<String>>();

    //store data of chosen player
	ArrayList<LinkedList<String>> personalAttributes = new ArrayList<LinkedList<String>>();

    //store all possible players with attribute data
	ArrayList<ArrayList<LinkedList<String>>> playerData = new ArrayList<ArrayList<LinkedList<String>>>();
    ArrayList<LinkedList<String>> playerAttributes = new ArrayList<LinkedList<String>>();


    //last guess names and index's - saves looping and comparison over curguess to get index
	int attributeNum; 
	String selectedAttribute;

	int valueNum;
	String selectedValue;

	/**
	 * Loads the game configuration from gameFilename, and also store the chosen
	 * person.
	 *
	 * @param gameFilename
	 *            Filename of game configuration.
	 * @param chosenName
	 *            Name of the chosen person for this player.
	 * @throws IOException
	 *             If there are IO issues with loading of gameFilename. Note you
	 *             can handle IOException within the constructor and remove the
	 *             "throws IOException" method specification, but make sure your
	 *             implementation exits gracefully if an IOException is thrown.
	 */
	public RandomGuessPlayer(String gameFilename, String chosenName) throws IOException {
		setName(chosenName);
		readAttributes(gameFilename, chosenName);
	} // end of RandomGuessPlayer()



    //returns random number in range
	public static int getRandomInt(int min, int max){

	    Random random = new Random();

   		//selects random from range
   	   int num = random.nextInt((max - min) + 1) + min;

       return num;

	}

	public Guess guess() {


		//selects attribute from remaining attributes
		 attributeNum = getRandomInt(0,totalAttributes.size() - 1);
		 selectedAttribute = totalAttributes.get(attributeNum).get(0);

		//selects values from remaing attributes list
		 valueNum = getRandomInt(1,totalAttributes.get(attributeNum).size() - 1);
		 selectedValue = totalAttributes.get(attributeNum).get(valueNum);


         //if only one possible player is left, return a player guess
		 if(playerData.size() == 1 ) {

             //gets player name 
		 	 String guessName = playerData.get(0).get(0).get(0);
		
			return new Guess(Guess.GuessType.Person, "", guessName);

		 }

         //returns Attribute guess
		 return new Guess(Guess.GuessType.Attribute, selectedAttribute, selectedValue);

	
	} // end of guess()

	public boolean answer(Guess currGuess) {

    	//Simply checks if the answer applies to their personal attributes.
		if (currGuess.getType().equals(Guess.GuessType.Person)) {
			if (personalAttributes.get(0).get(0).equals(currGuess.getValue())) {
				return true;
			}
		} else {
			for (int i = 0; i < personalAttributes.size() - 1; i++) {
				
				if(personalAttributes.get(i).get(0).equals(currGuess.getAttribute()))
					{
					
						if(personalAttributes.get(i).get(1).equals(currGuess.getValue()))
						{
							return true;
						}
					}
			}
		}
		// placeholder, replace
		return false;
	} // end of answer()

	public boolean receiveAnswer(Guess currGuess, boolean answer) {

         //removes last guess from pool to guess out of
         totalAttributes.get(attributeNum).remove(selectedValue);

         //if attribute list has no values left remove attribute entirly
         if( totalAttributes.get(attributeNum).size() == 1){

            totalAttributes.remove(attributeNum);
         }

        

		//stores if a players index should be removed from possible players
		boolean toRemove[] = new boolean[playerData.size()];

        // if guess is of type player and correct returns true
       	if (currGuess.getType().equals(Guess.GuessType.Person)) {
            if(answer){
                return true;
            }
            
            return false;            

        //else checks to see which players can be eliminated from pool
        }else{

		//loop through all possible players
		for(int i = 0 ; i < playerData.size(); i++){

			//loop through all attributes
			for(int j = 1; j < playerData.get(i).size(); j++){

				//checks if attribute matches guess
				if(playerData.get(i).get(j).get(0).equals(currGuess.getAttribute())){

					//checks if guess value matches possible player attribute value
					if(playerData.get(i).get(j).get(1).equals(currGuess.getValue())){
					
						if(!answer){
							toRemove[i] = true;
						}

					}else{

						if(answer){
							toRemove[i] = true;
						}

					}
				}
			}

		}

        //removes player starting from last to first to stop order issues
		for (int i = (playerData.size() - 1); i > 0; i--){

			if(toRemove[i] == true){
				playerData.remove(i);
			}
		}
        
        }

		
		return false;


	} // end of receiveAnswer()

	public void setName(String chosenName) {
		// TODO Auto-generated method stub
		this.name = chosenName;

	}

	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	public void readAttributes(String gameFilename, String chosenName) {
		// Luckily for us, we don't have to deal with the Config files being
		// faulty.
		// https://lms.rmit.edu.au/webapps/discussionboard/do/message?action=list_messages&forum_id=_512548_1&
		// nav=discussion_board_entry&conf_id=_392455_1&course_id=_341562_1&message_id=_3477702_1#msg__3477702_1Id
		String thisLine = null;
		LinkedList<String> newAttribute = new LinkedList<String>();
		boolean foundPlayer = false;

		try {
			// opens config file for reading
			BufferedReader br = new BufferedReader(new FileReader(gameFilename));
			while ((thisLine = br.readLine()) != null) {
				
				// Uses split to format to array
				String[] newAttributeLine = thisLine.split(" ");
				
				// Adds the total attributes
				// If the length is > 2
				if (newAttributeLine.length > 1 && foundPlayer == false) {
					for (int i = 0; i < newAttributeLine.length; i++) {
						newAttribute.add(newAttributeLine[i]);
					}
					totalAttributes.add(newAttribute);
					newAttribute = new LinkedList<String>();
				}

				// Adds the personal attributes.
				// System.out.println(Arrays.toString(newAttributeLine));
				if (newAttributeLine.length == 1 && newAttributeLine[0].equals(chosenName)) {
					//Total data is found at top of file, if we find player, close off the TotalAttributes
					foundPlayer = true;

					newAttribute = new LinkedList<String>();
					newAttribute.add(newAttributeLine[0]);
					personalAttributes.add(newAttribute);
					playerAttributes.add(newAttribute);
					newAttribute = new LinkedList<String>();

					newAttributeLine = br.readLine().split(" ");

					while (newAttributeLine.length == 2) {
						for (int i = 0; i < newAttributeLine.length; i++) {
							newAttribute.add(newAttributeLine[i]);
						}
						personalAttributes.add(newAttribute);
						playerAttributes.add(newAttribute);
						newAttribute = new LinkedList<String>();
						// Catches exception on last attempt here
						newAttributeLine = br.readLine().split(" ");
					}

				}

				
				// Adds the player attributes.
				// System.out.println(Arrays.toString(newAttributeLine));
				if (newAttributeLine.length == 1 && newAttributeLine[0].contains("P")) {
					//Total data is found at top of file, if we find player, close off the TotalAttributes
					foundPlayer = true;

					newAttribute = new LinkedList<String>();


					newAttribute.add(newAttributeLine[0]);
					playerAttributes.add(newAttribute);

					newAttribute = new LinkedList<String>();

					newAttributeLine = br.readLine().split(" ");

					while (newAttributeLine.length == 2) {
						for (int i = 0; i < newAttributeLine.length; i++) {
							newAttribute.add(newAttributeLine[i]);
						}
						playerAttributes.add(newAttribute);
						newAttribute = new LinkedList<String>();
						// Catches exception on last attempt here
						newAttributeLine = br.readLine().split(" ");
					}

                    //adds player attribute data to player pool
					playerData.add(playerAttributes);

					playerAttributes = new ArrayList<LinkedList<String>>();
				}
				
			}
			br.close();
		} catch (Exception e) {
		//	System.err.println(e);
		}

        printAttributes(personalAttributes);
		//System.out.println(personalAttributes.toString());
	}

    public void printAttributes(ArrayList<LinkedList<String>> attributesList){
    
        for(int i = 0; i < attributesList.size(); i++){
            
            	System.out.println(personalAttributes.get(i).toString());

        }    

        System.out.println("");

    }

} // end of class RandomGuessPlayer
