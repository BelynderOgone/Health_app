package com.example.myapplication.models;
/*
This is a model class for holding a particular service data as an 'Instance'
 */

public class ServicesModel {
    private String serviceId;
    private String serviceName;
    private String offeredInCountFacilities;

    ServicesModel() {
    }

    ServicesModel(String mServiceId, String mServiceName, String mOfferedInCountFacilities) {
        this.serviceId = mServiceId;
        this.serviceName = mServiceName;
        this.offeredInCountFacilities = mOfferedInCountFacilities;
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

    public String getOfferedInCountFacilities() {
        return offeredInCountFacilities;
    }

    public void setOfferedInCountFacilities(String offeredInCountFacilities) {
        this.offeredInCountFacilities = offeredInCountFacilities;
    }
}
