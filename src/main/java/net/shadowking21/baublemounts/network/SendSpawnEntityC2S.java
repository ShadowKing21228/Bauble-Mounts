package net.shadowking21.baublemounts.network;


import com.mojang.authlib.GameProfile;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handlers.ClientPayloadHandler;
import net.neoforged.neoforge.network.handlers.ServerPayloadHandler;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.shadowking21.baublemounts.items.MountBauble;
import net.shadowking21.baublemounts.utils.Utils;

public class SendSpawnEntityC2S {

    public record MyData(Boolean aBoolean) implements CustomPacketPayload {

        public static final CustomPacketPayload.Type<MyData> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("baublemounts", "playerdata"));

        // Each pair of elements defines the stream codec of the element to encode/decode and the getter for the element to encode
        // 'name' will be encoded and decoded as a string
        // 'age' will be encoded and decoded as an integer
        // The final parameter takes in the previous parameters in the order they are provided to construct the payload object
        public static final StreamCodec<ByteBuf, MyData> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.BOOL,
                MyData::aBoolean,
                MyData::new
        );

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
    public static class ServerPayloadHandler {
        public static void handleDataOnMain(final MyData data, final IPayloadContext context) {
            ServerPlayer player = (ServerPlayer) context.player();
            ItemStack itemStack = Utils.getMountBauble(player);
            if (itemStack.hasFoil())
            {
                MountBauble.spawnRideMount(player, itemStack);
            }
            //blah(data.age());
        }
    }
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playBidirectional(
                MyData.TYPE,
                MyData.STREAM_CODEC,
                ServerPayloadHandler::handleDataOnMain
        );
    }
}
