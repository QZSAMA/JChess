package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.board.Move.AttackMove;
import com.chess.engine.board.Move.MajorMove;
import com.google.common.collect.ImmutableList;

public class Rook extends Piece{

	private final static int[] CANDIDATE_MOVE_COORDINATE = {-8, -1, 1, 8};
	
	public Rook(Alliance pieceAlliance, int piecePosition) {
		super(PieceType.ROOK, piecePosition, pieceAlliance);
	}

	@Override
	public Collection<Move> calculateLegalMoves(Board board) {
		final List<Move> legalMoves = new ArrayList<Move>();
		
		for(final int candidateCoordinateOffset: CANDIDATE_MOVE_COORDINATE) {
			int candidateDestinationCoordinate = this.piecePosition + candidateCoordinateOffset; 
			while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
				
				if(isFirstColumExclusion(candidateDestinationCoordinate, candidateCoordinateOffset) ||
						isEightColumExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)) {
					break;
				}		
				final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
				if(!candidateDestinationTile.isTileOccupied()) {
					legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
				} else {
					final Piece pieceAtDestination = candidateDestinationTile.getPiece();
					final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
					
					if(this.getPieceAlliance() != pieceAlliance) {
						legalMoves.add(new AttackMove(board,this, candidateDestinationCoordinate, pieceAtDestination));
					}
					break;
				}
			}
		}
		return ImmutableList.copyOf(legalMoves);
	}

	@Override
	public Rook movePiece(Move move) {
		return new Rook(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
	}
	
	@Override
	public String toString() {
		return PieceType.ROOK.toString();
	}
	
	private static boolean isFirstColumExclusion(int currentPosition, int candidateOffset) {
		return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -1);
	}
	
	private static boolean isEightColumExclusion(int currentPosition, int candidateOffset) {
		return BoardUtils.EIGHT_COLUMN[currentPosition] && (candidateOffset == 1);
	}

}
