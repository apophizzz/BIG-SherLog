package com.big.instrumentation.transform;

import com.big.instrumentation.annotation.Transformer;
import com.big.instrumentation.integration.ClassMemberMonitoringCodeIntegrator;

/**
 * Created by patrick.kleindienst on 11.06.2015.
 */

@Transformer
public class ClassMemberMonitoringTransformer extends BaseTransformer {

	public ClassMemberMonitoringTransformer() {
		super(new ClassMemberMonitoringCodeIntegrator());
	}
}
