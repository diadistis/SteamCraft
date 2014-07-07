package com.noiz.steamcraft.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import TFC.Core.TFC_Core;
import TFC.Core.Player.PlayerInventory;

import com.noiz.steamcraft.containers.ContainerHeater;
import com.noiz.steamcraft.entities.tiles.TileEntityHeater;

public class GuiHeater extends GuiContainer {

	private TileEntityHeater heater;

	public GuiHeater(InventoryPlayer player, TileEntityHeater heater, World world, int x, int y, int z) {
		super(new ContainerHeater(player, heater, world, x, y, z));

		this.heater = heater;
		xSize = 175;
		ySize = 85 + PlayerInventory.invYSize;
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		TFC_Core.bindTexture(new ResourceLocation("steamcraft:textures/gui/gui_heater.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int w = (width - xSize) / 2;
		int h = (height - ySize) / 2;
		drawTexturedModalRect(w, h, 0, 0, xSize, ySize);

		drawTexturedModalRect(w + 8, h + 64 - heater.quantizedTemperature, 185, 31, 15, 6);

		int fuel_pc = (int) Math.ceil((100f * heater.getItemCount(TileEntityHeater.FuelSlot)) / heater.getMaxItemCount(TileEntityHeater.FuelSlot));
		String fuel = String.format("%d (%d%%)", heater.getItemCount(TileEntityHeater.FuelSlot), fuel_pc);

		int ash_pc = (int) Math.ceil((100f * heater.getItemCount(TileEntityHeater.AshesSlot)) / heater.getMaxItemCount(TileEntityHeater.AshesSlot));
		String ash = String.format("%d (%d%%)", heater.getItemCount(TileEntityHeater.AshesSlot), ash_pc);

		String targets = "No Targets";
		if (heater.heatTargets() > 0)
			targets = String.format("Targets: %d", heater.heatTargets());

		fontRenderer.drawString(fuel, w + 105, h + 30, 0x3c3c3c);
		fontRenderer.drawString(ash, w + 105, h + 50, 0x3c3c3c);
		fontRenderer.drawString(targets, w + 35, h + 67, 0x3c3c3c);

		PlayerInventory.drawInventory(this, width, height, ySize - PlayerInventory.invYSize);
	}

	@Override
	public void drawCenteredString(FontRenderer fontrenderer, String s, int i, int j, int k) {
		fontrenderer.drawString(s, i - fontrenderer.getStringWidth(s) / 2, j, k);
	}

	/**
	 * This function is what controls the hotbar shortcut check when you press a
	 * number key when hovering a stack.
	 */
	@Override
	protected boolean checkHotbarKeys(int par1) {
		if (this.mc.thePlayer.inventory.currentItem != par1 - 2) {
			super.checkHotbarKeys(par1);
			return true;
		} else
			return false;
	}
}
