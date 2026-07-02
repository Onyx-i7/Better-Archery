package com.onyxi7.betterarchery.event;

import com.onyxi7.betterarchery.entities.*;
import com.onyxi7.betterarchery.items.bows.CustomBow;
import com.onyxi7.betterarchery.items.ItemQuiverWithArrows;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BowEventHandler {
    
    // === ARROW NOCK EVENT - Allow loading bow if quiver has arrows ===
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onArrowNock(ArrowNockEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack bowStack = event.getBow();
        
        if (bowStack.isEmpty() || !(bowStack.getItem() instanceof ItemBow)) {
            return;
        }
        
        // Check if vanilla would allow it (arrows in inventory)
        boolean hasVanillaArrows = hasArrowsInInventory(player);
        
        if (hasVanillaArrows) {
            return; // Let vanilla handle it
        }
        
        // Check if player has Infinity
        boolean hasInfinity = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, bowStack) > 0;
        
        // Check if quiver has arrows
        ItemStack quiverArrow = ItemQuiverWithArrows.peekArrow(player);
        boolean hasQuiverArrows = !quiverArrow.isEmpty();
        
        // If has Infinity OR quiver has arrows, allow the nock
        if (hasInfinity || hasQuiverArrows) {
            player.setActiveHand(event.getHand());
            event.setAction(new ActionResult<>(EnumActionResult.SUCCESS, bowStack));
            event.setCanceled(true); // Cancel vanilla behavior, we handled it
        }
    }
    
    // === ARROW LOOSE EVENT - Use quiver arrows when shooting ===
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onArrowLoose(ArrowLooseEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        World world = player.world;
        ItemStack bowStack = event.getBow();
        
        if (bowStack.isEmpty() || !(bowStack.getItem() instanceof ItemBow)) {
            return;
        }
        
        // Check if player has vanilla arrows
        boolean hasVanillaArrows = hasArrowsInInventory(player);
        
        if (hasVanillaArrows) {
            return; // Let vanilla handle it
        }
        
        // Check Infinity
        boolean hasInfinity = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, bowStack) > 0;
        
        // Try to get arrow from quiver
        ItemStack arrowFromQuiver = ItemQuiverWithArrows.supplyArrow(player);
        
        if (arrowFromQuiver.isEmpty() && !hasInfinity) {
            event.setCanceled(true);
            return;
        }
        
        // Handle the shot manually
        if (!arrowFromQuiver.isEmpty()) {
            float charge = (float) event.getCharge() / 20.0F;
            charge = (charge * charge + charge * 2.0F) / 3.0F;
            
            if (charge < 0.1D) {
                return;
            }
            
            if (charge > 1.0F) {
                charge = 1.0F;
            }
            
            // Create arrow entity
            EntityArrow arrow = createArrow(world, arrowFromQuiver, player);
            
            // Get bow multipliers
            float arrowSpeedMult = getArrowSpeedMultiplier(bowStack);
            float damageMult = getDamageMultiplier(bowStack);
            
            // Shoot arrow with custom speed
            arrow.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, charge * 3.0F * arrowSpeedMult, 1.0F);
            
            if (charge == 1.0F) {
                arrow.setIsCritical(true);
            }
            
            // Apply enchantments with damage multiplier
            int powerLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, bowStack);
            if (powerLevel > 0) {
                arrow.setDamage((arrow.getDamage() + (double) powerLevel * 0.5D + 0.5D) * damageMult);
            } else {
                arrow.setDamage(arrow.getDamage() * damageMult);
            }
            
            int punchLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, bowStack);
            if (punchLevel > 0) {
                arrow.setKnockbackStrength(punchLevel);
            }
            
            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, bowStack) > 0) {
                arrow.setFire(100);
            }
            
            // Damage bow
            bowStack.damageItem(1, player);
            
            world.spawnEntity(arrow);
            
            event.setCanceled(true);
        }
    }
    
    // === HELPER METHODS ===
    
    private boolean hasArrowsInInventory(EntityPlayer player) {
        // Check main inventory
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (!stack.isEmpty() && (stack.getItem() instanceof ItemArrow || stack.getItem() == Items.ARROW)) {
                return true;
            }
        }
        
        // Check off-hand
        ItemStack offhand = player.getHeldItemOffhand();
        if (!offhand.isEmpty() && (offhand.getItem() instanceof ItemArrow || offhand.getItem() == Items.ARROW)) {
            return true;
        }
        
        return false;
    }
    
    private float getArrowSpeedMultiplier(ItemStack bowStack) {
        if (bowStack.isEmpty() || !(bowStack.getItem() instanceof CustomBow)) {
            return 1.0F; // Vanilla bow
        }
        
        String registryName = bowStack.getItem().getRegistryName().toString();
        
        // Return multipliers based on bow type
        if (registryName.contains("long_bow")) {
            return 2.0F;
        } else if (registryName.contains("recurve_bow")) {
            return 1.25F;
        } else if (registryName.contains("composite_bow")) {
            return 0.65F;
        } else if (registryName.contains("yumi_bow")) {
            return 1.5F;
        }
        
        return 1.0F;
    }
    
    private float getDamageMultiplier(ItemStack bowStack) {
        if (bowStack.isEmpty() || !(bowStack.getItem() instanceof CustomBow)) {
            return 1.0F; // Vanilla bow
        }
        
        String registryName = bowStack.getItem().getRegistryName().toString();
        
        // Return multipliers based on bow type
        if (registryName.contains("long_bow")) {
            return 1.0F;
        } else if (registryName.contains("recurve_bow")) {
            return 1.1F;
        } else if (registryName.contains("composite_bow")) {
            return 1.25F;
        } else if (registryName.contains("yumi_bow")) {
            return 1.0F;
        }
        
        return 1.0F;
    }
    
    private EntityArrow createArrow(World world, ItemStack arrowStack, EntityPlayer player) {
        ItemArrow itemarrow = (ItemArrow) ((arrowStack.getItem() instanceof ItemArrow) ? arrowStack.getItem() : Items.ARROW);
        EntityArrow entityarrow = itemarrow.createArrow(world, arrowStack, player);
        
        // Check if it's a special arrow from our mod
        String arrowType = arrowStack.getItem().getRegistryName().toString();
        
        if (arrowType.contains("fire_arrow")) {
            entityarrow = new EntityFireArrow(world, player);
            entityarrow.setFire(100);
        } else if (arrowType.contains("torch_arrow")) {
            entityarrow = new EntityTorchArrow(world, player);
        } else if (arrowType.contains("impact_arrow")) {
            entityarrow = new EntityImpactArrow(world, player);
        } else if (arrowType.contains("ender_arrow")) {
            entityarrow = new EntityEnderArrow(world, player);
        } else if (arrowType.contains("splitting_arrow")) {
            entityarrow = new EntitySplittingArrow(world, player);
        } else if (arrowType.contains("drill_arrow")) {
            entityarrow = new EntityDrillArrow(world, player);
        } else if (arrowType.contains("potion_arrow")) {
            entityarrow = new EntityPotionArrow(world, player);
            if (arrowStack.hasTagCompound()) {
                entityarrow.getEntityData().setTag("PotionData", arrowStack.getTagCompound());
            }
        }
        
        return entityarrow;
    }
}
