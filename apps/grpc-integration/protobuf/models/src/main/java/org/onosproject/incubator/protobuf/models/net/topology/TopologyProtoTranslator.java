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

package org.onosproject.incubator.protobuf.models.net.topology;

import org.onosproject.grpc.net.topology.models.TopologyProtoOuterClass.TopologyProto;
import org.onosproject.net.topology.Topology;

/**
 * gRPC TopologyProto message to equivalent ONOS Topology conversion related utilities.
 */
public final class TopologyProtoTranslator {


    /**
     * Translates {@link Topology} to gRPC Topology message.
     * @param topology {@link Topology}
     * @return gRPC message
     */
    public static TopologyProto translate(Topology topology) {

        TopologyProto topologyProto = TopologyProto
                .newBuilder()
                .setDeviceCount(topology.deviceCount())
                .setClusterCount(topology.clusterCount())
                .setComputeCost(topology.computeCost())
                .setCreationTime(topology.creationTime())
                .setLinkCount(topology.linkCount())
                .build();

        return topologyProto;
    }



    private TopologyProtoTranslator() {};
}
