package modelo;

import java.util.ArrayList;
import java.util.Arrays;

public class Converter extends Method {
    /**
     * convierte un movimiento de la notación controlador.PGN a un objeto modelo.Move.
     * primero detecta movimientos por su longitud y luego por notación especial que separaría su tipo de movimiento
     * de otros.
     * luego detecta la posición inicial y final del movimiento y agrega el movimiento a una Arraylist de objetos modelo.Move.
     */
    public static ArrayList<Move> convertMoves(String[] movesArray){
        ArrayList<Move> moves = new ArrayList<>();
        Method.resetSquares();
        for (int i = 0; i < movesArray.length; i++) {
            String s = movesArray[i];
            switch (s.length()) {
                case 2 -> {
                    int[] position = Method.getPosition(s);
                    int[] initial = detectPawn(i, position, '0');
                    moves.add(new Move(Method.getSquare(initial), Method.getSquare(position), Method.getPiece(initial), s));
                }
                case 3 -> {
                    if (s.equals("O-O")) {
                        moves.add(new Move(i % 2 == 0, true, false, false, s));
                    } else {
                        String piece = String.valueOf(s.charAt(0));
                        int[] position = Method.getPosition(s.charAt(1) + String.valueOf(s.charAt(2)));
                        int[] initial = detectPiece(i, piece, position, false);
                        moves.add(new Move(Method.getSquare(initial), Method.getSquare(position), Method.getPiece(initial), s));
                    }
                }
                case 4 -> {
                    if (s.contains("=")) {
                        int[] position = Method.getPosition(s.charAt(0) + String.valueOf(s.charAt(1)));
                        int[] initial = detectPawn(i, position, '0');
                        moves.add(new Move(Method.getSquare(initial), Method.getSquare(position), Method.getPiece(i, s.charAt(3)), Method.getPiece(initial), Piece.NONE, false, s));
                    } else if (s.contains("x")) {
                        int[] position = Method.getPosition(s.charAt(2) + String.valueOf(s.charAt(3)));
                        int[] initial;
                        if (Character.isUpperCase(s.charAt(0))) {
                            String piece = String.valueOf(s.charAt(0));
                            initial = detectPiece(i, piece, position, true);
                        } else {
                            initial = detectPawn(i, position, Character.toUpperCase(s.charAt(0)));
                        }
                        moves.add(new Move(Method.getSquare(initial), Method.getSquare(position), Method.getPiece(initial), Method.getPiece(position), Piece.NONE, false, s));
                    } else if (Character.isLowerCase(s.charAt(1))) {
                        String piece = String.valueOf(s.charAt(0));
                        String y = String.valueOf(s.charAt(1));
                        int[] position = Method.getPosition(s.charAt(2) + String.valueOf(s.charAt(3)));
                        int[] initial = detectPieceGivenInformation(i, piece, null, y, position);
                        moves.add(new Move(Method.getSquare(initial), Method.getSquare(position), Method.getPiece(initial), s));
                    } else {
                        String piece = String.valueOf(s.charAt(0));
                        String x = String.valueOf(s.charAt(1));
                        int[] position = Method.getPosition(s.charAt(2) + String.valueOf(s.charAt(3)));
                        int[] initial = detectPieceGivenInformation(i, piece, x, null, position);
                        moves.add(new Move(Method.getSquare(initial), Method.getSquare(position), Method.getPiece(initial), s));
                    }
                }
                case 5 -> {
                    if (s.equals("O-O-O")) {
                        moves.add(new Move(i % 2 == 0, false, true, false, s));
                    } else if (s.charAt(0) == 'Q' && Character.isLowerCase(s.charAt(1)) && Character.isLowerCase(s.charAt(3))
                            && Character.isDigit(s.charAt(2)) && Character.isDigit(s.charAt(4))){
                        int[] initial = Method.getPosition(s.charAt(1) + String.valueOf(s.charAt(2)));
                        int[] position = Method.getPosition(s.charAt(3) + String.valueOf(s.charAt(4)));
                        moves.add(new Move(Method.getSquare(initial), Method.getSquare(position), Method.getPiece(initial), s));
                    }
                    else if (Character.isLowerCase(s.charAt(1))) {
                        String piece = String.valueOf(s.charAt(0));
                        String y = String.valueOf(s.charAt(1));
                        int[] position = Method.getPosition(s.charAt(3) + String.valueOf(s.charAt(4)));
                        int[] initial = detectPieceGivenInformation(i, piece, null, y, position);
                        moves.add(new Move(Method.getSquare(initial), Method.getSquare(position), Method.getPiece(initial), Method.getPiece(position), Piece.NONE, false, s));
                    }
                    else {
                        String piece = String.valueOf(s.charAt(0));
                        String x = String.valueOf(s.charAt(1));
                        int[] position = Method.getPosition(s.charAt(3) + String.valueOf(s.charAt(4)));
                        int[] initial = detectPieceGivenInformation(i, piece, x, null, position);
                        moves.add(new Move(Method.getSquare(initial), Method.getSquare(position), Method.getPiece(initial), Method.getPiece(position), Piece.NONE, false, s));
                    }
                }
                case 6 -> {
                    if(s.contains("=")) {
                        int[] position = Method.getPosition(s.charAt(2) + String.valueOf(s.charAt(3)));
                        int[] initial = detectPawn(i, position, Character.toUpperCase(s.charAt(0)));
                        moves.add(new Move(Method.getSquare(initial), Method.getSquare(position), Method.getPiece(i, s.charAt(5)), Method.getPiece(position), Method.getPiece(initial), false, s));
                    }
                    else if(s.contains("x")){
                        int[] initial = Method.getPosition(s.charAt(1) + String.valueOf(s.charAt(2)));
                        int[] position = Method.getPosition(s.charAt(4) + String.valueOf(s.charAt(5)));
                        moves.add(new Move(Method.getSquare(initial), Method.getSquare(position), Method.getPiece(initial), Method.getPiece(position), Piece.NONE, false, s));
                    }
                }
            }
        }
        return moves;
    }

    /**
     * detecta la posición inicial de un movimiento de peón.
     * @param i
     * índice de jugada para determinar blanco o negro.
     * @param position
     * posición final del movimiento
     * @param s1
     * es la primera letra de la cadena de movimiento cuando el movimiento del peón incluye la captura.
     * "exd5" => s1 = 'e'
     * '0' => declara que el movimiento no incluye captura. como "e5" => s1 = '0'
     */
    public static int[] detectPawn(int i, int[] position, char s1){
        if(i % 2 == 0) {
            if(s1 == '0') {
                if (Method.getPiece(new int[]{position[0] - 1, position[1]}) == Piece.WHITE_PAWN)
                    return new int[]{position[0] - 1, position[1]};
                else
                    return new int[]{position[0] - 2, position[1]};
            } else
                return Method.getPosition(s1+String.valueOf(position[0]));
        } else {
            if(s1 == '0') {
                if (Method.getPiece(new int[]{position[0] + 1, position[1]}) == Piece.BLACK_PAWN)
                    return new int[]{position[0] + 1, position[1]};
                else
                    return new int[]{position[0] + 2, position[1]};
            } else
                return Method.getPosition(s1+String.valueOf(position[0]+2));
        }
    }

    /**
     * detecta la posición inicial del movimiento de una pieza. existe un método diferente para cada tipo de pieza.
     * para Torres busca una torre adecuada en casillas que estén verticales u horizontales a la posición final.
     * para Alfiles busca un alfil adecuado en casillas diagonales a la posición final.
     * para Caballeros busca un caballo adecuado en 8 casillas en las que podría estar un caballo antes de pasar a la posición final.
     * para Reina busca una reina adecuada en casillas que sean verticales u horizontales o diagonales a la posición final.
     * para Rey busca un rey adecuado en 8 casillas que podría ser un rey antes de pasar a la posición final.
     * @param i
     * índice de movimiento
     * @param piece
     * pieza en movimiento
     * @param position
     * posición final del movimiento
     * captura @param
     * indicar si el movimiento incluye captura o no.
     */
    public static int[] detectPiece(int i, String piece, int[] position, boolean capture){
        switch (piece.toUpperCase()) {
            case "R":
                for (int j = 0; j < 8; j++) {
                    int[] initial1 = new int[]{j, position[1]};
                    int[] initial2 = new int[]{position[0], j};
                    if(i % 2 == 0) {
                        if (Method.getPiece(initial1) == Piece.WHITE_ROOK) {
                            if (isAllowedMove(initial1, position, capture, "verticalmente")) {
                                if (isAllowedCheck(i, Method.getSquare(initial1), Method.getSquare(position), Method.getPiece(initial1)))
                                    return initial1;
                            }
                        }
                        if (Method.getPiece(initial2) == Piece.WHITE_ROOK) {
                            if (isAllowedMove(initial2, position, capture, "horizontalmente")) {
                                if (isAllowedCheck(i, Method.getSquare(initial2), Method.getSquare(position), Method.getPiece(initial2)))
                                    return initial2;
                            }
                        }
                    } else {
                        if (Method.getPiece(initial1) == Piece.BLACK_ROOK) {
                            if (isAllowedMove(initial1, position, capture, "verticalmente")) {
                                if (isAllowedCheck(i, Method.getSquare(initial1), Method.getSquare(position), Method.getPiece(initial1)))
                                    return initial1;
                            }
                        }
                        if (Method.getPiece(initial2) == Piece.BLACK_ROOK) {
                            if (isAllowedMove(initial2, position, capture, "horizontalmente")) {
                                if (isAllowedCheck(i, Method.getSquare(initial2), Method.getSquare(position), Method.getPiece(initial2)))
                                    return initial2;
                            }
                        }
                    }
                }
                break;
            case "B":
                for (int j = -7; j < 8; j++) {
                    if (position[0] + j >= 0 && position[0] + j <= 7 && position[1] + j >= 0 && position[1] + j <= 7) {
                        int[] initial1 = new int[]{position[0] + j, position[1] + j};
                        if(i % 2 == 0) {
                            if (Method.getPiece(initial1) == Piece.WHITE_BISHOP) {
                                if (isAllowedMove(initial1, position, capture, "diagonalmente")) {
                                    if (isAllowedCheck(i, Method.getSquare(initial1), Method.getSquare(position), Method.getPiece(initial1)))
                                        return initial1;
                                }
                            }
                        } else {
                            if (Method.getPiece(initial1) == Piece.BLACK_BISHOP) {
                                if (isAllowedMove(initial1, position, capture, "diagonalmente")) {
                                    if (isAllowedCheck(i, Method.getSquare(initial1), Method.getSquare(position), Method.getPiece(initial1)))
                                        return initial1;
                                }
                            }
                        }
                    }
                    if (position[0] + j >= 0 && position[0] + j <= 7 && position[1] - j >= 0 && position[1] - j <= 7) {
                        int[] initial2 = new int[]{position[0] + j, position[1] - j};
                        if(i % 2 == 0) {
                            if (Method.getPiece(initial2) == Piece.WHITE_BISHOP) {
                                if (isAllowedMove(initial2, position, capture, "diagonalmente")) {
                                    if (isAllowedCheck(i, Method.getSquare(initial2), Method.getSquare(position), Method.getPiece(initial2)))
                                        return initial2;
                                }
                            }
                        } else {
                            if (Method.getPiece(initial2) == Piece.BLACK_BISHOP) {
                                if (isAllowedMove(initial2, position, capture, "diagonalmente")) {
                                    if (isAllowedCheck(i, Method.getSquare(initial2), Method.getSquare(position), Method.getPiece(initial2)))
                                        return initial2;
                                }
                            }
                        }
                    }
                }
                break;
            case "N":
                if (position[0] - 1 >= 0 && position[1] - 2 >= 0) {
                    int[] initial = new int[]{position[0] - 1, position[1] - 2};
                    if(i % 2 == 0) {
                        if (Method.getPiece(initial) == Piece.WHITE_KNIGHT) {
                            if (isAllowedCheck(i, Method.getSquare(initial), Method.getSquare(position), Method.getPiece(initial)))
                                return initial;
                        }
                    } else {
                        if (Method.getPiece(initial) == Piece.BLACK_KNIGHT) {
                            if (isAllowedCheck(i, Method.getSquare(initial), Method.getSquare(position), Method.getPiece(initial)))
                                return initial;
                        }
                    }
                }
                if (position[0] - 1 >= 0 && position[1] + 2 <= 7) {
                    int[] initial = new int[]{position[0] - 1, position[1] + 2};
                    if(i % 2 == 0) {
                        if (Method.getPiece(initial) == Piece.WHITE_KNIGHT) {
                            if (isAllowedCheck(i, Method.getSquare(initial), Method.getSquare(position), Method.getPiece(initial)))
                                return initial;
                        }
                    } else {
                        if (Method.getPiece(initial) == Piece.BLACK_KNIGHT) {
                            if (isAllowedCheck(i, Method.getSquare(initial), Method.getSquare(position), Method.getPiece(initial)))
                                return initial;
                        }
                    }
                }
                if (position[0] - 2 >= 0 && position[1] - 1 >= 0) {
                    int[] initial = new int[]{position[0] - 2, position[1] - 1};
                    if(i % 2 == 0) {
                        if (Method.getPiece(initial) == Piece.WHITE_KNIGHT) {
                            if (isAllowedCheck(i, Method.getSquare(initial), Method.getSquare(position), Method.getPiece(initial)))
                                return initial;
                        }
                    } else {
                        if (Method.getPiece(initial) == Piece.BLACK_KNIGHT) {
                            if (isAllowedCheck(i, Method.getSquare(initial), Method.getSquare(position), Method.getPiece(initial)))
                                return initial;
                        }
                    }
                }
                if (position[0] - 2 >= 0 && position[1] + 1 <= 7) {
                    int[] initial = new int[]{position[0] - 2, position[1] + 1};
                    if(i % 2 == 0) {
                        if (Method.getPiece(initial) == Piece.WHITE_KNIGHT) {
                            if (isAllowedCheck(i, Method.getSquare(initial), Method.getSquare(position), Method.getPiece(initial)))
                                return initial;
                        }
                    } else {
                        if (Method.getPiece(initial) == Piece.BLACK_KNIGHT) {
                            if (isAllowedCheck(i, Method.getSquare(initial), Method.getSquare(position), Method.getPiece(initial)))
                                return initial;
                        }
                    }
                }
                if (position[0] + 1 <= 7 && position[1] - 2 >= 0) {
                    int[] initial = new int[]{position[0] + 1, position[1] - 2};
                    if(i % 2 == 0) {
                        if (Method.getPiece(initial) == Piece.WHITE_KNIGHT) {
                            if (isAllowedCheck(i, Method.getSquare(initial), Method.getSquare(position), Method.getPiece(initial)))
                                return initial;
                        }
                    } else {
                        if (Method.getPiece(initial) == Piece.BLACK_KNIGHT) {
                            if (isAllowedCheck(i, Method.getSquare(initial), Method.getSquare(position), Method.getPiece(initial)))
                                return initial;
                        }
                    }
                }
                if (position[0] + 1 <= 7 && position[1] + 2 <= 7) {
                    int[] initial = new int[]{position[0] + 1, position[1] + 2};
                    if(i % 2 == 0) {
                        if (Method.getPiece(initial) == Piece.WHITE_KNIGHT) {
                            if (isAllowedCheck(i, Method.getSquare(initial), Method.getSquare(position), Method.getPiece(initial)))
                                return initial;
                        }
                    } else {
                        if (Method.getPiece(initial) == Piece.BLACK_KNIGHT) {
                            if (isAllowedCheck(i, Method.getSquare(initial), Method.getSquare(position), Method.getPiece(initial)))
                                return initial;
                        }
                    }
                }
                if (position[0] + 2 <= 7 && position[1] - 1 >= 0) {
                    int[] initial = new int[]{position[0] + 2, position[1] - 1};
                    if(i % 2 == 0) {
                        if (Method.getPiece(initial) == Piece.WHITE_KNIGHT) {
                            if (isAllowedCheck(i, Method.getSquare(initial), Method.getSquare(position), Method.getPiece(initial)))
                                return initial;
                        }
                    } else {
                        if (Method.getPiece(initial) == Piece.BLACK_KNIGHT) {
                            if (isAllowedCheck(i, Method.getSquare(initial), Method.getSquare(position), Method.getPiece(initial)))
                                return initial;
                        }
                    }
                }
                if (position[0] + 2 <= 7 && position[1] + 1 <= 7) {
                    int[] initial = new int[]{position[0] + 2, position[1] + 1};
                    if(i % 2 == 0) {
                        if (Method.getPiece(initial) == Piece.WHITE_KNIGHT) {
                            if (isAllowedCheck(i, Method.getSquare(initial), Method.getSquare(position), Method.getPiece(initial)))
                                return initial;
                        }
                    } else {
                        if (Method.getPiece(initial) == Piece.BLACK_KNIGHT) {
                            if (isAllowedCheck(i, Method.getSquare(initial), Method.getSquare(position), Method.getPiece(initial)))
                                return initial;
                        }
                    }
                }
                break;
            case "Q":
                for (int j = 0; j < 8; j++) {
                    int[] initial1 = new int[]{j, position[1]};
                    int[] initial2 = new int[]{position[0], j};
                    if(i % 2 == 0) {
                        if (Method.getPiece(initial1) == Piece.WHITE_QUEEN) {
                            if (isAllowedMove(initial1, position, capture, "verticalmente")) {
                                if (isAllowedCheck(i, Method.getSquare(initial1), Method.getSquare(position), Method.getPiece(initial1)))
                                    return initial1;
                            }
                        }
                        if (Method.getPiece(initial2) == Piece.WHITE_QUEEN) {
                            if (isAllowedMove(initial2, position, capture, "horizontalmente")) {
                                if (isAllowedCheck(i, Method.getSquare(initial2), Method.getSquare(position), Method.getPiece(initial2)))
                                    return initial2;
                            }
                        }
                    } else {
                        if (Method.getPiece(initial1) == Piece.BLACK_QUEEN) {
                            if (isAllowedMove(initial1, position, capture, "verticalmente")) {
                                if (isAllowedCheck(i, Method.getSquare(initial1), Method.getSquare(position), Method.getPiece(initial1)))
                                    return initial1;
                            }
                        }
                        if (Method.getPiece(initial2) == Piece.BLACK_QUEEN) {
                            if (isAllowedMove(initial2, position, capture, "horizontalmente")) {
                                if (isAllowedCheck(i, Method.getSquare(initial2), Method.getSquare(position), Method.getPiece(initial2)))
                                    return initial2;
                            }
                        }
                    }
                }
                for (int j = -7; j < 8; j++) {
                    if (position[0] + j >= 0 && position[0] + j <= 7 && position[1] + j >= 0 && position[1] + j <= 7) {
                        int[] initial1 = new int[]{position[0] + j, position[1] + j};
                        if(i % 2 == 0) {
                            if (Method.getPiece(initial1) == Piece.WHITE_QUEEN) {
                                if (isAllowedMove(initial1, position, capture, "diagonalmente")) {
                                    if (isAllowedCheck(i, Method.getSquare(initial1), Method.getSquare(position), Method.getPiece(initial1)))
                                        return initial1;
                                }
                            }
                        } else {
                            if (Method.getPiece(initial1) == Piece.BLACK_QUEEN) {
                                if (isAllowedMove(initial1, position, capture, "diagonalmente")) {
                                    if (isAllowedCheck(i, Method.getSquare(initial1), Method.getSquare(position), Method.getPiece(initial1)))
                                        return initial1;
                                }
                            }
                        }
                    }
                    if (position[0] + j >= 0 && position[0] + j <= 7 && position[1] - j >= 0 && position[1] - j <= 7) {
                        int[] initial2 = new int[]{position[0] + j, position[1] - j};
                        if(i % 2 == 0) {
                            if (Method.getPiece(initial2) == Piece.WHITE_QUEEN) {
                                if (isAllowedMove(initial2, position, capture, "diagonalmente")) {
                                    if (isAllowedCheck(i, Method.getSquare(initial2), Method.getSquare(position), Method.getPiece(initial2)))
                                        return initial2;
                                }
                            }
                        } else {
                            if (Method.getPiece(initial2) == Piece.BLACK_QUEEN) {
                                if (isAllowedMove(initial2, position, capture, "diagonalmente")) {
                                    if (isAllowedCheck(i, Method.getSquare(initial2), Method.getSquare(position), Method.getPiece(initial2)))
                                        return initial2;
                                }
                            }
                        }
                    }
                }
                break;
            default:
                for (int j = -1; j < 2; j++) {
                    for (int k = -1; k < 2; k++) {
                        if(position[0] + j >= 0 && position[0] + j <= 7 &&
                                position[1] + k >= 0 && position[1] + k <= 7) {
                            int[] initial = new int[]{position[0] + j, position[1] + k};
                            if(i % 2 == 0) {
                                if (Method.getPiece(initial) == Piece.WHITE_KING)
                                    return initial;
                            } else {
                                if (Method.getPiece(initial) == Piece.BLACK_KING)
                                    return initial;
                            }
                        }
                    }
                }
                break;
        }
        return new int[]{};
    }

    /**
     * detecta la posición inicial del movimiento de una pieza cuando se proporciona información adicional en el movimiento de la cuerda.
     * @param i
     * índice de movimiento
     * @param piece
     * pieza en movimiento
     * @param x
     * información sobre el eje x.
     * @param x
     * información sobre el eje y.
     * @param y
     * posición final del movimiento
     */
    public static int[] detectPieceGivenInformation(int i, String piece, String x, String y, int[] position){
        if(x != null) {
            int x1 = Integer.parseInt(x)-1;
            switch (piece.toUpperCase()) {
                case "R":
                    return new int[]{x1, position[1]};
                case "N":
                    if(position[1]-1 >= 0) {
                        int[] initial = new int[]{x1, position[1] - 1};
                        if(i % 2 == 0) {
                            if (Method.getPiece(initial) == Piece.WHITE_KNIGHT)
                                return initial;
                        } else {
                            if (Method.getPiece(initial) == Piece.BLACK_KNIGHT)
                                return initial;
                        }
                    }
                    if(position[1]-2 >= 0) {
                        int[] initial = new int[]{x1, position[1] - 2};
                        if(i % 2 == 0) {
                            if (Method.getPiece(initial) == Piece.WHITE_KNIGHT)
                                return initial;
                        } else {
                            if (Method.getPiece(initial) == Piece.BLACK_KNIGHT)
                                return initial;
                        }
                    }
                    if(position[1]+1 <= 7) {
                        int[] initial = new int[]{x1, position[1] + 1};
                        if(i % 2 == 0) {
                            if (Method.getPiece(initial) == Piece.WHITE_KNIGHT)
                                return initial;
                        } else {
                            if (Method.getPiece(initial) == Piece.BLACK_KNIGHT)
                                return initial;
                        }
                    }
                    if(position[1]+2 <= 7) {
                        int[] initial = new int[]{x1, position[1] + 2};
                        if(i % 2 == 0) {
                            if (Method.getPiece(initial) == Piece.WHITE_KNIGHT)
                                return initial;
                        } else {
                            if (Method.getPiece(initial) == Piece.BLACK_KNIGHT)
                                return initial;
                        }
                    }
                    break;
                case "B":
                    for(int j = 0; j <= 7; j++){
                        int[] initial = new int[]{x1, j};
                        if(i % 2 == 0) {
                            if (Method.getPiece(initial) == Piece.WHITE_BISHOP)
                                return initial;
                        } else {
                            if (Method.getPiece(initial) == Piece.BLACK_BISHOP)
                                return initial;
                        }
                    }
                    break;
                case "Q":
                    for(int j = 0; j <= 7; j++){
                        int[] initial = new int[]{x1, j};
                        if(i % 2 == 0) {
                            if (Method.getPiece(initial) == Piece.WHITE_QUEEN)
                                return initial;
                        } else {
                            if (Method.getPiece(initial) == Piece.BLACK_QUEEN)
                                return initial;
                        }
                    }
                    break;
            }
        } else {
            int y1;
            switch (y.toUpperCase()){
                case ("A") -> y1 = 0;
                case ("B") -> y1 = 1;
                case ("C") -> y1 = 2;
                case ("D") -> y1 = 3;
                case ("E") -> y1 = 4;
                case ("F") -> y1 = 5;
                case ("G") -> y1 = 6;
                default -> y1 = 7;
            }
            switch (piece.toUpperCase()) {
                case "R":
                    if(y1 != position[1])
                        return new int[]{position[0], y1};
                    else{
                        for(int k = 0; k <= 7; k++){
                            int[] initial = new int[]{k, y1};
                            if(i % 2 == 0) {
                                if(Method.getPiece(initial) == Piece.WHITE_ROOK)
                                    return new int[]{k, y1};
                            } else {
                                if(Method.getPiece(initial) == Piece.BLACK_ROOK)
                                    return new int[]{k, y1};
                            }
                        }
                    }
                    break;
                case "N":
                    if(position[0]-1 >= 0) {
                        int[] initial = new int[]{position[0] - 1, y1};
                        if(i % 2 == 0) {
                            if (Method.getPiece(initial) == Piece.WHITE_KNIGHT)
                                return initial;
                        } else {
                            if (Method.getPiece(initial) == Piece.BLACK_KNIGHT)
                                return initial;
                        }
                    }
                    if(position[0]-2 >= 0) {
                        int[] initial = new int[]{position[0] - 2, y1};
                        if(i % 2 == 0) {
                            if (Method.getPiece(initial) == Piece.WHITE_KNIGHT)
                                return initial;
                        } else {
                            if (Method.getPiece(initial) == Piece.BLACK_KNIGHT)
                                return initial;
                        }
                    }
                    if(position[0]+1 <= 7) {
                        int[] initial = new int[]{position[0] + 1, y1};
                        if(i % 2 == 0) {
                            if (Method.getPiece(initial) == Piece.WHITE_KNIGHT)
                                return initial;
                        } else {
                            if (Method.getPiece(initial) == Piece.BLACK_KNIGHT)
                                return initial;
                        }
                    }
                    if(position[0]+2 <= 7) {
                        int[] initial = new int[]{position[0] + 2, y1};
                        if(i % 2 == 0) {
                            if (Method.getPiece(initial) == Piece.WHITE_KNIGHT)
                                return initial;
                        } else {
                            if (Method.getPiece(initial) == Piece.BLACK_KNIGHT)
                                return initial;
                        }
                    }
                    break;
                case "B":
                    for(int j = 0; j <= 7; j++){
                        int[] initial = new int[]{j, y1};
                        if(i % 2 == 0) {
                            if (Method.getPiece(initial) == Piece.WHITE_BISHOP)
                                return initial;
                        } else {
                            if (Method.getPiece(initial) == Piece.BLACK_BISHOP)
                                return initial;
                        }
                    }
                    break;
                case "Q":
                    for(int j = 0; j <= 7; j++){
                        int[] initial = new int[]{j, y1};
                        if(i % 2 == 0) {
                            if (Method.getPiece(initial) == Piece.WHITE_QUEEN)
                                return initial;
                        } else {
                            if (Method.getPiece(initial) == Piece.BLACK_QUEEN)
                                return initial;
                        }
                    }
                    break;
            }
        }
        return new int[]{};
    }

    /**
     * si una pieza detiene un cheque, entonces no podemos moverla.
     * Detecta la posición del rey y comprueba si el movimiento ocurre. ¿Hay algún camino claro hacia el rey vertical y horizontalmente?
     * y en diagonal o no. si lo hubiera, no lo permitiría y devolvería falso.
     * @param i
     * index of move
     * @param from
     * initial square
     * @param to
     * final square
     * @param piece
     * moving piece
     */
    public static boolean isAllowedCheck(int i, Square from, Square to, Piece piece){
        boolean allowed = true;

        Square.piece.put(from, Piece.NONE);
        Piece pieceInTo = Square.piece.get(to);
        Square.piece.put(to, piece);

        for(Square s: Square.values()){
            Piece p = Square.piece.get(s);
            int[] positionOfKing = new int[2];
            if(i % 2 == 0) {
                for(Square s1: Square.values()){
                    if(Square.piece.get(s1) == Piece.WHITE_KING) {
                        positionOfKing = Square.position.get(s1);
                        break;
                    }
                }
                if (p == Piece.BLACK_ROOK){
                    int[] positionOfRook = Square.position.get(s);
                    if(positionOfRook[0] == positionOfKing[0] && positionOfRook[1] != positionOfKing[1]){
                        for(int k = Math.min(positionOfRook[1], positionOfKing[1])+1;
                            k < Math.max(positionOfRook[1], positionOfKing[1]); k++){
                            if(Method.getPiece(new int[]{positionOfRook[0], k}) != Piece.NONE) {
                                allowed = true;
                                break;
                            }
                            allowed = false;
                        }
                    }
                    else if(positionOfRook[0] != positionOfKing[0] && positionOfRook[1] == positionOfKing[1]){
                        for(int k = Math.min(positionOfRook[0], positionOfKing[0])+1;
                            k < Math.max(positionOfRook[0], positionOfKing[0]); k++){
                            if(Method.getPiece(new int[]{k, positionOfRook[1]}) != Piece.NONE) {
                                allowed = true;
                                break;
                            }
                            allowed = false;
                        }
                    }
                    if(!allowed) {
                        Square.piece.put(from, piece);
                        Square.piece.put(to, pieceInTo);
                        return false;
                    }
                }
                else if (p == Piece.BLACK_BISHOP){
                    int[] positionOfBishop = Square.position.get(s);
                    if((positionOfBishop[0] + positionOfBishop[1]) % 2 == (positionOfKing[0] + positionOfKing[1]) % 2
                            && Math.abs(positionOfKing[0]-positionOfBishop[0]) == Math.abs(positionOfKing[1]-positionOfBishop[1])){
                        if(positionOfKing[0] > positionOfBishop[0] && positionOfKing[1] > positionOfBishop[1]){
                            for (int j = 1; j < Math.max(positionOfKing[0]-positionOfBishop[0], positionOfKing[1]-positionOfBishop[1]); j++) {
                                int[] pos = new int[]{positionOfBishop[0] + j, positionOfBishop[1] + j};
                                if(positionOfBishop[0] + j >=0 && positionOfBishop[0] + j <=7 &&
                                        positionOfBishop[1] + j >=0 && positionOfBishop[1] + j <= 7) {
                                    if (Method.getPiece(pos) != Piece.NONE) {
                                        allowed = true;
                                        break;
                                    }
                                    allowed = false;
                                }
                            }
                        }
                        else if(positionOfKing[0] > positionOfBishop[0] && positionOfKing[1] < positionOfBishop[1]){
                            for (int j = 1; j < Math.max(positionOfKing[0]-positionOfBishop[0], positionOfBishop[1]-positionOfKing[1]); j++) {
                                int[] pos = new int[]{positionOfBishop[0] + j, positionOfBishop[1] - j};
                                if(positionOfBishop[0] + j >=0 && positionOfBishop[0] + j <=7 &&
                                        positionOfBishop[1] - j >=0 && positionOfBishop[1] - j <= 7) {
                                    if (Method.getPiece(pos) != Piece.NONE) {
                                        allowed = true;
                                        break;
                                    }
                                    allowed = false;
                                }
                            }
                        }
                        else if(positionOfKing[0] < positionOfBishop[0] && positionOfKing[1] > positionOfBishop[1]){
                            for (int j = 1; j < Math.max(positionOfBishop[0]-positionOfKing[0], positionOfKing[1]-positionOfBishop[1]); j++) {
                                int[] pos = new int[]{positionOfBishop[0] - j, positionOfBishop[1] + j};
                                if(positionOfBishop[0] - j >=0 && positionOfBishop[0] - j <=7 &&
                                        positionOfBishop[1] + j >=0 && positionOfBishop[1] + j <= 7) {
                                    if (Method.getPiece(pos) != Piece.NONE) {
                                        allowed = true;
                                        break;
                                    }
                                    allowed = false;
                                }
                            }
                        }
                        else if(positionOfKing[0] < positionOfBishop[0] && positionOfKing[1] < positionOfBishop[1]){
                            for (int j = 1; j < Math.max(positionOfBishop[0]-positionOfKing[0], positionOfBishop[1]-positionOfKing[1]); j++) {
                                int[] pos = new int[]{positionOfBishop[0] - j, positionOfBishop[1] - j};
                                if(positionOfBishop[0] - j >=0 && positionOfBishop[0] - j <=7 &&
                                        positionOfBishop[1] - j >=0 && positionOfBishop[1] - j <= 7) {
                                    if (Method.getPiece(pos) != Piece.NONE) {
                                        allowed = true;
                                        break;
                                    }
                                    allowed = false;
                                }
                            }
                        }
                        if(!allowed) {
                            Square.piece.put(from, piece);
                            Square.piece.put(to, pieceInTo);
                            return false;
                        }
                    }
                }
                else if (p == Piece.BLACK_QUEEN) {
                    int[] positionOfQueen = Square.position.get(s);
                    if(positionOfQueen[0] == positionOfKing[0] && positionOfQueen[1] != positionOfKing[1]){
                        for(int k = Math.min(positionOfQueen[1], positionOfKing[1])+1;
                            k < Math.max(positionOfQueen[1], positionOfKing[1]); k++){
                            if(Method.getPiece(new int[]{positionOfQueen[0], k}) != Piece.NONE) {
                                allowed = true;
                                break;
                            }
                            allowed = false;
                        }
                    }
                    else if(positionOfQueen[0] != positionOfKing[0] && positionOfQueen[1] == positionOfKing[1]){
                        for(int k = Math.min(positionOfQueen[0], positionOfKing[0])+1;
                            k < Math.max(positionOfQueen[0], positionOfKing[0]); k++){
                            if(Method.getPiece(new int[]{k, positionOfQueen[1]}) != Piece.NONE) {
                                allowed = true;
                                break;
                            }
                            allowed = false;
                        }
                    }
                    if(!allowed) {
                        Square.piece.put(from, piece);
                        Square.piece.put(to, pieceInTo);
                        return false;
                    }
                    if((positionOfQueen[0] + positionOfQueen[1]) % 2 == (positionOfKing[0] + positionOfKing[1]) % 2
                            && Math.abs(positionOfKing[0]-positionOfQueen[0]) == Math.abs(positionOfKing[1]-positionOfQueen[1])){
                        if(positionOfKing[0] > positionOfQueen[0] && positionOfKing[1] > positionOfQueen[1]){
                            for (int j = 1; j < Math.max(positionOfKing[0]-positionOfQueen[0], positionOfKing[1]-positionOfQueen[1]); j++) {
                                int[] pos = new int[]{positionOfQueen[0] + j, positionOfQueen[1] + j};
                                if(positionOfQueen[0] + j >=0 && positionOfQueen[0] + j <=7 &&
                                        positionOfQueen[1] + j >=0 && positionOfQueen[1] + j <= 7) {
                                    if (Method.getPiece(pos) != Piece.NONE) {
                                        allowed = true;
                                        break;
                                    }
                                    allowed = false;
                                }
                            }
                        }
                        else if(positionOfKing[0] > positionOfQueen[0] && positionOfKing[1] < positionOfQueen[1]){
                            for (int j = 1; j < Math.max(positionOfKing[0]-positionOfQueen[0], positionOfQueen[1]-positionOfKing[1]); j++) {
                                int[] pos = new int[]{positionOfQueen[0] + j, positionOfQueen[1] - j};
                                if(positionOfQueen[0] + j >=0 && positionOfQueen[0] + j <=7 &&
                                        positionOfQueen[1] - j >=0 && positionOfQueen[1] - j <= 7) {
                                    if (Method.getPiece(pos) != Piece.NONE) {
                                        allowed = true;
                                        break;
                                    }
                                    allowed = false;
                                }
                            }
                        }
                        else if(positionOfKing[0] < positionOfQueen[0] && positionOfKing[1] > positionOfQueen[1]){
                            for (int j = 1; j < Math.max(positionOfQueen[0]-positionOfKing[0], positionOfKing[1]-positionOfQueen[1]); j++) {
                                int[] pos = new int[]{positionOfQueen[0] - j, positionOfQueen[1] + j};
                                if(positionOfQueen[0] - j >=0 && positionOfQueen[0] - j <=7 &&
                                        positionOfQueen[1] + j >=0 && positionOfQueen[1] + j <= 7) {
                                    if (Method.getPiece(pos) != Piece.NONE) {
                                        allowed = true;
                                        break;
                                    }
                                    allowed = false;
                                }
                            }
                        }
                        else if (positionOfKing[0] < positionOfQueen[0] && positionOfKing[1] < positionOfQueen[1]){
                            for (int j = 1; j < Math.max(positionOfQueen[0]-positionOfKing[0], positionOfQueen[1]-positionOfKing[1]); j++) {
                                int[] pos = new int[]{positionOfQueen[0] - j, positionOfQueen[1] - j};
                                if(positionOfQueen[0] - j >=0 && positionOfQueen[0] - j <=7 &&
                                        positionOfQueen[1] - j >=0 && positionOfQueen[1] - j <= 7) {
                                    if (Method.getPiece(pos) != Piece.NONE) {
                                        allowed = true;
                                        break;
                                    }
                                    allowed = false;
                                }
                            }
                        }
                        if(!allowed) {
                            Square.piece.put(from, piece);
                            Square.piece.put(to, pieceInTo);
                            return false;
                        }
                    }
                }
            }
            else {
                for (Square s1 : Square.values()) {
                    if (Square.piece.get(s1) == Piece.BLACK_KING) {
                        positionOfKing = Square.position.get(s1);
                        break;
                    }
                }
                if (p == Piece.WHITE_ROOK) {
                    int[] positionOfRook = Square.position.get(s);
                    if (positionOfRook[0] == positionOfKing[0] && positionOfRook[1] != positionOfKing[1]) {
                        for (int k = Math.min(positionOfRook[1], positionOfKing[1]) + 1;
                             k < Math.max(positionOfRook[1], positionOfKing[1]); k++) {
                            if (Method.getPiece(new int[]{positionOfRook[0], k}) != Piece.NONE) {
                                allowed = true;
                                break;
                            }
                            allowed = false;
                        }
                    } else if (positionOfRook[0] != positionOfKing[0] && positionOfRook[1] == positionOfKing[1]) {
                        for (int k = Math.min(positionOfRook[0], positionOfKing[0]) + 1;
                             k < Math.max(positionOfRook[0], positionOfKing[0]); k++) {
                            if (Method.getPiece(new int[]{k, positionOfRook[1]}) != Piece.NONE) {
                                allowed = true;
                                break;
                            }
                            allowed = false;
                        }
                    }
                    if (!allowed) {
                        Square.piece.put(from, piece);
                        Square.piece.put(to, pieceInTo);
                        return false;
                    }
                } else if (p == Piece.WHITE_BISHOP) {
                    int[] positionOfBishop = Square.position.get(s);
                    if ((positionOfBishop[0] + positionOfBishop[1]) % 2 == (positionOfKing[0] + positionOfKing[1]) % 2
                            && Math.abs(positionOfKing[0]-positionOfBishop[0]) == Math.abs(positionOfKing[1]-positionOfBishop[1])) {
                        if (positionOfKing[0] > positionOfBishop[0] && positionOfKing[1] > positionOfBishop[1]) {
                            for (int j = 1; j < Math.max(positionOfKing[0] - positionOfBishop[0], positionOfKing[1] - positionOfBishop[1]); j++) {
                                int[] pos = new int[]{positionOfBishop[0] + j, positionOfBishop[1] + j};
                                if (positionOfBishop[0] + j >= 0 && positionOfBishop[0] + j <= 7 &&
                                        positionOfBishop[1] + j >= 0 && positionOfBishop[1] + j <= 7) {
                                    if (Method.getPiece(pos) != Piece.NONE) {
                                        allowed = true;
                                        break;
                                    }
                                    allowed = false;
                                }
                            }
                        } else if (positionOfKing[0] > positionOfBishop[0] && positionOfKing[1] < positionOfBishop[1]) {
                            for (int j = 1; j < Math.max(positionOfKing[0] - positionOfBishop[0], positionOfBishop[1] - positionOfKing[1]); j++) {
                                int[] pos = new int[]{positionOfBishop[0] + j, positionOfBishop[1] - j};
                                if (positionOfBishop[0] + j >= 0 && positionOfBishop[0] + j <= 7 &&
                                        positionOfBishop[1] - j >= 0 && positionOfBishop[1] - j <= 7) {
                                    if (Method.getPiece(pos) != Piece.NONE) {
                                        allowed = true;
                                        break;
                                    }
                                    allowed = false;
                                }
                            }
                        } else if (positionOfKing[0] < positionOfBishop[0] && positionOfKing[1] > positionOfBishop[1]) {
                            for (int j = 1; j < Math.max(positionOfBishop[0] - positionOfKing[0], positionOfKing[1] - positionOfBishop[1]); j++) {
                                int[] pos = new int[]{positionOfBishop[0] - j, positionOfBishop[1] + j};
                                if (positionOfBishop[0] - j >= 0 && positionOfBishop[0] - j <= 7 &&
                                        positionOfBishop[1] + j >= 0 && positionOfBishop[1] + j <= 7) {
                                    if (Method.getPiece(pos) != Piece.NONE) {
                                        allowed = true;
                                        break;
                                    }
                                    allowed = false;
                                }
                            }
                        } else if (positionOfKing[0] < positionOfBishop[0] && positionOfKing[1] < positionOfBishop[1]) {
                            for (int j = 1; j < Math.max(positionOfBishop[0] - positionOfKing[0], positionOfBishop[1] - positionOfKing[1]); j++) {
                                int[] pos = new int[]{positionOfBishop[0] - j, positionOfBishop[1] - j};
                                if (positionOfBishop[0] - j >= 0 && positionOfBishop[0] - j <= 7 &&
                                        positionOfBishop[1] - j >= 0 && positionOfBishop[1] - j <= 7) {
                                    if (Method.getPiece(pos) != Piece.NONE) {
                                        allowed = true;
                                        break;
                                    }
                                    allowed = false;
                                }
                            }
                        }
                        if (!allowed) {
                            Square.piece.put(from, piece);
                            Square.piece.put(to, pieceInTo);
                            return false;
                        }
                    }
                } else if (p == Piece.WHITE_QUEEN) {
                    int[] positionOfQueen = Square.position.get(s);
                    if (positionOfQueen[0] == positionOfKing[0] && positionOfQueen[1] != positionOfKing[1]) {
                        for (int k = Math.min(positionOfQueen[1], positionOfKing[1]) + 1;
                             k < Math.max(positionOfQueen[1], positionOfKing[1]); k++) {
                            if (Method.getPiece(new int[]{positionOfQueen[0], k}) != Piece.NONE) {
                                allowed = true;
                                break;
                            }
                            allowed = false;
                        }
                    } else if (positionOfQueen[0] != positionOfKing[0]&& positionOfQueen[1] == positionOfKing[1]) {
                        for (int k = Math.min(positionOfQueen[0], positionOfKing[0]) + 1;
                             k < Math.max(positionOfQueen[0], positionOfKing[0]); k++) {
                            if (Method.getPiece(new int[]{k, positionOfQueen[1]}) != Piece.NONE) {
                                allowed = true;
                                break;
                            }
                            allowed = false;
                        }
                    }
                    if (!allowed) {
                        Square.piece.put(from, piece);
                        Square.piece.put(to, pieceInTo);
                        return false;
                    }
                    if ((positionOfQueen[0] + positionOfQueen[1]) % 2 == (positionOfKing[0] + positionOfKing[1]) % 2
                            && Math.abs(positionOfKing[0]-positionOfQueen[0]) == Math.abs(positionOfKing[1]-positionOfQueen[1])) {
                        if (positionOfKing[0] > positionOfQueen[0] && positionOfKing[1] > positionOfQueen[1]) {
                            for (int j = 1; j < Math.max(positionOfKing[0] - positionOfQueen[0], positionOfKing[1] - positionOfQueen[1]); j++) {
                                int[] pos = new int[]{positionOfQueen[0] + j, positionOfQueen[1] + j};
                                if (positionOfQueen[0] + j >= 0 && positionOfQueen[0] + j <= 7 &&
                                        positionOfQueen[1] + j >= 0 && positionOfQueen[1] + j <= 7) {
                                    if (Method.getPiece(pos) != Piece.NONE) {
                                        allowed = true;
                                        break;
                                    }
                                    allowed = false;
                                }
                            }
                        } else if (positionOfKing[0] > positionOfQueen[0] && positionOfKing[1] < positionOfQueen[1]) {
                            for (int j = 1; j < Math.max(positionOfKing[0] - positionOfQueen[0], positionOfQueen[1] - positionOfKing[1]); j++) {
                                int[] pos = new int[]{positionOfQueen[0] + j, positionOfQueen[1] - j};
                                if (positionOfQueen[0] + j >= 0 && positionOfQueen[0] + j <= 7 &&
                                        positionOfQueen[1] - j >= 0 && positionOfQueen[1] - j <= 7) {
                                    if (Method.getPiece(pos) != Piece.NONE) {
                                        allowed = true;
                                        break;
                                    }
                                    allowed = false;
                                }
                            }
                        } else if (positionOfKing[0] < positionOfQueen[0] && positionOfKing[1] > positionOfQueen[1]) {
                            for (int j = 1; j < Math.max(positionOfQueen[0] - positionOfKing[0], positionOfKing[1] - positionOfQueen[1]); j++) {
                                int[] pos = new int[]{positionOfQueen[0] - j, positionOfQueen[1] + j};
                                if (positionOfQueen[0] - j >= 0 && positionOfQueen[0] - j <= 7 &&
                                        positionOfQueen[1] + j >= 0 && positionOfQueen[1] + j <= 7) {
                                    if (Method.getPiece(pos) != Piece.NONE) {
                                        allowed = true;
                                        break;
                                    }
                                    allowed = false;
                                }
                            }
                        } else if (positionOfKing[0] < positionOfQueen[0] && positionOfKing[1] < positionOfQueen[1]){
                            for (int j = 1; j < Math.max(positionOfQueen[0] - positionOfKing[0], positionOfQueen[1] - positionOfKing[1]); j++) {
                                int[] pos = new int[]{positionOfQueen[0] - j, positionOfQueen[1] - j};
                                if (positionOfQueen[0] - j >= 0 && positionOfQueen[0] - j <= 7 &&
                                        positionOfQueen[1] - j >= 0 && positionOfQueen[1] - j <= 7) {
                                    if (Method.getPiece(pos) != Piece.NONE) {
                                        allowed = true;
                                        break;
                                    }
                                    allowed = false;
                                }
                            }
                        }
                        if (!allowed) {
                            Square.piece.put(from, piece);
                            Square.piece.put(to, pieceInTo);
                            return false;
                        }
                    }
                }
            }
        }

        Square.piece.put(from, piece);
        Square.piece.put(to, pieceInTo);
        return allowed;
    }

    /**
     * si no se puede hacer un movimiento (porque hay otra pieza entre la posición inicial y final)
     *no lo permitiría.
     * @param initial
     * initial posicion
     * @param position
     * final posicion
     * @param capture
     * indicar si el movimiento incluye captura o no.
     * @param type
     * tipo de movimiento: "horizontalmente" - "verticalmente" - "diagonalmente"
     */
    public static boolean isAllowedMove(int[] initial, int[] position, boolean capture, String type){
        boolean allowed = true;
        if(type.equals("verticalmente")){
            for (int k = Math.min(initial[0], position[0]); k <= Math.max(initial[0], position[0]); k++) {
                if (k != initial[0]) {
                    if (Method.getPiece(new int[]{k, position[1]}) != Piece.NONE) {
                        if(capture){
                            if(!Arrays.equals(new int[]{k, position[1]}, position)) {
                                allowed = false;
                                break;
                            }
                        } else {
                            allowed = false;
                            break;
                        }
                    }
                }
            }
        }
        else if(type.equals("horizontalmente")){
            for (int k = Math.min(initial[1], position[1]); k <= Math.max(initial[1], position[1]); k++) {
                if (k != initial[1]) {
                    if (Method.getPiece(new int[]{position[0], k}) != Piece.NONE) {
                        if(capture){
                            if(!Arrays.equals(new int[]{position[0], k}, position)) {
                                allowed = false;
                                break;
                            }
                        } else {
                            allowed = false;
                            break;
                        }
                    }
                }
            }
        }
        else {
            if((initial[0] + initial[1]) % 2 == (position[0] + position[1]) % 2
                    && Math.abs(position[0]-initial[0]) == Math.abs(position[1]-initial[1])){
                if(position[0] > initial[0] && position[1] > initial[1]){
                    for (int j = 1; j < Math.max(position[0]-initial[0], position[1]-initial[1]); j++) {
                        int[] pos = new int[]{initial[0] + j, initial[1] + j};
                        if(initial[0] + j >=0 && initial[0] + j <=7 &&
                                initial[1] + j >=0 && initial[1] + j <= 7) {
                            if (Method.getPiece(pos) != Piece.NONE) {
                                if(capture){
                                    if(!Arrays.equals(pos, position)) {
                                        allowed = false;
                                        break;
                                    }
                                } else {
                                    allowed = false;
                                    break;
                                }
                            }
                        }
                    }
                }
                else if(position[0] > initial[0] && position[1] < initial[1]){
                    for (int j = 1; j < Math.max(position[0]-initial[0], initial[1]-position[1]); j++) {
                        int[] pos = new int[]{initial[0] + j, initial[1] - j};
                        if(initial[0] + j >=0 && initial[0] + j <=7 &&
                                initial[1] - j >=0 && initial[1] - j <= 7) {
                            if (Method.getPiece(pos) != Piece.NONE) {
                                if(capture){
                                    if(!Arrays.equals(pos, position)) {
                                        allowed = false;
                                        break;
                                    }
                                } else {
                                    allowed = false;
                                    break;
                                }
                            }
                        }
                    }
                }
                else if(position[0] < initial[0] && position[1] > initial[1]){
                    for (int j = 1; j < Math.max(initial[0]-position[0], position[1]-initial[1]); j++) {
                        int[] pos = new int[]{initial[0] - j, initial[1] + j};
                        if(initial[0] - j >=0 && initial[0] - j <=7 &&
                                initial[1] + j >=0 && initial[1] + j <= 7) {
                            if (Method.getPiece(pos) != Piece.NONE) {
                                if(capture){
                                    if(!Arrays.equals(pos, position)) {
                                        allowed = false;
                                        break;
                                    }
                                } else {
                                    allowed = false;
                                    break;
                                }
                            }
                        }
                    }
                }
                else if(position[0] < initial[0] && position[1] < initial[1]){
                    for (int j = 1; j < Math.max(initial[0]-position[0], initial[1]-position[1]); j++) {
                        int[] pos = new int[]{initial[0] - j, initial[1] - j};
                        if(initial[0] - j >=0 && initial[0] - j <=7 &&
                                initial[1] - j >=0 && initial[1] - j <= 7) {
                            if (Method.getPiece(pos) != Piece.NONE) {
                                if(capture){
                                    if(!Arrays.equals(pos, position)) {
                                        allowed = false;
                                        break;
                                    }
                                } else {
                                    allowed = false;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return allowed;
    }

}
