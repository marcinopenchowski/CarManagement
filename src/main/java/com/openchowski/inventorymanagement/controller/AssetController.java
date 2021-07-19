package com.openchowski.inventorymanagement.controller;

import com.openchowski.inventorymanagement.entity.Asset;
import com.openchowski.inventorymanagement.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/assets")
public class AssetController {

    AssetService assetService;

    @Autowired
    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @GetMapping("/list")
    public String showAllAssets(Model theModel){

        List<Asset> assets = assetService.findAll();

        theModel.addAttribute("assets", assets);

        return "list-assets";
    }

}
