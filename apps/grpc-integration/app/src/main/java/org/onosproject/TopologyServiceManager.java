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
import org.onosproject.grpc.net.models.PathProtoOuterClass;
import org.onosproject.grpc.net.models.TopoServiceGrpc;
import org.onosproject.grpc.net.models.TopoServiceGrpc.TopoServiceImplBase;
import org.onosproject.grpc.net.topology.models.ClusterIdProtoOuterClass.ClusterIdProto;
import org.onosproject.grpc.net.topology.models.TopologyClusterProtoOuterClass;
import org.onosproject.grpc.net.topology.models.TopologyGraphProtoOuterClass.TopologyGraphProto;
import org.onosproject.grpc.net.topology.models.TopologyProtoOuterClass.TopologyProto;
import org.onosproject.grpc.net.models.ServicesProto.Empty;
import org.onosproject.grpc.net.models.ServicesProto.getPathRequest;
import org.onosproject.grpc.net.models.ServicesProto.Paths;
import org.onosproject.grpcintegration.api.TopoService;
import org.onosproject.incubator.protobuf.models.net.device.DeviceProtoTranslator;
import org.onosproject.incubator.protobuf.models.net.topology.TopologyGraphProtoTranslator;
import org.onosproject.incubator.protobuf.models.net.topology.TopologyProtoTranslator;
import org.onosproject.net.DeviceId;
import org.onosproject.net.Link;
import org.onosproject.net.Path;
import org.onosproject.net.topology.ClusterId;
import org.onosproject.net.topology.Topology;
import org.onosproject.net.topology.TopologyCluster;
import org.onosproject.net.topology.TopologyGraph;
import org.onosproject.net.topology.TopologyService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.slf4j.Logger;

import java.util.List;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Implements gRPC Topology Service.
 */
@Component(immediate = true, service = TopoService.class)
public class TopologyServiceManager
        extends TopoServiceImplBase
        implements TopoService {

    private final Logger log = getLogger(getClass());

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected TopologyService topologyService;


    @Activate
    protected void activate() {
        log.info("Topology Service has been activated");

    }

    @Deactivate
    protected void deactivate() {
        log.info("Topology Service has been deactivated");
    }

    @Override
    public void getGraph (Empty empty,
                          StreamObserver<TopologyGraphProto> observer) {
        topologyService = DefaultServiceDirectory.getService(TopologyService.class);
        TopologyGraph topologyGraph =
                topologyService.getGraph(topologyService.currentTopology());

        TopologyGraphProto topologyGraphProto =
                TopologyGraphProtoTranslator.translate(topologyGraph);

        observer.onNext(topologyGraphProto);
        observer.onCompleted();
    }

    @Override
    public void currentTopology (Empty empty, StreamObserver<TopologyProto> observer) {

        topologyService = DefaultServiceDirectory.getService(TopologyService.class);
        Topology topology = topologyService.currentTopology();
        TopologyProto topologyProto = TopologyProtoTranslator.translate(topology);

        observer.onNext(topologyProto);
        observer.onCompleted();
    }

    @Override
    public void getPaths (getPathRequest getPathRequest,
                          StreamObserver<Paths> observer) {


        topologyService = DefaultServiceDirectory.getService(TopologyService.class);
        Topology topology = topologyService.currentTopology();
        DeviceId srcDeviceId =  DeviceId.deviceId(getPathRequest.getSrcDevice().getDeviceId());
        DeviceId dstDeviceId = DeviceId.deviceId(getPathRequest.getDstDevice().getDeviceId());
        Set<Path> paths = topologyService.getPaths(topology,srcDeviceId,dstDeviceId);

        Paths.Builder builder = Paths.newBuilder();

    }


}
