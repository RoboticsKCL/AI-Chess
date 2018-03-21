package chess;
/**
 * 
 */

import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTCommLogListener;
import lejos.pc.comm.NXTConnector;
import player.*;
import pieces.*;

public class Chess {
	private static DataOutputStream outDat;
	public static void main(String[] args) {
		// Initialise connection with NXT
		NXTConnector conn = new NXTConnector();
		
		conn.addLogListener(new NXTCommLogListener(){

			public void logEvent(String message) {
				System.out.println("USBSend Log.listener: "+message);
				
			}

			public void logEvent(Throwable throwable) {
				System.out.println("USBSend Log.listener - stack trace: ");
				 throwable.printStackTrace();
				
			}
			
		} 
		);
		
		if (!conn.connectTo("usb://")){
			System.err.println("No NXT found using USB");
			System.exit(1);
		}
		outDat = new DataOutputStream(conn.getOutputStream());
		int draw = 0;
		
		// Create Chess Game
		
		Board board = new Board();
		Player player1 = new AlphaBetaPlayer(Piece.WHITE,2);
		Player player2 = new HumanPlayer(Piece.BLACK);		
		int winner = play(player1, player2, board);		
		if(winner == 1) {
			System.out.println("The AI Computer has won!");
		}
		else if(winner == 0) {
			System.out.println("It's a draw!");
		}
		else {
			System.out.println("You have won!");
		}
		try {
			outDat.close();
			System.out.println("Closed data streams");
		} catch (IOException ioe) {
			System.err.println("IO Exception Closing connection");
		}
		
		try {
			conn.close();
			System.out.println("Closed connection");
		} catch (IOException ioe) {
			System.err.println("IO Exception Closing connection");
		}
	}
	
	/** Returns 1 if player1 wins
	 * Returns 0 if draw
	 * Returns -1 if player2 wins
	 */
	public static int play(Player player1, Player player2, Board b) {
		Move move;
		int result;
		int turn = 0;
		while(true) {
			if(turn++ > 200) 
				return 0;
			
			move = player1.getNextMove(b);
			System.out.println("AI moved: " + move);

			if(move == null && b.isCheck(player1.getColor())) // check and can't move
				return -1;
			if(move == null) // no check but can't move
				return 0;
			
			result = b.makeMove(move);
			System.out.println(b);
			
			try {
				   outDat.writeInt(move.toNumber());
				   outDat.flush();
		
				} catch (IOException ioe) {
					System.err.println("IO Exception writing bytes");
				}

			move = player2.getNextMove(b);
			System.out.println();

			if(move == null && b.isCheck(player2.getColor())) // check and can't move
				return 1;
			if(move == null) // no check but can't move
				return 0;
			
			result = b.makeMove(move);
			System.out.println(b);
		} 
	}

}
