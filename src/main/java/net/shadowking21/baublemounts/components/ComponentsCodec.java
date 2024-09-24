package net.shadowking21.baublemounts.components;


import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.UUID;


public class ComponentsCodec {
        //String string = uuid.toString();
        //UUID uuid2 = UUID.fromString(string);
        //CompoundTag compoundtag = new CompoundTag();
        //String string2 = compoundtag.toString();
        //compoundtag.
    public static final StreamCodec<ByteBuf, MountRecord> BASIC_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG, MountRecord::compoundTag,
            ByteBufCodecs.STRING_UTF8, MountRecord::uuid,
            MountRecord::new
    );
}