package com.noiz.ti.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import TFC.Core.TFC_Core;
import TFC.Core.Player.PlayerInventory;

import com.noiz.ti.TerraIndustrialisConstants;
import com.noiz.ti.containers.ContainerHeater;
import com.noiz.ti.entities.tiles.TileEntityHeater;
import com.noiz.ti.physics.Units;

public class GuiHeater extends GuiContainer {

	private TileEntityHeater heater;

	public GuiHeater(InventoryPlayer player, TileEntityHeater heater, World world, int x, int y, int z) {
		super(new ContainerHeater(player, heater, world, x, y, z));

		this.heater = heater;
		xSize = 175;
		ySize = 85 + PlayerInventory.invYSize;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		TFC_Core.bindTexture(new ResourceLocation(TerraIndustrialisConstants.ModId + ":textures/gui/gui_heater.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int w = (width - xSize) / 2;
		int h = (height - ySize) / 2;
		drawTexturedModalRect(w, h, 0, 0, xSize, ySize);

		drawTexturedModalRect(w + 8, h + 64 - heater.quantizedEnergy, 185, 31, 15, 6);

		int fuel_pc = (int) Math.ceil((100f * heater.getItemCount(TileEntityHeater.FuelSlot)) / heater.getMaxItemCount(TileEntityHeater.FuelSlot));
		String fuel = String.format("%d (%d%%)", heater.getItemCount(TileEntityHeater.FuelSlot), fuel_pc);

		int ash_pc = (int) Math.ceil((100f * heater.getItemCount(TileEntityHeater.AshesSlot)) / heater.getMaxItemCount(TileEntityHeater.AshesSlot));
		String ash = String.format("%d (%d%%)", heater.getItemCount(TileEntityHeater.AshesSlot), ash_pc);

		float nrg = GUITools.reverseQuantize(heater.quantizedEnergy, heater.structureSize() * TileEntityHeater.MaxEnergyPerBlock);
		float btu = Units.joule2btu(nrg);

		int b_pfx = 0;
		for (; btu > 1000 && b_pfx < GUITools.UnitPrefices.length - 1; ++b_pfx)
			btu /= 1000;

		String content = String.format("Stored: %d%sBTU", (int) Math.ceil(btu), b_pfx == 0 ? "" : GUITools.UnitPrefices[b_pfx] + " ");

		int power = heater.quantizedOutput_Wh;

		int p_pfx = 0;
		for (; power > 1000 && p_pfx < GUITools.UnitPrefices.length - 1; ++p_pfx)
			power /= 1000;

		String output = String.format("Output (%d): %d%sWatt", heater.heatTargets(), power, GUITools.UnitPrefices[p_pfx]);

		fontRenderer.drawString(content, w + 35, h + 12, 0x3c3c3c);
		fontRenderer.drawString(fuel, w + 105, h + 30, 0x3c3c3c);
		fontRenderer.drawString(ash, w + 105, h + 50, 0x3c3c3c);
		fontRenderer.drawString(output, w + 35, h + 67, 0x3c3c3c);

		PlayerInventory.drawInventory(this, width, height, ySize - PlayerInventory.invYSize);
	}

	@Override
	public void drawCenteredString(FontRenderer fontrenderer, String s, int i, int j, int k) {
		fontrenderer.drawString(s, i - fontrenderer.getStringWidth(s) / 2, j, k);
	}

	@Override
	protected boolean checkHotbarKeys(int par1) {
		if (this.mc.thePlayer.inventory.currentItem != par1 - 2) {
			super.checkHotbarKeys(par1);
			return true;
		} else
			return false;
	}
}
