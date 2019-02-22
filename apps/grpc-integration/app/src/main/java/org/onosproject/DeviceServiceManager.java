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
import org.onlab.packet.IpAddress;
import org.onosproject.grpc.grpcintegration.models.ControlMessagesProto.Ports;
import org.onosproject.grpc.grpcintegration.models.ControlMessagesProto.DeviceCountProto;
import org.onosproject.grpc.grpcintegration.models.ControlMessagesProto.Devices;
import org.onosproject.grpc.grpcintegration.models.ControlMessagesProto.Empty;
import org.onosproject.grpc.grpcintegration.models.DeviceServiceGrpc.DeviceServiceImplBase;
import org.onosproject.grpc.net.models.DeviceIdProtoOuterClass.DeviceIdProto;
import org.onosproject.grpc.net.models.DeviceProtoOuterClass.DeviceProto;
import org.onosproject.grpcintegration.api.DevicegrpcService;


import org.onosproject.incubator.protobuf.models.net.ConnectPointProtoTranslator;
import org.onosproject.incubator.protobuf.models.net.device.PortProtoTranslator;
import org.onosproject.net.Device;
import org.onosproject.net.DeviceId;
import org.onosproject.net.Port;
import org.onosproject.net.device.DeviceService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.slf4j.Logger;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Implements gRPC Device Service.
 */
@Component(immediate = true, service = DevicegrpcService.class)
public class DeviceServiceManager
        extends DeviceServiceImplBase
        implements DevicegrpcService {

    private final Logger log = getLogger(getClass());

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected DeviceService deviceService;


    @Activate
    protected void activate() {

        log.info("Device Service has been activated");
    }

    @Deactivate
    protected void deactivate() {

        log.info("Device Service has been deactivated");
    }

    @Override
    public void getDevices (Empty empty, StreamObserver<Devices> observer) {
       // TODO: DeviceProtoTranslator needs to be completed.

    }

    /**
     * Returns number of Devices in the network topology.
     * @param empty {@link Empty}
     * @param observer {@link DeviceCountProto}
     */
    @Override
    public void getDeviceCount (Empty empty, StreamObserver<DeviceCountProto> observer) {
        deviceService = DefaultServiceDirectory.getService(DeviceService.class);
        DeviceCountProto deviceCountProto = DeviceCountProto
                .newBuilder()
                .setCount(deviceService.getDeviceCount()).build();

        observer.onNext(deviceCountProto);
        observer.onCompleted();
    }

    @Override
    public void getDevice (DeviceIdProto deviceIdProto,
                           StreamObserver<DeviceProto> observer) {
        deviceService = DefaultServiceDirectory.getService(DeviceService.class);
        Device device = deviceService.getDevice(DeviceId.deviceId(deviceIdProto.getDeviceId()));

        //TODO: DeviceProto Translator is needed

    }

    @Override
    public void getPorts(DeviceIdProto deviceIdProto, StreamObserver<Ports> observer) {

        deviceService = DefaultServiceDirectory.getService(DeviceService.class);
        Ports.Builder portsBuilder = Ports.newBuilder();

        List<Port> ports = deviceService.getPorts(DeviceId.deviceId(deviceIdProto.getDeviceId()));

        for (Port port:ports) {
            //TODO: PortProtoTranslator needs to be completed.
        }

    }





}
