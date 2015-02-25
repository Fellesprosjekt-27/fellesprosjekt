package com.gruppe27.fellesprosjekt.common;

import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryo.Kryo;

import java.util.HashSet;

public class Network {
    public static final int PORT = 5000;

    public static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(TestMessage.class);
        kryo.register(User.class);
        kryo.register(AuthMessage.class);
        kryo.register(AuthMessage.Command.class);
        kryo.register(UserMessage.class);
        kryo.register(UserMessage.Command.class);
        kryo.register(GeneralMessage.class);
        kryo.register(GeneralMessage.Command.class);
        kryo.register(ErrorMessage.class);
        kryo.register(HashSet.class);
    }

}

