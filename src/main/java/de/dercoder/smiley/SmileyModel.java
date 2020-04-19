package de.dercoder.smiley;

import com.google.common.base.Preconditions;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public final class SmileyModel {
    private String name;
    private List<SmileyPiece> smileyPieces;
    private int speed;

    public SmileyModel() {

    }

    public SmileyModel(
            String name,
            List<SmileyPiece> smileyPieces,
            int speed
    ) {
        this.name = name;
        this.smileyPieces = smileyPieces;
        this.speed = speed;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSmileyPieces(List<SmileyPiece> smileyPieces) {
        this.smileyPieces = smileyPieces;
    }

    public List<SmileyPiece> getSmileyPieces() {
        return smileyPieces;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }

    public Smiley toSmiley() {
        return Smiley.of(
                name,
                parseSmileyPieces(),
                speed
        );
    }

    private List<ItemStack> parseSmileyPieces() {
        return smileyPieces.stream()
                .map(this::pieceToItemStack)
                .collect(Collectors.toList());
    }

    private ItemStack pieceToItemStack(SmileyPiece smileyPiece) {
        var itemStack = new ItemStack(Material.PLAYER_HEAD);
        var itemMeta = itemStack.getItemMeta();
        var gameProfile = profileFromPiece(smileyPiece);
        profileToMeta(itemMeta, gameProfile);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private GameProfile profileFromPiece(SmileyPiece smileyPiece) {
        var gameProfile = new GameProfile(UUID.randomUUID(), "");
        gameProfile.getProperties().put("textures",
                new Property("textures",
                        smileyPiece.getTextureValue(),
                        smileyPiece.getTextureSignature()
                )
        );
        return gameProfile;
    }

    private void profileToMeta(ItemMeta itemMeta, GameProfile gameProfile) {
        try {
            var profileField = itemMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(itemMeta, gameProfile);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static SmileyModel ofSmiley(Smiley smiley) {
        Preconditions.checkNotNull(smiley);
        return new SmileyModel(
                smiley.name(),
                parseItemStacks(smiley.itemStacks()),
                smiley.speed()
        );
    }

    private static List<SmileyPiece> parseItemStacks(List<ItemStack> itemStacks) {
        return itemStacks.stream()
                .map(SmileyModel::itemStackToPiece)
                .collect(Collectors.toList());
    }

    private static SmileyPiece itemStackToPiece(ItemStack itemStack) {
        var gameProfile = profileFromMeta(itemStack.getItemMeta());
        var gameProfileProperty = gameProfile.getProperties()
                .get("textures").iterator().next();
        var textureValue = gameProfileProperty.getValue();
        var textureSignature = gameProfileProperty.getSignature();
        return new SmileyPiece(textureValue, textureSignature);
    }

    private static GameProfile profileFromMeta(ItemMeta itemMeta) {
        try {
            var profileField = itemMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            return (GameProfile) profileField.get(itemMeta);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
