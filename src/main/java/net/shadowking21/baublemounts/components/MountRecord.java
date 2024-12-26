package net.shadowking21.baublemounts.components;

import net.minecraft.nbt.CompoundTag;

public record MountRecord(CompoundTag compoundTag, String uuid) {
    public static MountRecord DEFAULT = new MountRecord(
            new CompoundTag(), ""
    );
}