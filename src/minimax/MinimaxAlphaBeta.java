/**
 * 
 */
package minimax;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import chess.Board;
import chess.Move;
import chess.Tile;
import pieces.Piece;

public class MinimaxAlphaBeta {

	boolean color;
	int maxDepth;
	Random rand;
	/**
	 * 
	 */
	public MinimaxAlphaBeta(boolean color, int maxDepth) {
		this.color = color;
		this.maxDepth = maxDepth;
		rand = new Random();
	}
	
	private float maxValue(Board b, ArrayList<Move> state, float alpha, float beta, int depth) {
		if(depth > maxDepth)
			return eval1(b, state, color);
		
		ArrayList<Move> moves = b.getMovesAfter(color, state);
		if(moves.size() == 0) // TODO add draw
			return Float.NEGATIVE_INFINITY;
		
		for(int i = 0; i < moves.size(); i++) {
			state.add(moves.get(i));
			float tmp = minValue(b, state, alpha, beta, depth + 1);
			state.remove(state.lastIndexOf(moves.get(i)));
			if(tmp > alpha) {
				alpha = tmp;
			}
			
			if(beta <= alpha)
				break;
			
			//if (max >= beta)
			//	return max;
			
			//if (max > alpha)
			//	alpha = max;
		}
		
		return alpha;
	}
	
	private float minValue(Board b, ArrayList<Move> state, float alpha, float beta, int depth) {
		if(depth > maxDepth)
			return eval1(b, state, !color);
		
		ArrayList<Move> moves = b.getMovesAfter(!color, state);
		if(moves.size() == 0) // TODO add draw
			return Float.POSITIVE_INFINITY;
		
		for(int i = 0; i < moves.size(); i++) {
			state.add(moves.get(i));
			float tmp = maxValue(b, state, alpha, beta, depth + 1);
			state.remove(state.lastIndexOf(moves.get(i)));
			if(tmp < beta) {
				beta = tmp;
			}
			
			if(beta <= alpha)
				break;
				
				
			//if (min <= beta)
			//	return min;
			
			//if (min < beta)
			//	beta = min;
		}
		
		return beta;
	}

	public Move decision(Board b) {
		// get maximum move
		
		ArrayList<Move> moves = b.getMoves(color);
		ArrayList<Move> state = new ArrayList<Move>();
		float[] costs = new float[moves.size()];
 		for(int i = 0; i < moves.size(); i++) {
			state.add(moves.get(i));
			float tmp = minValue(b, state, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, 1);
			costs[i] = tmp;
			state.remove(state.lastIndexOf(moves.get(i)));
		}
 		
 		// max
 		int maxi = -1;
		float max = Float.NEGATIVE_INFINITY;
 		for(int i = 0; i < moves.size(); i++) {
 			if(costs[i] >= max) {
 				if(Math.abs(costs[i]-max) < 0.1) // add a little random element
 					if(rand.nextBoolean())
 						continue;

 				max = costs[i];
 				maxi = i;
 			}
 		}
 		
 		if(maxi == -1)
 			return null;
 		else
 			return moves.get(maxi);
	}
	
	private float eval2(Board b, ArrayList<Move> moves, boolean currentColor) {
		Tile[][] tiles = b.getTilesAfter(moves);
		
		// check if king missing
		boolean blackKing = false, whiteKing = false;
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++) {
				if(tiles[i][j].isOccupied()) {
					if(tiles[i][j].getPiece().toString().equals("K")) {
						whiteKing = true;
					}
					if(tiles[i][j].getPiece().toString().equals("k")) {
						blackKing = true;
					}
				}
			}
		
		if(color == Piece.WHITE) {
			if(whiteKing == false)
				return Float.NEGATIVE_INFINITY;
			if(blackKing == false)
				return Float.POSITIVE_INFINITY;
		}
		else {
			if(whiteKing == false)
				return Float.POSITIVE_INFINITY;
			if(blackKing == false)
				return Float.NEGATIVE_INFINITY;
		}
		
		
			
		
		int whiteScore = 0;
		int blackScore = 0;
		
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++) {
				if(tiles[i][j].isOccupied())
					if(tiles[i][j].getPiece().getColor() == Piece.WHITE)
						whiteScore += tiles[i][j].getPiece().getValue();
					else
						blackScore += tiles[i][j].getPiece().getValue();
			}
		
		
		if(color == Piece.WHITE)
			return whiteScore - blackScore;
		else
			return blackScore - whiteScore;
	}
	
	
	private float eval1(Board b, ArrayList<Move> moves, boolean currentColor) {
		Tile[][] tiles = b.getTilesAfter(moves);
		
		if(b.getMoves(currentColor).size() == 0) {
			if(b.isCheckAfter(currentColor, moves))
				return (currentColor == this.color) ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY;
			else
				return Float.NEGATIVE_INFINITY; // we don't like draws
		}
		
		int whiteScore = 0;
		int blackScore = 0;
		
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++) {
				if(tiles[i][j].isOccupied())
					if(tiles[i][j].getPiece().getColor() == Piece.WHITE)
						whiteScore += tiles[i][j].getPiece().getValue();
					else
						blackScore += tiles[i][j].getPiece().getValue();
			}
		
		
		if(color == Piece.WHITE)
			return whiteScore - blackScore;
		else
			return blackScore - whiteScore;
	}

}
