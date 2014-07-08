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
import com.noiz.ti.containers.ContainerTank;
import com.noiz.ti.entities.tiles.TileEntityTank;
import com.noiz.ti.physics.Units;

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

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		TFC_Core.bindTexture(new ResourceLocation(TerraIndustrialisConstants.ModId + ":textures/gui/gui_tank.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int w = (width - xSize) / 2;
		int h = (height - ySize) / 2;
		drawTexturedModalRect(w, h, 0, 0, xSize, ySize);

		drawTexturedModalRect(w + 8, h + 64 - tank.quantizedWater, 185, 31, 15, 6);
		drawTexturedModalRect(w + 27, h + 64 - tank.quantizedTemperature, 185, 31, 15, 6);
		drawTexturedModalRect(w + 46, h + 64 - GUITools.quantize(tank.pressure(), TileEntityTank.MaxPressure), 185, 31, 15, 6);

		String water = String.format("   %dlt / %dlt", tank.water(), tank.capacity());
		String status = "Status:      " + tank.status();
		String pressure = String.format("  %.1fpsi", Units.pascal2psi(tank.pressure()));
		String tempr = String.format("  %dC", (int) tank.temperature());

		boolean warning = tank.pressure() > .8 * TileEntityTank.MaxPressure;

		fontRenderer.drawString(water, w + 65, h + 15, 0x3c3c3c);
		fontRenderer.drawString(status, w + 65, h + 28, 0x3c3c3c);
		if (tank.temperature() >= 100)//
			fontRenderer.drawString(pressure, w + 65, h + 54, warning ? 0xec3c3c : 0x3c3c3c);
		fontRenderer.drawString(tempr, w + 65, h + 67, warning ? 0xec3c3c : 0x3c3c3c);

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
