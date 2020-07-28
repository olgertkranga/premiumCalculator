package com.oleg.premiumCalculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.math.BigDecimal;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.oleg.premiumCalculator.model.Policy;
import com.oleg.premiumCalculator.model.PolicyObject;
import com.oleg.premiumCalculator.model.PolicySubObject;
import com.oleg.premiumCalculator.model.PolicySubObject.Type;
import com.oleg.premiumCalculator.service.PremiumCalculatorService;

@SpringBootTest
class PremiumCalculatorAppTests {
	
	@Autowired
	PremiumCalculatorService premiumCalculatorService;
	
	@Test
	void checkCalculation() {
		Policy policy = new Policy();
		policy.setNumber("LV20-02-100000-5");
		policy.setStatus(Policy.Status.REGISTERED);
		PolicyObject policyObject = new PolicyObject();
		policyObject.setName("House");
		policyObject.getSubObjects().add(new PolicySubObject("TV", new BigDecimal("500"), Type.FIRE));
		policyObject.getSubObjects().add(new PolicySubObject("TV", new BigDecimal("102.51"), Type.THEFT));
		policy.getObjects().add(policyObject);
		BigDecimal sum = premiumCalculatorService.calculate(policy);
		assertEquals(new BigDecimal("17.13"), sum);
	}
	
	@Test
	void whenInputIsInvalid_thenThrowsException() {
		Policy policy = new Policy();
		assertThrows(ConstraintViolationException.class, () -> {
			premiumCalculatorService.calculate(policy);
	    });
		policy.setNumber("LV20-02-100000-5");
		assertThrows(ConstraintViolationException.class, () -> {
			premiumCalculatorService.calculate(policy);
		});
		policy.setStatus(Policy.Status.REGISTERED);
		assertThrows(ConstraintViolationException.class, () -> {
			premiumCalculatorService.calculate(policy);
		});
		PolicyObject policyObject = new PolicyObject();
		policy.getObjects().add(policyObject);
		assertThrows(ConstraintViolationException.class, () -> {
			premiumCalculatorService.calculate(policy);
		});
		policyObject.setName("House");
		PolicySubObject policySubObject = new PolicySubObject();
		policyObject.getSubObjects().add(policySubObject);
		assertThrows(ConstraintViolationException.class, () -> {
			premiumCalculatorService.calculate(policy);
		});
		policySubObject.setName("TV");
		assertThrows(ConstraintViolationException.class, () -> {
			premiumCalculatorService.calculate(policy);
		});
		policySubObject.setAmount(new BigDecimal("5"));
		assertThrows(ConstraintViolationException.class, () -> {
			premiumCalculatorService.calculate(policy);
		});
		policySubObject.setType(Type.FIRE);
		BigDecimal sum = premiumCalculatorService.calculate(policy);
		assertEquals(new BigDecimal("0.07"), sum);
	}
	
	@Test
	void checkCalculationIncorrect() {
		Policy policy = new Policy();
		policy.setNumber("AAA");
    	policy.setStatus(Policy.Status.APPROVED);
		PolicyObject policyObject = new PolicyObject();
		policyObject.setName("Car");
		policyObject.getSubObjects().add(new PolicySubObject("Window", new BigDecimal("5000"), Type.FIRE));
		policyObject.getSubObjects().add(new PolicySubObject("Mirrors", new BigDecimal("1020.51"), Type.THEFT));
		policy.getObjects().add(policyObject);
		BigDecimal sum = premiumCalculatorService.calculate(policy);
		assertNotEquals(new BigDecimal("1700.13"), sum);
	}	
	
	@Test
	void checkCalculation2() {
		Policy policy = new Policy();
		policy.setNumber("LV20-02-100000-5");
    	policy.setStatus(Policy.Status.REGISTERED);
		PolicyObject policyObject = new PolicyObject();
		policyObject.setName("House");
		policyObject.getSubObjects().add(new PolicySubObject("TV", new BigDecimal("15"), Type.FIRE));
		policyObject.getSubObjects().add(new PolicySubObject("TV", new BigDecimal("25.25"), Type.THEFT));
		policy.getObjects().add(policyObject);
		BigDecimal sum = premiumCalculatorService.calculate(policy);
		assertEquals(new BigDecimal("1.47"), sum);
	}

}
