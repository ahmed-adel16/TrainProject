
/*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package GUI;

import GUI.TrainCardController;
import Train.DatabaseManager;
import Train.Line;
import Train.Train;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Viber
 */

public class ReserveMenuController extends Methods implements Initializable {

        @FXML
        private Pane topMenu;

        @FXML
        private GridPane trainGridPane;
        @FXML
        private Label emailLabel, telLabel, nameLabel;

        private Connection c;
        private PreparedStatement ps;
        private Statement st;
        private ResultSet rs;

        private Image image;

        private Train train;
        private String name, email,tel, password;
        private int id, age;


        private ObservableList<Train> trainCardData = FXCollections.observableArrayList();

      public void initData(String email) throws IOException{

        this.email = email;
        try{
            c = DatabaseManager.getConnection();
            ps = c.prepareStatement("SELECT * FROM Passengers WHERE email = ?");
            ps.setString(1, email);
            rs = ps.executeQuery();

            this.id = rs.getInt("passenger_id");
            this.name = rs.getString("name");
            this.tel = rs.getString("tel");
            this.password = rs.getString("password");
            this.age = rs.getInt("age");

            rs.close();
            ps.close();
            c.close();
            
            emailLabel.setText(email);
            telLabel.setText(tel);
            nameLabel.setText(name);
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TrainCard.fxml"));
            loader.load();
            TrainCardController controller = loader.getController();
            controller.intitData(email);
            
        }catch(SQLException e){
        }
    }

        public String getEmail() {
            return email;
        }

        public int getId() {
            return id;
        }

        public ObservableList<Train> getData() {
            ObservableList<Train> CardListData = FXCollections.observableArrayList();
            try {
                c = DatabaseManager.getConnection();
                ps = c.prepareStatement("SELECT * FROM Trains");
                rs = ps.executeQuery();
                while (rs.next()) {
                    train = new Train(
                            rs.getInt("train_number"),
                            rs.getString("train_name"), rs.getInt("seats"),
                            new Line(rs.getString("departure_station"),
                                    rs.getString("arrival_station")),
                            rs.getInt("tickets_left"));
                    CardListData.add(train);
                }
                ps.close();
                rs.close();
                c.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return CardListData;
        }

        public void dispalayCards(){
            trainCardData.clear();
            trainCardData.addAll(getData());

            int row = 1;
            int column = 0;

            trainGridPane.getRowConstraints().clear();
            trainGridPane.getColumnConstraints().clear();

            for (int i = 0; i < trainCardData.size(); i++) {

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("TrainCard.fxml"));
                    AnchorPane pane = loader.load();
                    TrainCardController tc = loader.getController();
                    tc.setData(trainCardData.get(i));

                    if (column == 3){
                        column = 0;
                        ++row;
                    }
                    GridPane.setMargin(pane, new Insets(12));
                    trainGridPane.add(pane, column++, row);
                    trainGridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
                    trainGridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
                    trainGridPane.setMaxWidth(Region.USE_PREF_SIZE);

                    trainGridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
                    trainGridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
                    trainGridPane.setMaxHeight(Region.USE_PREF_SIZE);
                    trainGridPane.setManaged(true);
                    trainGridPane.layout();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        public void close(){
            Methods.confirmAndExit();
        }
        
        public void back(ActionEvent event) throws IOException{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PassengerMenu.fxml"));
            Parent root = loader.load();
            PassengerMenuController controller = loader.getController();
            controller.initData(email);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Main Menu");
            stage.show();
    }
        
        @Override
        public void initialize(URL url, ResourceBundle rb) {
            dispalayCards();

        }    

}
