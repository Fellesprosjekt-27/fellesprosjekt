package com.gruppe27.fellesprosjekt.common;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.gruppe27.fellesprosjekt.common.messages.AuthCompleteMessage;
import com.gruppe27.fellesprosjekt.common.messages.AuthMessage;
import com.gruppe27.fellesprosjekt.common.messages.ErrorMessage;
import com.gruppe27.fellesprosjekt.common.messages.EventMessage;
import com.gruppe27.fellesprosjekt.common.messages.GeneralMessage;
import com.gruppe27.fellesprosjekt.common.messages.InviteMessage;
import com.gruppe27.fellesprosjekt.common.messages.NotificationMessage;
import com.gruppe27.fellesprosjekt.common.messages.ParticipantStatusMessage;
import com.gruppe27.fellesprosjekt.common.messages.RoomMessage;
import com.gruppe27.fellesprosjekt.common.messages.RoomRequestMessage;
import com.gruppe27.fellesprosjekt.common.messages.UserMessage;
import com.gruppe27.fellesprosjekt.common.serializers.LocalDateSerializer;
import com.gruppe27.fellesprosjekt.common.serializers.LocalDateTimeSerializer;
import com.gruppe27.fellesprosjekt.common.serializers.LocalTimeSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;

public class Network {
    public static final int PORT = 5000;

    public static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();

        // -- COMMON --

        kryo.register(Event.class);
        kryo.register(Group.class);
        kryo.register(HashSet.class);
        kryo.register(LocalDate.class, new LocalDateSerializer());
        kryo.register(LocalTime.class, new LocalTimeSerializer());
        kryo.register(LocalDateTime.class, new LocalDateTimeSerializer());
        kryo.register(Notification.class);
        kryo.register(Room.class);
        kryo.register(String.class);
        kryo.register(User.class);

        // -- MESSAGES --
        kryo.register(AuthCompleteMessage.class);
        kryo.register(AuthCompleteMessage.Command.class);

        kryo.register(AuthMessage.class);
        kryo.register(AuthMessage.Command.class);

        kryo.register(ErrorMessage.class);

        kryo.register(EventMessage.class);
        kryo.register(EventMessage.Command.class);

        kryo.register(GeneralMessage.class);
        kryo.register(GeneralMessage.Command.class);

        kryo.register(InviteMessage.class);
        kryo.register(InviteMessage.Command.class);

        kryo.register(ParticipantStatusMessage.class);
        kryo.register(ParticipantStatusMessage.Command.class);

        kryo.register(RoomMessage.class);
        kryo.register(RoomMessage.Command.class);

        kryo.register(RoomRequestMessage.class);
        kryo.register(RoomRequestMessage.Command.class);

        kryo.register(UserMessage.class);
        kryo.register(UserMessage.Command.class);

        kryo.register(GeneralMessage.class);
        kryo.register(GeneralMessage.Command.class);

        kryo.register(Notification.class);
        kryo.register(NotificationMessage.class);
        kryo.register(NotificationMessage.Command.class);
    }

}

