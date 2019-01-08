/*
 * Copyright 2018-present Open Networking Foundation
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

import org.apache.kafka.clients.producer.ProducerRecord;
import org.onosproject.cluster.ClusterService;
import org.onosproject.cluster.LeadershipService;
import org.onosproject.cluster.NodeId;
import org.onosproject.kafkaintegration.api.KafkaEventPublisherService;
import org.onosproject.kafkaintegration.api.KafkaPublisherService;
import org.onosproject.kafkaintegration.api.dto.OnosEvent;
import org.osgi.service.component.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Component(service = KafkaEventPublisherService.class)
public class EventPublisher implements KafkaEventPublisherService {

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected KafkaPublisherService kafkaPublisher;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected LeadershipService leadershipService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected ClusterService clusterService;

    private static final String PUBLISHER_TOPIC = "WORK_QUEUE_PUBLISHER";

    private NodeId localNodeId;



    private final Logger log = LoggerFactory.getLogger(getClass());

    @Activate
    protected void activate() {
        log.info("Started");

        localNodeId = clusterService.getLocalNode().id();
        leadershipService.runForLeadership(PUBLISHER_TOPIC);
    }

    @Deactivate
    protected void deactivate() {
        log.info("Stopped");
    }

    @Override
    public void publishEvent(OnosEvent onosEvent) {

        // do not allow to proceed without leadership
        NodeId leaderNodeId = leadershipService.getLeader(PUBLISHER_TOPIC);
        if (!Objects.equals(localNodeId, leaderNodeId)) {
                log.debug("Not a Leader, cannot publish!");
                return;
        }

        if (onosEvent != null) {
            try {
                kafkaPublisher.send(new ProducerRecord<>(onosEvent.type().toString(),
                        onosEvent.subject())).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            log.debug("Event Type - {}, Subject {} sent successfully.",
                    onosEvent.type(), onosEvent.subject());
        }

        log.debug("Published {} Event to Kafka Server", onosEvent.type());
    }



}
