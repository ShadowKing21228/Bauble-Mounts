package net.shadowking21.baublemounts.network;


import com.mojang.authlib.GameProfile;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handlers.ClientPayloadHandler;
import net.neoforged.neoforge.network.handlers.ServerPayloadHandler;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.shadowking21.baublemounts.BaubleMounts;
import net.shadowking21.baublemounts.items.MountBauble;
import net.shadowking21.baublemounts.utils.Utils;
import org.jetbrains.annotations.NotNull;

public class SendSpawnEntityC2S {

    public record PlayersMountSummonData(Boolean aBoolean) implements CustomPacketPayload {

        public static final Type<PlayersMountSummonData> TYPE = new Type<>(BaubleMounts.prefix("player_mount_summon_data"));
        public static final StreamCodec<RegistryFriendlyByteBuf, PlayersMountSummonData> STREAM_CODEC = CustomPacketPayload.codec(PlayersMountSummonData::write, PlayersMountSummonData::new);

        public PlayersMountSummonData(FriendlyByteBuf buf) {
            this(buf.readBoolean());
        }

        public void write(FriendlyByteBuf buf) {
            buf.writeBoolean(this.aBoolean);
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        public static void handle(PlayersMountSummonData message, IPayloadContext ctx) {
            ctx.enqueueWork(() -> {
                ServerPlayer player = (ServerPlayer) ctx.player();
                ItemStack itemStack = Utils.getMountBauble(player);
                if (itemStack.hasFoil())
                {
                    MountBauble.spawnRideMount(player, itemStack);
                }
            });
        }
    }
    }