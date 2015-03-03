package com.gruppe27.fellesprosjekt.common.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.time.LocalTime;

public class LocalTimeSerializer extends Serializer<LocalTime> {
    public void write(Kryo kryo, Output output, LocalTime time) {
        output.writeString(time.toString());
    }

    public LocalTime read(Kryo kryo, Input input, Class<LocalTime> type) {
        return LocalTime.parse(input.readString());
    }
}
