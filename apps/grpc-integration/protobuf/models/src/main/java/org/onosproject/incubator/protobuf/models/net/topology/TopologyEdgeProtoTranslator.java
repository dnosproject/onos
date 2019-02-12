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

import org.onosproject.grpc.net.link.models.LinkEnumsProto.LinkTypeProto;
import org.onosproject.grpc.net.link.models.LinkEnumsProto.LinkStateProto;
import org.onosproject.grpc.net.models.ConnectPointProtoOuterClass.ConnectPointProto;
import org.onosproject.grpc.net.models.LinkProtoOuterClass.LinkProto;
import org.onosproject.grpc.net.topology.models.TopologyEdgeProtoOuterClass.TopologyEdgeProto;
import org.onosproject.net.DeviceId;
import org.onosproject.net.topology.DefaultTopologyEdge;
import org.onosproject.net.topology.DefaultTopologyVertex;
import org.onosproject.net.topology.TopologyEdge;
import org.onosproject.net.topology.TopologyVertex;

/**
 * gRPC TopologyEdgeProto message to equivalent ONOS TopologyEdge conversion related utilities.
 */
public final class TopologyEdgeProtoTranslator {

    /**
     * Translates gRPC TopologyEdge message to {@link TopologyEdge}.
     * @param topologyEdgeProto gRPC message
     * @return {@link TopologyEdge}
     */
    public static TopologyEdge translate(TopologyEdgeProto topologyEdgeProto) {

        DeviceId srcDeviceId = DeviceId.deviceId(topologyEdgeProto.getLink()
                .getSrc().getDeviceId());
        DeviceId dstDeviceId = DeviceId.deviceId(topologyEdgeProto.getLink()
                .getDst().getDeviceId());

        TopologyVertex srcTopoVertex = new DefaultTopologyVertex(srcDeviceId);
        TopologyVertex dstTopoVertex = new DefaultTopologyVertex(dstDeviceId);

        // How should we initilized link here ?
        TopologyEdge topologyEdge = new DefaultTopologyEdge(srcTopoVertex, dstTopoVertex, null);

        return topologyEdge;
    }

    /**
     * Translates {@link TopologyEdge} to gRPC TopologyEdge message.
     * @param topologyEdge {@link TopologyEdge}
     * @return gRPC message
     */
    public static TopologyEdgeProto translate(TopologyEdge topologyEdge) {

        ConnectPointProto srcConnectPointProto = ConnectPointProto
                .newBuilder()
                .setDeviceId(topologyEdge.link().src().deviceId().toString())
                .setPortNumber(topologyEdge.link().src().port().name())
                .build();

        ConnectPointProto dstConnectPointProto = ConnectPointProto
                .newBuilder()
                .setDeviceId(topologyEdge.link().dst().deviceId().toString())
                .setPortNumber(topologyEdge.link().dst().port().name())
                .build();

        LinkStateProto linkStateProto = LinkStateProto
                .valueOf(topologyEdge.link().state().name());

        LinkTypeProto linkTypeProto = LinkTypeProto
                .valueOf(topologyEdge.link().type().name());

        LinkProto linkProto = LinkProto.newBuilder()
                .setSrc(srcConnectPointProto)
                .setDst(dstConnectPointProto)
                .setState(linkStateProto)
                .setType(linkTypeProto)
                .build();

        TopologyEdgeProto topologyEdgeProto = TopologyEdgeProto
                .newBuilder()
                .setLink(linkProto)
                .build();

        return topologyEdgeProto;

    }

    private TopologyEdgeProtoTranslator() {}
}
