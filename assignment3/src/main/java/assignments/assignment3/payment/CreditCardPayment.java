package assignments.assignment3.payment;

import assignments.assignment3.*;
// import assignments.assignment2.;

//implementasi class yang implement interface 
public class CreditCardPayment implements DepeFoodPaymentSystem{
    // Persentase biaya transaksi
    private static final double TRANSACTION_FEE_PERCENTAGE = 0.02;
    
    // Implementasi metode dari antarmuka DepeFoodPaymentSystem
    @Override
    public long processPayment(long amount) {
        return  amount + countTransactionFee(amount) ;
    }

    public long countTransactionFee(long amount) {
        return (long) (amount * TRANSACTION_FEE_PERCENTAGE);
        
    }
}
