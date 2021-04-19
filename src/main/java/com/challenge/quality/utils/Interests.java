package com.challenge.quality.utils;

import com.challenge.quality.dtos.PaymentMethodDTO;
import com.challenge.quality.exceptionHandler.InvalidData;

import java.util.Locale;

public class Interests {
    public static Double calculateInterest(PaymentMethodDTO paymentMethod, long amount) throws InvalidData {
        Double interest = 0d;
        if (paymentMethod.getType().toLowerCase(Locale.ROOT).trim().equals("credit")){
            if (paymentMethod.getDues() <= 2) interest = amount * 0.05;
            if (paymentMethod.getDues() >= 3 && paymentMethod.getDues() <= 6) interest = amount * 0.1;
            if (paymentMethod.getDues() >= 7) throw new InvalidData("No more than 6 dues");
        } else {
            if (paymentMethod.getDues() != 1) throw new InvalidData("You cannot pay in dues with a debit card");
        }
        return interest;
    }
}
