import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Binary-search based guessing player. This player is for task C.
 *
 * You may implement/extend other interfaces or classes, but ensure ultimately
 * that this class implements the Player interface (directly or indirectly).
 */
public class BinaryGuessPlayer implements Player {
	
	// Attributes distance is the number of lines of attributes each playerhas.
	int attibutesDistance = 0;
	// Arrays for use
	// Used for when scanning for most common answers
	ArrayList<LinkedList<String>> totalAttributes = new ArrayList<LinkedList<String>>();
	// Not used yet. Might end up removing
	ArrayList<LinkedList<String>> playerAttributes = new ArrayList<LinkedList<String>>();
	// Players personal attributes, used for when checking answers
	ArrayList<LinkedList<String>> personalAttributes = new ArrayList<LinkedList<String>>();
	// Possible players remaining (choices)
	ArrayList<String> possibleNames = new ArrayList<String>();
	// Used for indexing efficiently when traversing through config.
	HashMap<String, Integer> possiblePeopleMap = new HashMap<String, Integer>();

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
	public BinaryGuessPlayer(String gameFilename, String chosenName) throws IOException {

		readAttributes(gameFilename, chosenName);
	} // end of RandomGuessPlayer()

	public Guess guess() {
		String mostCommonAttribute = null;
		String mostCommonValue = null;
		int mostCommonNumber = 0;

		if (possibleNames.size() > 1) {
			/*
			 * Yes I know this looks awfully hacky, most efficient way to get
			 * attributes I could think of considering config files have no need
			 * for error checking
			 */
			if (attibutesDistance == 0) {
				attibutesDistance = (possiblePeopleMap.get(possibleNames.get(1))
						- possiblePeopleMap.get(possibleNames.get(0)));
			}

			try {
				String current;
				// -1 skips the Player name/title
				for (int i = 1; i < attibutesDistance; i++) {
					int nameIndex = 0;
					// attributesDistance - 2 is the number of attributes from
					// Person title to the blank config space
					String currentAttribute[] = new String[possibleNames.size()];

					for (int looper = 0; looper <= possibleNames.size() - 1; looper++) {

						currentAttribute[looper] = playerAttributes
								.get(possiblePeopleMap.get(possibleNames.get(nameIndex)) + i).get(1);

						if (nameIndex < possibleNames.size() - 1) {
							nameIndex++;
						}

					}

					// index of the attribute type we're on, Glasses, Height,
					// etc. (Not the adjective like yellow, etc)

					HashMap<String, Integer> commonAttribute = getCommonAttribute(currentAttribute);

					// Need to grab the key for checking most common number
					String foundValue = null;
					for (String key : commonAttribute.keySet()) {
						foundValue = key;
					}

					// Makes sure found key doesn't apply for all possible
					// people, and checks if higher than current set

					if (commonAttribute.get(foundValue) > mostCommonNumber
							&& commonAttribute.get(foundValue) != possibleNames.size()) {

						// Sets the attribute ("height" "glasses" "colour" etc.)
						mostCommonAttribute = playerAttributes.get(possiblePeopleMap.get(possibleNames.get(0)) + i)
								.get(0);
						// Sets the value ("Red", 2 etc)
						mostCommonValue = foundValue;
						// number of times the value was found
						mostCommonNumber = commonAttribute.get(foundValue);
					}
				}

				// placeholder, replace
			} catch (Exception e) {

			}

			return new Guess(Guess.GuessType.Attribute, mostCommonAttribute, mostCommonValue);
		}
		// Guess' person if only one person remaining
		return new Guess(Guess.GuessType.Person, "", possibleNames.get(0));
	} // end of guess()

	public HashMap<String, Integer> getCommonAttribute(String[] attributes) {
		// Finds the most common value of an attribute, sets that and the number
		// of
		// Times it is found in a hashmap
		HashMap<String, Integer> commonAttribute = new HashMap<String, Integer>();
		int count;
		int finalCount = 0;
		String finalValue;
		String currentValue = null;

		// solution if number of possible people is 2
		if (attributes.length == 2) {
			if (attributes[0].equals(attributes[1])) {
				commonAttribute.put(attributes[0], 2);
				return commonAttribute;
			} else {
				commonAttribute.put(attributes[0], 1);
				return commonAttribute;
			}
		}

		// else search through and work out how many times the items is in the
		// array
		for (int i = 0; i < attributes.length - 1; i++) {
			count = 0;
			currentValue = attributes[i];

			if (i == 0) {
				count = 1;
			}

			for (int looper = 0; looper < attributes.length - 1; looper++) {
				if (currentValue.equals(attributes[looper])) {
					count++;
				}
			}
			if (count > finalCount) {
				finalCount = count;
				finalValue = currentValue;
			}
		}

		commonAttribute.put(currentValue, finalCount);
		return commonAttribute;

	}

	public boolean answer(Guess currGuess) {
		// Simply checks if the answer applies to their personal attributes.

		if (currGuess.getType().equals(Guess.GuessType.Person)) {
			if (personalAttributes.get(0).get(0).equals(currGuess.getValue())) {
				return true;
			}
		} else {
			for (int i = 0; i < personalAttributes.size() - 1; i++) {

				if (personalAttributes.get(i).get(0).equals(currGuess.getAttribute())) {

					if (personalAttributes.get(i).get(1).equals(currGuess.getValue())) {
						return true;
					}

				}
			}
			return false;
		}
		// placeholder, replace
		return false;
	} // end of answer()

	public boolean receiveAnswer(Guess currGuess, boolean answer) {
		// Gets the answer applies deletions on true and false
		String currentPlayer = null;

		// Quick check for if the guess was a person, only time it can be true
		if (currGuess.getType() == Guess.GuessType.Person) {
			if (answer == true) {
				return true;
			}

			return false;
		}

		else {
			// searches through player file to check when the attributes match.
			// Removes the players that has same attribute value
			if (answer == false) {
				for (int i = 0; i < playerAttributes.size() - 1; i++) {
					if (playerAttributes.get(i).size() == 1 & playerAttributes.get(i).get(0).contains("P")) {
						currentPlayer = playerAttributes.get(i).get(0);
					}
					if (playerAttributes.get(i).get(0).equals(currGuess.getAttribute())) {
						if (playerAttributes.get(i).get(1).equals(currGuess.getValue())) {
							removePlayer(currentPlayer);
						}
					}
				}

			} 
			else {
				for (int i = 0; i < playerAttributes.size() - 1; i++) {
					if (playerAttributes.get(i).size() == 1 & playerAttributes.get(i).get(0).contains("P")) {
						currentPlayer = playerAttributes.get(i).get(0);
					}
					if (playerAttributes.get(i).get(0).equals(currGuess.getAttribute())) {
						if (!playerAttributes.get(i).get(1).equals(currGuess.getValue())) {
							removePlayer(currentPlayer);
						}
					}
				}
			}

			return false;
		}

	} // end of receiveAnswer()

	public void removePlayer(String currentPlayer) {
		// Removes from Map as well as possibleNames list
		// These two are the only structures that need altering for accurate
		// guess
		for (int i = 0; i < possibleNames.size(); i++) {
			if (possibleNames.get(i).equals(currentPlayer)) {
				possibleNames.remove(i);
			}
		}
		possiblePeopleMap.remove(currentPlayer);

	}

	public void readAttributes(String gameFilename, String chosenName) {
		// Luckily for us, we don't have to deal with the Config files being
		// faulty.
		// https://lms.rmit.edu.au/webapps/discussionboard/do/message?action=list_messages&forum_id=_512548_1&
		// nav=discussion_board_entry&conf_id=_392455_1&course_id=_341562_1&message_id=_3477702_1#msg__3477702_1Id

		// Index incorporates for the player attribtues only
		int index = 0;

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

				if (newAttributeLine.length == 1 && newAttributeLine[0].equals(chosenName)) {
					// Total data is found at top of file, if we find player,
					// close off the TotalAttributes
					foundPlayer = true;

					newAttribute = new LinkedList<String>();
					newAttribute.add(newAttributeLine[0]);
					personalAttributes.add(newAttribute);
					playerAttributes.add(newAttribute);
					possibleNames.add(newAttributeLine[0]);
					// adds the player name to possible people left
					possiblePeopleMap.put(newAttributeLine[0], index);
					newAttribute = new LinkedList<String>();

					newAttributeLine = br.readLine().split(" ");
					index++;
					while (newAttributeLine.length == 2) {
						for (int i = 0; i < newAttributeLine.length; i++) {
							newAttribute.add(newAttributeLine[i]);
						}
						personalAttributes.add(newAttribute);
						playerAttributes.add(newAttribute);
						newAttribute = new LinkedList<String>();
						// Catches exception on last attempt here
						newAttributeLine = br.readLine().split(" ");
						index++;
					}

				}

				// Adds the player attributes.

				if (newAttributeLine.length == 1 && newAttributeLine[0].contains("P")) {

					// Total data is found at top of file, if we find player,
					// close off the TotalAttributes
					foundPlayer = true;

					// adds the player name to possible people left
					possiblePeopleMap.put(newAttributeLine[0], index);
					possibleNames.add(newAttributeLine[0]);

					newAttribute = new LinkedList<String>();
					newAttribute.add(newAttributeLine[0]);
					playerAttributes.add(newAttribute);
					newAttribute = new LinkedList<String>();

					newAttributeLine = br.readLine().split(" ");
					index++;

					while (newAttributeLine.length == 2) {
						for (int i = 0; i < newAttributeLine.length; i++) {
							newAttribute.add(newAttributeLine[i]);
						}
						playerAttributes.add(newAttribute);
						newAttribute = new LinkedList<String>();
						// Catches exception on last attempt here
						newAttributeLine = br.readLine().split(" ");
						index++;
					}

				}

			}

			br.close();

		} catch (Exception e) {
		}

	}

} // end of class BinaryGuessPlayer
