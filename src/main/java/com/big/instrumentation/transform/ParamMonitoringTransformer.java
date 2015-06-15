package com.big.instrumentation.transform;

import com.big.instrumentation.annotation.Transformer;
import com.big.instrumentation.integration.ParamMonitoringCodeIntegrator;

/**
 * Created by patrick.kleindienst on 10.06.2015.
 */

@Transformer
public class ParamMonitoringTransformer extends BaseTransformer {

	public ParamMonitoringTransformer() {
		super();
		this.setCodeIntegrator(new ParamMonitoringCodeIntegrator());
	}
}
