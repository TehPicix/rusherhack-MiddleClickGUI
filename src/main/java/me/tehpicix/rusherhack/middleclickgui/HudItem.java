package me.tehpicix.rusherhack.middleclickgui;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.feature.hud.ResizeableHudElement;
import org.rusherhack.client.api.render.RenderContext;
import org.rusherhack.core.setting.BooleanSetting;
import org.rusherhack.core.setting.EnumSetting;

enum ItemMode {
	PEARL,
	FIREWORK,
	GAPPLE;
}

public class HudItem extends ResizeableHudElement {

	private final BooleanSetting middleClickEnabled = new BooleanSetting("MiddleClickItem", true);
	private final EnumSetting<ItemMode> itemMode = new EnumSetting<>("Item", ItemMode.FIREWORK).setVisibility(middleClickEnabled::getValue);

	private final BooleanSetting disabledItemEnabled = new BooleanSetting("DisabledItem", false);
	private final EnumSetting<ItemMode> disabledItemMode = new EnumSetting<>("Item", ItemMode.FIREWORK).setVisibility(disabledItemEnabled::getValue);

	public HudItem() {
		super("MiddleClickGUI");
		middleClickEnabled.addSubSettings(itemMode);
		registerSettings(middleClickEnabled);
		disabledItemEnabled.addSubSettings(disabledItemMode);
		registerSettings(disabledItemEnabled);
	}

	@Override
	public double getWidth() {
		return 16;
	}

	@Override
	public double getHeight() {
		return 16;
	}

	@Override
	public void renderContent(RenderContext ctx, double mouseX, double mouseY) {

		boolean enabled = RusherHackAPI
		                      .getModuleManager()
		                      .getFeature("MiddleClick")
		                      .get()
		                      .serialize()
		                      .getAsJsonObject()
		                      .get("toggled")
		                      .getAsBoolean();

		if (!enabled && !disabledItemEnabled.getValue()) return;
		if (enabled && !middleClickEnabled.getValue()) return;

		ItemStack stack = new ItemStack(switch (enabled ? itemMode.getValue() : disabledItemMode.getValue()) {
		    case PEARL -> Items.ENDER_PEARL;
			case FIREWORK -> Items.FIREWORK_ROCKET;
			case GAPPLE -> Items.ENCHANTED_GOLDEN_APPLE;
		});

		int count = mc.player.getInventory().items.stream().filter(s -> s.getItem() == stack.getItem()).mapToInt(ItemStack::getCount).sum();
		if (count <= 0) return;

		ctx.graphics().renderItem(stack, 0, 0);
		ctx.graphics().renderItemDecorations(mc.fontFilterFishy, stack, 0, 0, String.valueOf(count));
	}
}
