import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

/**
 * Three seed tests. Coverage is intentionally light:
 *  - computeIncomeTax: only the middle slab branch is hit
 *  - computeVAT: only the 18% rate is hit
 *  - applyExemption: only the "under cap" branch is hit
 *  - roundToPaise: not exercised
 *  - isEligibleForReturn: not exercised at all
 */
public class TaxCalculatorTest {

    @Test
    public void incomeTax_middleSlab() {
        TaxCalculator c = new TaxCalculator();
        BigDecimal tax = c.computeIncomeTax(new BigDecimal("700000"));
        // 12500 (5% on 250k-500k) + 40000 (20% on 500k-700k) = 52500
        assertEquals(0, tax.compareTo(new BigDecimal("52500.00")));
    }

    @Test
    public void vat_eighteenPercent() {
        TaxCalculator c = new TaxCalculator();
        BigDecimal gst = c.computeVAT(new BigDecimal("1000"), 18);
        assertEquals(0, gst.compareTo(new BigDecimal("180.00")));
    }

    @Test
    public void exemption_underCap() {
        TaxCalculator c = new TaxCalculator();
        BigDecimal net = c.applyExemption(
                new BigDecimal("600000"), new BigDecimal("120000"));
        assertEquals(0, net.compareTo(new BigDecimal("480000")));
    }

    @Test
    public void isEligibleForReturn_nullGrossIncome_false() {
        // Arrange
        TaxCalculator c = new TaxCalculator();
        // Act
        boolean eligible = c.isEligibleForReturn(null, 35);
        // Assert
        assertFalse(eligible);
    }

    @Test
    public void isEligibleForReturn_negativeAge_false() {
        // Arrange
        TaxCalculator c = new TaxCalculator();
        // Act
        boolean eligible = c.isEligibleForReturn(new BigDecimal("300000"), -1);
        // Assert
        assertFalse(eligible);
    }

    @Test
    public void isEligibleForReturn_ageZero_true() {
        // Arrange
        TaxCalculator c = new TaxCalculator();
        BigDecimal grossIncome = new BigDecimal("250000.01");
        // Act
        boolean eligible = c.isEligibleForReturn(grossIncome, 0);
        // Assert
        assertTrue(eligible);
    }

    @Test
    public void isEligibleForReturn_zeroIncome_false() {
        // Arrange
        TaxCalculator c = new TaxCalculator();
        // Act
        boolean eligible = c.isEligibleForReturn(BigDecimal.ZERO, 35);
        // Assert
        assertFalse(eligible);
    }

    @Test
    public void isEligibleForReturn_underSixtyAtThreshold_false() {
        // Arrange
        TaxCalculator c = new TaxCalculator();
        // Act
        boolean eligible = c.isEligibleForReturn(new BigDecimal("250000"), 59);
        // Assert
        assertFalse(eligible);
    }

    @Test
    public void isEligibleForReturn_underSixtyAboveThreshold_true() {
        // Arrange
        TaxCalculator c = new TaxCalculator();
        // Act
        boolean eligible = c.isEligibleForReturn(new BigDecimal("250000.01"), 59);
        // Assert
        assertTrue(eligible);
    }

    @Test
    public void isEligibleForReturn_ageSixtyAtThreshold_false() {
        // Arrange
        TaxCalculator c = new TaxCalculator();
        // Act
        boolean eligible = c.isEligibleForReturn(new BigDecimal("300000"), 60);
        // Assert
        assertFalse(eligible);
    }

    @Test
    public void isEligibleForReturn_ageSixtyAboveThreshold_true() {
        // Arrange
        TaxCalculator c = new TaxCalculator();
        // Act
        boolean eligible = c.isEligibleForReturn(new BigDecimal("300000.01"), 60);
        // Assert
        assertTrue(eligible);
    }

    @Test
    public void isEligibleForReturn_ageSeventyNineAboveSeniorThreshold_true() {
        // Arrange
        TaxCalculator c = new TaxCalculator();
        // Act
        boolean eligible = c.isEligibleForReturn(new BigDecimal("300000.01"), 79);
        // Assert
        assertTrue(eligible);
    }

    @Test
    public void isEligibleForReturn_ageEightyAtThreshold_false() {
        // Arrange
        TaxCalculator c = new TaxCalculator();
        // Act
        boolean eligible = c.isEligibleForReturn(new BigDecimal("500000"), 80);
        // Assert
        assertFalse(eligible);
    }

    @Test
    public void isEligibleForReturn_ageEightyAboveThreshold_true() {
        // Arrange
        TaxCalculator c = new TaxCalculator();
        // Act
        boolean eligible = c.isEligibleForReturn(new BigDecimal("500000.01"), 80);
        // Assert
        assertTrue(eligible);
    }
}
