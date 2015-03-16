package com.gruppe27.fellesprosjekt.client.components;

import com.gruppe27.fellesprosjekt.client.controllers.CalendarController;
import com.gruppe27.fellesprosjekt.client.events.EventBoxClicked;
import com.gruppe27.fellesprosjekt.common.Event;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import org.controlsfx.control.PopOver;

public class EventPopOver {

    private final String formatStr = "%s\nDato: %s \nTid: %s-%s \nRom: %s \nDeltakerstatus: ";
    private PopOver popOver;
    private Event event;

    public EventPopOver(EventBoxClicked e, CalendarController controller) {
        event = e.getEvent();

        ObservableList<String> participationStatusChoice = FXCollections.observableArrayList();
        participationStatusChoice.addAll("Kanskje", "Deltar", "Deltar ikke");

        Pane pane = new Pane();
        VBox box = new VBox();
        HBox hBox = new HBox();

        String str = String.format(formatStr, event.getName(), event.getDate(), event.getStartTime(),
                event.getEndTime(), event.getRoom());
        Text text = new Text(str);

        ChoiceBox<String> status = new ChoiceBox<String>(participationStatusChoice);
        String sValue = "Kanskje";
        switch (event.getStatus()) {
            case ATTENDING:
                sValue = "Deltar";
                break;
            case MAYBE:
                sValue = "Kanskje";
                break;
            case NOT_ATTENDING:
                sValue = "Deltar ikke";
                break;
        }
        status.getSelectionModel().select(participationStatusChoice.indexOf(sValue));
        Button button = new Button("Endre");
        button.setOnMouseClicked((MouseEvent mEvent) -> {
            switch (status.getValue()) {
                case "Deltar":
                    event.setStatus(Event.Status.ATTENDING);
                    break;
                case "Kanskje":
                    event.setStatus(Event.Status.MAYBE);
                    break;
                case "Deltar ikke":
                    event.setStatus(Event.Status.NOT_ATTENDING);
                    break;
            }

            controller.handleChangeParticipationStatus(event.getStatus(), event.getId());
        });

        hBox.getChildren().addAll(status, button);
        box.getChildren().addAll(text, hBox);
        if (e.getConflictingEvents().size() != 0) {
            String conflicts = "Overlappende avtaler: ";
            for (Event conflictingEvent : e.getConflictingEvents()) {
                conflicts = conflicts + conflictingEvent.getName() + " ";
            }
            Text conflictsText = new Text(conflicts);
            box.getChildren().add(conflictsText);
            conflictsText.setFill(Color.RED);
        }
        pane.getChildren().add(box);

        popOver = new PopOver(pane);
        popOver.setDetachable(false);
        popOver.show(controller.getRoot(), e.getScreenX(), e.getScreenY());
    }

    public void hide() {
        popOver.hide();
    }

    public PopOver getPopOver() {
        return popOver;
    }
}
