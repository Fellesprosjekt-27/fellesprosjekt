package com.gruppe27.fellesprosjekt.client.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class ParticipantComboBox extends ComboBox<String>{
    
    private ObservableList<String> allUserNames;
    private ObservableList<String> bufferList = FXCollections.observableArrayList();
    private String previousValue = "";
    
    public void init(ObservableList<String> usernames){
        this.setItems(usernames);
        allUserNames = usernames;
        this.configAutoFilterListener();
    }
    
    private void configAutoFilterListener(){
        final ParticipantComboBox currentInstance = this;
        this.getEditor().textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                previousValue = oldValue;
                TextField editor = currentInstance.getEditor();
                String selected = currentInstance.getSelectionModel().getSelectedItem();

                if(selected == null || selected.equals(editor.getText())){
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
        if(filter.startsWith(previousValue) && !previousValue.isEmpty()){
            ObservableList<String> filteredList = this.getFilteredList(filter, bufferList);
            bufferList.clear();
            bufferList = filteredList;
        }else{
            bufferList = this.getFilteredList(filter, allUserNames);
        }
        comboBox.setItems(bufferList);
    }

    private ObservableList<String> getFilteredList(String filter, ObservableList<String> originalList) {
        ObservableList<String> filteredList = FXCollections.observableArrayList();
        for (String username : originalList) {
            if(username.toLowerCase().startsWith(filter.toLowerCase())){
                filteredList.add(username);
            }
        }
        return filteredList;
    }
    
    private void setUserInputToOnlyOption(ParticipantComboBox currentInstance, final TextField editor) {
        String onlyOption = currentInstance.getItems().get(0);
        String currentText = editor.getText();
        if(onlyOption.length() > currentText.length()){
            editor.setText(onlyOption);
        }
    }

}
