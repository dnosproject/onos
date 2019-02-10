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

import com.google.protobuf.ByteString;
import org.onlab.packet.Ip4Address;
import org.onlab.packet.IpAddress;
import org.onlab.packet.IpPrefix;
import org.onlab.packet.MacAddress;


import org.onlab.packet.TpPort;
import org.onosproject.grpc.net.flow.criteria.models.CriterionProtoOuterClass.CriterionProto;
import org.onosproject.grpc.net.flow.models.TrafficSelectorProtoOuterClass.TrafficSelectorProto;
import org.onosproject.net.PortNumber;
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
        ByteString bs;


        for (CriterionProto criterionProto: criterionProtos) {

            switch (criterionProto.getType()) {

                case IN_PORT:
                    long port = criterionProto.getPortCriterion().getPort();
                    String name = criterionProto.getPortCriterion().getName();
                    builder.matchInPort(PortNumber.portNumber(port, name));
                    break;
                case ETH_SRC:
                    byte[] ethSrc = criterionProto
                            .getEthCriterion().getMacAddress().toByteArray();
                    builder.matchEthSrc(MacAddress.valueOf(ethSrc));
                    break;
                case ETH_DST:
                    byte[] ethDst = criterionProto
                            .getEthCriterion().getMacAddress().toByteArray();
                    builder.matchEthDst(MacAddress.valueOf(ethDst));
                case ETH_TYPE:
                    builder.matchEthType((short) criterionProto
                            .getEthTypeCriterion().getEthType());
                    break;
                case IPV4_SRC:
                    byte[] ipSrcPrefix = criterionProto.getIpCriterion()
                            .getIpPrefix().toByteArray();
                    IpAddress srcIp = IpAddress.valueOf(IpAddress.Version.INET, ipSrcPrefix);
                    int srcIpPrefixLength = criterionProto.getIpCriterion().getPrefixLength();
                    builder.matchIPSrc(IpPrefix.valueOf(srcIp, srcIpPrefixLength));
                    break;
                case IPV4_DST:
                    byte[] ipDstPrefix = criterionProto.getIpCriterion()
                            .getIpPrefix().toByteArray();
                    IpAddress dstIp = IpAddress.valueOf(IpAddress.Version.INET, ipDstPrefix);
                    int dstIpPrefixLength = criterionProto.getIpCriterion().getPrefixLength();
                    builder.matchIPDst(IpPrefix.valueOf(dstIp, dstIpPrefixLength));
                    break;
                case TCP_SRC:
                    int tcpSrcPort = criterionProto
                            .getTcpPortCriterion().getTcpPort();
                    builder.matchTcpSrc(TpPort.tpPort(tcpSrcPort));
                    break;
                case TCP_DST:
                    int tcpDstPort = criterionProto
                            .getTcpPortCriterion().getTcpPort();
                    builder.matchTcpDst(TpPort.tpPort(tcpDstPort));
                    break;
                case ICMPV4_CODE:
                    int icmpCode = criterionProto
                            .getIcmpCodeCriterion().getIcmpCode();
                    builder.matchIcmpCode((byte) icmpCode);
                    break;
                case ICMPV4_TYPE:
                    int icmpType = criterionProto
                            .getIcmpTypeCriterion().getIcmpType();
                    builder.matchIcmpType((byte) icmpType);
                    break;
                case ICMPV6_CODE:
                    int icmpv6Code = criterionProto
                            .getIcmpv6CodeCriterion().getIcmpv6Code();
                    builder.matchIcmpv6Code((byte) icmpv6Code);
                    break;
                case ICMPV6_TYPE:
                    int icmpv6Type = criterionProto
                            .getIcmpv6TypeCriterion().getIcmpv6Type();
                    builder.matchIcmpv6Type((byte) icmpv6Type);
                    break;
                case SCTP_SRC:
                    int sctpSrc = criterionProto
                            .getSctpPortCriterion().getSctpPort();
                    builder.matchSctpSrc(TpPort.tpPort(sctpSrc));
                    break;
                case SCTP_DST:
                    int sctpDst = criterionProto
                            .getSctpPortCriterion().getSctpPort();
                    builder.matchSctpDst(TpPort.tpPort(sctpDst));
                    break;
                case ARP_OP:
                    int arpOp = criterionProto.getArpOpCriterion().getArpOp();
                    builder.matchArpOp(arpOp);
                    break;
                case ARP_SHA:
                    builder.matchArpSha(MacAddress
                            .valueOf(criterionProto.getArpHaCriterion()
                                    .getMac().toByteArray()));
                    break;
                case ARP_THA:
                    builder.matchArpTha(MacAddress
                            .valueOf(criterionProto.getArpHaCriterion()
                                    .getMac().toByteArray()));
                    break;
                case ARP_SPA:
                    builder.matchArpSpa(Ip4Address
                            .valueOf(criterionProto.getArpPaCriterion()
                                    .getIp4Address().toByteArray()));
                    break;
                case ARP_TPA:
                    builder.matchArpTpa(Ip4Address
                            .valueOf(criterionProto.getArpPaCriterion()
                                    .getIp4Address().toByteArray()));
                    break;
                default:
                    break;


            }

        }

        return builder.build();

    }

    private  TrafficSelectorProtoTranslator() {};
}
