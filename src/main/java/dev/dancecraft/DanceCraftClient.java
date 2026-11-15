package dev.dancecraft;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.glfw.GLFW;

public final class DanceCraftClient implements ClientModInitializer {
    private static KeyMapping selfDanceKey;
    private static KeyMapping targetDanceKey;

    @Override
    public void onInitializeClient() {
        KeyMapping.Category category = KeyMapping.Category.register(
                Identifier.fromNamespaceAndPath("dancecraft", "controls")
        );

        selfDanceKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.dancecraft.toggle_self",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
                category
        ));

        targetDanceKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.dancecraft.toggle_target",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_N,
                category
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.level == null) return;

            while (selfDanceKey.consumeClick()) {
                boolean nowDancing = DanceState.toggle(client.player.getUUID());
                client.player.displayClientMessage(
                        Component.literal(nowDancing ? "DanceCraft: танец включён" : "DanceCraft: танец выключен"),
                        true
                );
            }

            while (targetDanceKey.consumeClick()) {
                Entity pointed = client.crosshairPickEntity;
                if (pointed instanceof Player target && target != client.player) {
                    boolean nowDancing = DanceState.toggle(target.getUUID());
                    client.player.displayClientMessage(
                            Component.literal((nowDancing ? "Локально включён танец: " : "Локально выключен танец: ")
                                    + target.getName().getString()),
                            true
                    );
                } else {
                    client.player.displayClientMessage(
                            Component.literal("Наведи прицел на игрока и нажми N"),
                            true
                    );
                }
            }

            DanceState.tick();
        });
    }
}
