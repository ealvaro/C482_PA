/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Alvaro Escobar
 */
public class OutSourced extends Part {

	private String companyName;

	public OutSourced(int partID, String name, double price, int numInStock, int min, int max, String company) {
		this.setPartID(partID);
		this.setName(name);
		this.setPrice(price);
		this.setInStock(numInStock);
		this.setMin(min);
		this.setMax(max);
		this.setCompanyName(company);
	}

	public void setCompanyName(String name) {
		this.companyName = name;
	}

	public String getCompanyName() {
		return companyName;
	}

}
