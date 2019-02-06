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

package org.onosproject.incubator.protobuf.models.net.flow;

import org.onosproject.grpc.net.flow.instructions.models.InstructionProtoOuterClass.InstructionProto;
import org.onosproject.grpc.net.flow.models.TrafficTreatmentProtoOuterClass.TrafficTreatmentProto;
import org.onosproject.net.PortNumber;
import org.onosproject.net.flow.DefaultTrafficTreatment;
import org.onosproject.net.flow.TrafficTreatment;

import java.util.List;

/**
 * gRPC TrafficSelectorProto message to equivalent ONOS TrafficTreatment conversion related utilities.
 */
public final class TrafficTreatmentProtoTranslator {

    /**
     * Translates gRPC TrafficTreatment to {@link TrafficTreatment}.
     * @param trafficTreatmentProto gRPC message
     * @return {@link TrafficTreatment}
     */
    public static TrafficTreatment translate(TrafficTreatmentProto trafficTreatmentProto) {

        TrafficTreatment.Builder builder = DefaultTrafficTreatment.builder();
        List<InstructionProto> instructionProtoList = trafficTreatmentProto.getAllInstructionsList();

        for (InstructionProto instructionProto: instructionProtoList) {
            switch (instructionProto.getInstructionCase()) {
                case OUTPUT:
                    switch (instructionProto.getOutput().getPort().getPortNumber()) {
                        case "CONTROLLER":
                            builder.setOutput(PortNumber.CONTROLLER);
                            break;

                         default:
                             builder.setOutput(PortNumber.portNumber(instructionProto
                                .getOutput().getPort().getPortNumber()));
                        break;
                    }

                default:
            }
        }


        return builder.build();


    }


    private TrafficTreatmentProtoTranslator() {};
}
