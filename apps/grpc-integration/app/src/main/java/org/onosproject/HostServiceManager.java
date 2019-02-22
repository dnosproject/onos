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
import org.onosproject.grpc.grpcintegration.models.ControlMessagesProto.HostCountProto;
import org.onosproject.grpc.grpcintegration.models.ControlMessagesProto.Hosts;
import org.onosproject.grpc.grpcintegration.models.ControlMessagesProto.Empty;
import org.onosproject.grpc.net.models.HostIdProtoOuterClass.HostIdProto;
import org.onosproject.grpc.net.models.HostProtoOuterClass.HostProto;
import org.onosproject.grpcintegration.api.HostgrpcService;
import org.onosproject.incubator.protobuf.models.net.HostIdProtoTranslator;
import org.onosproject.incubator.protobuf.models.net.HostProtoTranslator;
import org.onosproject.net.Host;
import org.onosproject.net.host.HostService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.onosproject.grpc.grpcintegration.models.HostServiceGrpc.HostServiceImplBase;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Implements gRPC Host Service.
 */
@Component(immediate = true, service = HostgrpcService.class)
public class HostServiceManager
        extends HostServiceImplBase
        implements HostgrpcService {

    private final Logger log = getLogger(getClass());

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected HostService hostService;


    @Activate
    protected void activate() {
        log.info("Topology Service has been activated");
    }

    @Deactivate
    protected void deactivate() {
        log.info("Topology Service has been deactivated");
    }

    /**
     * Implements getHosts function.
     * @param empty {@link Empty}
     * @param observer Returns {@link Hosts}
     */
    @Override
    public void getHosts (Empty empty,
                          StreamObserver<Hosts> observer) {
        hostService = DefaultServiceDirectory.getService(HostService.class);
        Hosts.Builder hostsBuilder = Hosts.newBuilder();
        Iterable<Host> hosts = hostService.getHosts();

        for(Host host:hosts) {
            HostProto hostProto = HostProtoTranslator.translate(host);
            hostsBuilder.addHost(hostProto);
        }

        observer.onNext(hostsBuilder.build());
        observer.onCompleted();

    }

    /**
     * Implements getHostCount function.
     * @param empty {@link Empty}
     * @param observer Returns {@link HostCountProto}
     */
    @Override
    public void getHostCount (Empty empty,
                              StreamObserver<HostCountProto> observer) {
        hostService = DefaultServiceDirectory.getService(HostService.class);
        HostCountProto hostCountProto = HostCountProto
                .newBuilder()
                .setCount(hostService.getHostCount())
                .build();
        observer.onNext(hostCountProto);
        observer.onCompleted();

    }

    /**
     * Implements getHost function.
     * @param hostIdProto {@link HostIdProto}
     * @param observer Returns {@link HostProto}
     */
    @Override
    public void getHost (HostIdProto hostIdProto,
                         StreamObserver<HostProto> observer) {
        hostService = DefaultServiceDirectory.getService(HostService.class);
        Host host = hostService.getHost(HostIdProtoTranslator.translate(hostIdProto));
        HostProto hostProto = HostProtoTranslator.translate(host);

        observer.onNext(hostProto);
        observer.onCompleted();
    }

}
