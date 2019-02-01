/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;

/**
 *
 * @author Alvaro Escobar
 */
public class Inventory {

	private ArrayList<Product> products;
	private ArrayList<Part> allParts;

	public Inventory() {
		products = new ArrayList<>();
		allParts = new ArrayList<>();
	}

	public void addProduct(Product productToAdd) {
		if (productToAdd != null) {
			this.products.add(productToAdd);
		}
	}

	public boolean removeProduct(int productToRemove) {
		for (int i = 0; i < products.size(); i++) {
			if (products.get(i).getProductID() == productToRemove) {
				products.remove(i);
				break;
			}
			else {
				return false;
			}
		}
		return true;
	}

	public Product lookUpProduct(int productToSearch) {
		if (!products.isEmpty()) {
			for (int i = 0; i < products.size(); i++) {
				if (products.get(i).getProductID() == productToSearch) {
					return products.get(i);
				}
			}
		}
		return null;
	}

	public void updateProduct(Product product) {
		for (int i = 0; i < products.size(); i++) {
			if (products.get(i).getProductID() == product.getProductID()) {
				products.set(i, product);
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
				break;
			}
			else {
				return false;
			}
		}

		return true;
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

	public void updatePart(Part partToUpdate) {
		for (int i = 0; i < allParts.size(); i++) {
			if (allParts.get(i).getPartID() == partToUpdate.partID) {
				allParts.set(i, partToUpdate);
				break;
			}
		}
		return;
	}

	public int productListSize() {
		return products.size();
	}

	public int partListSize() {
		return allParts.size();
	}

	public ArrayList<Integer> retrievePartsIDList() {
		ArrayList<Integer> list = new ArrayList<>();
		if (!allParts.isEmpty()) {
			for (int i = 0; i < allParts.size(); i++) {
				list.add(allParts.get(i).getPartID());
			}
		}
		return list;
	}

	public ArrayList<Integer> retrieveProductIDList() {
		ArrayList<Integer> list = new ArrayList<>();
		if (!products.isEmpty()) {
			for (int i = 0; i < products.size(); i++) {
				list.add(products.get(i).getProductID());
			}
		}
		return list;
	}

}
