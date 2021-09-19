package com.lender.ledger.LenderApplication.validation;

import com.lender.ledger.LenderApplication.enums.Operation;

public interface IRequestValidator<K> {
	
	void validate(K input, Operation operation);
}
