package com.oleg.premiumCalculator.controller;

import java.math.BigDecimal;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.oleg.premiumCalculator.model.Policy;
import com.oleg.premiumCalculator.service.PremiumCalculatorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1")
@Api(tags = "Premium Calculator")
public class PremiumCalculatorController {
	
	@Autowired
	PremiumCalculatorService premiumCalculatorService;

	@ApiOperation(value = "Calculate premium", notes = "Compose calculator object and send it to get results")
	@PostMapping("calculate")
	public BigDecimal calculate(@RequestBody @Valid Policy policy) {
		BigDecimal sum = premiumCalculatorService.calculate(policy);
		return sum;
	}
	
}
