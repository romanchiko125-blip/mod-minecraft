package com.example.cityvillages;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.entity.villager.AbstractVillagerEntity;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mod(CityVillagesMod.MODID)
public class CityVillagesMod {
    public static final String MODID = "cityvillages";
    private static final Random RANDOM = new Random();

    public CityVillagesMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> ForgeRegistries.BIOMES.getValues().forEach(this::boostBiomeSpawns));
    }

    private void boostBiomeSpawns(Biome biome) {
        // More common passive mobs.
        biome.getSpawns(EntityClassification.CREATURE).add(new Biome.SpawnListEntry(EntityType.SHEEP, 28, 8, 14));
        biome.getSpawns(EntityClassification.CREATURE).add(new Biome.SpawnListEntry(EntityType.PIG, 26, 8, 14));
        biome.getSpawns(EntityClassification.CREATURE).add(new Biome.SpawnListEntry(EntityType.COW, 26, 8, 14));
        biome.getSpawns(EntityClassification.CREATURE).add(new Biome.SpawnListEntry(EntityType.CHICKEN, 24, 8, 16));
        biome.getSpawns(EntityClassification.CREATURE).add(new Biome.SpawnListEntry(EntityType.WOLF, 18, 4, 10));
        biome.getSpawns(EntityClassification.WATER_CREATURE).add(new Biome.SpawnListEntry(EntityType.COD, 34, 8, 18));
        biome.getSpawns(EntityClassification.WATER_CREATURE).add(new Biome.SpawnListEntry(EntityType.SALMON, 28, 8, 14));

        // Village-related population boost near village biomes.
        biome.getSpawns(EntityClassification.CREATURE).add(new Biome.SpawnListEntry(EntityType.CAT, 20, 3, 6));
        biome.getSpawns(EntityClassification.CREATURE).add(new Biome.SpawnListEntry(EntityType.VILLAGER, 20, 4, 8));
        biome.getSpawns(EntityClassification.CREATURE).add(new Biome.SpawnListEntry(EntityType.IRON_GOLEM, 10, 1, 3));
    }

    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
        if (!(event.getEntityLiving() instanceof AnimalEntity
                || event.getEntityLiving() instanceof MonsterEntity
                || event.getEntityLiving() instanceof AbstractFishEntity
                || event.getEntityLiving() instanceof WolfEntity
                || event.getEntityLiving() instanceof AbstractVillagerEntity)) {
            return;
        }

        if (event.getDrops().isEmpty()) {
            return;
        }

        List<ItemEntity> duplicates = new ArrayList<>();
        int multiplier = 1 + RANDOM.nextInt(2); // +100% to +200%
        for (ItemEntity original : event.getDrops()) {
            for (int i = 0; i < multiplier; i++) {
                duplicates.add(new ItemEntity(event.getEntity().world,
                        event.getEntity().getPosX(),
                        event.getEntity().getPosY(),
                        event.getEntity().getPosZ(),
                        original.getItem().copy()));
            }
        }
        event.getDrops().addAll(duplicates);
    }
}
