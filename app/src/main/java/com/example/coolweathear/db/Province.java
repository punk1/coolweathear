package com.example.coolweathear.db;

import org.litepal.LitePal;

import org.litepal.crud.DataSupport;
import org.litepal.exceptions.DataSupportException;

public class Province extends DataSupport {
    private  int id;
    private String provinceName;
    private int province;

    public int getId() {
        return id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public int getProvince() {
        return province;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public void setProvince(int province) {
        this.province = province;
    }
}
