package dev.dancecraft.mixin;

import dev.dancecraft.DanceState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin {
    @Shadow @Final public ModelPart rightArm;
    @Shadow @Final public ModelPart leftArm;
    @Shadow @Final public ModelPart rightLeg;
    @Shadow @Final public ModelPart leftLeg;
    @Shadow @Final public ModelPart body;
    @Shadow @Final public ModelPart head;

    @Inject(method = "setupAnim", at = @At("TAIL"))
    private void dancecraft$animate(HumanoidRenderState state, CallbackInfo ci) {
        if (state.entityType != EntityType.PLAYER) return;
        Minecraft client = Minecraft.getInstance();
        if (client.level == null) return;

        // 1.21.11 renders players from HumanoidRenderState rather than passing
        // the live Player entity into setupAnim. Match the render-state position
        // to the client-side player to recover the UUID used by DanceState.
        Player dancer = null;
        double bestDistance = 0.04D;
        for (Player candidate : client.level.players()) {
            double dx = candidate.getX() - state.x;
            double dy = candidate.getY() - state.y;
            double dz = candidate.getZ() - state.z;
            double distance = dx * dx + dy * dy + dz * dz;
            if (distance < bestDistance) {
                bestDistance = distance;
                dancer = candidate;
            }
        }
        if (dancer == null || !DanceState.isDancing(dancer.getUUID())) return;

        float t = DanceState.danceTime(dancer.getUUID());
        float cycle = t % 8.0f;
        float wave = (float) Math.sin(t * Math.PI * 2.0f / 1.15f);
        float sway = (float) Math.sin(t * Math.PI * 2.0f / 2.0f);

        float armLift;
        if (cycle < 1.0f) {
            armLift = -0.10f - 1.15f * smooth(cycle);
        } else if (cycle < 5.8f) {
            armLift = -1.15f - 0.24f * wave;
        } else {
            float p = smooth((cycle - 5.8f) / 2.2f);
            armLift = -1.15f * (1.0f - p) - 0.10f * p;
        }

        rightArm.xRot = armLift + 0.16f * wave;
        leftArm.xRot = armLift - 0.16f * wave;
        rightArm.yRot = -0.12f - 0.20f * sway;
        leftArm.yRot = 0.12f + 0.20f * sway;
        rightArm.zRot = -0.18f + 0.13f * wave;
        leftArm.zRot = 0.18f - 0.13f * wave;
        body.yRot = 0.10f * sway;
        body.zRot = 0.035f * wave;
        head.yRot += 0.035f * sway;
        rightLeg.xRot = 0.22f * wave;
        leftLeg.xRot = -0.22f * wave;
        rightLeg.yRot = -0.05f * sway;
        leftLeg.yRot = 0.05f * sway;
    }

    private static float smooth(float value) {
        float x = Math.max(0.0f, Math.min(1.0f, value));
        return x * x * (3.0f - 2.0f * x);
    }
}
