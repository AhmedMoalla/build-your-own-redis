package com.amoalla.redis.handler.info;

import com.amoalla.redis.command.InfoCommand;
import com.amoalla.redis.handler.RedisCommandHandler;
import com.amoalla.redis.types.BulkString;
import com.amoalla.redis.types.DataType;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.impl.collector.Collectors2;

import java.util.List;

public class InfoHandler implements RedisCommandHandler<InfoCommand> {

    private final ImmutableMap<InfoType, InfoProvider> infoProviders;

    public InfoHandler(List<InfoProvider> infoProviders) {
        this.infoProviders = infoProviders.stream()
                .collect(Collectors2.toImmutableMap(InfoProvider::type, provider -> provider));
    }

    @Override
    public List<DataType> handle(InfoCommand command) {
        StringBuilder sb = new StringBuilder();

        for (String section : command.sections()) {
            InfoType type = InfoType.valueOf(section.toUpperCase());
            sb.append(infoProviders.get(type).info()).append("\n");
        }

        return List.of(new BulkString(sb.toString()));
    }


}
