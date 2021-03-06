package com.oleg.premiumCalculator.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import com.oleg.premiumCalculator.model.Policy;
import com.oleg.premiumCalculator.model.PolicySubObject;
import com.oleg.premiumCalculator.model.PolicySubObject.Type;

@Service
@Validated
public class PremiumCalculatorService {

	public BigDecimal calculate(@Valid Policy policy) {
		BigDecimal total = BigDecimal.ZERO;
		Map<Type, BigDecimal> collect = policy.getObjects()
				.stream()
				.flatMap(p -> p.getSubObjects().stream())
				.collect(Collectors.groupingBy(PolicySubObject::getType, Collectors.reducing(BigDecimal.ZERO,
						PolicySubObject::getAmount,
                        BigDecimal::add)));
		total = collect.entrySet().stream().map(x -> x.getKey().calculate(x.getValue())).reduce(total, BigDecimal::add);
		total = total.setScale(2, RoundingMode.HALF_UP);
		return total;
	}
	
}
