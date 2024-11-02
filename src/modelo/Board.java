package modelo;

import java.util.ArrayList;
import java.util.Stack;

public class Board extends Method {
    private ArrayList<Move> moves;
    private final Stack<Move> movesPerformed;
    static char[][] finalPositions;
    private final ArrayList<Piece> capturedPieces;

    public Board(){
        moves = new ArrayList<>();
        movesPerformed = new Stack<>();
        finalPositions = buildFinalPosition();
        capturedPieces = new ArrayList<>();
    }

    public Board(ArrayList<Move> moves){
        this.setMoves(moves);
        movesPerformed = new Stack<>();
        finalPositions = buildFinalPosition();
        capturedPieces = new ArrayList<>();
    }

    public ArrayList<Move> getMoves() {
        return moves;
    }

    public void setMoves(ArrayList<Move> moves) {
        this.moves = moves;
    }

    /**
     * realiza un movimiento de movimientos.
     */
    public void doMove(Move move){
        if(move.getTypeOfConstructor() == 1)
            movesPerformed.push(new Move(move.getFrom(), move.getTo(), move.getMovingPiece(), move.getStringMove()));
        else if(move.getTypeOfConstructor() == 2){
            movesPerformed.push(new Move(move.getFrom(), move.getTo(), move.getMovingPiece(), move.getCaptured(), move.getSpecialPawn(), false, move.getStringMove()));
            if(move.getCaptured() == Piece.NONE){
                if(move.getMovingPiece() == Piece.WHITE_PAWN)
                    capturedPieces.add(Piece.BLACK_PAWN);
                else
                    capturedPieces.add(Piece.WHITE_PAWN);
            } else {
                if((Method.getPosition(move.getTo())[0] != 0 && Method.getPosition(move.getTo())[0] != 7)
                        || (move.getCaptured() != Piece.WHITE_PAWN && move.getCaptured() != Piece.BLACK_PAWN))
                    capturedPieces.add(move.getCaptured());
            }
        }
        else
            movesPerformed.push(new Move(move.isWhite(), move.isKingSideCastle(), move.isQueenSideCastle(), false, move.getStringMove()));
    }

    /**
     * deshace el último movimiento.
     */
    public void undoMove() {
        if (!movesPerformed.empty()) {
            Move lastMove = movesPerformed.pop();
            if (lastMove.getTypeOfConstructor() == 1)
                new Move(lastMove.getTo(), lastMove.getFrom(), lastMove.getMovingPiece(), lastMove.getStringMove());
            else if (lastMove.getTypeOfConstructor() == 2) {
                new Move(lastMove.getFrom(), lastMove.getTo(), lastMove.getMovingPiece(), lastMove.getCaptured(), lastMove.getSpecialPawn(), true, lastMove.getStringMove());
                if (lastMove.getCaptured() == Piece.NONE) {
                    if (lastMove.getMovingPiece() == Piece.WHITE_PAWN)
                        capturedPieces.remove(Piece.BLACK_PAWN);
                    else
                        capturedPieces.remove(Piece.WHITE_PAWN);
                } else {
                    if((Method.getPosition(lastMove.getTo())[0] != 0 && Method.getPosition(lastMove.getTo())[0] != 7)
                            || (lastMove.getCaptured() != Piece.WHITE_PAWN && lastMove.getCaptured() != Piece.BLACK_PAWN))
                        capturedPieces.remove(lastMove.getCaptured());
                }
            } else
                new Move(lastMove.isWhite(), lastMove.isKingSideCastle(), lastMove.isQueenSideCastle(), true, lastMove.getStringMove());
        }
    }

    /**
     * devuelve todas las piezas capturadas al punto de juego en el que se llama al método.
     */
    public ArrayList<Piece> capturedPieces(){
        return capturedPieces;
    }

    /**
     * restablece la posición de las piezas a la posición inicial del juego de ajedrez.
     * También elimina todos los movimientos realizados y piezas capturadas.
     */
    public void resetBoard(){
        finalPositions = buildFinalPosition();
        movesPerformed.removeAllElements();
        capturedPieces.clear();
    }

    /**
     * construiría una base char[][] en la posición inicial del juego de ajedrez.
     */
    private char[][] buildFinalPosition(){
        Method.resetSquares();
        char[][] finalPosition = new char[8][8];
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                finalPosition[i][j] = Method.getNotationPiece(new int[]{i, j});
            }
        }
        return finalPosition;
    }

    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 8; j++)
                result.append(finalPositions[i][j]);
            if(i != 0)
                result.append("\n");
            else
                result.append("\nLado: Blanca");
        }
        return result.toString();
    }
}
