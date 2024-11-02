package controlador;

import modelo.Game;
import modelo.Move;

import java.util.*;
import java.util.stream.Collectors;

public class GameStat {
    private PGN pgn;

    GameStat(PGN pgn){
        this.setPgn(pgn);
    }

    GameStat(){

    }

    public PGN getPgn() {
        return pgn;
    }

    public void setPgn(PGN pgn) {
        this.pgn = pgn;
    }

    /**
     * toma el nombre de un jugador y devuelve los juegos de ese jugador.
     */
    ArrayList<Game> getGames(String playerName){
        ArrayList<Game> result = new ArrayList<>();
        for(Game g: getPgn().getGames()){
            if(g.getWhitePlayer().equals(playerName) || g.getBlackPlayer().equals(playerName))
                result.add(g);
        }
        return result;
    }

    /**
     * toma el nombre de un jugador y devuelve la proporción del número de juegos
     * que el jugador ganó y los que perdieron.
     */
    public double getWinLoseRatio(String playerName){
        ArrayList<Game> games = getGames(playerName);
        double win = 0, lose = 0;
        for(Game g: games){
            if(g.getWhitePlayer().equals(playerName)){
                if(g.getResult().length() == 3) {
                    if (g.getResult().charAt(0) == '1')
                        win++;
                    else
                        lose++;
                }
            }
            else if(g.getBlackPlayer().equals(playerName)){
                if(g.getResult().length() == 3) {
                    if (g.getResult().charAt(2) == '1')
                        win++;
                    else
                        lose++;
                }
            }
        }
        if(lose != 0)
            return win/lose;
        else
            return win;
    }

    /**
     * devuelve n movimientos de los movimientos más utilizados en todos los juegos del archivo. devuelve una lista de matrices del objeto modelo.Move.
     * si general es verdadero, devuelve los movimientos más utilizados excluyendo el acto de captura. Af5 y Axf5 serían lo mismo.
     * si general es falso. Devuelve los movimientos más utilizados, incluido el acto de captura. Af5 y Axf5 ahora son diferentes.
     */
    public ArrayList<Move> mostUsedMoves(int n, boolean general){
        ArrayList<Move> allMoves = new ArrayList<>();
        for(Game g: getPgn().getGames()){
            allMoves.addAll(g.board.getMoves());
        }
        if(general)
            return customSortV2(allMoves, n);
        return customSort(allMoves, n);
    }

    /**
     * devuelve n movimientos de los movimientos más utilizados en todos los juegos del archivo.
     * devuelve una lista de matrices de representación de cadena del movimiento.
     * debido a que este método no requiere la conversión de movimiento de cadena a objeto modelo.Move, se ejecuta mucho más rápido.
     * por lo que este método se utilizará en toString.
     */
    public ArrayList<String> mostUsedMovesString(int n){
        ArrayList<String> allMoves = new ArrayList<>();
        for(Game g: getPgn().getGames()){
            allMoves.addAll(Arrays.asList(g.getStringMovesArray()));
        }
        return customSortV3(allMoves, n);
    }

    /**
     * Número de retorno del juego que comienza con esa apertura.
     */
    public int numberOfGamesWithOpening(String opening){
        int count = 0;
        for(Game g: getPgn().getGames()){
            if(g.getOpening().equals(opening))
                count++;
        }
        return count;
    }

    /**
     * Devuelve el porcentaje de juegos ganadores con cierta apertura.
     */
    public double openingWinRate(String opening){
        double win = 0, count = 0;
        for(Game g: getPgn().getGames()){
            if(g.getOpening().equals(opening)) {
                count++;
                if(g.getResult().length() == 3 && g.getResult().charAt(0) == '1')
                    win++;
            }
        }
        return (win/count)*100;
    }

    /**
     * devuelve el número medio de movimientos que tienen lugar antes de la primera captura en todos los juegos.     */
    public double expectedMovesBeforeFirstCapture(){
        double sum = 0, count = 0;
        for(Game g: getPgn().getGames()){
            for(int i = 0; i < g.getStringMovesArray().length; i++){
                if(g.getStringMovesArray()[i].contains("x")) {
                    sum += i;
                    count++;
                    break;
                }
            }
        }
        return sum/count;
    }

    /**
     * devuelve el porcentaje de juegos ganadores que comienzan con un determinado movimiento (primer movimiento de las blancas o primer movimiento de las negras).
     *se necesita un modelo.Mover objeto.
     */
    public double getWinRate(Move m){
        double win = 0, count = 0;
        for(Game g: getPgn().getGames()) {
            if(g.board.getMoves().size() > 2) {
                if (m.equals(g.board.getMoves().get(0))) {
                    count++;
                    if (g.getResult().length() == 3 && g.getResult().charAt(0) == '1')
                        win++;
                } else if (m.equals(g.board.getMoves().get(1))) {
                    count++;
                    if (g.getResult().length() == 3 && g.getResult().charAt(2) == '1')
                        win++;
                }
            }
        }
        if(count != 0)
            return (win/count)*100;
        else
            return 0;
    }

    /**
     * devuelve el porcentaje de juegos ganadores que comienzan con cierto movimiento (primer movimiento de las blancas o primer movimiento de las negras).
     * se necesita una representación de cadena del movimiento.
     */
    public double getWinRateString(String m){
        double win = 0, count = 0;
        for(Game g: getPgn().getGames()) {
            if(g.getStringMovesArray().length > 2) {
                if (m.equals(g.getStringMovesArray()[0])) {
                    count++;
                    if (g.getResult().length() == 3 && g.getResult().charAt(0) == '1')
                        win++;
                } else if (m.equals(g.getStringMovesArray()[1])) {
                    count++;
                    if (g.getResult().length() == 3 && g.getResult().charAt(2) == '1')
                        win++;
                }
            }
        }
        if(count != 0)
            return (win/count)*100;
        else
            return 0;
    }

    /**
     * dados algunos movimientos y predecir n movimientos más probables para el próximo movimiento.
     * se necesita una lista de matrices del modelo.Mover objeto
     */
    public ArrayList<Move> nextMoves(ArrayList<Move> moves, int n){
        ArrayList<Move> expected = new ArrayList<>();
        Collections.reverse(moves);
        for(Game g: getPgn().getGames()) {
            ArrayList<Move> gMove = g.board.getMoves();
            for (int i = 0; i < gMove.size() - 1; i++) {
                if (gMove.get(i).equals(moves.get(0))) {
                    expected.add(gMove.get(i + 1));
                    if (i > 0 && i < moves.size()) {
                        for (int j = 1; j < Math.min(n, i + 1); j++) {
                            if (gMove.get(i - j).equals(moves.get(j))) {
                                expected.add(gMove.get(i + 1));
                            }
                            else
                                break;
                        }
                    }
                }
            }
        }
        return customSort(expected, n);
    }

    /**
     * dados algunos movimientos y predecir n movimientos más probables para el próximo movimiento.
     * se necesita una lista de matrices de representación de cadena del movimiento.
     */
    public ArrayList<String> nextMovesString(ArrayList<String> moves, int n){
        Collections.reverse(moves);
        ArrayList<String> expected = new ArrayList<>();
        for(Game g: getPgn().getGames()) {
            String[] gMove = g.getStringMovesArray();
            for (int i = 0; i < gMove.length - 1; i++) {
                if (gMove[i].equals(moves.get(0))) {
                    expected.add(gMove[i + 1]);
                    if (i > 0 && i < moves.size()) {
                        for (int j = 1; j < Math.min(n, i + 1); j++) {
                            if (gMove[i - j].equals(moves.get(j))) {
                                expected.add(gMove[i + 1]);
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
        }
        return customSortV3(expected, n);
    }

    /**
     * devuelve el siguiente movimiento probable en un juego dados todos los movimientos de ese juego al movimiento actual.
     * requiere conversión de movimiento de cadena a modelo.Move objeto.
     */
    public ArrayList<Move> nextMovesInGame(int indexOfGame, int indexOfMove, int n) {
        if(indexOfMove == -1){
            ArrayList<Move> allFirstMoves = new ArrayList<>();
            for(Game g: getPgn().getGames()){
                if(g.board.getMoves().size() > 0)
                    allFirstMoves.add(g.board.getMoves().get(0));
            }
            return customSort(allFirstMoves, n);
        } else {
            Game game = pgn.getGames().get(indexOfGame);
            ArrayList<Move> moves = new ArrayList<>();
            for (int i = 0; i <= indexOfMove; i++)
                moves.add(game.board.getMoves().get(i));
            return nextMoves(moves, n);
        }
    }

    /**
     * devuelve el siguiente movimiento probable en un juego dados todos los movimientos de ese juego al movimiento actual.
     * No requiere conversión de movimiento de cadena a modelo.Move objeto. por lo que corre mucho más rápido.
     * entonces esto se usaría en toString.
     */
    public ArrayList<String> nextMovesInGameString(int indexOfGame, int indexOfMove, int n) {
        if(indexOfMove == -1){
            ArrayList<String> allFirstMoves = new ArrayList<>();
            for(Game g: getPgn().getGames()){
                if(g.getStringMovesArray().length > 0)
                    allFirstMoves.add(g.getStringMovesArray()[0]);
            }
            return customSortV3(allFirstMoves, n);
        } else {
            Game game = pgn.getGames().get(indexOfGame);
            ArrayList<String> moves = new ArrayList<>();
            for (int i = 0; i <= indexOfMove; i++)
                moves.add(game.getStringMovesArray()[i]);
            return nextMovesString(moves, n);
        }
    }

    /**
     * ordena una lista de matrices de modelo.Move Object según la frecuencia y devuelve el primer n movimiento. comparar movimientos "incluye" el acto de captura.
     */
    private ArrayList<Move> customSort(ArrayList<Move> moves, int n) {
        Map<String, Data> hm = new HashMap<>();
        for (int i = 0; i < moves.size(); i++) {
            hm.putIfAbsent(moves.get(i).toString(), new Data(moves.get(i), 0, i));
            hm.get(moves.get(i).toString()).count++;
        }
        List<Data> values = hm.values().stream()
                .sorted()
                .collect(Collectors.toList());

        ArrayList<Move> result = new ArrayList<>();
        for (Data data: values) {
            if(n > 0)
                result.add(data.move);
            else
                break;
            n--;
        }
        return result;
    }

    /**
     * ordena una lista de matrices de modelo.Move Object según la frecuencia y devuelve el primer n movimiento. comparar movimientos "excluye" el acto de captura.     */
    private ArrayList<Move> customSortV2(ArrayList<Move> moves, int n) {
        Map<String, Data> hm = new HashMap<>();
        for (int i = 0; i < moves.size(); i++) {
            hm.putIfAbsent(moves.get(i).toStringV2(), new Data(moves.get(i), 0, i));
            hm.get(moves.get(i).toStringV2()).count++;
        }
        List<Data> values = hm.values().stream()
                .sorted()
                .collect(Collectors.toList());

        ArrayList<Move> result = new ArrayList<>();
        for (Data data: values) {
            if(n > 0)
                result.add(data.move);
            else
                break;
            n--;
        }
        return result;
    }

    /**
     * ordena una lista de matrices de cadenas de movimiento según la frecuencia y devuelve los primeros n movimientos. comparar movimientos "excluye" el acto de captura.     */
    private ArrayList<String> customSortV3(ArrayList<String> moves, int n) {
        Map<String, DataV2> hm = new HashMap<>();
        for (int i = 0; i < moves.size(); i++) {
            hm.putIfAbsent(moves.get(i), new DataV2(moves.get(i), 0, i));
            hm.get(moves.get(i)).count++;
        }
        List<DataV2> values = hm.values().stream()
                .sorted()
                .collect(Collectors.toList());
        ArrayList<String> result = new ArrayList<>();
        for (DataV2 dataV2: values) {
            if(n > 0)
                result.add(dataV2.move);
            else
                break;
            n--;
        }
        return result;
    }

    public String toString(int indexOfGame, int indexOfMove, boolean moveStat){
        StringBuilder result = new StringBuilder();
        Game game = pgn.getGames().get(indexOfGame);
        String whitePlayer = game.getWhitePlayer();
        String blackPlayer = game.getBlackPlayer();
        String opening = game.getOpening();
        ArrayList<String> moves;

        if(whitePlayer != null)
            result.append(String.format("No. de %s's Juegos:  %d\n", whitePlayer, getGames(whitePlayer).size()));
        if(blackPlayer != null)
            result.append(String.format("No. de %s's Juegos:  %d\n", blackPlayer, getGames(blackPlayer).size()));
        if(whitePlayer != null)
            result.append(String.format("Proporción de victorias y pérdidas de %s:  %.2f\n", whitePlayer, getWinLoseRatio(whitePlayer)));
        if(blackPlayer != null)
            result.append(String.format("Proporción de victorias y pérdidas de %s:  %.2f\n", blackPlayer, getWinLoseRatio(blackPlayer)));
        if(opening != null) {
            result.append(String.format("No. de juegos con %s apertura:  %d\n", opening, numberOfGamesWithOpening(opening)));
            result.append(String.format("Tasa de ganar con %s:  %.2f%%\n", opening, openingWinRate(opening)));
        }
        if(game.getResult() != null) {
            result.append(String.format("Tasa de victorias de las blancas con este primer modelo.:  %.2f%%\n", getWinRateString(game.getStringMovesArray()[0])));
            result.append(String.format("Tasa de victorias de las negras con este primer modelo.:  %.2f%%\n", getWinRateString(game.getStringMovesArray()[1])));
        }
        result.append(String.format("Movimientos esperados antes de la primera captura.:  %.2f\n", expectedMovesBeforeFirstCapture()));

        if(moveStat) {
            result.append("Los 5 movimientos más utilizados en todos los juegos:\n");
            moves = mostUsedMovesString(5);
            for (String m : moves)
                result.append(m).append("\n");

            if(indexOfMove < game.getStringMovesArray().length-1) {
                result.append("Top 5 Siguientes movimientos más probables del juego:\n");
                moves = nextMovesInGameString(indexOfGame, indexOfMove, 5);
                for (String m : moves)
                    result.append(m).append("\n");
            }
        }

        if(result.toString().contains("\n")) {
            int lastNextLine = result.lastIndexOf("\n");
            return result.delete(lastNextLine, lastNextLine + 2).toString();
        } else {
            return result.toString();
        }
    }

    private static class Data implements Comparable<Data> {
        Move move;
        int count, index;

        public Data(Move move, int count, int index) {
            this.move = move;
            this.count = count;
            this.index = index;
        }

        @Override
        public int compareTo(Data obj) {
            if (this.count != obj.count) {
                return obj.count - this.count;
            }
            return this.index - obj.index;
        }
    }

    private static class DataV2 implements Comparable<DataV2> {
        String move;
        int count, index;

        public DataV2(String move, int count, int index) {
            this.move = move;
            this.count = count;
            this.index = index;
        }

        @Override
        public int compareTo(DataV2 obj) {
            if (this.count != obj.count) {
                return obj.count - this.count;
            }
            return this.index - obj.index;
        }
    }

}
