/**
 * 
 */
package player;

import java.util.ArrayList;
import java.util.Scanner;

import chess.Move;
import chess.Board;

public class HumanPlayer extends Player {
	/**
	 * @param color
	 */
	public HumanPlayer(boolean color) {
		super(color);
	}


	/**
	 * Function to prompt the player to make a move after the first move has
	 * already been made
	 * 
	 * @param b
	 *            the board to parse
	 * @return the selected move
	 */
	public Move getNextMove(Board b) {
		Scanner scan = new Scanner(System.in);
		System.out.println("Please insert move in old position - new position format (e.g. A5-B6)");
		while(scan.hasNextLine()) {
			String input = scan.nextLine();
			if(input.matches("[A-H][1-8]-[A-H][1-8]")) {
				System.out.println("You moved: " + input);
				return new Move(input.charAt(0)-'A', input.charAt(1)-'0'-1, input.charAt(3)-'A', input.charAt(4)-'0'-1);
			}
			else {
				System.out.println("Please use the format (e.g. A5-B6)");
			}
		}
		scan.close();
		return null;
	}

}
