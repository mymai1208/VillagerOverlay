package net.mymai1208.villageroverlay.mixin.impl;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.village.VillagerProfession;
import net.mymai1208.villageroverlay.VillagerOverlay;
import net.mymai1208.villageroverlay.mixin.IMinecraftClientMixin;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin implements IMinecraftClientMixin {
    @Shadow @Nullable public HitResult crosshairTarget;

    @Shadow @Nullable public ClientPlayerInteractionManager interactionManager;

    @Shadow @Nullable public ClientPlayerEntity player;

    @Override
    public HitResult villagerOverlay$getCrossHairTarget() {
        return crosshairTarget;
    }

    private static final List<VillagerProfession> NO_JOBS = Arrays.asList(VillagerProfession.NONE, VillagerProfession.NITWIT);

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;tick()V"))
    public void tick(CallbackInfo ci) {
        if(crosshairTarget != null) {
            if (crosshairTarget.getType() != HitResult.Type.ENTITY) {
                return;
            }

            EntityHitResult entityHitResult = (EntityHitResult) this.crosshairTarget;

            if (!(entityHitResult.getEntity() instanceof VillagerEntity villageEntity)) {
                return;
            }

            if (NO_JOBS.contains(villageEntity.getVillagerData().getProfession())) {
                return;
            }

            if (VillagerOverlay.getOpenedVillagers().contains(villageEntity.getUuid())) {
                return;
            }

            if(VillagerOverlay.getCurrentOpenVillager() != null) {
                return;
            }

            if (interactionManager == null) {
                return;
            }

            if (interactionManager.interactEntityAtLocation(player, villageEntity, entityHitResult, Hand.MAIN_HAND).isAccepted()) {
                VillagerOverlay.setCurrentOpenVillager(villageEntity.getUuid());

                VillagerOverlay.getLOGGER().info("Auto opened villager: " + villageEntity.getUuid());

                return;
            }

            if (interactionManager.interactEntity(player, villageEntity, Hand.MAIN_HAND).isAccepted()) {
                VillagerOverlay.setCurrentOpenVillager(villageEntity.getUuid());

                VillagerOverlay.getLOGGER().info("Auto opened villager: " + villageEntity.getUuid());
            }
        }
    }
}