package com.gruppe27.fellesprosjekt.client;

import javafx.scene.text.Text;

public class SortableText extends Text implements Comparable {


    public SortableText(String username) {
        super(username);
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof SortableText) {
            String you = ((SortableText) o).getText();
            String me = this.getText();

            return me.compareTo(you);

        } else {
            throw new IllegalArgumentException("Cannot compare Text to non-Text.");
        }
    }
}
