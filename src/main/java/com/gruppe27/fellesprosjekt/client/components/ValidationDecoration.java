package com.gruppe27.fellesprosjekt.client.components;

import javafx.scene.control.Control;
import org.controlsfx.control.decoration.Decoration;
import org.controlsfx.validation.decoration.GraphicValidationDecoration;

import java.util.ArrayList;
import java.util.Collection;

public class ValidationDecoration extends GraphicValidationDecoration {

    public ValidationDecoration() {
    }

    @Override
    protected Collection<Decoration> createRequiredDecorations(Control target) {
        return new ArrayList<>();
    }

        /*
        @Override
        protected Node createErrorNode() {
            return super.createErrorNode();
        }

        @Override
        protected Node createWarningNode() {
            Node n = super.createWarningNode();
            n.setScaleX(1.5);
            return n;
        }
        */
}
