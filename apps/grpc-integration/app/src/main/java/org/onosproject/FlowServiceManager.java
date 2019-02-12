/*
 * Copyright 2019-present Open Networking Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.onosproject.grpcintegration.app;

import io.grpc.stub.StreamObserver;
import org.onlab.osgi.DefaultServiceDirectory;
import org.onosproject.core.ApplicationId;
import org.onosproject.core.CoreService;
import org.onosproject.grpc.grpcintegration.models.FlowServiceGrpc.FlowServiceImplBase;
import org.onosproject.grpc.grpcintegration.models.StatusProto.FlowServiceStatus;
import org.onosproject.grpc.net.flow.models.FlowRuleProto;
import org.onosproject.grpcintegration.api.FlowService;
import org.onosproject.incubator.protobuf.models.net.flow.FlowRuleProtoTranslator;
import org.onosproject.net.flow.FlowRule;
import org.onosproject.net.flow.FlowRuleService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 *  Implements Flow gRPC service.
 */
@Component(immediate = true, service = FlowService.class)
public class FlowServiceManager
        extends FlowServiceImplBase
        implements FlowService {

  private final Logger log = getLogger(getClass());

  @Reference(cardinality = ReferenceCardinality.MANDATORY)
  protected FlowRuleService flowRuleService;

  @Reference(cardinality = ReferenceCardinality.MANDATORY)
  protected CoreService coreService;

  @Activate
  protected void activate() {

    log.info("Flow Service has been activated");
  }

  @Deactivate
  protected void deactivate() {

    log.info("Flow Service has been deactivated");
  }

  @Override
  public void addFlow(FlowRuleProto flowRuleRequest,
                      StreamObserver<FlowServiceStatus> responseObserver) {

    flowRuleService = DefaultServiceDirectory.getService(FlowRuleService.class);
    coreService = DefaultServiceDirectory.getService(CoreService.class);
    FlowRule.Builder flowRule = FlowRuleProtoTranslator.translate(flowRuleRequest);
    if (flowRuleRequest.getAppName() != null) {
      ApplicationId applicationId = coreService.registerApplication(flowRuleRequest.getAppName());
      flowRule.fromApp(applicationId);
    }


    flowRuleService.applyFlowRules(flowRule.build());

    FlowServiceStatus flowServiceStatus = FlowServiceStatus
            .newBuilder().setStat(true).build();
    responseObserver.onNext(flowServiceStatus);
    responseObserver.onCompleted();
  }

  @Override
  public void removeFlow(FlowRuleProto flowRuleRequest,
                         StreamObserver<FlowServiceStatus> responseObserver)  {
      flowRuleService = DefaultServiceDirectory.getService(FlowRuleService.class);

      FlowRule.Builder flowRule = FlowRuleProtoTranslator.translate(flowRuleRequest);
      if (flowRuleRequest.getAppName() != null) {
          ApplicationId applicationId = coreService.registerApplication(flowRuleRequest.getAppName());
          flowRule.fromApp(applicationId);
      }

      flowRuleService.removeFlowRules(flowRule.build());

      FlowServiceStatus flowServiceStatus = FlowServiceStatus
              .newBuilder().setStat(true).build();
      responseObserver.onNext(flowServiceStatus);
      responseObserver.onCompleted();


  }
}
