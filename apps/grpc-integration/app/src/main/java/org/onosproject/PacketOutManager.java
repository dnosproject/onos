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
import org.onosproject.grpc.net.models.ServicesProto;
import org.onosproject.grpc.net.packet.models.OutboundPacketProtoOuterClass;
import org.onosproject.incubator.protobuf.models.net.packet.OutboundPacketProtoTranslator;
import org.onosproject.net.packet.OutboundPacket;
import org.onosproject.net.packet.PacketService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.Reference;

import org.onosproject.grpc.net.models.PacketOutServiceGrpc.PacketOutServiceImplBase;
import org.onosproject.grpcintegration.api.PacketOutService;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Implements PacketOut gRPC Service.
 */
@Component(immediate = true, service = PacketOutService.class)
public class PacketOutManager
        extends PacketOutServiceImplBase
        implements PacketOutService {

    private final Logger log = getLogger(getClass());

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected PacketService packetService;

    @Activate
    protected void activate() {

        log.info("Packetout Service has been activated");
    }

    @Deactivate
    protected void deactivate() {
        log.info("deactivated");
    }

    @Override
    public void emit(OutboundPacketProtoOuterClass.OutboundPacketProto request, StreamObserver<ServicesProto.PacketOutStatus> responseObserver) {

        OutboundPacket outboundPacket = OutboundPacketProtoTranslator.translate(request);

        packetService = DefaultServiceDirectory.getService(PacketService.class);
        packetService.emit(outboundPacket);

        ServicesProto.PacketOutStatus reply = ServicesProto.PacketOutStatus.newBuilder().setStat(true).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

}
