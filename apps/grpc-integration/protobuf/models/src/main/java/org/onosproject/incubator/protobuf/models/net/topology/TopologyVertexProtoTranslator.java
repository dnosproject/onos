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

import org.onosproject.grpc.net.models.DeviceIdProtoOuterClass.DeviceIdProto;
import org.onosproject.grpc.net.topology.models.TopologyVertexProtoOuterClass.TopologyVertexProto;
import org.onosproject.net.DeviceId;
import org.onosproject.net.topology.DefaultTopologyVertex;
import org.onosproject.net.topology.TopologyVertex;

/**
 * gRPC TopologVertexProto message to equivalent ONOS TopologyVertex conversion related utilities.
 */
public final class TopologyVertexProtoTranslator {

    /**
     * Translates gRPC TopologyVertex to {@link TopologyVertex}.
     * @param topologyVertexProto gRPC message
     * @return {@link TopologyVertex}
     */
    public static TopologyVertex translate(TopologyVertexProto topologyVertexProto) {

        DeviceId deviceId = DeviceId.deviceId(topologyVertexProto
                .getDeviceId().getDeviceId());
        TopologyVertex topologyVertex = new DefaultTopologyVertex(deviceId);
        return topologyVertex;
    }

    /**
     * Translates {@link TopologyVertex} to gRPC TopologyVertex message.
     * @param topologyVertex {@link TopologyVertex}
     * @return gRPC message
     */
    public static TopologyVertexProto translate(TopologyVertex topologyVertex) {

        DeviceIdProto deviceIdProto = DeviceIdProto
                .newBuilder()
                .setDeviceId(topologyVertex.deviceId().toString())
                .build();
        TopologyVertexProto topologyVertexProto = TopologyVertexProto
                .newBuilder()
                .setDeviceId(deviceIdProto)
                .build();

        return topologyVertexProto;
    }

    private TopologyVertexProtoTranslator() {};
}
