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

import org.onlab.packet.MacAddress;


import org.onosproject.grpc.net.flow.criteria.models.CriterionProtoOuterClass.CriterionProto;
import org.onosproject.grpc.net.flow.models.TrafficSelectorProtoOuterClass.TrafficSelectorProto;
import org.onosproject.net.flow.DefaultTrafficSelector;
import org.onosproject.net.flow.TrafficSelector;
import org.slf4j.Logger;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * gRPC TrafficSelectorProto message to equivalent ONOS TrafficSelector conversion related utilities.
 */
public final class TrafficSelectorProtoTranslator {

    private static final Logger log = getLogger(TrafficSelectorProtoTranslator.class);
    /**
     * Translates gRPC TrafficSelector to {@link TrafficSelector}.
     * @param trafficSelectorProto gRPC message
     * @return {@link TrafficSelector}
     */
    public static TrafficSelector translate(TrafficSelectorProto trafficSelectorProto) {

        TrafficSelector.Builder builder = DefaultTrafficSelector.builder();
        List<CriterionProto> criterionProtos = trafficSelectorProto.getCriterionList();

        for (CriterionProto criterionProto: criterionProtos) {

            switch (criterionProto.getType()) {

                case ETH_SRC:
                    builder.matchEthSrc(MacAddress.valueOf(criterionProto
                            .getEthCriterion().getMacAddress().toString()));
                    break;
                case ETH_DST:
                    builder.matchEthDst(MacAddress.valueOf(criterionProto
                           .getEthCriterion().getMacAddress().toString()));
                case ETH_TYPE:
                    builder.matchEthType((short) criterionProto
                            .getEthTypeCriterion().getEthType());
                    break;
                default:
                    break;


            }

        }

        return builder.build();

    }

    private  TrafficSelectorProtoTranslator() {};
}
