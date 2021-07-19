package com.openchowski.inventorymanagement.dao;

import com.openchowski.inventorymanagement.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepo extends JpaRepository<Asset, Integer> {
}
