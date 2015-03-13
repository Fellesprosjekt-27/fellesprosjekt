package com.gruppe27.fellesprosjekt.client.controllers;

import com.gruppe27.fellesprosjekt.client.SortableText;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.util.HashMap;
import java.util.Map;

public class ParticipantComboBox extends ComboBox<SortableText> {

    private ObservableList<String> allUserNameStrings;
    private Map<String, SortableText> stringTextMap;
    private ObservableList<String> bufferList = FXCollections.observableArrayList();
    private String previousValue = "";

    public ParticipantComboBox() {
        this.setConverter(new StringConverter<SortableText>() {
            @Override
            public String toString(SortableText object) {
                if (object == null) {
                    return null;
                } else {
                    return object.getText();
                }
            }

            @Override
            public SortableText fromString(String string) {
                if (string == null) {
                    return null;
                } else {
                    return stringTextMap.get(string);
                }
            }
        });
    }

    public void init(ObservableList<SortableText> usernameTexts) {
        this.setItems(usernameTexts);
        stringTextMap = new HashMap<>();
        allUserNameStrings = FXCollections.observableArrayList();

        for (SortableText text : usernameTexts) {
            stringTextMap.put(text.getText(), text);
            allUserNameStrings.add(text.getText());
        }
        this.configAutoFilterListener();
    }

    private void configAutoFilterListener() {
        final ParticipantComboBox currentInstance = this;
        this.getEditor().textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                previousValue = oldValue;
                TextField editor = currentInstance.getEditor();
                Text selected = currentInstance.getSelectionModel().getSelectedItem();

                if (selected == null || !selected.getText().equals(editor.getText())) {
                    filterItems(newValue, currentInstance);
                    currentInstance.show();
//                    if(currentInstance.getItems().size() == 1){
//                        setUserInputToOnlyOption(currentInstance, editor);
//                    }
                }
            }
        });
    }

    private void filterItems(String filter, ParticipantComboBox comboBox) {
        if (filter.startsWith(previousValue) && !previousValue.isEmpty()) {
            ObservableList<String> filteredList = this.getFilteredList(filter, bufferList);
            bufferList.clear();
            bufferList = filteredList;
        } else {
            bufferList = this.getFilteredList(filter, allUserNameStrings);
        }

        comboBox.setItems(getTextBufferList(bufferList));
    }

    private ObservableList<SortableText> getTextBufferList(ObservableList<String> bufferList) {
        ObservableList<SortableText> textBufferList = FXCollections.observableArrayList();
        for (String username : bufferList) {
            SortableText text = stringTextMap.get(username);
            textBufferList.add(text);
        }
        return textBufferList;
    }

    private ObservableList<String> getFilteredList(String filter, ObservableList<String> originalList) {
        ObservableList<String> filteredList = FXCollections.observableArrayList();
        for (String username : originalList) {
            if (username.toLowerCase().startsWith(filter.toLowerCase())) {
                filteredList.add(username);
            }
        }
        return filteredList;
    }

    private void setUserInputToOnlyOption(ParticipantComboBox currentInstance, final TextField editor) {
        String onlyOption = currentInstance.getItems().get(0).getText();
        String currentText = editor.getText();
        if (onlyOption.length() > currentText.length()) {
            editor.setText(onlyOption);
        }
    }

}
