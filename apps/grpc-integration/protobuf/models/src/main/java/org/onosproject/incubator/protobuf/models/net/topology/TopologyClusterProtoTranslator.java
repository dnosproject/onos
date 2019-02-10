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
import org.onosproject.grpc.net.topology.models.ClusterIdProtoOuterClass.ClusterIdProto;
import org.onosproject.grpc.net.topology.models.TopologyClusterProtoOuterClass.TopologyClusterProto;
import org.onosproject.grpc.net.topology.models.TopologyVertexProtoOuterClass.TopologyVertexProto;
import org.onosproject.net.topology.TopologyCluster;

/**
 * gRPC TopologyClusterProto message to equivalent ONOS TopologyCluster conversion related utilities.
 */
public final class TopologyClusterProtoTranslator {

    /**
     * Translates {@link TopologyCluster} to gRPC TopologyCluster message.
     * @param topologyCluster {@link TopologyCluster}
     * @return gRPC message
     */
    public TopologyClusterProto translate(TopologyCluster topologyCluster) {

        ClusterIdProto clusterIdProto = ClusterIdProto
                .newBuilder()
                .setClusterId(topologyCluster.id().index())
                .build();

        DeviceIdProto rootDeviceIdProto = DeviceIdProto
                .newBuilder()
                .setDeviceId(topologyCluster.root().deviceId().toString())
                .build();

        TopologyVertexProto root = TopologyVertexProto
                .newBuilder()
                .setDeviceId(rootDeviceIdProto)
                .build();

        TopologyClusterProto topologyClusterProto = TopologyClusterProto
                .newBuilder()
                .setClusterId(clusterIdProto)
                .setDeviceCount(topologyCluster.deviceCount())
                .setLinkCount(topologyCluster.linkCount())
                .setRoot(root)
                .build();

        return topologyClusterProto;
    }

    private TopologyClusterProtoTranslator() {};
}
