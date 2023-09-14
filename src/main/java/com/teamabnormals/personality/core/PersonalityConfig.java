package com.teamabnormals.personality.core;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import org.apache.commons.lang3.tuple.Pair;

public class PersonalityConfig {

	public static class Client {
		public final ForgeConfigSpec.BooleanValue toggleCrawl;
		public final ForgeConfigSpec.BooleanValue toggleSitting;
		public final ConfigValue<Boolean> climbingAnimation;

		public Client(ForgeConfigSpec.Builder builder) {
			builder.push("animation");
			this.climbingAnimation = builder.comment("If there should be a special animation for climbing ladders").define("Climbing animation", true);
			builder.pop();
			builder.push("keybindings");
			this.toggleCrawl = builder.comment("If true, crawling will be toggled on or off instead of the keybinding being held down").define("toggleCrawl", false);
			this.toggleSitting = builder.comment("If true, sitting will be toggled on or off instead of the keybinding being held down").define("toggleSitting", false);
			builder.pop();
		}
	}

	public static final ForgeConfigSpec CLIENT_SPEC;
	public static final Client CLIENT;

	static {
		Pair<Client, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(Client::new);
		CLIENT_SPEC = clientSpecPair.getRight();
		CLIENT = clientSpecPair.getLeft();
	}
}
