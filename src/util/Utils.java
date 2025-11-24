package util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {


    public String formatPrice(double price) {
        return String.format("$%.2f", price);
    }


    public void randomSleep(int maxMillis) {
        try {
            Thread.sleep((long) (Math.random() * maxMillis));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
