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
import org.onosproject.grpc.net.link.models.LinkEventProto;
import org.onosproject.grpc.net.models.ServicesProto.Notification;
import org.onosproject.grpc.net.models.ServicesProto.RegistrationRequest;
import org.onosproject.grpc.net.models.ServicesProto.RegistrationResponse;
import org.onosproject.grpc.net.models.ServicesProto.Topic;
import org.onosproject.grpc.net.models.ServicesProto.topicType;
import org.onosproject.grpc.net.packet.models.PacketContextProtoOuterClass;
import org.onosproject.incubator.protobuf.models.net.link.LinkNotificationProtoTranslator;
import org.onosproject.incubator.protobuf.models.net.packet.PacketContextProtoTranslator;
import org.onosproject.net.link.LinkEvent;
import org.onosproject.net.link.LinkListener;
import org.onosproject.net.link.LinkService;
import org.onosproject.net.packet.PacketContext;
import org.onosproject.net.packet.PacketProcessor;
import org.onosproject.net.packet.PacketService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.Reference;
import org.onosproject.grpc.net.models.EventNotificationGrpc.EventNotificationImplBase;
import org.onosproject.grpcintegration.api.EventNotficationService;
import org.slf4j.Logger;


import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Implements EventNotificiation gRPC service.
 */
@Component(immediate = true, service = EventNotficationService.class)
public class EventNotificationManager
        extends EventNotificationImplBase
        implements EventNotficationService {


    private final Logger log = getLogger(getClass());

    protected static Map<String, StreamObserver<Notification>>
            observerMap = new HashMap<>();
    protected static Set<String> clientList = new HashSet<>();
    protected static Map<String, List<String>> clientKeyMap = new HashMap<>();
    ExecutorService executorService = Executors.newFixedThreadPool(1);


    private final InternalPacketProcessor packetListener = new InternalPacketProcessor();
    private final LinkListener linkListener = new InternalLinkListener();
    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected PacketService packetService;
    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected LinkService linkService;

    @Activate
    protected void activate() {

        log.info("Event Notification Service has been activated");
        packetService.addProcessor(packetListener, PacketProcessor.director(11));
        linkService.addListener(linkListener);
    }

    @Deactivate
    protected void deactivate() {
        log.info("Packet Event Service has been deactivated");
        packetService.removeProcessor(packetListener);
        linkService.removeListener(linkListener);
    }

    @Override
    public void register(
            RegistrationRequest registrationRequest,
            StreamObserver<RegistrationResponse> observer) {

        log.info("Registration request has been recevied");
        RegistrationResponse registrationResponse =
                RegistrationResponse.newBuilder()
                        .setClientId(registrationRequest.getClientId())
                        .setServerId("grpc-integration")
                        .build();

        clientList.add(registrationRequest.getClientId());
        observer.onNext(registrationResponse);
        observer.onCompleted();
    }



    @Override
    public void onEvent(Topic topic,
                        StreamObserver<Notification> observer) {

        observerMap.put(topic.getClientId() + topic.getType(), observer);
        log.info("The client " + topic.getClientId()
                + " subscribed to " + topic.getType().name() );

    }

    class InternalPacketProcessor implements PacketProcessor {

        @Override
        public void process(PacketContext context) {

            if (context == null) {
                log.error("Packet context is null");
                return;
            }

            PacketContextProtoOuterClass.PacketContextProto packetContextProto =
                    PacketContextProtoTranslator.translate(context);

            for (String clientId : clientList) {
                String key = clientId + topicType.PACKET_EVENT.toString();
                Notification notification =
                        Notification.newBuilder()
                                .setClientId(clientId)
                                .setPacketContext(packetContextProto)
                                .build();
                if (observerMap.containsKey(key)) {
                    Runnable runnable =
                            () -> {
                                observerMap.get(key).onNext(notification);
                            };

                    executorService.execute(runnable);
                }
            }
        }
    }
    private class InternalLinkListener implements LinkListener {

        @Override
        public void event(LinkEvent event) {

            LinkEventProto.LinkNotificationProto linkNotificationProto =
                    LinkNotificationProtoTranslator.translate(event);

            for (String clientId : clientList) {
                String key = clientId + topicType.LINK_EVENT.toString();
                Notification notification =
                        Notification.newBuilder()
                                .setClientId(clientId)
                                .setLinkEvent(linkNotificationProto)
                                .build();
                if (observerMap.containsKey(key)) {
                    Runnable runnable =
                            () -> {
                                observerMap.get(key).onNext(notification);
                            };
                    executorService.execute(runnable);
                }
            }

        }
    }
}
