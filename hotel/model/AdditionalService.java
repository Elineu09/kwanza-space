package hotel.model;

import hotel.model.enums.BillingType;
import hotel.model.enums.ServiceType;
import hotel.interfaces.Chargeable;

public class AdditionalService implements Chargeable {
    private String description;
    private ServiceType serviceType;
    private double unitPrice;
    private int quantity;
    private BillingType billingType;

    public AdditionalService(String description, ServiceType serviceType, double unitPrice, 
                           int quantity, BillingType billingType) {
        this.description = description;
        this.serviceType = serviceType;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.billingType = billingType;
    }

    public String getDescription() {
        return description;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public BillingType getBillingType() {
        return billingType;
    }

    @Override
    public double calculateCharge(int nights) {
        switch (billingType) {
            case PER_NIGHT:
                return unitPrice * nights;
            case FIXED:
                return unitPrice;
            case PER_UNIT:
                return unitPrice * quantity;
            default:
                return 0;
        }
    }

    @Override
    public String toString() {
        return "AdditionalService{" +
                "description='" + description + '\'' +
                ", serviceType=" + serviceType +
                ", unitPrice=" + unitPrice +
                ", quantity=" + quantity +
                ", billingType=" + billingType +
                '}';
    }
}
