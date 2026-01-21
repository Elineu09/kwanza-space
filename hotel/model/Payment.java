package hotel.model;

import hotel.model.enums.PaymentMethod;
import hotel.model.enums.PaymentStatus;
import java.io.Serializable;
import java.time.LocalDate;

public class Payment implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private double amountPaid;
    private LocalDate paymentDate;
    private PaymentMethod method;
    private PaymentStatus status;

    public Payment(double amountPaid, LocalDate paymentDate, PaymentMethod method, PaymentStatus status) {
        this.amountPaid = amountPaid;
        this.paymentDate = paymentDate;
        this.method = method;
        this.status = status;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "amountPaid=" + amountPaid +
                ", paymentDate=" + paymentDate +
                ", method=" + method +
                ", status=" + status +
                '}';
    }
}
