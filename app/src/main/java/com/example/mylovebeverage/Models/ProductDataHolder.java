package com.example.mylovebeverage.Models;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.example.mylovebeverage.AddNewWarehouse;
import com.example.mylovebeverage.ManageProduct;

public class ProductDataHolder {
    private int selected;
    private ArrayAdapter<CharSequence> adapter;
    private WarehouseDetailModel warehouseDetailModel;
    private String Warehouse_ID ="";
    private String Product_ID ="";
    private int Quantity =0;
    private String Unit ="";
    private int Income =0;
    private int Outcome =0;
    private int Profit =0;
    private String Product_Name="";

    public ProductDataHolder(Context parent, String warehouse_ID, String product_ID, String product_Name, int quantity, int income, int outcome, int profit, String unit) {
        Warehouse_ID = warehouse_ID;
        Product_ID = product_ID;
        Product_Name = product_Name;
        Quantity = quantity;
        Income = income;
        Outcome = income+income*profit;
        Profit = profit;
        Unit = unit;
        adapter = new ArrayAdapter(parent, android.R.layout.simple_list_item_1, AddNewWarehouse.productNameList);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
    }

    public String getWarehouse_ID() {
        return Warehouse_ID;
    }

    public void setWarehouse_ID(String warehouse_ID) {
        Warehouse_ID = warehouse_ID;
    }

    public String getProduct_ID() {
        return Product_ID;
    }

    public void setProduct_ID(String product_ID) {
        Product_ID = product_ID;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public int getIncome() {
        return Income;
    }

    public void setIncome(int income) {
        Income = income;
    }

    public int getOutcome() {
        return Outcome;
    }

    public void setOutcome(int outcome) {
        Outcome = outcome;
    }

    public int getProfit() {
        return Profit;
    }

    public void setProfit(int profit) {
        Profit = profit;
    }

    public String getProduct_Name() {
        return Product_Name;
    }

    public void setProduct_Name(String product_Name) {
        Product_Name = product_Name;
    }

    public ArrayAdapter<CharSequence> getAdapter() {
        return adapter;
    }

    public String getText() {
        return (String) adapter.getItem(selected);
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }


}
