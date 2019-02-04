COMPILE_DEPS = CORE_DEPS + KRYO + JACKSON + [
    "//apps/grpc-integration/api:onos-apps-grpc-integration-api",
    "//core/store/dist:onos-core-dist",
    "//core/store/serializers:onos-core-serializers",
    #"//apps/grpc-integration/protobuf/models/proto:onos-apps-grpc-integration-protobuf-models-proto",
    #"//apps/grpc-integration/protobuf/models:onos-apps-grpc-integration-protobuf-models",
    "//core/protobuf/models:onos-core-protobuf-models",
    "//core/protobuf/models/proto:onos-core-protobuf-models-proto",
    "@io_grpc_grpc_java//core",
    "@io_grpc_grpc_java//netty",
    "@io_grpc_grpc_java//stub",
]

osgi_jar(
    deps = COMPILE_DEPS,
)
