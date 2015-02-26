package com.gruppe27.fellesprosjekt.common;

import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryo.Kryo;
import com.gruppe27.fellesprosjekt.common.messages.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;

public class Network {
    public static final int PORT = 5000;

    public static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(TestMessage.class);
        kryo.register(User.class);
        kryo.register(Event.class);
        kryo.register(LocalTime.class);
        kryo.register(LocalDate.class);
        kryo.register(String.class);
        kryo.register(AuthMessage.class);
        kryo.register(AuthMessage.Command.class);
        kryo.register(UserMessage.class);
        kryo.register(UserMessage.Command.class);
        kryo.register(GeneralMessage.class);
        kryo.register(GeneralMessage.Command.class);
        kryo.register(ErrorMessage.class);
        kryo.register(EventMessage.class);
        kryo.register(EventMessage.Command.class);
        kryo.register(HashSet.class);
    }

}

