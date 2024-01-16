// scraped from https://github.com/Hennamann/Fog-Tweaker/blob/master/src/main/java/com/henrikstabell/fogtweaker/FogTweaker.java
package com.gmail.guitaekm.technoenderling.mixin;

import com.gmail.guitaekm.technoenderling.config.FogBiomeSetting;
import com.gmail.guitaekm.technoenderling.config.FogBiomes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import net.minecraft.entity.Entity;

import java.util.Optional;

@Mixin(BackgroundRenderer.class)
public abstract class FogHandler {
    @Unique
    private static int BLOCKS_PER_TICK = 4;
    @Unique
    private static Optional<Long> timestamp = Optional.empty();

    @Unique
    private static Optional<FogBiomeSetting> biomeSetting = Optional.empty();

    @Inject(method = "applyFog", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", ordinal = 0, shift = Shift.AFTER, target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogEnd(F)V", remap = false), remap = false)
    private static void applyFogModifyWaterEnd(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, CallbackInfo ci, CameraSubmersionType cameraSubmersionType, Entity entity, float end)
    {
        if (entity == null || !(entity instanceof PlayerEntity) || !entity.getWorld().isClient()) {
            return;
        }
        if (thickFog || fogType == BackgroundRenderer.FogType.FOG_SKY) {
            return;
        }
        Optional<Float> modified = getFogEndDistance(entity, -8.0F, end);
        if (modified.isPresent()) {
            RenderSystem.setShaderFogEnd(modified.get());
        }
    }

    @Inject(method = "applyFog", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", ordinal = 1, shift = Shift.AFTER, target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V", remap = false), remap = false)
    private static void applyFogModifyStart(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, CallbackInfo info, CameraSubmersionType cameraSubmersionType, Entity entity, float start, float end)
    {
        if (entity == null || !(entity instanceof PlayerEntity) || !entity.getWorld().isClient()) {
            return;
        }
        if (thickFog || fogType == BackgroundRenderer.FogType.FOG_SKY) {
            return;
        }
        Optional<Float> modified = getFogStartDistance(entity, start, end);
        if (modified.isPresent()) {
            RenderSystem.setShaderFogStart(modified.get());
        }
    }

    @Inject(method = "applyFog", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", ordinal = 1, shift = Shift.AFTER, target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogEnd(F)V", remap = false), remap = false)
    private static void applyFogModifyEnd(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, CallbackInfo info, CameraSubmersionType cameraSubmersionType, Entity entity, float start, float end)
    {
        if (entity == null || !(entity instanceof PlayerEntity) || !entity.getWorld().isClient()) {
            return;
        }
        if (thickFog || fogType == BackgroundRenderer.FogType.FOG_SKY) {
            return;
        }
        Optional<Float> modified = getFogEndDistance(entity, start, end);
        if (modified.isPresent()) {
            RenderSystem.setShaderFogEnd(modified.get());
        }
    }

    @Unique
    private static FogBiomeSetting getResultingSettings(Entity entity, float start, float end) {
        if (entity == null) {
            // shouldn't happen, should probably throw an error
            return null;
        }
        Identifier biomeIdentifier = entity.getWorld().getBiomeKey(entity.getBlockPos()).get().getValue();
        String newBiome = biomeIdentifier.toString();
        if (FogBiomes.getBiomeSetting(newBiome).isPresent()) {
            return FogBiomes.getBiomeSetting(newBiome).get();
        } else {
            return new FogBiomeSetting(start, end);
        }
    }

    @Unique
    private static void initializeTime(Entity entity) {
        if (FogHandler.timestamp.isEmpty()) {
            FogHandler.timestamp = Optional.of(entity.getWorld().getTime());
        }
    }

    /**
     * steps the fog settings one tick if not already done this tick
     * read the result in FogHandler.biomeSetting
     * @param endSetting where to converge to
     * @param entity the player entity
     * @param start the values calculated from BackgroundRenderer
     * @param end the values calculated from BackgroundRenderer
     */
    @Unique
    private static void stepFogSetting(
            FogBiomeSetting endSetting,
            Entity entity,
            float start,
            float end
            ) {
        if (entity == null) {
            return;
        }
        if (FogHandler.biomeSetting.isEmpty()) {
            FogHandler.biomeSetting = Optional.of(FogHandler.getResultingSettings(entity, start, end));
        }
        FogBiomeSetting startSetting = FogHandler.biomeSetting.get();
        FogHandler.initializeTime(entity);
        if (entity.getWorld().getTime() <= FogHandler.timestamp.get()) {
            return;
        }
        // don't do anything for small differences, else it will flash
        float differenceMin = endSetting.minDistance() - startSetting.minDistance();
        boolean increaseMin = differenceMin > FogHandler.BLOCKS_PER_TICK;
        boolean decreaseMin = differenceMin < -FogHandler.BLOCKS_PER_TICK;
        float min = startSetting.minDistance();
        min += increaseMin ? FogHandler.BLOCKS_PER_TICK : 0;
        min -= decreaseMin ? FogHandler.BLOCKS_PER_TICK : 0;
        if (!increaseMin && !decreaseMin) {
            min = endSetting.minDistance();
        }

        float differenceMax = endSetting.maxDistance() - startSetting.maxDistance();
        boolean increaseMax = differenceMax > FogHandler.BLOCKS_PER_TICK;
        boolean decreaseMax = differenceMax < -FogHandler.BLOCKS_PER_TICK;
        float max = startSetting.maxDistance();
        max += increaseMax ? FogHandler.BLOCKS_PER_TICK : 0;
        max -= decreaseMax ? FogHandler.BLOCKS_PER_TICK : 0;
        if (!increaseMax && !decreaseMax) {
            max = endSetting.maxDistance();
        }
        FogBiomeSetting newSetting = new FogBiomeSetting(min, max);
        FogHandler.biomeSetting = Optional.of(newSetting);
        FogHandler.timestamp = FogHandler.timestamp.map((Long timestamp)->timestamp + 1);
    }

    @Unique
    private static Optional<Float> getFogStartDistance(Entity entity, float start, float end) {
        FogHandler.stepFogSetting(getResultingSettings(entity, start, end), entity, start, end);
        return FogHandler.biomeSetting.map(FogBiomeSetting::minDistance);
    }
    @Unique
    private static Optional<Float> getFogEndDistance(Entity entity, float start, float end) {
        FogHandler.stepFogSetting(getResultingSettings(entity, start, end), entity, start, end);
        return FogHandler.biomeSetting.map(FogBiomeSetting::maxDistance);
    }
}
