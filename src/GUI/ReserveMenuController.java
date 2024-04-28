    /*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
     */
    package GUI;

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
    import javafx.scene.Parent;
    import javafx.scene.control.Label;
    import javafx.scene.image.Image;
    import javafx.scene.layout.AnchorPane;
    import javafx.scene.layout.GridPane;
    import javafx.scene.layout.Pane;
    import javafx.scene.layout.Region;
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
        private Label emailLabel, telLabel;

        private Connection c;
        private PreparedStatement ps;
        private Statement st;
        private ResultSet rs;

        private Image image;

        private Train train;
        private String name, email,tel, password;
        private int id, age;


        private ObservableList<Train> trainCardData = FXCollections.observableArrayList();

        public void initData(int id, int age, String tel, String email, String password){
            this.id = id;
            this.age = age;
            this.tel = tel;
            this.email = email;
            this.password = password;
            emailLabel.setText(email);
            telLabel.setText(tel);
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
        public void back(ActionEvent e) throws IOException{
            loadFXML("PassengerMenu.fxml","Main Menu",e);
        }
        @Override
        public void initialize(URL url, ResourceBundle rb) {
            dispalayCards();

        }    

    }
