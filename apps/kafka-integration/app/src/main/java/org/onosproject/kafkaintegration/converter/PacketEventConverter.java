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

package org.onosproject.kafkaintegration.converter;


import com.google.protobuf.GeneratedMessageV3;
import org.onosproject.incubator.protobuf.models.net.packet.PacketContextProtoTranslator;
import org.onosproject.net.packet.PacketContext;
import org.onosproject.grpc.net.packet.models.PacketContextProtoOuterClass.PacketContextProto;

/**
 * Converts for ONOS Packet event message to protobuf format.
 */
public class PacketEventConverter implements EventConverter {

    @Override
    public byte[] convertToProtoMessage(PacketContext context) {
        //TODO: Do we need to check any conditions here?
        return ((GeneratedMessageV3) buildPacketContextProtoMessage(context)).toByteArray();

    }

    private PacketContextProto buildPacketContextProtoMessage(PacketContext context) {
        PacketContextProto packetContextProto = PacketContextProtoTranslator.translate(context);
        return packetContextProto;

    }









}
