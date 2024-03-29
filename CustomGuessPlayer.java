import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;


/**
 * Your customised guessing player.
 * This player is for bonus task.
 *
 * You may implement/extend other interfaces or classes, but ensure ultimately
 * that this class implements the Player interface (directly or indirectly).
 */
public class CustomGuessPlayer implements Player
{
	String name;
	ArrayList<LinkedList<String>> totalAttributes = new ArrayList<LinkedList<String>>();
	ArrayList<LinkedList<String>> playerAttributes = new ArrayList<LinkedList<String>>();
	ArrayList<LinkedList<String>> personalAttributes = new ArrayList<LinkedList<String>>();

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
	public CustomGuessPlayer(String gameFilename, String chosenName) throws IOException {
		setName(chosenName);
		readAttributes(gameFilename, chosenName);
	} // end of RandomGuessPlayer()

	public Guess guess() {

		// placeholder, replace
		return new Guess(Guess.GuessType.Person, "", "Placeholder");
	} // end of guess()

	public boolean answer(Guess currGuess) {

		// placeholder, replace
		return false;
	} // end of answer()

	public boolean receiveAnswer(Guess currGuess, boolean answer) {

		// placeholder, replace
		return true;
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
				if (newAttributeLine.length == 1 && newAttributeLine[0].contains(chosenName)) {
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

				}
				
			}
			br.close();
		} catch (Exception e) {
		}
		//System.out.println(playerAttributes.toString());
	//	System.out.println(totalAttributes.toString());
		//System.out.println(personalAttributes.toString());
	}

	
} // end of class CustomGuessPlayer
