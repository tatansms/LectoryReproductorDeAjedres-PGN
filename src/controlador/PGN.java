package controlador;

import modelo.Board;
import modelo.Game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class PGN {
    private ArrayList<Game> games;
    private String path;
    private GameStat gstat;

    PGN(){

    }

    public PGN(String path){
        this.setPath(path);
        this.setGstat();
        games = new ArrayList<>();
        addAdvanced();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public GameStat getGstat() {
        return gstat;
    }

    public void setGstat() {
        this.gstat = new GameStat(this);
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    /**
     * toma la ruta de un archivo pgn y lee ese archivo y devuelve cada etiqueta y movimientos de un juego
     * en una Arraylist de cadenas.
     */
    private ArrayList<String> read(String path){
        File f = new File(path);
        ArrayList<String> lines = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            long countEmpty = 0L;
            StringBuilder gameLine = new StringBuilder();
            while((line = br.readLine()) != null){
                if(line.equals("")) {
                    countEmpty++;
                    continue;
                }
                if (line.contains("["))
                    lines.add(line);
                if(countEmpty % 2 == 1) {
                    gameLine = (gameLine == null ? new StringBuilder() : gameLine).append(line);
                    gameLine.append(" ");
                }
                else if(countEmpty > 1) {
                    if (gameLine != null) {
                        lines.add(gameLine.toString());
                        gameLine = null;
                        lines.add("fin del juego");
                    }
                }
            }
            if (gameLine != null) {
                lines.add(gameLine.toString());
                lines.add("fin del juego");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lines;
    }

    /**
     * preparar un objeto modelo.Game y agregarlo a los juegos.
     */
    void addAdvanced(){
        ArrayList<String> lines = read(getPath());
        String event = null, site = null, date = null, round = null, whitePlayer = null,
                blackPlayer = null, result = null, whiteTitle = null, blackTitle = null, whiteElo = null,
                blackElo = null, eco = null, opening = null, variation = null, whiteFideId = null,
                blackFideId = null, eventDate = null, termination = null, strMovesText = null;
        String[] movesArray = new String[0];
        for(String line: lines){
            if(line.contains("[Fecha del evento ")) {
                eventDate = line.replaceAll("\\[Fecha del evento \"", "");
                eventDate = eventDate.replaceAll("\"]", "");
            }
            else if(line.contains("[Evento ")) {
                event = line.replaceAll("\\[Evento \"", "");
                event = event.replaceAll("\"]", "");
            }
            else if(line.contains("[Sitio ")) {
                site = line.replaceAll("\\[Sitio \"", "");
                site = site.replaceAll("\"]", "");
            }
            else if(line.contains("[Fecha ")) {
                date = line.replaceAll("\\[Fecha \"", "");
                date = date.replaceAll("\"]", "");
            }
            else if(line.contains("[Round ")) {
                round = line.replaceAll("\\[Round \"", "");
                round = round.replaceAll("\"]", "");
            }
            else if(line.contains("[WhiteElo ")) {
                whiteElo = line.replaceAll("\\[WhiteElo \"", "");
                whiteElo = whiteElo.replaceAll("\"]", "");
            }
            else if(line.contains("[BlackElo ")) {
                blackElo = line.replaceAll("\\[BlackElo \"", "");
                blackElo = blackElo.replaceAll("\"]", "");
            }
            else if(line.contains("[WhiteTitle ")) {
                whiteTitle = line.replaceAll("\\[WhiteTitle \"", "");
                whiteTitle = whiteTitle.replaceAll("\"]", "");
            }
            else if(line.contains("[BlackTitle ")) {
                blackTitle = line.replaceAll("\\[BlackTitle \"", "");
                blackTitle = blackTitle.replaceAll("\"]", "");
            }
            else if(line.contains("[WhiteFideId ")) {
                whiteFideId = line.replaceAll("\\[WhiteFideId \"", "");
                whiteFideId = whiteFideId.replaceAll("\"]", "");
            }
            else if(line.contains("[BlackFideId ")) {
                blackFideId = line.replaceAll("\\[BlackFideId \"", "");
                blackFideId = blackFideId.replaceAll("\"]", "");
            }
            else if(line.contains("[Blanca ")) {
                whitePlayer = line.replaceAll("\\[Blanca \"", "");
                whitePlayer = whitePlayer.replaceAll("\"]", "");
            }
            else if(line.contains("[Negra ")) {
                blackPlayer = line.replaceAll("\\[Negra \"", "");
                blackPlayer = blackPlayer.replaceAll("\"]", "");
            }
            else if(line.contains("[Opening ")) {
                opening = line.replaceAll("\\[Opening \"", "");
                opening = opening.replaceAll("\"]", "");
            }
            else if(line.contains("[Result ")) {
                result = line.replaceAll("\\[Result \"", "");
                result = result.replaceAll("\"]", "");
            }
            else if(line.contains("[ECO ")) {
                eco = line.replaceAll("\\[ECO \"", "");
                eco = eco.replaceAll("\"]", "");
            }
            else if(line.contains("[Variation ")) {
                variation = line.replaceAll("\\[Variation \"", "");
                variation = variation.replaceAll("\"]", "");
            }
            else if(line.contains("[Termination ")) {
                termination = line.replaceAll("\\[Termination \"", "");
                termination = termination.replaceAll("\"]", "");
            }
            else if(line.contains("Fin del juego")){
                add(new Game(event, site, date, round, whitePlayer, blackPlayer, result,
                        whiteTitle, blackTitle, whiteElo, blackElo, eco, opening, variation,
                        whiteFideId, blackFideId, eventDate, termination, new Board(), strMovesText, movesArray));
            }
            else {
                strMovesText = line;
                strMovesText = strMovesText.replaceAll(" e.p.", "");
                movesArray = preparingMoves(line);
            }
        }
   }

    /**
     * agregar un objeto modelo.Game a los juegos.
     * */
    void add(Game g){
        games.add(g);
    }

    /**
     * eliminar un juego de los juegos.
     * @param g
     */
    void remove(Game g) {
       games.remove(g);
   }

    /**
     * eliminar notaciones innecesarias en las cadenas de movimientos y luego separar cada movimiento en una cadena.
     **/
    String[] preparingMoves(String movesLine){
        movesLine = movesLine.replaceAll("\\?", "");
        movesLine = movesLine.replaceAll("!", "");
        movesLine = movesLine.replaceAll("#", "");
        movesLine = movesLine.replaceAll("\\+", "");
        movesLine = movesLine.replaceAll("1-0", "");
        movesLine = movesLine.replaceAll("0-1", "");
        movesLine = movesLine.replaceAll("1/2-1/2", "");
        movesLine = movesLine.replaceAll("\\*", "");
        movesLine = movesLine.replaceAll(" e.p.", "");

        String[] movesArray = movesLine.split(" ");
        for (int i = 0; i < movesArray.length; i++) {
            if (movesLine.charAt(2) == ' ') {
                if (i % 3 == 0)
                    movesArray[i] = null;
            } else {
                if (i % 2 == 0)
                    movesArray[i] = movesArray[i].substring(movesArray[i].indexOf('.') + 1);
            }
        }
        movesArray = Arrays.stream(movesArray).filter(Objects::nonNull).toArray(String[]::new);

        return movesArray;
    }
}
