package net.shadowking21.baublemounts.components;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;


public class ComponentsCodec {
    public CompoundTag compoundTag;
    public String uuid;
    public static final Codec<MountRecord> MOUNT_CODEC = RecordCodecBuilder.create(mountRecordInstance ->
            mountRecordInstance.group(
                    CompoundTag.CODEC.fieldOf("compoundTag").forGetter(MountRecord::compoundTag),
                    Codec.STRING.fieldOf("uuid").forGetter(MountRecord::uuid)
            ).apply(mountRecordInstance, MountRecord::new)
    );
    public static final StreamCodec<ByteBuf, MountRecord> BASIC_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG, MountRecord::compoundTag,
            ByteBufCodecs.STRING_UTF8, MountRecord::uuid,
            MountRecord::new
    );
}