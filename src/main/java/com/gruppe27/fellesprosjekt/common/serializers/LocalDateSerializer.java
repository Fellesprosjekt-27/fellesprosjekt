package com.gruppe27.fellesprosjekt.common.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.time.LocalDate;

public class LocalDateSerializer extends Serializer<LocalDate> {
    public void write (Kryo kryo, Output output, LocalDate time) {
        output.writeString(time.toString());
    }

    public LocalDate read (Kryo kryo, Input input, Class<LocalDate> type) {
        return LocalDate.parse(input.readString());
    }
}
