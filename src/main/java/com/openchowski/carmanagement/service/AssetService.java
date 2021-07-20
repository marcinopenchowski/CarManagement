package com.openchowski.carmanagement.service;

import com.openchowski.carmanagement.dao.AssetRepo;
import com.openchowski.carmanagement.entity.Asset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AssetService {

    AssetRepo assetRepo;

    @Autowired
    public AssetService(AssetRepo assetRepo) {
        this.assetRepo = assetRepo;
    }

    public List<Asset> findAll(){

        List<Asset> assets = new ArrayList<>();

        assets = assetRepo.findAll();

        return assets;
    }



}
