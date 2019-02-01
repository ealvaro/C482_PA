/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Alvaro Escobar
 */
public class AddProductController implements Initializable {

    Inventory inv;

    @FXML
    private TextField id;
    @FXML
    private TextField name;
    @FXML
    private TextField price;
    @FXML
    private TextField count;
    @FXML
    private TextField min;
    @FXML
    private TextField max;
    @FXML
    private TextField search;
    @FXML
    private TableView<Part> partSearchTable;
    @FXML
    private TableView<Part> assocPartsTable;

    private ObservableList<Part> partsInventory = FXCollections.observableArrayList();
    private ObservableList<Part> partsInventorySearch = FXCollections.observableArrayList();
    private ObservableList<Part> assocPartList = FXCollections.observableArrayList();
    ArrayList<Integer> partIDList;

    public AddProductController(Inventory inv) {
        this.inv = inv;
        partIDList = inv.retrievePartsIDList();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        generateProductID();
        populateSearchTable();
    }

    @FXML
    private void clearTextField(MouseEvent event) {
        Object source = event.getSource();
        TextField field = (TextField) source;
        field.setText("");
    }

    @FXML
    private void searchForPart(MouseEvent event) {
        if (search.getText().trim().length() == 0 | search.getText() == null) {
            return;
        } else {
            partsInventorySearch.clear();
            for (int i = 0; i < inv.partListSize(); i++) {
                if (inv.lookUpPart(partIDList.get(i)).getName().contains(search.getText().trim())) {
                    partsInventorySearch.add(inv.lookUpPart(partIDList.get(i)));
                }
            }
            partSearchTable.setItems(partsInventorySearch);
        }

    }

    @FXML
    private void addPart(MouseEvent event) {
        Part addPart = partSearchTable.getSelectionModel().getSelectedItem();
        boolean repeatedItem = false;

        if (addPart == null) {
            return;
        } else {
            int id = addPart.getPartID();
            for (int i = 0; i < assocPartList.size(); i++) {
                if (assocPartList.get(i).getPartID() == id) {
                    errorWindow(2, null);
                    repeatedItem = true;
                }
            }

            if (!repeatedItem) {
                assocPartList.add(addPart);

            }
            assocPartsTable.setItems(assocPartList);
        }
    }

    @FXML
    private void deletePart(MouseEvent event
    ) {
        Part removePart = assocPartsTable.getSelectionModel().getSelectedItem();
        boolean deleted = false;
        if (removePart != null) {
            boolean remove = confirmationWindow(removePart.getName());
            if (remove) {
                assocPartList.remove(removePart);
                assocPartsTable.refresh();
            }
        } else {
            return;
        }
        if (deleted) {
            infoWindow(1, removePart.getName());
        } else {
            infoWindow(2, "");
        }

    }

    private void infoWindow(int code, String name) {
        if (code != 2) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmed");
            alert.setHeaderText(null);
            alert.setContentText(name + " has been deleted!");

            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("There was an error!");
        }
    }

    @FXML
    private void cancelAddProduct(MouseEvent event
    ) {
        boolean cancel = cancel();
        if (cancel) {
            mainScreen(event);
        } else {
            return;
        }
    }

    @FXML
    private void saveAddProduct(MouseEvent event
    ) {
        resetFieldsStyle();
        boolean end = false;
        TextField[] fieldCount = {count, price, min, max};
        double minCost = 0;
        for (int i = 0; i < assocPartList.size(); i++) {
            minCost += assocPartList.get(i).getPrice();
        }
        if (name.getText().trim().isEmpty() || name.getText().trim().toLowerCase().equals("part name")) {
            errorWindow(4, name);
            return;
        }
        for (int i = 0; i < fieldCount.length; i++) {
            boolean valueError = checkValue(fieldCount[i]);
            if (valueError) {
                end = true;
                break;
            }
            boolean typeError = checkType(fieldCount[i]);
            if (typeError) {
                end = true;
                break;
            }

        }
        if (Integer.parseInt(min.getText().trim()) > Integer.parseInt(max.getText().trim())) {
            errorWindow(10, min);
            return;
        }
        if (Integer.parseInt(count.getText().trim()) < Integer.parseInt(min.getText().trim())) {
            errorWindow(8, count);
            return;
        }
        if (Integer.parseInt(count.getText().trim()) > Integer.parseInt(max.getText().trim())) {
            errorWindow(9, count);
            return;
        }
        if (Double.parseDouble(price.getText().trim()) < minCost) {
            errorWindow(6, price);
            return;
        }
        if (assocPartList.size() == 0) {
            errorWindow(7, null);
            return;
        }

        saveProduct();
        mainScreen(event);

    }

    private void fieldError(TextField field) {
        if (field == null) {
            return;
        } else {
            field.setStyle("-fx-border-color: red");
        }
    }

    private void saveProduct() {
        Product product = new Product(Integer.parseInt(id.getText().trim()), name.getText().trim(), Double.parseDouble(price.getText().trim()),
                Integer.parseInt(count.getText().trim()), Integer.parseInt(min.getText().trim()), Integer.parseInt(max.getText().trim()));
        for (int i = 0; i < assocPartList.size(); i++) {
            product.addAssociatedPart(assocPartList.get(i));
        }

        inv.addProduct(product);

    }

    private void resetFieldsStyle() {
        name.setStyle("-fx-border-color: lightgray");
        count.setStyle("-fx-border-color: lightgray");
        price.setStyle("-fx-border-color: lightgray");
        min.setStyle("-fx-border-color: lightgray");
        max.setStyle("-fx-border-color: lightgray");

    }

    private void generateProductID() {
        Random randomNum = new Random();
        Integer num = randomNum.nextInt(1000);
        id.setText(num.toString());

    }

    private void populateSearchTable() {
        if (partIDList.isEmpty()) {
            return;
        } else {
            for (int i = 0; i < partIDList.size(); i++) {
                partsInventory.add(inv.lookUpPart(partIDList.get(i)));
            }
        }

        partSearchTable.setItems(partsInventory);
    }

    @FXML
    void clearField(MouseEvent event) {
        search.setText("");
        if (partsInventory.size() != 0) {
            partSearchTable.setItems(partsInventory);
        }

    }

    private void mainScreen(Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
            MainScreenController controller = new MainScreenController(inv);

            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {

        }
    }

    private boolean checkValue(TextField field) {
        boolean error = false;
        try {
            if (field.getText().trim().isEmpty() || field.getText().trim() == null) {
                errorWindow(1, field);
                return true;
            }
            if (field == price && Double.parseDouble(field.getText().trim()) < 0) {
                errorWindow(5, field);
                error = true;
            }
        } catch (Exception e) {
            error = true;
            errorWindow(3, field);
            System.out.println(e);

        }
        return error;
    }

    private boolean checkType(TextField field) {

        if (field == price & !field.getText().trim().matches("\\d+(\\.\\d+)?")) {
            errorWindow(3, field);
            return true;
        }
        if (field != price & !field.getText().trim().matches("[0-9]*")) {
            errorWindow(3, field);
            return true;
        }
        return false;

    }

    private void errorWindow(int code, TextField field) {
        fieldError(field);

        if (code == 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error adding product");
            alert.setHeaderText("Cannot add product");
            alert.setContentText("Field is empty!");
            alert.showAndWait();
        } else if (code == 2) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error adding part");
            alert.setHeaderText("Cannot add part");
            alert.setContentText("Part is already is associated with this product!");
            alert.showAndWait();
        } else if (code == 3) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error adding product");
            alert.setHeaderText("Cannot add product");
            alert.setContentText("Invalid format!");
            alert.showAndWait();
        } else if (code == 4) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error adding product");
            alert.setHeaderText("Cannot add product");
            alert.setContentText("Name is invalid!");
            alert.showAndWait();
        } else if (code == 5) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error adding product");
            alert.setHeaderText("Cannot add product");
            alert.setContentText("Value cannot be negative!");
            alert.showAndWait();
        } else if (code == 6) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error adding product");
            alert.setHeaderText("Cannot add product");
            alert.setContentText("Product cost cannot be lower than it's parts!");
            alert.showAndWait();
        } else if (code == 7) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error adding product");
            alert.setHeaderText("Cannot add product");
            alert.setContentText("Product must have at least one part!");
            alert.showAndWait();
        } else if (code == 8) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error adding part");
            alert.setHeaderText("Cannot add part");
            alert.setContentText("Inventory cannot be lower than min!");
            alert.showAndWait();
        } else if (code == 9) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error adding part");
            alert.setHeaderText("Cannot add part");
            alert.setContentText("Inventory cannot be greater than max!");
            alert.showAndWait();
        } else if (code == 10) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error adding part");
            alert.setHeaderText("Cannot add part");
            alert.setContentText("Min cannot be greater than max!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error adding product");
            alert.setHeaderText("Cannot add product");
            alert.setContentText("Unknown error!");
            alert.showAndWait();
        }
    }

    private boolean cancel() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setHeaderText("Are you sure you want to cancel?");
        alert.setContentText("Click ok to confirm");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

    private boolean confirmationWindow(String name) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete part");
        alert.setHeaderText("Are you sure you want to delete: " + name);
        alert.setContentText("Click ok to confirm");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }
}
