package me.tehpicix.rusherhack.middleclickgui;

import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.plugin.Plugin;

public class Main extends Plugin {

	@Override
	public void onLoad() {

		//creating and registering a new hud element
		final HudItem fireworkHud = new HudItem();
		RusherHackAPI.getHudManager().registerFeature(fireworkHud);
	}

	@Override
	public void onUnload() {
	}
}