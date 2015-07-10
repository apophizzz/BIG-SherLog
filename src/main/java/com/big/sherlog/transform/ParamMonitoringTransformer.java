package com.big.sherlog.transform;

import com.big.sherlog.annotation.SherlogTransformer;
import com.big.sherlog.integration.ParamMonitoringCodeIntegrator;

/**
 * CAUTION: Implementation is currently paused because identifying method
 * parameters by name requires compiling with '-parameter' flag when using
 * javac.<br/>
 * 
 * Created by patrick.kleindienst on 10.06.2015.
 * 
 * @author patrick.kleindienst
 */

@SherlogTransformer
public class ParamMonitoringTransformer extends BaseTransformer {

	public ParamMonitoringTransformer() {
		super();
		this.setCodeIntegrator(new ParamMonitoringCodeIntegrator());
	}
}
