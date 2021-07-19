package com.openchowski.inventorymanagement.entity;

import javax.persistence.*;

@Entity
@Table(name = "asset")
public class Asset {

    // make relations with other entities

    // define a field

    // add annotation

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "model")
    private String model;

    @Column(name = "description")
    private String description;

    @Column(name = "invoice_number")
    private String invoiceNumber;

    @Column(name = "date_of_purchase")
    private String dateOfPurchase;

    @ManyToOne()
    @JoinColumn(name = "type_of_asset_id")
    private TypeOfAsset typeOfAssetId;

    @ManyToOne()
    @JoinColumn(name = "employee_id")
    private Employee employeeId;

    // create constructor/getter/setter/toString

    public Asset() {
    }

    public Asset(String model, String description, String invoiceNumber, String dateOfPurchase, TypeOfAsset typeOfAssetId) {
        this.model = model;
        this.description = description;
        this.invoiceNumber = invoiceNumber;
        this.dateOfPurchase = dateOfPurchase;
        this.typeOfAssetId = typeOfAssetId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(String dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public TypeOfAsset getTypeOfAssetId() {
        return typeOfAssetId;
    }

    public void setTypeOfAssetId(TypeOfAsset typeOfAssetId) {
        this.typeOfAssetId = typeOfAssetId;
    }

    public Employee getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Employee employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public String toString() {
        return "Asset{" +
                "id=" + id +
                ", model='" + model + '\'' +
                ", description='" + description + '\'' +
                ", invoiceNumber='" + invoiceNumber + '\'' +
                ", dateOfPurchase='" + dateOfPurchase + '\'' +
                ", typeOfAssetId=" + typeOfAssetId +
                ", employeeId=" + employeeId +
                '}';
    }


}
