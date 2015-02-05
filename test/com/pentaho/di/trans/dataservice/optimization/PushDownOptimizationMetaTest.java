/*!
 * PENTAHO CORPORATION PROPRIETARY AND CONFIDENTIAL
 *
 * Copyright 2002 - 2014 Pentaho Corporation (Pentaho). All rights reserved.
 *
 * NOTICE: All information including source code contained herein is, and
 * remains the sole property of Pentaho and its licensors. The intellectual
 * and technical concepts contained herein are proprietary and confidential
 * to, and are trade secrets of Pentaho and may be covered by U.S. and foreign
 * patents, or patents in process, and are protected by trade secret and
 * copyright laws. The receipt or possession of this source code and/or related
 * information does not convey or imply any rights to reproduce, disclose or
 * distribute its contents, or to manufacture, use, or sell anything that it
 * may describe, in whole or in part. Any reproduction, modification, distribution,
 * or public display of this information without the express written authorization
 * from Pentaho is strictly prohibited and in violation of applicable laws and
 * international treaties. Access to the source code contained herein is strictly
 * prohibited to anyone except those individuals and entities who have executed
 * confidentiality and non-disclosure agreements or other agreements with Pentaho,
 * explicitly covering such access.
 */

package com.pentaho.di.trans.dataservice.optimization;

import com.pentaho.di.trans.dataservice.DataServiceExecutor;
import org.junit.Before;
import org.junit.Test;
import org.pentaho.di.core.sql.SQL;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.step.StepInterface;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PushDownOptimizationMetaTest {

  private PushDownOptimizationMeta pushDownOptimizationMeta;
  private DataServiceExecutor executor;
  private Trans trans;
  private PushDownType pushDownType;
  private SQL sql;
  public static final String STEP_NAME = "Optimized Step";

  @Before
  public void setUp() throws Exception {
    pushDownOptimizationMeta = new PushDownOptimizationMeta();
    executor = mock( DataServiceExecutor.class );
    trans = mock( Trans.class );
    pushDownType = mock( PushDownType.class );
    sql = mock( SQL.class );

    pushDownOptimizationMeta.setStepName( STEP_NAME );
    pushDownOptimizationMeta.setType( pushDownType );
    when( executor.getServiceTrans() ).thenReturn( trans );
    when( executor.getSql() ).thenReturn( sql );
  }

  @Test
  public void testActivate() throws Exception {
    assertThat( pushDownOptimizationMeta.activate( executor ), is( false ) );

    StepInterface stepInterface = mock( StepInterface.class );
    when( trans.findRunThread( STEP_NAME ) ).thenReturn( stepInterface );

    when( pushDownType.activate( executor, stepInterface ) ).thenReturn( true, false );
    assertThat( pushDownOptimizationMeta.activate( executor ), is( true ) );
    assertThat( pushDownOptimizationMeta.activate( executor ), is( false ) );
  }
}