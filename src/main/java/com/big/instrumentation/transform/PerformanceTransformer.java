package com.big.instrumentation.transform;

import com.big.instrumentation.annotation.Transformer;
import com.big.instrumentation.integration.PerformanceCodeIntegrator;

/**
 * Created by patrick.kleindienst on 03.06.2015.
 */

@Transformer
public class PerformanceTransformer extends BaseTransformer {

	public PerformanceTransformer() {
		super();
		this.setCodeIntegrator(new PerformanceCodeIntegrator());
	}
}
