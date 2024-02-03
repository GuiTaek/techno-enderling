package com.gmail.guitaekm.technoenderling.mixin;

import com.gmail.guitaekm.technoenderling.access.IServerPlayerEntityAccess;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements IServerPlayerEntityAccess {
    @Unique
    private @Nullable BlockPos lastUsedPocketPortal = null;

    @Override
    public void technoEnderling$setLastUsedPocketPortal(@Nullable BlockPos position) {
        this.lastUsedPocketPortal = position;
    }

    @Override
    public @Nullable BlockPos technoEnderling$getLastUsedPocketPortal() {
        return this.lastUsedPocketPortal;
    }

    @Inject(method = "writeCustomDataToNbt", at=@At("TAIL"))
    public void writeCustomDataToNbtTail(NbtCompound nbt, CallbackInfo ci) {
        if (this.lastUsedPocketPortal == null) {
            return;
        }
        NbtCompound nbtCompound = new NbtCompound();

        nbtCompound.putDouble("x", this.lastUsedPocketPortal.getX());
        nbtCompound.putDouble("y", this.lastUsedPocketPortal.getY());
        nbtCompound.putDouble("z", this.lastUsedPocketPortal.getZ());
        nbt.put("lastUsedPocketPortal", nbtCompound);
    }

    @Inject(method = "readCustomDataFromNbt", at=@At("TAIL"))
    public void readCustomDataFromNbtTail(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("lastUsedPocketPortal", 10)) {
            NbtCompound nbtCompound = nbt.getCompound("lastUsedPocketPortal");
            this.lastUsedPocketPortal = new BlockPos(nbtCompound.getDouble("x"), nbtCompound.getDouble("y"), nbtCompound.getDouble("z"));
        }
    }
}
