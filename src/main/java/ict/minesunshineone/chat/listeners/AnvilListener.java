package ict.minesunshineone.chat.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ict.minesunshineone.chat.utils.ComponentUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;

public class AnvilListener implements Listener {

    @EventHandler
    public void onAnvilPrepare(PrepareAnvilEvent event) {
        ItemStack result = event.getResult();
        if (result == null) {
            return;
        }

        Player player = (Player) event.getView().getPlayer();
        if (!player.hasPermission("simplechat.anvil.color")) {
            return;
        }

        ItemMeta meta = result.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) {
            return;
        }

        String name = meta.getDisplayName();
        Component coloredName = ComponentUtils.legacySerializer().deserialize(name);
        if (!name.contains("&o")) {
            coloredName = coloredName.decoration(TextDecoration.ITALIC, false);
        }

        meta.displayName(coloredName);
        result.setItemMeta(meta);
        event.setResult(result);
    }
}
