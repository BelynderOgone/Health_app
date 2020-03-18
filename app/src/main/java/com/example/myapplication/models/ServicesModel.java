package com.example.myapplication.models;
/*
This is a model class for holding a particular service data as an 'Instance'
 */

public class ServicesModel {
    private String serviceId;
    private String serviceName;

    public ServicesModel() {
    }

    ServicesModel(String mServiceId, String mServiceName) {
        this.serviceId = mServiceId;
        this.serviceName = mServiceName;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

}
