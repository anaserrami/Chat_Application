package err.anas.chatyou.presentation.controllers;

import err.anas.chatyou.dao.entities.User;
import err.anas.chatyou.presentation.AppNavigator;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ChatController {

    @FXML private TextField messageField;
    @FXML private ListView<String> chatListView;
    private User selectedUser,authenticatedUser;
    private ObservableList<String> chatMessages;
    final  String sender="Me";
    private boolean shouldContinueListening = true;

    @FXML
    private void initialize() {
        shouldContinueListening=true;
        chatMessages = FXCollections.observableArrayList();
        chatListView.setItems(chatMessages);
        // Set a custom cell factory to control the appearance of each item in the ListView
        chatListView.setCellFactory(list -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    System.out.println();
                    String [] split=item.split(":");
                    String userName=split[0];
                    String message=split[1];

                    setText(message);

                    if (userName.startsWith("Me")) {

                        setStyle("-fx-text-fill: green; -fx-font-size: 18; -fx-alignment: CENTER-RIGHT;");

                    } else {
                        setStyle("-fx-text-fill: blue; -fx-font-size: 18; -fx-alignment: CENTER-LEFT;");
                    }
                }
            }
        });
        // Set the background color of the ListView
        chatListView.setStyle("-fx-control-inner-background: black;");
        listenForMessageUI();
    }
    @FXML
    private void handleSend(ActionEvent event) {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            // Implement your message sending logic here
            ClientController.sendMessage(selectedUser.getIdUser(),message);
            sendMessageUI(sender,message);
            // Clear the message field after sending
            messageField.clear();
        }
    }

    public void  listenForMessageUI(){
        ClientController.listenForMessage((String usernameSender,String messageReceived)->{
            if(usernameSender.equals(authenticatedUser.getUsername()))usernameSender="Me";
            String finalUsernameSender = usernameSender;
            Platform.runLater(() -> {
                if (shouldContinueListening) {
                    sendMessageUI(finalUsernameSender, messageReceived);
                }
            });

            return !shouldContinueListening;
        });
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
        ClientController.sendMessage(selectedUser.getIdUser(),"$$loadMessages$$");
    }

    public void setAuthenticatedUser(User authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    private void sendMessageUI(String sender,String message) {
        // Assuming you have a service layer to handle message sending
        String formattedMessage = sender + ":" + message;
        chatMessages.add(formattedMessage);
    }
    @FXML
    private void goBackListUsers(ActionEvent event) {
        shouldContinueListening=false;
        ClientController.sendMessage(selectedUser.getIdUser(),"$$$stop$$$");
        AppNavigator.loadUserListScene(authenticatedUser);
    }
}