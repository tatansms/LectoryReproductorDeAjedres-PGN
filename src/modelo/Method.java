package modelo;

public class Method {
    /**
     * regresar al cuadrado desde la posición dada.
     */
    public static Square getSquare(int[] position){
        String name = null;

        switch (position[1]){
            case 0 -> name = "A";
            case 1 -> name = "B";
            case 2 -> name = "C";
            case 3 -> name = "D";
            case 4 -> name = "E";
            case 5 -> name = "F";
            case 6 -> name = "G";
            case 7 -> name = "H";
        }
        name += position[0]+1;
        return Square.valueOf(name);
    }

    /**
     * posición de retorno desde el cuadrado dado.
     */
    public static int[] getPosition(Square square){
        return Square.position.get(square);
    }

    /**
     * posición de retorno de una cadena de cuadrados dada.
     */
    public static int[] getPosition(String s){
        return Square.position.get(Square.valueOf(s.toUpperCase()));
    }

    /**
     * devolver pieza desde la posición dada.
     */
    public static Piece getPiece(int[] position){
        return Square.piece.get(getSquare(position));
    }

    /**
     * devolver pieza del índice de movimiento dado y carácter de la primera letra de la pieza.
     * Utiliza el índice de movimiento para determinar blanco o negro.
     */
    public static Piece getPiece(int i, char c){
        String s = String.valueOf(c);
        if(i % 2 == 0)
            s = s.toUpperCase();
        else
            s = s.toLowerCase();
        switch (s){
            case "P" -> {
                return Piece.WHITE_PAWN;
            }
            case "R" -> {
                return Piece.WHITE_ROOK;
            }
            case "N" -> {
                return Piece.WHITE_KNIGHT;
            }
            case "B" -> {
                return Piece.WHITE_BISHOP;
            }
            case "Q" -> {
                return Piece.WHITE_QUEEN;
            }
            case "K" -> {
                return Piece.WHITE_KING;
            }
            case "p" -> {
                return Piece.BLACK_PAWN;
            }
            case "r" -> {
                return Piece.BLACK_ROOK;
            }
            case "n" -> {
                return Piece.BLACK_KNIGHT;
            }
            case "b" -> {
                return Piece.BLACK_BISHOP;
            }
            case "q" -> {
                return Piece.BLACK_QUEEN;
            }
            case "k" -> {
                return Piece.BLACK_KING;
            }
        }
        return null;
    }

    /**
     * notación de devolución de pieza de pieza dada.
     */
    public static char getNotationPiece(Piece piece){
        return Piece.notation.get(piece);
    }

    /**
     * notación de retorno de pieza desde una posición dada.
     */
    public static char getNotationPiece(int[] position){
        return Piece.notation.get(Square.piece.get(getSquare(position)));
    }

    /**
     * restablece la relación entre casillas y piezas a la posición inicial del ajedrez.
     */
    public static void resetSquares(){
        Square[] square = Square.values();
        for (Square s : square) {
            String[] arr = s.toString().split("");
            switch (arr[1]) {
                case "1":
                    switch (arr[0]) {
                        case "A", "H" -> Square.piece.put(s, Piece.WHITE_ROOK);
                        case "B", "G" -> Square.piece.put(s, Piece.WHITE_KNIGHT);
                        case "C", "F" -> Square.piece.put(s, Piece.WHITE_BISHOP);
                        case "D" -> Square.piece.put(s, Piece.WHITE_QUEEN);
                        case "E" -> Square.piece.put(s, Piece.WHITE_KING);
                    }
                    break;
                case "8":
                    switch (arr[0]) {
                        case "A", "H" -> Square.piece.put(s, Piece.BLACK_ROOK);
                        case "B", "G" -> Square.piece.put(s, Piece.BLACK_KNIGHT);
                        case "C", "F" -> Square.piece.put(s, Piece.BLACK_BISHOP);
                        case "D" -> Square.piece.put(s, Piece.BLACK_QUEEN);
                        case "E" -> Square.piece.put(s, Piece.BLACK_KING);
                    }
                    break;
                case "2":
                    Square.piece.put(s, Piece.WHITE_PAWN);
                    break;
                case "7":
                    Square.piece.put(s, Piece.BLACK_PAWN);
                    break;
                default:
                    Square.piece.put(s, Piece.NONE);
                    break;
            }
        }
    }
}
