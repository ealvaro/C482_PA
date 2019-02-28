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
import javafx.scene.control.Alert.AlertType;
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
public class ModifyProductController implements Initializable {

    Inventory inv;
    Product product;

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
    private TableView<Part> assocPartsTable;
    @FXML
    private TableView<Part> partSearchTable;
    @FXML
    private TextField search;

    private ObservableList<Part> partsInventory = FXCollections.observableArrayList();
    private ObservableList<Part> partsInventorySearch = FXCollections.observableArrayList();
    private ObservableList<Part> assocPartList = FXCollections.observableArrayList();
    ArrayList<Integer> partIDList;

    public ModifyProductController(Inventory inv, Product product) {
        this.inv = inv;
        this.product = product;
        this.partIDList = inv.retrievePartsIDList();

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateSearchTable();
        setData();
    }

    @FXML
    void clearTextField(MouseEvent event) {
        Object source = event.getSource();
        TextField field = (TextField) source;
        field.setText("");
        if (field == search) {
            partSearchTable.setItems(partsInventory);
        }
    }

    @FXML
    private void modifyProductSearch(MouseEvent event) {
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
    private void deletePart(MouseEvent event) {
        Part removePart = assocPartsTable.getSelectionModel().getSelectedItem();
        boolean deleted = false;
        if (removePart != null) {
            boolean remove = confirmationWindow(removePart.getName());
            if (remove) {
                deleted = product.removeAssociatedPart(removePart.getPartID());
                assocPartList.remove(removePart);
                assocPartsTable.refresh();
            }
        } else {
            return;
        }
        if (deleted) {
            AlertMessage.infoWindow(1, removePart.getName());
        } else {
            AlertMessage.infoWindow(2, "");
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
                    AlertMessage.errorProduct(2, null);
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
    private void cancelModify(MouseEvent event) {
        boolean cancel = AlertMessage.cancel();
        if (cancel) {
            mainScreen(event);
        } else {
            return;
        }
    }

    @FXML
    private void saveProduct(MouseEvent event) {
        resetFieldsStyle();
        boolean end = false;
        TextField[] fieldCount = {count, price, min, max};
        double minCost = 0;
        for (int i = 0; i < assocPartList.size(); i++) {
            minCost += assocPartList.get(i).getPrice();
        }
        if (name.getText().trim().isEmpty() || name.getText().trim().toLowerCase().equals("part name")) {
            AlertMessage.errorProduct(4, name);
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
            AlertMessage.errorProduct(10, min);
            return;
        }
        if (Integer.parseInt(count.getText().trim()) < Integer.parseInt(min.getText().trim())) {
            AlertMessage.errorProduct(8, count);
            return;
        }
        if (Integer.parseInt(count.getText().trim()) > Integer.parseInt(max.getText().trim())) {
            AlertMessage.errorProduct(9, count);
            return;
        }
        if (Double.parseDouble(price.getText().trim()) < minCost) {
            AlertMessage.errorProduct(6, price);
            return;
        }
        if (assocPartList.size() == 0) {
            AlertMessage.errorProduct(7, null);
            return;
        }

        saveProduct();
        mainScreen(event);

    }

    private void saveProduct() {
        Product product = new Product(Integer.parseInt(id.getText().trim()), name.getText().trim(), Double.parseDouble(price.getText().trim()),
                Integer.parseInt(count.getText().trim()), Integer.parseInt(min.getText().trim()), Integer.parseInt(max.getText().trim()));
        for (int i = 0; i < assocPartList.size(); i++) {
            product.addAssociatedPart(assocPartList.get(i));
        }

        inv.updateProduct(product);

    }

    private void setData() {
        for (int i = 0; i < 1000; i++) {
            Part part = product.lookupAssociatedPart(i);
            if (part != null) {
                assocPartList.add(part);
            }
        }

        assocPartsTable.setItems(assocPartList);

        this.name.setText(product.getName());
        this.id.setText((Integer.toString(product.getProductID())));
        this.count.setText((Integer.toString(product.getInStock())));
        this.price.setText((Double.toString(product.getPrice())));
        this.min.setText((Integer.toString(product.getMin())));
        this.max.setText((Integer.toString(product.getMax())));

    }

    private void populateSearchTable() {
        if (inv.partListSize() == 0) {
            return;
        } else {
            for (int i = 0; i < partIDList.size(); i++) {
                partsInventory.add(inv.lookUpPart(partIDList.get(i)));
            }
        }

        partSearchTable.setItems(partsInventory);
    }

    private void resetFieldsStyle() {
        name.setStyle("-fx-border-color: lightgray");
        count.setStyle("-fx-border-color: lightgray");
        price.setStyle("-fx-border-color: lightgray");
        min.setStyle("-fx-border-color: lightgray");
        max.setStyle("-fx-border-color: lightgray");

    }

    private boolean confirmationWindow(String name) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
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
                AlertMessage.errorProduct(1, field);
                return true;
            }
            if (field == price && Double.parseDouble(field.getText().trim()) < 0) {
                AlertMessage.errorProduct(5, field);
                error = true;
            }
        } catch (Exception e) {
            error = true;
            AlertMessage.errorProduct(3, field);
            System.out.println(e);

        }
        return error;
    }

    private boolean checkType(TextField field) {

        if (field == price & !field.getText().trim().matches("\\d+(\\.\\d+)?")) {
            AlertMessage.errorProduct(3, field);
            return true;
        }
        if (field != price & !field.getText().trim().matches("[0-9]*")) {
            AlertMessage.errorProduct(3, field);
            return true;
        }
        return false;

    }

}
