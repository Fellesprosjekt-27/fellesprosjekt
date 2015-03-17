package com.gruppe27.fellesprosjekt.client.components;

import com.gruppe27.fellesprosjekt.client.controllers.CalendarController;
import com.gruppe27.fellesprosjekt.client.events.EventBoxClicked;
import com.gruppe27.fellesprosjekt.common.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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

        ChoiceBox<String> status = new ChoiceBox<String>(participationStatusChoice);
        String sValue = event.statusToString();

        String str = String.format(formatStr, event.getName(), event.getDate(), event.getStartTime(),
                event.getEndTime(), event.getRoom().getRoomName());
        Text text = new Text(str);

        status.getSelectionModel().select(participationStatusChoice.indexOf(sValue));
        Button button = new Button("Endre");
        button.setOnAction((ActionEvent changeStatusEvent) -> {
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

            controller.handleChangeParticipationStatus(event);
        });
        Button editEventButton = new Button("Vis Info/Endre Avtale");
        editEventButton.setOnAction((ActionEvent editEventEvent) -> {
            controller.editEvent(event);
        });


        hBox.getChildren().addAll(status, button);



        box.getChildren().addAll(text, hBox);
        if(event.getCreator().getUsername().equals(controller.getConnectedUser().getUsername())){
            box.getChildren().add(editEventButton);
        }
        if (e.getConflictingEvents().size() != 0) {
            String conflicts = "Overlappende avtaler: ";
            for (Event conflictingEvent : e.getConflictingEvents()) {
                conflicts = conflicts + "\n" + conflictingEvent.getName();
            }
            Text conflictsText = new Text(conflicts);
            box.getChildren().add(conflictsText);
            conflictsText.setFill(Color.RED);
        }
        pane.getChildren().add(box);

        box.setPadding(new Insets(10));
        box.setSpacing(6);
        hBox.setSpacing(5);
        text.setLineSpacing(2);

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
