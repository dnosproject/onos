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

import org.onosproject.grpc.net.topology.models.TopologyEdgeProtoOuterClass.TopologyEdgeProto;
import org.onosproject.grpc.net.topology.models.TopologyGraphProtoOuterClass.TopologyGraphProto;
import org.onosproject.grpc.net.topology.models.TopologyVertexProtoOuterClass.TopologyVertexProto;
import org.onosproject.net.topology.TopologyEdge;
import org.onosproject.net.topology.TopologyGraph;
import org.onosproject.net.topology.TopologyVertex;

import java.util.Set;

/**
 * gRPC TopologyGraph message to equivalent ONOS TopologyGraph conversion related utilities.
 */
public final class TopologyGraphProtoTranslator {

    /**
     * Translates {@link TopologyGraph} to gRPC TopologyGraph message.
     * @param topologyGraph {@link TopologyGraph}
     * @return gRPC message
     */
    public static TopologyGraphProto translate(TopologyGraph topologyGraph) {

        Set<TopologyEdge> topologyEdges = topologyGraph.getEdges();
        Set<TopologyVertex> topologyVertexes = topologyGraph.getVertexes();
        TopologyGraphProto.Builder topoGraphBuilder = TopologyGraphProto.newBuilder();

        for (TopologyEdge topologyEdge:topologyEdges) {

            TopologyEdgeProto topologyEdgeProto = TopologyEdgeProtoTranslator
                    .translate(topologyEdge);
            topoGraphBuilder.addEdges(topologyEdgeProto);
        }

        for (TopologyVertex topologyVertex:topologyVertexes) {

            TopologyVertexProto topologyVertexProto = TopologyVertexProtoTranslator
                    .translate(topologyVertex);
            topoGraphBuilder.addVertexes(topologyVertexProto);
        }

        return topoGraphBuilder.build();
    }

    private TopologyGraphProtoTranslator() {};
}
