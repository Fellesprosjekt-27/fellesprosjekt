package com.gruppe27.fellesprosjekt.common.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.time.LocalDateTime;

public class LocalDateTimeSerializer extends Serializer<LocalDateTime> {
    public void write(Kryo kryo, Output output, LocalDateTime dateTime) {
        output.writeString(dateTime.toString());
    }

    public LocalDateTime read(Kryo kryo, Input input, Class<LocalDateTime> type) {
        return LocalDateTime.parse(input.readString());
    }
}
