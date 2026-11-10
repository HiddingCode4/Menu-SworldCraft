package com.sworldcraft.menu;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.minecraft.client.gui.screens.TitleScreen;

@Mod(value = SworldcraftMenu.ID, dist = Dist.CLIENT)
public final class SworldcraftMenu {
    public static final String ID = "sworldcraft_menu";

    @EventBusSubscriber(modid = ID, value = Dist.CLIENT)
    public static final class Events {
        @SubscribeEvent
        public static void opening(ScreenEvent.Opening event) {
            // Exact class only: leave other mods' custom title screens alone.
            if (event.getNewScreen() != null && event.getNewScreen().getClass() == TitleScreen.class) {
                event.setNewScreen(new RunicTitleScreen());
            }
        }
    }
}
