package ru.borisov.dataanalyzergrpcmicroservice.service;

import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.borisov.dataanalyzergrpcmicroservice.model.Data;
import ru.borisov.grpccommon.AnalyticsServerGrpc;
import ru.borisov.grpccommon.GRPCAnalyticsRequest;
import ru.borisov.grpccommon.GRPCData;
import ru.borisov.grpccommon.MeasurementType;

import java.time.ZoneOffset;
import java.util.List;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class GRPCAnalyticsService extends AnalyticsServerGrpc.AnalyticsServerImplBase {

    private final DataService dataService;

    @Override
    public void askForData(GRPCAnalyticsRequest request, StreamObserver<GRPCData> responseObserver) {

        List<Data> data = dataService.getWithBatch(request.getBatchSize());
        for (Data d : data) {
            GRPCData dataRequest = getGrpcData(d);
            responseObserver.onNext(dataRequest);
        }
        log.info("Batch was sent");
        responseObserver.onCompleted();
    }

    private static GRPCData getGrpcData(Data data) {

        return GRPCData.newBuilder()
                .setSensorId(data.getSensorId())
                .setTimestamp(
                        Timestamp.newBuilder()
                                .setSeconds(
                                        data.getTimestamp().toEpochSecond(ZoneOffset.UTC)
                                )
                                .build()
                )
                .setMeasurementType(MeasurementType.valueOf(data.getMeasurementType().name()))
                .setMeasurement(data.getMeasurement())
                .build();
    }
}
