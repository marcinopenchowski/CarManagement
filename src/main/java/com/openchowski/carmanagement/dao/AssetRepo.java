package com.openchowski.carmanagement.dao;

import com.openchowski.carmanagement.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepo extends JpaRepository<Asset, Integer> {
}
