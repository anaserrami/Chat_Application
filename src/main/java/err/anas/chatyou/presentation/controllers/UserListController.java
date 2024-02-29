package err.anas.chatyou.presentation.controllers;

import err.anas.chatyou.dao.UserDaoImpl;
import err.anas.chatyou.dao.entities.User;
import err.anas.chatyou.presentation.AppNavigator;
import err.anas.chatyou.service.IServiceUserImpl;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;

import java.util.List;
import java.util.Objects;

import static err.anas.chatyou.presentation.controllers.ClientController.loadOnlineUsers;
import static err.anas.chatyou.presentation.controllers.ClientController.sendMessage;

public class UserListController {

    @FXML private ListView<User> userListView;
    private User authenticatedUser;
    public void setAuthenticatedUser(User authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }
    @FXML
    private void initialize() {
        // Load the list of users from the database and populate the ListView
        Platform.runLater(this::loadUserList);
    }
    private void loadUserList() {
        // Assuming you have a service layer to interact with your database
        IServiceUserImpl userService = new IServiceUserImpl(new UserDaoImpl());
        List<User> users=userService.getAllUsers().stream()
                .filter(user -> !Objects.equals(user.getUsername(), authenticatedUser.getUsername()))
                .toList();
        // Get the list of all users
        ObservableList<User> userList = FXCollections.observableArrayList(users);
        List<String> usernames=loadOnlineUsers();
        // Set the cell factory to customize the appearance of each cell
        userListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);

                if (empty || user == null) {
                    setText(null);
                } else {
                    // Set the username in the first label
                    Label usernameLabel = new Label(user.getUsername());
                    Label onlineLabel=null;
                    // Add the "Online" label next to the username
                    boolean isEqual=false;
                    for (String username:usernames){
                        if(username.equals(usernameLabel.getText())){
                            isEqual=true;
                        }
                    }
                    if(isEqual){
                        onlineLabel = new Label("Online");
                        onlineLabel.setTextFill(Paint.valueOf("green"));

                    }else{
                        onlineLabel = new Label("Offline");
                        onlineLabel.setTextFill(Paint.valueOf("black"));
                    }

                    // Create an HBox to hold both labels (username and "Online")
                    HBox hbox = new HBox(usernameLabel, onlineLabel);
                    hbox.setSpacing(10); // Adjust spacing between labels if needed

                    // Set the HBox as the graphic for the cell
                    setGraphic(hbox);


                }
            }
        });

        // Populate the ListView with the list of users
        userListView.setItems(userList);
    }
    @FXML
    private void handleStartChat(ActionEvent event) {
        // Get the selected user from the ListView
        User selectedUser = userListView.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            // Start the chat with the selected user
            AppNavigator.loadChatScene(selectedUser,authenticatedUser);
        } else {
            // No user selected, display an error message or take appropriate action
            System.out.println("Please select a user to start the chat.");
        }
    }
    @FXML
    private void handleLogout(ActionEvent event) {
        AppNavigator.loadLoginScene();
        sendMessage(authenticatedUser.getIdUser(),"$$disconnect$$");
    }
    @FXML
    private void handleRefresh(ActionEvent event) {
        loadUserList();
    }
}