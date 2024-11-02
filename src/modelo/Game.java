package modelo;

import java.util.LinkedHashMap;

public class Game{
    public Board board;
    private final LinkedHashMap<String, String> tags;
    private String[] stringMovesArray;
    private String strMovesText;

    public Game(String event, String site, String date, String round, String whitePlayer, String blackPlayer,
                String result, String whiteTitle, String blackTitle, String whiteElo, String blackElo, String eco,
                String opening, String variation, String whiteFideId, String blackFideId, String eventDate,
                String termination, Board board, String strMovesText, String[] stringMovesArray){
        tags = new LinkedHashMap<>();
        this.setEvent(event);
        this.setSite(site);
        this.setDate(date);
        this.setRound(round);
        this.setWhitePlayer(whitePlayer);
        this.setBlackPlayer(blackPlayer);
        this.setResult(result);
        this.setWhiteTitle(whiteTitle);
        this.setBlackTitle(blackTitle);
        this.setWhiteElo(whiteElo);
        this.setBlackElo(blackElo);
        this.setEco(eco);
        this.setOpening(opening);
        this.setVariation(variation);
        this.setWhiteFideId(whiteFideId);
        this.setBlackFideId(blackFideId);
        this.setEventDate(eventDate);
        this.setTermination(termination);
        this.setStringMovesArray(stringMovesArray);
        //line 39 would not convert moves for board. we will do that later in Chess class.
        //because this way, we optimise program and it runs faster; we only need to convert
        //moves of one game: the game that player choose to see.
        //but if we use line 40, program would convert moves of all games.
        //if you want to use toString method of modelo.Board class, or run classes without gui program;
        //you may need to uncomment line 40 and comment line 39.
        this.board = board;
        //this.board = new modelo.Board(modelo.Converter.convertMoves(getStringMovesArray()));
        this.setStrMovesText(strMovesText);
    }

    public void setEvent(String event) {
        tags.put("Evento", event);
    }

    public void setSite(String site) {
        tags.put("Sitio", site);
    }

    public void setDate(String date) {
        tags.put("Fecha", date);
    }

    public void setRound(String round) {
        tags.put("Round", round);
    }

    public void setWhiteElo(String whiteElo) {
        tags.put("Blanca Elo", whiteElo);
    }

    public void setBlackElo(String blackElo) {
        tags.put("Negra Elo", blackElo);
    }

    public void setEco(String eco) {
        tags.put("ECO", eco);
    }

    public String getWhitePlayer() {
        return tags.get("Blanca");
    }

    public void setWhitePlayer(String whitePlayer) {
        tags.put("Blanca", whitePlayer);
    }

    public String getBlackPlayer() {
        return tags.get("Negra");
    }

    public void setBlackPlayer(String blackPlayer) {
        tags.put("Negra", blackPlayer);
    }

    public String getOpening() {
        return tags.get("Opening");
    }

    public void setOpening(String opening) {
        tags.put("Opening", opening);
    }

    public String getResult() {
        return tags.get("Resultado");
    }

    public void setResult(String result) {
        tags.put("Resultado", result);
    }

    public void setWhiteTitle(String whiteTitle) {
        tags.put("Título blanca", whiteTitle);
    }

    public void setBlackTitle(String blackTitle) {
        tags.put("Título negra", blackTitle);
    }

    public void setWhiteFideId(String whiteFideId) {
        tags.put("Fide blanca Id", whiteFideId);
    }

    public void setBlackFideId(String blackFideId) {
        tags.put("Fide negra Id", blackFideId);
    }

    public void setVariation(String variation) {
        tags.put("Variacion", variation);
    }

    public void setEventDate(String eventDate) {
        tags.put("Fecha de evento", eventDate);
    }

    public void setTermination(String termination) {
        tags.put("Terminacion", termination);
    }

    public String getStrMovesText() {
        return strMovesText;
    }

    public void setStrMovesText(String strMovesText) {
        this.strMovesText = strMovesText;
    }

    public String[] getStringMovesArray() {
        return stringMovesArray;
    }

    public void setStringMovesArray(String[] stringMovesArray) {
        this.stringMovesArray = stringMovesArray;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for(String tag: tags.keySet()){
            if(tags.get(tag) != null)
                result.append(tag).append(":   ").append(tags.get(tag)).append("\n");
        }
        if(result.toString().contains("\n")) {
            int lastNextLine = result.lastIndexOf("\n");
            return result.delete(lastNextLine, lastNextLine + 2).toString();
        } else {
            return result.toString();
        }
    }
}
