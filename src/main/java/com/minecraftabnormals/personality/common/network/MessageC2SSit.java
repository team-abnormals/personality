package com.minecraftabnormals.personality.common.network;

import com.minecraftabnormals.personality.common.network.handler.ServerNetHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public final class MessageC2SSit {
    private final boolean sit;

    public MessageC2SSit(boolean sit) {
        this.sit = sit;
    }

    public static MessageC2SSit deserialize(PacketBuffer buf) {
        return new MessageC2SSit(buf.readBoolean());
    }

    public static void handle(MessageC2SSit message, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        if (context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
            context.enqueueWork(() -> ServerNetHandler.handleSit(message, context));
            context.setPacketHandled(true);
        }
    }

    public void serialize(PacketBuffer buf) {
        buf.writeBoolean(this.sit);
    }

    public boolean isSitting() {
        return sit;
    }
}