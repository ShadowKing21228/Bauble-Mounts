package net.shadowking21.baublemounts.components;

import net.minecraft.nbt.CompoundTag;

import java.util.Objects;
import java.util.UUID;

// A record example
public record MountRecord(CompoundTag compoundTag, String uuid) { }