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
public class Product {

	private ArrayList<Part> associatedParts = new ArrayList<Part>();
	private int productID;
	private String name;
	private double price = 0.0;
	private int inStock = 0;
	private int min;
	private int max;
	private double cost;

	public Product(int productID, String name, double price, int inStock, int min, int max) {
		setProductID(productID);
		setName(name);
		setPrice(price);
		setInStock(inStock);
		setMin(min);
		setMax(max);

	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getPrice() {
		return this.price;
	}

	public void setInStock(int quantity) {
		this.inStock = quantity;
	}

	public int getInStock() {
		return this.inStock;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMin() {
		return this.min;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getMax() {
		return this.max;
	}

	public void addAssociatedPart(Part partToAdd) {
		associatedParts.add(partToAdd);
	}

	public boolean removeAssociatedPart(int partToRemove) {
		int i;
		for (i = 0; i < associatedParts.size(); i++) {
			if (associatedParts.get(i).getPartID() == partToRemove) {
				associatedParts.remove(i);
				return true;
			}
		}

		return false;
	}

	public Part lookupAssociatedPart(int partToSearch) {
		for (int i = 0; i < associatedParts.size(); i++) {
			if (associatedParts.get(i).getPartID() == partToSearch) {
				return associatedParts.get(i);
			}
		}
		return null;
	}

	public void setProductID(int id) {
		this.productID = id;
	}

	public int getProductID() {
		return this.productID;
	}

	public int getPartsListSize() {
		return associatedParts.size();
	}

}
