package com.noiz.steamcraft.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import TFC.Core.TFC_Core;
import TFC.Core.Player.PlayerInventory;

import com.noiz.steamcraft.containers.ContainerTank;
import com.noiz.steamcraft.entities.tiles.TileEntityTank;

public class GuiTank extends GuiContainer {

	private TileEntityTank tank;

	public GuiTank(InventoryPlayer player, TileEntityTank tank, World world, int x, int y, int z) {
		super(new ContainerTank(player, tank, world, x, y, z));
		this.tank = tank;
		xSize = 175;
		ySize = 85 + PlayerInventory.invYSize;
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
	}

	protected void drawGuiContainerForegroundLayer() {
		fontRenderer.drawString("Tank", 8, (ySize - 96) + 2, 0x404040);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		TFC_Core.bindTexture(new ResourceLocation("steamcraft:textures/gui/gui_tank.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int w = (width - xSize) / 2;
		int h = (height - ySize) / 2;
		drawTexturedModalRect(w, h, 0, 0, xSize, ySize);

		drawTexturedModalRect(w + 8, h + 64 - tank.quantizedWater, 185, 31, 15, 6);
		drawTexturedModalRect(w + 27, h + 64 - tank.quantizedTemperature, 185, 31, 15, 6);
		drawTexturedModalRect(w + 46, h + 64 - tank.quantizedPressure, 185, 31, 15, 6);

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
