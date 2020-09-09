package de.ellpeck.actuallyadditions.common.inventory.gui;

import java.util.Arrays;

import de.ellpeck.actuallyadditions.common.ActuallyAdditions;
import de.ellpeck.actuallyadditions.common.inventory.ContainerFeeder;
import de.ellpeck.actuallyadditions.common.tile.TileEntityBase;
import de.ellpeck.actuallyadditions.common.tile.TileEntityFeeder;
import de.ellpeck.actuallyadditions.common.util.AssetUtil;
import de.ellpeck.actuallyadditions.common.util.StringUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiFeeder extends GuiWtfMojang {

    private static final ResourceLocation RES_LOC = AssetUtil.getGuiLocation("gui_feeder");
    public final TileEntityFeeder tileFeeder;

    public GuiFeeder(InventoryPlayer inventory, TileEntityBase tile) {
        super(new ContainerFeeder(inventory, tile));
        this.tileFeeder = (TileEntityFeeder) tile;
        this.xSize = 176;
        this.ySize = 70 + 86;
    }

    @Override
    public void drawScreen(int x, int y, float f) {
        super.drawScreen(x, y, f);
        if (x >= this.guiLeft + 69 && y >= this.guiTop + 30 && x <= this.guiLeft + 69 + 10 && y <= this.guiTop + 30 + 10) {
            String[] array = new String[] { this.tileFeeder.currentAnimalAmount + " " + StringUtil.localize("info." + ActuallyAdditions.MODID + ".gui.animals"), this.tileFeeder.currentAnimalAmount >= 2 && this.tileFeeder.currentAnimalAmount < TileEntityFeeder.THRESHOLD ? StringUtil.localize("info." + ActuallyAdditions.MODID + ".gui.enoughToBreed") : this.tileFeeder.currentAnimalAmount >= TileEntityFeeder.THRESHOLD ? StringUtil.localize("info." + ActuallyAdditions.MODID + ".gui.tooMany") : StringUtil.localize("info." + ActuallyAdditions.MODID + ".gui.notEnough") };
            this.drawHoveringText(Arrays.asList(array), x, y);
        }
    }

    @Override
    public void drawGuiContainerForegroundLayer(int x, int y) {
        AssetUtil.displayNameString(this.fontRenderer, this.xSize, -10, this.tileFeeder);
    }

    @Override
    public void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(AssetUtil.GUI_INVENTORY_LOCATION);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop + 70, 0, 0, 176, 86);
        this.mc.getTextureManager().bindTexture(RES_LOC);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 176, 70);

        if (this.tileFeeder.currentTimer > 0) {
            int i = this.tileFeeder.getCurrentTimerToScale(20);
            this.drawTexturedModalRect(this.guiLeft + 85, this.guiTop + 42 - i, 181, 19 + 19 - i, 6, 20);
        }

        if (this.tileFeeder.currentAnimalAmount >= 2 && this.tileFeeder.currentAnimalAmount < TileEntityFeeder.THRESHOLD) {
            this.drawTexturedModalRect(this.guiLeft + 70, this.guiTop + 31, 192, 16, 8, 8);
        }

        if (this.tileFeeder.currentAnimalAmount >= TileEntityFeeder.THRESHOLD) {
            this.drawTexturedModalRect(this.guiLeft + 70, this.guiTop + 31, 192, 24, 8, 8);
        }
    }
}