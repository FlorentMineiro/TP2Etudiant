package sio.tp2;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import sio.tp2.Model.RendezVous;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ResourceBundle;
import java.util.TreeMap;

public class TP2Controller implements Initializable {

    private TreeMap<String, TreeMap<String, RendezVous>> monPlanning;
    TreeItem root;
    @FXML
    private TextField txtNomPatient;
    @FXML
    private ComboBox cboNomPathologie;
    @FXML
    private TreeView tvPlanning;
    @FXML
    private DatePicker dpDateRdv;
    @FXML
    private Spinner spHeure;
    @FXML
    private Spinner spMinute;
    @FXML
    private Button cmdValider;
    private RendezVous rendezVous;

    @FXML
    public void cmdValiderClicked(Event event)
    {
        rendezVous = new RendezVous(spHeure.getValue().toString(),spMinute.getValue().toString(),txtNomPatient.getText(),cboNomPathologie.getSelectionModel().getSelectedItem().toString());

        if (txtNomPatient.getText().isEmpty() && dpDateRdv.getValue() == null) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Erreur !");
            a.setHeaderText("Veuillez saisir le nom du patient ET la date !");
            a.showAndWait();
        } else if (txtNomPatient.getText().isEmpty()) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Erreur !");
            a.setHeaderText("Veuillez saisir le nom du patient !");
            a.showAndWait();
        } else if (dpDateRdv.getValue() == null) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Erreur !");
            a.setHeaderText("Veuillez saisir la date !");
            a.showAndWait();
        }
        // heure et minutes choisie
        String heureChoisie = spHeure.getValue().toString();
        String minuteChoisie = spMinute.getValue().toString();
        if(heureChoisie.length() == 1){
            heureChoisie = "0" + spHeure.getValue().toString();
        }
        if (minuteChoisie.length() == 1){
            minuteChoisie = "0" + spMinute.getValue().toString();
        }
        String heureBonne = heureChoisie + ":" + minuteChoisie;
        // format date et heure
        DateTimeFormatter dft = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String maDate = dft.format(dpDateRdv.getValue());
    }
    /*public void planning(){


    }*/



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // A ne pas effacer
        monPlanning = new TreeMap<>();
        root = new TreeItem("Mon planning");
        tvPlanning.setRoot(root);
        cboNomPathologie.getItems().addAll("Angine","Grippe","Covid","Gastro");
        cboNomPathologie.getSelectionModel().selectFirst();
        SpinnerValueFactory spinnerValueFactoryHeure = new SpinnerValueFactory.IntegerSpinnerValueFactory(8,19,8,1);
        spHeure.setValueFactory(spinnerValueFactoryHeure);
        SpinnerValueFactory spinnerValueFactoryMinute = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,45,0,15);
        spMinute.setValueFactory(spinnerValueFactoryMinute);
    }
}