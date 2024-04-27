/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
public class Main1 extends Application{
    private double xOffset = 0;
    private double yOffset = 0;
    

    @Override
    public void start(Stage stage) throws Exception { // setting up Application settings
        stage.setTitle("EaseTrain - Train Res System");
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        Parent root = FXMLLoader.load(getClass().getResource("LoginMenu.fxml"));
        Pane topMenu = (Pane) root.lookup("#topMenu");
        
        topMenu.setOnMousePressed(this::handleMousePressed);
        topMenu.setOnMouseDragged(this::handleMouseDragged);
        
        Scene scene = new Scene(root);
        
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                Node focusedNode = scene.getFocusOwner();
                if (focusedNode instanceof Button) {
                    Button focusedButton = (Button) focusedNode;
                    focusedButton.fire();
                }
            }
        });
        Image icon = new Image("/image/train1.png");
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }
    


    private void handleMousePressed(MouseEvent event) {
    xOffset = event.getSceneX();
    yOffset = event.getSceneY();
    }

    private void handleMouseDragged(MouseEvent event) {
        Stage stage = (Stage) ((Pane) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }
    
    public static void main(String[] args){
        launch(args);
    }
    
}
