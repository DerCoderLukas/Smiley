package de.dercoder.smiley;

import com.google.common.base.Preconditions;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public final class Smiley {
    private String name;
    private List<ItemStack> itemStacks;
    private int speed;

    private Smiley(
            String name,
            List<ItemStack> itemStacks,
            int speed
    ) {
        this.name = name;
        this.itemStacks = itemStacks;
        this.speed = speed;
    }

    public String name() {
        return name;
    }

    public List<ItemStack> itemStacks() {
        return List.copyOf(itemStacks);
    }

    public int speed() {
        return speed;
    }

    public static Smiley of(
            String name,
            List<ItemStack> itemStacks,
            int speed
    ) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(itemStacks);
        return new Smiley(name, itemStacks, speed);
    }
}
