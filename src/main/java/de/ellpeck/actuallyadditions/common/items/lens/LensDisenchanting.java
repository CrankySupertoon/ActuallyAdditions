package de.ellpeck.actuallyadditions.common.items.lens;

import java.util.List;
import java.util.Map;

import de.ellpeck.actuallyadditions.api.internal.IAtomicReconstructor;
import de.ellpeck.actuallyadditions.api.lens.Lens;
import de.ellpeck.actuallyadditions.common.util.ItemUtil;
import de.ellpeck.actuallyadditions.common.util.StackUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class LensDisenchanting extends Lens {

    public static final int ENERGY_USE = 250000;

    @Override
    public boolean invoke(IBlockState hitState, BlockPos hitBlock, IAtomicReconstructor tile) {
        if (tile.getEnergy() >= ENERGY_USE) {
            List<EntityItem> items = tile.getWorldObject().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(hitBlock.getX(), hitBlock.getY(), hitBlock.getZ(), hitBlock.getX() + 1, hitBlock.getY() + 1, hitBlock.getZ() + 1));
            if (items != null && !items.isEmpty()) {
                EntityItem book = null;
                EntityItem toDisenchant = null;
                for (EntityItem item : items) {
                    if (item != null && !item.isDead) {
                        ItemStack stack = item.getItem();
                        if (StackUtil.isValid(stack) && stack.getCount() == 1) {
                            Item stackItem = stack.getItem();
                            if (stackItem == Items.BOOK || stackItem == Items.ENCHANTED_BOOK) {
                                if (book == null) {
                                    book = item;
                                } else {
                                    return false;
                                }
                            } else {
                                Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(stack);
                                if (enchants != null && !enchants.isEmpty()) {
                                    if (toDisenchant == null) {
                                        toDisenchant = item;
                                    } else {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }

                if (book != null && toDisenchant != null) {
                    ItemStack disenchantStack = toDisenchant.getItem();
                    ItemStack bookStack = book.getItem();

                    Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(disenchantStack);
                    if (enchants != null && !enchants.isEmpty()) {
                        Enchantment enchant = enchants.keySet().iterator().next();
                        int level = enchants.get(enchant);

                        ItemStack newDisenchantStack = disenchantStack.copy();
                        ItemStack newBookStack;
                        if (bookStack.getItem() == Items.BOOK) {
                            newBookStack = new ItemStack(Items.ENCHANTED_BOOK);
                        } else {
                            newBookStack = bookStack.copy();
                        }

                        ItemUtil.removeEnchantment(newDisenchantStack, enchant);
                        ItemEnchantedBook.addEnchantment(newBookStack, new EnchantmentData(enchant, level));

                        EntityItem disenchanted = new EntityItem(toDisenchant.getEntityWorld(), toDisenchant.posX, toDisenchant.posY, toDisenchant.posZ, newDisenchantStack);
                        EntityItem newBook = new EntityItem(book.getEntityWorld(), book.posX, book.posY, book.posZ, newBookStack);
                        toDisenchant.setDead();
                        book.setDead();
                        tile.getWorldObject().spawnEntity(newBook);
                        tile.getWorldObject().spawnEntity(disenchanted);

                        tile.extractEnergy(ENERGY_USE);

                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public float[] getColor() {
        return new float[] { 234F / 255F, 173F / 255F, 255F / 255F };
    }

    @Override
    public int getDistance() {
        return 5;
    }

    @Override
    public boolean canInvoke(IAtomicReconstructor tile, EnumFacing sideToShootTo, int energyUsePerShot) {
        return tile.getEnergy() - energyUsePerShot >= ENERGY_USE;
    }
}
