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
import javafx.scene.text.Text;
import org.controlsfx.control.PopOver;

public class EventPopOver {

    private final String formatStr = "%s\nDato: %s \nTid: %s-%s \nRom: %s \nDeltakerstatus: ";
    private PopOver popOver;
    private Event event;
    private ObservableList<String> participationStatusChoice;

    public EventPopOver(EventBoxClicked e, CalendarController controller) {
        event = e.getEvent();
        participationStatusChoice = FXCollections.observableArrayList();
        participationStatusChoice.addAll("Kanskje", "Deltar", "Deltar ikke");

        Pane pane = new Pane();
        VBox box = new VBox();
        HBox hBox = new HBox();

        String str = String.format(formatStr, event.getName(), event.getDate(), event.getStartTime(),
                event.getEndTime(), event.getRoom(), "PLACEHOLDER");
        Text text = new Text(str);

        ChoiceBox<String> status = new ChoiceBox<String>(participationStatusChoice);
        status.getSelectionModel().selectFirst();
        Button button = new Button("Endre");
        button.setOnMouseClicked((MouseEvent mEvent) -> {

            controller.handleChangeParticipationStatus(status.getValue(), event.getId());
        });

        hBox.getChildren().addAll(status, button);
        box.getChildren().addAll(text, hBox);
        pane.getChildren().add(box);

        popOver = new PopOver(pane);
        popOver.setDetachable(false);
        popOver.show(controller.getRoot(),e.getScreenX(), e.getScreenY());
    }

    public void hide() {
        popOver.hide();
    }

    public PopOver getPopOver() {
        return popOver;
    }
}
