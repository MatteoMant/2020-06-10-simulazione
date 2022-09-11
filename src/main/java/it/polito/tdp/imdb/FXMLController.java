/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimili"
    private Button btnSimili; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimulazione"
    private Button btnSimulazione; // Value injected by FXMLLoader

    @FXML // fx:id="boxGenere"
    private ComboBox<String> boxGenere; // Value injected by FXMLLoader

    @FXML // fx:id="boxAttore"
    private ComboBox<Actor> boxAttore; // Value injected by FXMLLoader

    @FXML // fx:id="txtGiorni"
    private TextField txtGiorni; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
 
    @FXML
    void doAttoriSimili(ActionEvent event) {
    	Actor attore = boxAttore.getValue();
    	if (attore == null) {
    		txtResult.setText("Per favore selezionare un attore dalla apposita tendina\n");
    		return;
    	}
    	List<Actor> attoriSimili = this.model.getAttoriSimili(attore);
    	Collections.sort(attoriSimili);
    	txtResult.setText("ATTORI SIMILI A: " + attore +"\n");
    	for (Actor a : attoriSimili) {
    		txtResult.appendText(a + "\n");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	boxAttore.getItems().clear();
    	this.model.creaGrafo(boxGenere.getValue());
    	txtResult.setText("Grafo creato!\n");
    	txtResult.appendText("# Vertici : " + this.model.getNumVertici() + "\n");
    	txtResult.appendText("# Archi : " + this.model.getNumArchi() + "\n");
    	
    	List<Actor> attori = new LinkedList<>(this.model.getAllActors());
    	Collections.sort(attori);
    	boxAttore.getItems().addAll(attori);
    }

    @FXML
    void doSimulazione(ActionEvent event) {
    	txtResult.clear();
    	
    	try {
    		int n = Integer.parseInt(txtGiorni.getText());
    		this.model.simula(n);
    		
    		int numeroPause = this.model.getNumPause();
    		Set<Actor> intervistati = this.model.getIntervistati();
    		
    		txtResult.appendText("Numero di pause prese dal produttore: " + numeroPause + "\n");
    		txtResult.appendText("\nAttori intervistati: \n");
    		for (Actor a : intervistati) {
    			txtResult.appendText(a + "\n");
    		}
    	} catch (NumberFormatException e) {
    		txtResult.setText("Inserire un numero di giorni valido!\n");
    		return;
    	}
  
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimili != null : "fx:id=\"btnSimili\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimulazione != null : "fx:id=\"btnSimulazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGenere != null : "fx:id=\"boxGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAttore != null : "fx:id=\"boxAttore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGiorni != null : "fx:id=\"txtGiorni\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	List<String> lista = this.model.getAllGenres();
    	Collections.sort(lista);
    	boxGenere.getItems().addAll(lista);
    }
}
