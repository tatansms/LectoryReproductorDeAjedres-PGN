package modelo;

public class Move extends Method {
    private Square from;
    private Square to;
    private Piece movingPiece;
    private Piece captured;
    private Piece specialPawn;
    private boolean white;
    private boolean kingSideCastle;
    private boolean queenSideCastle;
    private boolean reverse;
    private int typeOfConstructor;
    private String stringMove;

    Move(Square s1, Square s2, Piece movingPiece, String stringMove){
        this.setFrom(s1);
        this.setTo(s2);
        this.setMovingPiece(movingPiece);
        this.setTypeOfConstructor(1);
        if(stringMove != null)
            this.setStringMove(stringMove);

        setNewPosition(s1, Piece.NONE);
        setNewPosition(s2, movingPiece);
    }

    Move(Square s1, Square s2, Piece movingPiece, Piece captured, Piece specialPawn, boolean reverse, String stringMove){
        this.setFrom(s1);
        this.setTo(s2);
        this.setMovingPiece(movingPiece);
        this.setCaptured(captured);
        this.setSpecialPawn(specialPawn);
        this.setReverse(reverse);
        this.setTypeOfConstructor(2);
        this.setStringMove(stringMove);

        if(notReverse())
            new Move(s1, s2, movingPiece, null);
        else {
            if ((getPosition(s1)[0] != 0 && getPosition(s2)[0] != 7)
                    || (captured != Piece.WHITE_PAWN && captured != Piece.BLACK_PAWN)) {
                setNewPosition(s2, captured);
                if (specialPawn == Piece.NONE)
                    setNewPosition(s1, movingPiece);
                else
                    setNewPosition(s1, specialPawn);
            } else {
                setNewPosition(s1, captured);
                setNewPosition(s2, Piece.NONE);
            }
        }

        if (captured == Piece.NONE) {
            if (movingPiece == Piece.WHITE_PAWN) {
                int[] enPassant = new int[]{getPosition(s2)[0] - 1, getPosition(s2)[1]};
                if(notReverse())
                    setNewPosition(enPassant, Piece.NONE);
                else
                    setNewPosition(enPassant, Piece.BLACK_PAWN);
            }
            else if (movingPiece == Piece.BLACK_PAWN) {
                int[] enPassant = new int[]{getPosition(s2)[0] + 1, getPosition(s2)[1]};
                if(notReverse())
                    setNewPosition(enPassant, Piece.NONE);
                else
                    setNewPosition(enPassant, Piece.WHITE_PAWN);
            }
        }
    }

    Move(boolean white, boolean kingSideCastle, boolean queenSideCastle, boolean reverse, String stringMove){
        this.setWhite(white);
        this.setKingSideCastle(kingSideCastle);
        this.setQueenSideCastle(queenSideCastle);
        this.setReverse(reverse);
        this.setTypeOfConstructor(3);
        this.setStringMove(stringMove);
        if (white) {
            if (kingSideCastle) {
                if(notReverse()) {
                    new Move(Square.E1, Square.G1, Piece.WHITE_KING, null);
                    new Move(Square.H1, Square.F1, Piece.WHITE_ROOK, null);
                } else {
                    new Move(Square.G1, Square.E1, Piece.WHITE_KING, null);
                    new Move(Square.F1, Square.H1, Piece.WHITE_ROOK, null);
                }
            }
            if (queenSideCastle) {
                if(notReverse()) {
                    new Move(Square.E1, Square.C1, Piece.WHITE_KING, null);
                    new Move(Square.A1, Square.D1, Piece.WHITE_ROOK, null);
                } else {
                    new Move(Square.C1, Square.E1, Piece.WHITE_KING, null);
                    new Move(Square.D1, Square.A1, Piece.WHITE_ROOK, null);
                }
            }
        } else {
            if (kingSideCastle) {
                if(notReverse()) {
                    new Move(Square.E8, Square.G8, Piece.BLACK_KING, null);
                    new Move(Square.H8, Square.F8, Piece.BLACK_ROOK, null);
                } else {
                    new Move(Square.G8, Square.E8, Piece.BLACK_KING, null);
                    new Move(Square.F8, Square.H8, Piece.BLACK_ROOK, null);
                }
            }
            if (queenSideCastle) {
                if(notReverse()) {
                    new Move(Square.E8, Square.C8, Piece.BLACK_KING, null);
                    new Move(Square.A8, Square.D8, Piece.BLACK_ROOK, null);
                } else {
                    new Move(Square.C8, Square.E8, Piece.BLACK_KING, null);
                    new Move(Square.D8, Square.A8, Piece.BLACK_ROOK, null);
                }
            }
        }

    }

    public Square getFrom() {
        return from;
    }

    public void setFrom(Square from) {
        this.from = from;
    }

    public Square getTo() {
        return to;
    }

    public void setTo(Square to) {
        this.to = to;
    }

    public Piece getMovingPiece() {
        return movingPiece;
    }

    public void setMovingPiece(Piece movingPiece) {
        this.movingPiece = movingPiece;
    }

    public Piece getCaptured() {
        return captured;
    }

    public void setCaptured(Piece captured) {
        this.captured = captured;
    }

    public Piece getSpecialPawn() {
        return specialPawn;
    }

    public void setSpecialPawn(Piece specialPawn) {
        this.specialPawn = specialPawn;
    }

    public boolean isWhite() {
        return white;
    }

    public void setWhite(boolean white) {
        this.white = white;
    }

    public boolean isKingSideCastle() {
        return kingSideCastle;
    }

    public void setKingSideCastle(boolean kingSideCastle) {
        this.kingSideCastle = kingSideCastle;
    }

    public boolean isQueenSideCastle() {
        return queenSideCastle;
    }

    public void setQueenSideCastle(boolean queenSideCastle) {
        this.queenSideCastle = queenSideCastle;
    }

    public int getTypeOfConstructor() {
        return typeOfConstructor;
    }

    public void setTypeOfConstructor(int typeOfConstructor) {
        this.typeOfConstructor = typeOfConstructor;
    }

    public boolean notReverse() {
        return !reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    public String getStringMove() {
        return stringMove;
    }

    public void setStringMove(String stringMove) {
        this.stringMove = stringMove;
    }

    /**
     * colocar nueva pieza en el cuadrado dado
     */
    private void setNewPosition(Square square, Piece piece){
        int[] position = getPosition(square);
        Board.finalPositions[position[0]][position[1]] = getNotationPiece(piece);
        Square.piece.put(square, piece);
    }

    /**
     * establecer una nueva pieza en una posición determinada
     */
    private void setNewPosition(int[] position, Piece piece){
        Board.finalPositions[position[0]][position[1]] = getNotationPiece(piece);
        Square.piece.put(getSquare(position), piece);
    }

    /**
     * devolver el nombre de la pieza dada
     **/
    private String stringPiece(Piece piece){
        switch (piece) {
            case Piece.WHITE_ROOK -> {
                return "Torre blanca";
            }
            case Piece.WHITE_KING -> {
                return "Rey blanco";
            }
            case Piece.WHITE_PAWN -> {
                return "Peón blanco";
            }
            case Piece.WHITE_QUEEN -> {
                return "Reina blanca";
            }
            case Piece.WHITE_BISHOP -> {
                return "Alfiler blanca";
            }
            case Piece.WHITE_KNIGHT -> {
                return "Caballo blancoo";
            }
            case Piece.BLACK_KING -> {
                return "Rey negro";
            }
            case Piece.BLACK_PAWN -> {
                return "Peon negro";
            }
            case Piece.BLACK_ROOK -> {
                return "Torre negra";
            }
            case Piece.BLACK_QUEEN -> {
                return "Reina negra";
            }
            case Piece.BLACK_BISHOP -> {
                return "Alfiler negra";
            }
            default -> {
                return "Caballo negro";
            }
        }
    }

    @Override
    public String toString() {
        return getStringMove();
    }

    /**
     * toString versión 2: solo le importa moverse y excluye el acto de captura. Ag4 y Axg4 son iguales aquí.
     **/
    public String toStringV2(){
        if(getTypeOfConstructor() == 1)
            return "["+ stringPiece(getMovingPiece())+" from "+getFrom()+" to "+getTo()+"]";
        else if(getTypeOfConstructor() == 2) {
            if(getSpecialPawn() == Piece.NONE) {
                if ((getPosition(getTo())[0] != 0 && getPosition(getTo())[0] != 7)
                        || (getCaptured() != Piece.WHITE_PAWN && getCaptured() != Piece.BLACK_PAWN))
                    return "[" + stringPiece(getMovingPiece()) + " from " + getFrom() + " to " + getTo() + "]";
                else {
                    return "[" + stringPiece(getCaptured()) + " from " + getFrom() + " to " + getTo() + " promote to " + stringPiece(getMovingPiece()) + "]";
                }
            } else {
                return "[" + stringPiece(getSpecialPawn()) + " from " + getFrom() + " to " + getTo() + " promote to " + stringPiece(getMovingPiece()) + "]";
            }
        }
        else {
            if (isWhite()) {
                if (isKingSideCastle())
                    return "[Castillo del lado del rey blanco]";
                else
                    return "[Castillo lateral de la reina blanca]";
            } else {
                if (isKingSideCastle())
                    return "[Castillo del lado del rey negro]";
                else
                    return "[Castillo lateral de la reina negra]";
            }
        }
    }

    @Override
    public boolean equals(Object o){
        if(o.getClass() == this.getClass()) {
            Move m = (Move) o;
            if (this.getTypeOfConstructor() == m.getTypeOfConstructor()) {
                if (this.getTypeOfConstructor() == 1 && this.getFrom() == m.getFrom() && this.getTo() == m.getTo()
                        && this.getMovingPiece() == m.getMovingPiece())
                    return true;
                else if (this.getTypeOfConstructor() == 2 && this.getFrom() == m.getFrom() && this.getTo() == m.getTo()
                    && this.getMovingPiece() == m.getMovingPiece()) {
                    if(this.getSpecialPawn() == m.getSpecialPawn() && this.getSpecialPawn() == Piece.NONE) {
                        if ((getPosition(getTo())[0] != 0 && getPosition(getTo())[0] != 7)
                                || (getCaptured() != Piece.WHITE_PAWN && getCaptured() != Piece.BLACK_PAWN)) {
                            return true;
                        } else {
                            return this.getCaptured() == m.getCaptured();
                        }
                    } else return this.getSpecialPawn() == m.getSpecialPawn() && this.getCaptured() == m.getCaptured();
                }
                else return this.getTypeOfConstructor() == 3 && this.isWhite() == m.isWhite()
                            && this.isKingSideCastle() == m.isKingSideCastle()
                            && this.isQueenSideCastle() == m.isQueenSideCastle();
            } else
                return false;
        } else
            return false;
    }
}
