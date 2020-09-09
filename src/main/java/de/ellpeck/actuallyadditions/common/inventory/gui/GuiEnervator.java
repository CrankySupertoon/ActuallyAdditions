package de.ellpeck.actuallyadditions.common.inventory.gui;

import de.ellpeck.actuallyadditions.common.inventory.ContainerEnervator;
import de.ellpeck.actuallyadditions.common.tile.TileEntityBase;
import de.ellpeck.actuallyadditions.common.tile.TileEntityEnervator;
import de.ellpeck.actuallyadditions.common.util.AssetUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiEnervator extends GuiWtfMojang {

    private static final ResourceLocation RES_LOC = AssetUtil.getGuiLocation("gui_energizer");
    private final TileEntityEnervator enervator;
    private EnergyDisplay energy;

    public GuiEnervator(EntityPlayer inventory, TileEntityBase tile) {
        super(new ContainerEnervator(inventory, tile));
        this.enervator = (TileEntityEnervator) tile;
        this.xSize = 176;
        this.ySize = 93 + 86;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.energy = new EnergyDisplay(this.guiLeft + 56, this.guiTop + 5, this.enervator.storage);
    }

    @Override
    public void drawScreen(int x, int y, float f) {
        super.drawScreen(x, y, f);
        this.energy.drawOverlay(x, y);
    }

    @Override
    public void drawGuiContainerForegroundLayer(int x, int y) {
        AssetUtil.displayNameString(this.fontRenderer, this.xSize, -10, this.enervator);
    }

    @Override
    public void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(AssetUtil.GUI_INVENTORY_LOCATION);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop + 93, 0, 0, 176, 86);

        this.mc.getTextureManager().bindTexture(RES_LOC);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 176, 93);

        this.energy.draw();
    }
}