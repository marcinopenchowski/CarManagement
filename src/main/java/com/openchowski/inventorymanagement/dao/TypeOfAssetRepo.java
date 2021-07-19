package com.openchowski.inventorymanagement.dao;

import com.openchowski.inventorymanagement.entity.TypeOfAsset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeOfAssetRepo extends JpaRepository<TypeOfAsset, Integer> {
}
