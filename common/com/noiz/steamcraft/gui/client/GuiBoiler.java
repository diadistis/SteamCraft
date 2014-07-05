package com.noiz.steamcraft.gui.client;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import TFC.Core.TFC_Core;
import TFC.Core.Player.PlayerInventory;
import TFC.Core.Util.StringUtil;

import com.noiz.steamcraft.SteamCraftConstants;
import com.noiz.steamcraft.containers.ContainerBoiler;
import com.noiz.steamcraft.entities.tiles.TEBoiler;

public class GuiBoiler extends GuiContainer {

	private TEBoiler boiler;
	private EntityPlayer player;

	public GuiBoiler(InventoryPlayer player, TEBoiler boiler, World world,
			int x, int y, int z) {
		super(new ContainerBoiler(player, boiler, world, x, y, z));

		this.boiler = boiler;
		this.player = player.player;
		xSize = 176;
		ySize = 85 + PlayerInventory.invYSize;
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
	}

	protected void drawGuiContainerForegroundLayer() {
		// drawCenteredString(fontRenderer,StringUtil.localize("gui.LogPile"),
		// 87, 6, 0x000000);
		// fontRenderer.drawString("Log Pile", 28, 6, 0x404040);
		fontRenderer.drawString(StringUtil.localize("gui.Inventory"), 8,
				(ySize - 96) + 2, 0x404040);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		TFC_Core.bindTexture(new ResourceLocation(SteamCraftConstants.ModId,
				SteamCraftConstants.AssetPathGui + "gui_boiler.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;
		drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);

		PlayerInventory.drawInventory(this, width, height, ySize
				- PlayerInventory.invYSize);
	}

	@Override
	public void drawCenteredString(FontRenderer fontrenderer, String s, int i,
			int j, int k) {
		fontrenderer
				.drawString(s, i - fontrenderer.getStringWidth(s) / 2, j, k);
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
