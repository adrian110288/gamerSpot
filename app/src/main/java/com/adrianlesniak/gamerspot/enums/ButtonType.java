package com.adrianlesniak.gamerspot.enums;

/**
 * Created by Adrian on 12-Jul-14.
 */
public enum  ButtonType {

    ARTICLE(0),
    SEARCH(1),
    DISMISS(2),
    EMAIL(3),
    PLAIN(4);

    private int type;

    private ButtonType(int typeIn) {
        this.type = typeIn;
    }

    public int getType() {
        return this.type;
    }
}
