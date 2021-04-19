package com.challenge.quality.utils;

import com.challenge.quality.dtos.PaymentMethodDTO;
import com.challenge.quality.exceptionHandler.InvalidData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InterestsTest {

    @Test
    @DisplayName("Interests for credit card and 1 due")
    void calculateInterestCreditOneDue() throws InvalidData {
        PaymentMethodDTO paymentMethod = new PaymentMethodDTO("CREDIT", "1234-1234-1234-1234", 1);

        assertEquals(0.5, Interests.calculateInterest(paymentMethod, 10));
    }

    @Test
    @DisplayName("Interests for credit card and 4 dues")
    void calculateInterestCreditFourDues() throws InvalidData {
        PaymentMethodDTO paymentMethod = new PaymentMethodDTO("CREDIT", "1234-1234-1234-1234", 4);

        assertEquals(1, Interests.calculateInterest(paymentMethod, 10));
    }

    @Test
    @DisplayName("Interests for credit card and 7 dues")
    void calculateInterestCreditSevenDues() {
        PaymentMethodDTO paymentMethod = new PaymentMethodDTO("CREDIT", "1234-1234-1234-1234", 7);

        assertThrows(InvalidData.class, () -> Interests.calculateInterest(paymentMethod, 10));
    }

    @Test
    @DisplayName("Interests for debit card and 1 due")
    void calculateInterestDebitOneDue() throws InvalidData {
        PaymentMethodDTO paymentMethod = new PaymentMethodDTO("DEBIT", "1234-1234-1234-1234", 1);

        assertEquals(0, Interests.calculateInterest(paymentMethod, 10));
    }

    @Test
    @DisplayName("Interests for debit card and 2 dues")
    void calculateInterestdebitTwoDues() {
        PaymentMethodDTO paymentMethod = new PaymentMethodDTO("DEBIT", "1234-1234-1234-1234", 2);

        assertThrows(InvalidData.class, () -> Interests.calculateInterest(paymentMethod, 10));
    }
}