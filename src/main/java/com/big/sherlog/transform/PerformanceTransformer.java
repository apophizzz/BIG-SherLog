package com.big.sherlog.transform;

import com.big.sherlog.annotation.Transformer;
import com.big.sherlog.integration.PerformanceCodeIntegrator;

/**
 * {@link BaseTransformer} subclass which uses {@link PerformanceCodeIntegrator}
 * class to insert code for performance measurement during method execution.<br/>
 * 
 * Created by patrick.kleindienst on 03.06.2015.
 * 
 * @author patrick.kleindienst
 */

@Transformer
public class PerformanceTransformer extends BaseTransformer {

	public PerformanceTransformer() {
		super(new PerformanceCodeIntegrator());
	}
}
