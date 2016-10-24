# algorithmsGuessWho
Assignment two of Algorithms and Analysis.


Assignment consists of implementing a guess who game incorporating two types of Guessing algorithms.

A Random Guessing Player.


Player guesses a random Attribute Value pair to eventually narrow down the possible result.

A Binary Search Based Guessing Player.


Player guesses a the most common Attribute Value pair to eventually narrow down the possible result.



The Binary search player would be implementing a decrease and conquer search based strategy by regularly halving the set of possible candidates.
This binary search player would therefore have a better worst case complexity than the random implementation.
 

Application uses the JOpt Simple command line parse library, so compilation and run details are:

javac -cp .:jopt-simple-5.0.2.jar *.java  

Java -cp .:jopt-simple-5.0.2.jar GuessWho -l <game log file> <game config file> <chosen person file> <player 1 type> <player 2 type>

 (.; for Windows users)

Where:
• game log file: name of the file to write the log of the game. 

• game configuration file: name of the file that contains the attributes, values and persons in the Guess Who game. 

• chosen person file: name of the file that specifies which person that each player have chosen. 

• player 1 type: specifies which type of player to use for first player, one of [random — binary — custom]. random is the random guessing player, binary is the binary-search based guessing player, and custom is the customised guessing player. 

• player 2 type: specifies which type of player to use for second player, one of [random — binary — custom].