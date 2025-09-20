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
        if(heureChoisie.length() == 1)
        {
            heureChoisie = "0" + spHeure.getValue().toString();
        }
        if (minuteChoisie.length() == 1)
        {
            minuteChoisie = "0" + spMinute.getValue().toString();
        }
        String heureBonne = heureChoisie + ":" + minuteChoisie;
        // format date et heure
        DateTimeFormatter dft = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String maDate = dft.format(dpDateRdv.getValue());

        if (rechercherRDV(maDate,heureBonne)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur !");
            alert.setHeaderText("Il y a déja un rendez vous à cette date et cette heure");
            alert.showAndWait();
        }
else {
            // Ajout dans le TreeMap
            rendezVous = new RendezVous(heureBonne
                    , txtNomPatient.getText()
                    , cboNomPathologie.getSelectionModel().getSelectedItem().toString());


            // attention vérifier si date et heure sont présentes

            // Verifier si date existe ou pas
            if (!monPlanning.containsKey(maDate)) {
                TreeMap<String, RendezVous> lesRDV = new TreeMap<>();
                monPlanning.put(maDate, lesRDV);
            }
            monPlanning.get(maDate).put(heureBonne, rendezVous);


            int bidon = 12;


            // On affiche dans le treeview
            TreeItem noeudDate;
            TreeItem noeudHeure;
            TreeItem noeudDivers;

            //root.getChildren().clear();

            for (String date : monPlanning.keySet()) {
                noeudDate = new TreeItem(date);

                for (String heure : monPlanning.get(date).keySet()) {
                    noeudHeure = new TreeItem(heure);
                    noeudDivers = new TreeItem(monPlanning.get(date).get(heure).getNomPatient());
                    noeudHeure.getChildren().add(noeudDivers);
                    noeudDivers = new TreeItem(monPlanning.get(date).get(heure).getNomPathologie());
                    noeudHeure.getChildren().add(noeudDivers);
                    noeudHeure.setExpanded(true);
                    noeudDate.getChildren().add(noeudHeure);
                    noeudHeure.setExpanded(true);
                }
                root.getChildren().add(noeudDate);

            }
            tvPlanning.setRoot(root);
        }
    }
    /*Vérifier quelques saisies , nom patient + choix de la date (alert)
    Verifier si RDV existe deja à cette date et heure (indice : containsKey(date) et containsKey(heure))
    Vérifier si la date et heure n'existe pas -> creer la TreeMap des heures , créer l'objet Render-vous , créer la date
     */
    public boolean rechercherRDV(String uneDate,String uneHeure){

        boolean trouve = false;
        if (monPlanning.containsKey(uneDate)){
            if (monPlanning.containsKey(uneHeure)){
                trouve = true;
            }
        }
        return trouve;
    }


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