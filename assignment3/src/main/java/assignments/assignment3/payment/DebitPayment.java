package assignments.assignment3.payment;

public class DebitPayment implements DepeFoodPaymentSystem{
    
    // Properti
    private static final double MINIMUM_TOTAL_PRICE = 50000; // Minimal harga yang harus dipesan

    // Mengimplementasi method dari inheritance
    @Override
    public long processPayment(long amount) {
        return amount;
    }

}
