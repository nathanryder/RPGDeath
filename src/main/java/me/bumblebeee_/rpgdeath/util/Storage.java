package me.bumblebeee_.rpgdeath.util;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Storage {

    private static @Getter Map<UUID, List<ItemStack>> items = new HashMap<>();

    public void addArmorItem(UUID uuid, ItemStack item) {
        if (!items.containsKey(uuid)) {
            items.put(uuid, new ArrayList<>(Collections.singletonList(item)));
            return;
        }

        List<ItemStack> items = getItems().get(uuid);
        items.add(item);
        getItems().put(uuid, items);
    }

    public List<ItemStack> getArmorItems(UUID uuid) {
        return items.get(uuid);
    }

    public void saveArmor() {
        //TODO save on shutdown
    }

    public void loadArmor() {
        //TODO load on start
    }

    public String getItemType(ItemStack i) {
        switch (i.getType()) {
            case CHAINMAIL_CHESTPLATE:
                return "chestplate";
            case DIAMOND_CHESTPLATE:
                return "chestplate";
            case GOLD_CHESTPLATE:
                return "chestplate";
            case IRON_CHESTPLATE:
                return "chestplate";
            case LEATHER_CHESTPLATE:
                return "chestplate";
            case CHAINMAIL_HELMET:
                return "helmet";
            case DIAMOND_HELMET:
                return "helmet";
            case GOLD_HELMET:
                return "helmet";
            case IRON_HELMET:
                return "helmet";
            case LEATHER_HELMET:
                return "helmet";
            case CHAINMAIL_LEGGINGS:
                return "leggings";
            case DIAMOND_LEGGINGS:
                return "leggings";
            case GOLD_LEGGINGS:
                return "leggings";
            case IRON_LEGGINGS:
                return "leggings";
            case LEATHER_LEGGINGS:
                return "leggings";
            case DIAMOND_BOOTS:
                return "boots";
            case GOLD_BOOTS:
                return "boots";
            case IRON_BOOTS:
                return "boots";
            case LEATHER_BOOTS:
                return "boots";
            default:
                return "failed";
        }
    }

}
