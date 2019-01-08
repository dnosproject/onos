/**
 * Copyright 2016-present Open Networking Foundation
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

package org.onosproject.kafkaintegration.kafka;

import org.onosproject.kafkaintegration.api.EventConversionService;
import org.onosproject.kafkaintegration.api.EventSubscriptionService;
import org.onosproject.kafkaintegration.api.KafkaEventPublisherService;
import org.onosproject.kafkaintegration.api.dto.OnosEvent;
import org.onosproject.net.device.DeviceEvent;
import org.onosproject.net.device.DeviceListener;
import org.onosproject.net.device.DeviceService;
import org.onosproject.net.link.LinkEvent;
import org.onosproject.net.link.LinkListener;
import org.onosproject.net.link.LinkService;
import org.onosproject.net.packet.PacketContext;
import org.onosproject.net.packet.PacketProcessor;
import org.onosproject.net.packet.PacketService;
import org.osgi.service.component.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.onlab.util.Tools.groupedThreads;
import static org.onosproject.kafkaintegration.api.dto.OnosEvent.Type.*;


/**
 * Encapsulates the behavior of monitoring various ONOS events.
 * */
@Component(immediate = true)
public class EventListener {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected EventSubscriptionService eventSubscriptionService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected EventConversionService eventConversionService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected DeviceService deviceService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected LinkService linkService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected PacketService packetService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected KafkaEventPublisherService kafkaEventPublisherService;

    private final DeviceListener deviceListener = new InternalDeviceListener();
    private final LinkListener linkListener = new InternalLinkListener();
    private final InternalPacketProcessor packetListener = new InternalPacketProcessor();

    protected ExecutorService eventExecutor;



    @Activate
    protected void activate() {

        eventExecutor = Executors.newSingleThreadScheduledExecutor(groupedThreads("onos/onosEvents", "events-%d", log));

        deviceService.addListener(deviceListener);
        linkService.addListener(linkListener);
        packetService.addProcessor(packetListener, PacketProcessor.director(11));

        log.info("Started");
    }

    @Deactivate
    protected void deactivate() {
        deviceService.removeListener(deviceListener);
        linkService.removeListener(linkListener);
        packetService.removeProcessor(packetListener);

        eventExecutor.shutdownNow();
        eventExecutor = null;

        log.info("Stopped");
    }

    private class InternalDeviceListener implements DeviceListener {

        @Override
        public void event(DeviceEvent event) {

            if (!eventSubscriptionService.getEventSubscribers(DEVICE).isEmpty()) {
                OnosEvent onosEvent = eventConversionService.convertEvent(event);
                eventExecutor.execute(() -> {
                    kafkaEventPublisherService.publishEvent(onosEvent);
                });
                log.debug("Pushed event {} to kafka storage", onosEvent);
            }

        }
    }

    private class InternalLinkListener implements LinkListener {

        @Override
        public void event(LinkEvent event) {

            if (!eventSubscriptionService.getEventSubscribers(LINK).isEmpty()) {
                OnosEvent onosEvent = eventConversionService.convertEvent(event);
                eventExecutor.execute(() -> {
                    kafkaEventPublisherService.publishEvent(onosEvent);
                });
                log.debug("Pushed event {} to kafka storage", onosEvent);
            }

        }
    }
    private class InternalPacketProcessor implements PacketProcessor {

        @Override
        public void process(PacketContext context) {

            if (context == null) {
                log.error("Packet context is null");
                return;
            }

            if (!eventSubscriptionService.getEventSubscribers(PACKET).isEmpty()) {

                OnosEvent onosEvent = eventConversionService.convertEvent(context);

                eventExecutor.execute(() -> {
                    kafkaEventPublisherService.publishEvent(onosEvent);
                    });
                //log.debug("Pushed event {} to kafka storage", onosEvent);

            }


        }
    }
}