package com.big.instrumentation.transform;

import com.big.instrumentation.annotation.Transformer;
import com.big.instrumentation.integration.ClassMemberMonitoringCodeIntegrator;

/**
 * {@link BaseTransformer} subclass which uses the
 * {@link ClassMemberMonitoringCodeIntegrator} class in order to log the values
 * of instance variables during a method call.<br/>
 *
 * Created by patrick.kleindienst on 11.06.2015.
 * 
 * @author patrick.kleindienst
 */

@Transformer
public class ClassMemberMonitoringTransformer extends BaseTransformer {

	public ClassMemberMonitoringTransformer() {
		super(new ClassMemberMonitoringCodeIntegrator());
	}
}
