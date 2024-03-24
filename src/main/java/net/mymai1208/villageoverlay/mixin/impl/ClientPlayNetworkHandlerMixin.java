package net.mymai1208.villageoverlay.mixin.impl;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.network.packet.s2c.play.SetTradeOffersS2CPacket;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.hit.EntityHitResult;
import net.mymai1208.villageoverlay.mixin.IMinecraftClientMixin;
import net.mymai1208.villageroverlay.VillagerOverlay;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Shadow @Final private MinecraftClient client;
    @Inject(method = "onSetTradeOffers", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/MerchantScreenHandler;setCanRefreshTrades(Z)V"))
    public void onSetTradeOffers(SetTradeOffersS2CPacket packet, CallbackInfo ci) {
        if(packet.getSyncId() != client.player.currentScreenHandler.syncId) {
            return;
        }

        IMinecraftClientMixin mcClient = (IMinecraftClientMixin)client;

        if(!(mcClient.villagerOverlay$getCrossHairTarget() instanceof EntityHitResult entityHitResult)) {
            return;
        }

        if(!(entityHitResult.getEntity() instanceof VillagerEntity villagerEntity)) {
            return;
        }

        if(!VillagerOverlay.getOpenedVillagers().contains(villagerEntity.getUuid())) {
            villagerEntity.setOffers(packet.getOffers());

            System.out.println("set trade offers");

            VillagerOverlay.getOpenedVillagers().add(villagerEntity.getUuid());

            if(VillagerOverlay.getCurrentOpenVillager() != null) {
                VillagerOverlay.setCurrentOpenVillager(null);
                client.player.closeHandledScreen();
            }
        }
    }
}
