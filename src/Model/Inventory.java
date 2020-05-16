/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Alvaro Escobar
 */
public class Inventory {

    private ArrayList<Product> allProducts;
    private ArrayList<Part> allParts;

    public Inventory() {
        allProducts = new ArrayList<>();
        allParts = new ArrayList<>();
    }

    public void addProduct(Product productToAdd) {
        if (productToAdd != null) {
            this.allProducts.add(productToAdd);
        }
    }

    public boolean removeProduct(int productToRemove) {
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getProductID() == productToRemove) {
                allProducts.remove(i);
                return true;
            }
        }
        return false;
    }

    public Product lookUpProduct(int productToSearch) {
        if (!allProducts.isEmpty()) {
            for (int i = 0; i < allProducts.size(); i++) {
                if (allProducts.get(i).getProductID() == productToSearch) {
                    return allProducts.get(i);
                }
            }
        }
        return null;
    }

    public ObservableList<Part> lookUpProduct(String productNameToLookUp) {
        return null;
    }
    
    public void updateProduct(Product product) {
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getProductID() == product.getProductID()) {
                allProducts.set(i, product);
                break;
            }
        }
        return;
    }

    public void addPart(Part partToAdd) {
        if (partToAdd != null) {
            allParts.add(partToAdd);
        }
    }

    public boolean deletePart(Part partToDelete) {
        for (int i = 0; i < allParts.size(); i++) {
            if (allParts.get(i).getPartID() == partToDelete.getPartID()) {
                allParts.remove(i);
                return true;
            }
        }
        return false;
    }

    public Part lookUpPart(int partToLookUp) {
        if (!allParts.isEmpty()) {
            for (int i = 0; i < allParts.size(); i++) {
                if (allParts.get(i).getPartID() == partToLookUp) {
                    return allParts.get(i);
                }
            }

        }
        return null;
    }

    public ObservableList<Part> lookUpPart(String partNameToLookUp) {
        if (!allParts.isEmpty()) {
            ObservableList searchPartsList = FXCollections.observableArrayList();
             for (Part p : getAllParts()) {
                if (p.getName().contains(partNameToLookUp)) {
                    searchPartsList.add(p);
                }
            }
             return searchPartsList;
        }
        return null;
    }

    public void updatePart(Part partToUpdate) {
        for (int i = 0; i < allParts.size(); i++) {
            if (allParts.get(i).getPartID() == partToUpdate.partID) {
                allParts.set(i, partToUpdate);
                break;
            }
        }
        return;
    }

    public ArrayList<Product> getAllProducts() {
        return allProducts;
    }

    public ArrayList<Part> getAllParts() {
        return allParts;
    }

    public int partListSize() {
        return allParts.size();
    }

    public int productListSize() {
        return allProducts.size();
    }

}
