package tn.esprit.examenspring.entities;

public enum Emoji {
    THUMBS_UP("\uD83D\uDC4D"),
    SMILE("\uD83D\uDE04"),
    HEART("\u2764\uFE0F");
    // Add more emojis as needed

    private final String symbol;

    Emoji(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

}
