package com.big.sherlog.transform;

import com.big.sherlog.annotation.SherlogTransformer;
import com.big.sherlog.integration.ClassMemberMonitoringCodeIntegrator;

/**
 * {@link BaseTransformer} subclass which uses the
 * {@link ClassMemberMonitoringCodeIntegrator} class in order to log the values
 * of instance variables during a method call.<br/>
 *
 * Created by patrick.kleindienst on 11.06.2015.
 * 
 * @author patrick.kleindienst
 */

@SherlogTransformer
public class ClassMemberMonitoringTransformer extends BaseTransformer {

	public ClassMemberMonitoringTransformer() {
		super(new ClassMemberMonitoringCodeIntegrator());
	}
}
