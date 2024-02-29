package err.anas.chatyou.presentation;

import err.anas.chatyou.dao.entities.User;
import err.anas.chatyou.presentation.controllers.ChatController;
import err.anas.chatyou.presentation.controllers.UserListController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppNavigator {

    private static Stage stage;
    public static void setStage(Stage primaryStage) {
        stage = primaryStage;
    }
    public static void loadLoginScene() {
        loadScene("/err/anas/chatyou/views/Login.fxml", "Login");
    }
    public static void loadRegisterScene() {
        loadScene("/err/anas/chatyou/views/Register.fxml", "Register");
    }
    public static void loadUserListScene(User authenticatedUser) {
        try {
            FXMLLoader loader = new FXMLLoader(AppNavigator.class.getResource("/err/anas/chatyou/views/user-list.fxml"));

            Parent root = loader.load();
            UserListController userListController= loader.getController();
            userListController.setAuthenticatedUser(authenticatedUser);
            stage.setScene(new Scene(root));
            stage.setTitle("userList");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadChatScene(User selectedUser,User authenticatedUser) {
        try {
            FXMLLoader loader = new FXMLLoader(AppNavigator.class.getResource("/err/anas/chatyou/views/Chat.fxml"));
            Parent root = loader.load();

            System.out.println(selectedUser);
            // Get the controller instance and set the selected user
            ChatController chatController = loader.getController();
            chatController.setSelectedUser(selectedUser);
            chatController.setAuthenticatedUser(authenticatedUser);
            stage.setScene(new Scene(root));
            stage.setTitle("Chat");
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static FXMLLoader loadScene(String fxml, String title) {
        FXMLLoader loader=null;
        try {
            loader = new FXMLLoader(AppNavigator.class.getResource(fxml));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loader;
    }
}