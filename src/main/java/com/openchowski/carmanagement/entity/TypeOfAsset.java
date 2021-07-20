package com.openchowski.carmanagement.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "type_of_asset")
public class TypeOfAsset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @OneToMany(targetEntity = Asset.class, mappedBy = "typeOfAssetId")
    private List<Asset> assets;

    public TypeOfAsset() {
    }

    public TypeOfAsset(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }

    @Override
    public String toString() {
        return "TypeOfAsset{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", assets=" + assets +
                '}';
    }
}
