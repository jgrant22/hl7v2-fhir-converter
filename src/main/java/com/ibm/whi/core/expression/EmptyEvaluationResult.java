/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.whi.core.expression;

import java.util.ArrayList;
import java.util.List;
import com.ibm.whi.api.EvaluationResult;
import com.ibm.whi.api.ResourceValue;


/**
 * Represents value returned after the expression is evaluated.
 * 
 *
 * @author pbhallam
 */
public class EmptyEvaluationResult implements EvaluationResult {


  @Override
  public Object getValue() {
    return null;
  }

  @Override
  public String toString() {
    return " Value : [ null ]";
  }

  @Override
  public Class<?> getValueType() {
    return null;
  }



  @Override
  public boolean isEmpty() {
    return true;
  }

  @Override
  public String getIdentifier() {
    return null;
  }


  public List<ResourceValue> getAdditionalResources() {
    return new ArrayList<>();
  }



}
